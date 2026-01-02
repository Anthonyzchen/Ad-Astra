package earth.terrarium.adastra.mixins.fabric.client;

import earth.terrarium.adastra.common.entities.vehicles.Vehicle;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    // @Inject(method = "setup", at = @At("TAIL"))
    // public void adastra$update(BlockGetter area, Entity focusedEntity, boolean
    // thirdPerson, boolean inverseView,
    // float tickDelta, CallbackInfo ci) {
    // if (thirdPerson && focusedEntity.getVehicle() instanceof Vehicle vehicle
    // && vehicle.zoomOutCameraInThirdPerson()) {
    // moveBy(-getMaxZoom(12.0F), 0.0F, 0.0F);
    // }
    // }
    //
    // @Shadow
    // protected abstract void moveBy(float x, float y, float z);
    //
    // @Shadow
    // protected abstract float getMaxZoom(float desiredCameraDistance);
}