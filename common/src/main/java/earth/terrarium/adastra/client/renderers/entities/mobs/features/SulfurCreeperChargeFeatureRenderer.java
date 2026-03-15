package earth.terrarium.adastra.client.renderers.entities.mobs.features;

import earth.terrarium.adastra.client.models.entities.mobs.SulfurCreeperModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

// LEGACY ENTITY. WILL BE REPLACED IN THE FUTURE.
public class SulfurCreeperChargeFeatureRenderer extends EnergySwirlLayer<LivingEntityRenderState, SulfurCreeperModel<LivingEntityRenderState>> {

    private static final Identifier SKIN = Identifier.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");
    private final SulfurCreeperModel<LivingEntityRenderState> model;

    public SulfurCreeperChargeFeatureRenderer(RenderLayerParent<LivingEntityRenderState, SulfurCreeperModel<LivingEntityRenderState>> context, EntityModelSet loader) {
        super(context);
        this.model = new SulfurCreeperModel<>(loader.bakeLayer(SulfurCreeperModel.LAYER_LOCATION));
    }

    @Override
    protected float xOffset(float partialAge) {
        return partialAge * 0.01f;
    }

    @Override
    protected Identifier getTextureLocation() {
        return SKIN;
    }

    @Override
    protected EntityModel<LivingEntityRenderState> model() {
        return this.model;
    }
}
