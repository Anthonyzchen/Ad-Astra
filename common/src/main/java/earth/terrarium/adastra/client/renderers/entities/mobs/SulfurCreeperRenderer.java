package earth.terrarium.adastra.client.renderers.entities.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.client.models.entities.mobs.SulfurCreeperModel;
import earth.terrarium.adastra.client.renderers.entities.mobs.features.SulfurCreeperChargeFeatureRenderer;
import earth.terrarium.adastra.common.entities.mob.SulfurCreeper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

// LEGACY ENTITY. WILL BE REPLACED IN THE FUTURE.
public class SulfurCreeperRenderer extends MobRenderer<SulfurCreeper, LivingEntityRenderState, SulfurCreeperModel<LivingEntityRenderState>> {

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "textures/entity/mob/sulfur_creeper.png");

    public SulfurCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new SulfurCreeperModel<>(context.bakeLayer(SulfurCreeperModel.LAYER_LOCATION)), 0.7f);
        this.addLayer(new SulfurCreeperChargeFeatureRenderer(this, context.getModelSet()));
    }

    @Override
    protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
        // TODO: 1.21.11 - Restore creeper swelling animation using custom render state
    }

    @Override
    public @NotNull Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }
}
