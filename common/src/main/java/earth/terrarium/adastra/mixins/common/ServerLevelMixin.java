package earth.terrarium.adastra.mixins.common;

import com.google.common.collect.ImmutableList;
import earth.terrarium.adastra.api.planets.PlanetApi;
import earth.terrarium.adastra.api.systems.OxygenApi;
import earth.terrarium.adastra.common.entities.mob.lunarians.LunarianWanderingTraderSpawner;
import earth.terrarium.adastra.common.systems.EnvironmentEffects;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.RandomSequences;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @Shadow
    @Mutable
    @Final
    private List<CustomSpawner> customSpawners;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void adastra$init(
        MinecraftServer server,
        Executor dispatcher,
        LevelStorageSource.LevelStorageAccess levelStorageAccess,
        ServerLevelData serverLevelData,
        ResourceKey<Level> dimension,
        LevelStem levelStem,
        boolean isDebug, long biomeZoomSeed,
        List<CustomSpawner> customSpawners,
        boolean tickTime,
        RandomSequences randomSequences,
        CallbackInfo ci
    ) {
        if (!PlanetApi.API.isPlanet(dimension)) return;
        this.customSpawners = ImmutableList.<CustomSpawner>builder()
            .addAll(customSpawners)
            .add(new LunarianWanderingTraderSpawner(serverLevelData))
            .build();
    }

    @Inject(method = "method_18203", at = @At("TAIL"))
    public void tickChunk(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        if (!OxygenApi.API.hasOxygen(chunk.getLevel())) {
            var level = chunk.getLevel();
            net.minecraft.util.profiling.Profiler.get().popPush("adastra$spaceeffects");
            EnvironmentEffects.tickChunk((ServerLevel) level, chunk);
            net.minecraft.util.profiling.Profiler.get().pop();
        }
    }

    @Inject(
        method = "method_18765",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;setDayTime(J)V",
            shift = At.Shift.AFTER
        )
    )
    public void adastra$tick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        if (PlanetApi.API.isExtraterrestrial(level) && !Level.OVERWORLD.equals(level.dimension())) {
            // Fix night not advancing when sleeping in space
            long time = level.getLevelData().getDayTime() + 24000L;
            level.getServer().getAllLevels().forEach(l -> l.setDayTime(time - time % 24000L));
        }
    }
}
