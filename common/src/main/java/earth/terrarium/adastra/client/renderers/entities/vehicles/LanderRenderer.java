package earth.terrarium.adastra.client.renderers.entities.vehicles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.client.models.entities.vehicles.LanderModel;
import earth.terrarium.adastra.common.entities.vehicles.Lander;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class LanderRenderer extends EntityRenderer<Lander, EntityRenderState> {

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "textures/entity/lander/lander.png");

    protected final EntityModel<EntityRenderState> model;

    public LanderRenderer(EntityRendererProvider.Context context, ModelLayerLocation layer) {
        super(context);
        this.shadowRadius = 0.5f;
        this.model = new LanderModel(context.bakeLayer(layer));
    }

    @Override
    public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        super.submit(state, poseStack, collector, cameraState);
        // TODO: 1.21.11 - Migrate lander rendering to the new SubmitNodeCollector pipeline.
        // The old render method used MultiBufferSource which is no longer available here.
        // Entity-specific data (entityYaw, xRot, etc.) should be extracted via
        // extractRenderState and stored in a custom EntityRenderState subclass.
    }
}
