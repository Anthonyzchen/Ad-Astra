package earth.terrarium.adastra.client.renderers.entities.mobs;

import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.client.models.entities.mobs.MoglerModel;
import earth.terrarium.adastra.common.entities.mob.Mogler;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

// LEGACY ENTITY. WILL BE REPLACED IN THE FUTURE.
public class MoglerRenderer extends MobRenderer<Mogler, LivingEntityRenderState, MoglerModel<LivingEntityRenderState>> {

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "textures/entity/mob/mogler.png");

    public MoglerRenderer(EntityRendererProvider.Context context) {
        super(context, new MoglerModel<>(context.bakeLayer(MoglerModel.LAYER_LOCATION)), 0.7f);
    }

    @Override
    public @NotNull Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    protected boolean isShaking(LivingEntityRenderState state) {
        // TODO: 1.21.11 - Restore isConverting check using custom render state
        return super.isShaking(state);
    }
}
