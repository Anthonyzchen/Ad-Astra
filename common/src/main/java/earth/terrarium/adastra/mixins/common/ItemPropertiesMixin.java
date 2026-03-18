package earth.terrarium.adastra.mixins.common;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * In 1.21.11, Item.Properties requires the item ID to be set before
 * effectiveDescriptionId() and effectiveModel() are called in the constructor.
 * ResourcefulLib's registry creates items before setting IDs, causing a crash.
 */
@Mixin(Item.Properties.class)
public abstract class ItemPropertiesMixin {

    @Shadow
    private ResourceKey<Item> id;

    @Inject(method = "effectiveDescriptionId", at = @At("HEAD"), cancellable = true)
    private void adastra$effectiveDescriptionId(CallbackInfoReturnable<String> cir) {
        if (this.id == null) {
            cir.setReturnValue("item.unknown.unknown");
        }
    }

    @Inject(method = "effectiveModel", at = @At("HEAD"), cancellable = true)
    private void adastra$effectiveModel(CallbackInfoReturnable<Identifier> cir) {
        if (this.id == null) {
            cir.setReturnValue(Identifier.fromNamespaceAndPath("ad_astra", "unknown"));
        }
    }
}
