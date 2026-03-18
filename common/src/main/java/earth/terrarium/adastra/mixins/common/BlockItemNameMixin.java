package earth.terrarium.adastra.mixins.common;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * In 1.21.11, BlockItem no longer inherits its description from the Block.
 * This mixin restores that behavior by delegating to the block's description ID.
 */
@Mixin(Item.class)
public abstract class BlockItemNameMixin {

    @Inject(method = "getName()Lnet/minecraft/network/chat/Component;", at = @At("HEAD"), cancellable = true)
    private void adastra$getName(CallbackInfoReturnable<Component> cir) {
        if ((Object) this instanceof BlockItem blockItem) {
            String blockDescId = blockItem.getBlock().getDescriptionId();
            String itemDescId = blockItem.getDescriptionId();
            if (!itemDescId.equals(blockDescId) && blockDescId.startsWith("block.")) {
                cir.setReturnValue(Component.translatable(blockDescId));
            }
        }
    }
}
