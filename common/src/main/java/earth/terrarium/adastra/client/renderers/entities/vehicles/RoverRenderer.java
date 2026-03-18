package earth.terrarium.adastra.client.renderers.entities.vehicles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.client.models.entities.vehicles.RoverModel;
import earth.terrarium.adastra.common.entities.vehicles.Rover;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RoverRenderer extends EntityRenderer<Rover, EntityRenderState> {

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "textures/entity/rover/tier_1_rover.png");

    protected final EntityModel<EntityRenderState> model;

    public RoverRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 1.0f;
        this.model = new RoverModel(context.bakeLayer(RoverModel.LAYER));
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        super.submit(state, poseStack, collector, cameraState);
        // TODO: 1.21.11 - Migrate rover rendering to the new SubmitNodeCollector pipeline.
        // The old render method used MultiBufferSource which is no longer available here.
        // Entity-specific data (entityYaw, xRot, etc.) should be extracted via
        // extractRenderState and stored in a custom EntityRenderState subclass.
    }

    public static class ItemRenderer {

        private EntityModel<?> model;

        public ItemRenderer() {
        }

        public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
            if (model == null) {
                model = new RoverModel(Minecraft.getInstance().getEntityModels().bakeLayer(RoverModel.LAYER));
            }
            var consumer = buffer.getBuffer(RenderTypes.entityCutoutNoCullZOffset(TEXTURE));
            poseStack.pushPose();
            try {
                poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                poseStack.translate(0.0, -1.501, 0.0);
                model.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, -1);
            } finally {
                poseStack.popPose();
            }
        }
    }
}
