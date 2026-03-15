package earth.terrarium.adastra.mixins.neoforge.common;

import earth.terrarium.adastra.client.neoforge.ClientPlatformUtilsImpl;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionSpecialEffects.class)
public abstract class DimensionSpecialEffectsMixin {

    // Mixin directly instead of using the event because Ad Astra dimension renderers are added dynamically via resource pack.
    // 1.21.1: DimensionType.effectsLocation() was renamed to effects()
    // TODO: 1.21.11 - Verify DimensionSpecialEffects.forType() still exists and DimensionType.effects() is still the correct method.
    @Inject(method = "forType", at = @At("HEAD"), cancellable = true)
    private static void adastra$forType(DimensionType type, CallbackInfoReturnable<DimensionSpecialEffects> cir) {
        ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, type.effects());
        if (ClientPlatformUtilsImpl.DIMENSION_RENDERERS.containsKey(dimension)) {
            cir.setReturnValue(ClientPlatformUtilsImpl.DIMENSION_RENDERERS.get(dimension));
        }
    }
}
