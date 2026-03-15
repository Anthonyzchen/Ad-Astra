package earth.terrarium.adastra.mixins.common.environment;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import earth.terrarium.adastra.api.systems.GravityApi;
import earth.terrarium.adastra.api.systems.TemperatureApi;
import earth.terrarium.adastra.common.constants.PlanetConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlowingFluid.class)
public abstract class FlowingFluidMixin {


    @WrapOperation(
        method = "method_15727",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;isSolid()Z"
        )
    )
    private boolean adastra$getNewLiquid(BlockState instance, Operation<Boolean> original, Level level, BlockPos pos, BlockState state) {
        if (!TemperatureApi.API.isLiveable(level, pos)) {
            return false; // Prevent infinite fluid source in hot and cold areas
        }
        return original.call(instance);
    }

    @Inject(method = "method_15725", at = @At(value = "HEAD"), cancellable = true)
    private void adastra$spread(Level level, BlockPos pos, FluidState state, CallbackInfo ci) {
        if (GravityApi.API.getGravity(level, pos) <= PlanetConstants.ZERO_GRAVITY_THRESHOLD) {
            ci.cancel(); // Prevent fluid from spreading in zero gravity
        }
    }
}
