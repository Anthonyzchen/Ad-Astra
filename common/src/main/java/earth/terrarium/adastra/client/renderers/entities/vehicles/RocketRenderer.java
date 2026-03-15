package earth.terrarium.adastra.client.renderers.entities.vehicles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.client.models.entities.vehicles.RocketModel;
import earth.terrarium.adastra.common.entities.vehicles.Rocket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RocketRenderer extends EntityRenderer<Rocket, EntityRenderState> {

    public static final Identifier TIER_1_TEXTURE = Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "textures/entity/rocket/tier_1_rocket.png");
    public static final Identifier TIER_2_TEXTURE = Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "textures/entity/rocket/tier_2_rocket.png");
    public static final Identifier TIER_3_TEXTURE = Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "textures/entity/rocket/tier_3_rocket.png");
    public static final Identifier TIER_4_TEXTURE = Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "textures/entity/rocket/tier_4_rocket.png");

    protected final EntityModel<EntityRenderState> model;
    private final Identifier texture;

    public RocketRenderer(EntityRendererProvider.Context context, ModelLayerLocation layer, Identifier texture) {
        super(context);
        this.shadowRadius = 0.5f;
        this.model = new RocketModel(context.bakeLayer(layer));
        this.texture = texture;
    }

    @Override
    public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        super.submit(state, poseStack, collector, cameraState);
        // TODO: 1.21.11 - Migrate rocket rendering to the new SubmitNodeCollector pipeline.
        // The old render method used MultiBufferSource which is no longer available here.
        // Entity-specific data (entityYaw, isLaunching, etc.) should be extracted via
        // extractRenderState and stored in a custom EntityRenderState subclass.
    }

    public static class ItemRenderer {

        private final ModelLayerLocation layer;
        private final Identifier texture;

        private EntityModel<?> model;

        public ItemRenderer(ModelLayerLocation layer, Identifier texture) {
            this.layer = layer;
            this.texture = texture;
        }

        public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
            if (model == null) {
                model = new RocketModel(Minecraft.getInstance().getEntityModels().bakeLayer(layer));
            }
            var consumer = buffer.getBuffer(RenderType.entityCutoutNoCullZOffset(texture));
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
