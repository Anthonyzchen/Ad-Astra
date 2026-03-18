package earth.terrarium.adastra.mixins.common;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * In 1.21.11, BlockBehaviour.Properties requires the block ID to be set before
 * effectiveDrops() and effectiveDescriptionId() are called in the constructor.
 * ResourcefulLib's registry creates blocks before setting IDs, causing a crash.
 * This mixin provides safe fallbacks when the ID hasn't been set yet.
 */
@Mixin(BlockBehaviour.Properties.class)
public abstract class BlockBehaviourPropertiesMixin {

    @Shadow
    private ResourceKey<Block> id;

    @Inject(method = "effectiveDrops", at = @At("HEAD"), cancellable = true)
    private void adastra$effectiveDrops(CallbackInfoReturnable<Optional<ResourceKey<LootTable>>> cir) {
        if (this.id == null) {
            cir.setReturnValue(Optional.empty());
        }
    }

    @Inject(method = "effectiveDescriptionId", at = @At("HEAD"), cancellable = true)
    private void adastra$effectiveDescriptionId(CallbackInfoReturnable<String> cir) {
        if (this.id == null) {
            cir.setReturnValue("block.unknown.unknown");
        }
    }
}
