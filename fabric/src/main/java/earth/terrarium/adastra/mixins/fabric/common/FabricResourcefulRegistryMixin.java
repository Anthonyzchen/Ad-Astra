package earth.terrarium.adastra.mixins.fabric.common;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.fabric.FabricResourcefulRegistry;
import earth.terrarium.adastra.common.registry.RegistryIdContext;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(value = FabricResourcefulRegistry.class, remap = false)
public abstract class FabricResourcefulRegistryMixin<T> {

    @Shadow
    public abstract String namespace();

    @Inject(method = "register", at = @At("HEAD"))
    private <I extends T> void adastra$setIdContext(String id, Supplier<I> supplier, CallbackInfoReturnable<RegistryEntry<I>> cir) {
        RegistryIdContext.CURRENT_ID.set(Identifier.fromNamespaceAndPath(namespace(), id));
    }

    @Inject(method = "register", at = @At("RETURN"))
    private <I extends T> void adastra$clearIdContext(String id, Supplier<I> supplier, CallbackInfoReturnable<RegistryEntry<I>> cir) {
        RegistryIdContext.CURRENT_ID.remove();
    }
}
