package earth.terrarium.adastra.mixins.fabric.client;

import earth.terrarium.adastra.common.entities.vehicles.Vehicle;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Inject(method = "setup", at = @At("TAIL"))
    public void adastra$update(Level level, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (thirdPerson && focusedEntity.getVehicle() instanceof Vehicle vehicle && vehicle.zoomOutCameraInThirdPerson()) {
            move(-getMaxZoom(12.0f), 0.0f, 0.0f);
        }
    }

    @Shadow
    protected abstract void move(float x, float y, float z);

    @Shadow
    private float getMaxZoom(float desiredCameraDistance) {
        throw new UnsupportedOperationException("mixin shadow");
    }
}
