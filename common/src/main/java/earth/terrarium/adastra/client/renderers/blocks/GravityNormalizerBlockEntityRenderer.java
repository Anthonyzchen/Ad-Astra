package earth.terrarium.adastra.client.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.client.ClientPlatformUtils;
import earth.terrarium.adastra.common.blockentities.machines.GravityNormalizerBlockEntity;
import earth.terrarium.adastra.common.blocks.base.SidedMachineBlock;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class GravityNormalizerBlockEntityRenderer implements BlockEntityRenderer<GravityNormalizerBlockEntity, GravityNormalizerBlockEntityRenderer.GravityNormalizerRenderState> {

    public static final Identifier TOP = Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "block/gravity_normalizer_top");
    public static final Identifier TOE = Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "block/gravity_normalizer_toe");

    private static final float SIN_45 = (float) Math.sin(Math.PI / 4);

    public static class GravityNormalizerRenderState extends BlockEntityRenderState {
        public float animation;
        public AttachFace face;
        public Direction direction;
    }

    @Override
    public GravityNormalizerRenderState createRenderState() {
        return new GravityNormalizerRenderState();
    }

    @Override
    public void extractRenderState(GravityNormalizerBlockEntity entity, GravityNormalizerRenderState state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        state.animation = Mth.lerp(partialTick, entity.lastAnimation(), entity.animation());
        state.face = entity.getBlockState().getValue(SidedMachineBlock.FACE);
        state.direction = entity.getBlockState().getValue(SidedMachineBlock.FACING);
    }

    @Override
    public void submit(GravityNormalizerRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        // TODO: 1.21.11 - Migrate to new SubmitNodeCollector rendering pipeline.
        // The old MultiBufferSource-based rendering needs to be converted to the new
        // deferred rendering system. For now, this is a stub.
    }

    // Taken from geckolib
    protected static void rotateBlock(Direction facing, PoseStack poseStack) {
        switch (facing) {
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
            case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(0));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(270));
            case UP -> poseStack.mulPose(Axis.XP.rotationDegrees(90));
            case DOWN -> poseStack.mulPose(Axis.XN.rotationDegrees(90));
        }
    }

    static void renderStatic(BlockState state, float animation, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        BlockStateModel blockModel = ClientPlatformUtils.getModel(Minecraft.getInstance().getModelManager(), TOP);
        BlockStateModel toeModel = ClientPlatformUtils.getModel(Minecraft.getInstance().getModelManager(), TOE);

        poseStack.pushPose();
        try {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.7, 0.5);

            poseStack.mulPose(Axis.XP.rotationDegrees(animation));
            poseStack.mulPose(Axis.YP.rotationDegrees(animation));
            poseStack.mulPose(Axis.ZP.rotationDegrees(animation));

            float yRot = animation / 1.2f;
            poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
            poseStack.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SIN_45, 0, SIN_45));
            poseStack.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SIN_45, 0, SIN_45));
            poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
            poseStack.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SIN_45, 0, SIN_45));
            poseStack.mulPose(Axis.YP.rotationDegrees(yRot));

            poseStack.translate(-0.5, -0.7, -0.5);

            ModelBlockRenderer.renderModel(
                poseStack.last(),
                buffer.getBuffer(Sheets.cutoutBlockSheet()),
                blockModel,
                1, 1, 1,
                packedLight, packedOverlay);

            poseStack.popPose();

            for (int i = 0; i < 4; i++) {
                poseStack.pushPose();

                poseStack.translate(0.5, 0, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(90 * i));
                poseStack.translate(-0.5, 0, -0.5);

                poseStack.translate(0.27, 0.27, 0.27);
                poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(animation / 50 + i) * 10));
                poseStack.translate(-0.27, -0.27, -0.27);

                ModelBlockRenderer.renderModel(
                    poseStack.last(),
                    buffer.getBuffer(Sheets.cutoutBlockSheet()),
                    toeModel,
                    1, 1, 1,
                    packedLight, packedOverlay);

                poseStack.popPose();
            }
        } finally {
            poseStack.popPose();
        }
    }

    public static class ItemRenderer {

        public ItemRenderer() {
        }

        public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
            BlockState state = BuiltInRegistries.BLOCK.getValue(BuiltInRegistries.ITEM.getKey(stack.getItem())).defaultBlockState();

            var minecraft = Minecraft.getInstance();
            float yRot = Util.getMillis() / 5f;

            poseStack.pushPose();
            try {
                var model = minecraft.getBlockRenderer().getBlockModel(state);
                ModelBlockRenderer.renderModel(poseStack.last(),
                    buffer.getBuffer(Sheets.cutoutBlockSheet()),
                    model,
                    1, 1, 1,
                    packedLight, packedOverlay);
                renderStatic(state, yRot, poseStack, buffer, packedLight, packedOverlay);
            } finally {
                poseStack.popPose();
            }
        }
    }
}
