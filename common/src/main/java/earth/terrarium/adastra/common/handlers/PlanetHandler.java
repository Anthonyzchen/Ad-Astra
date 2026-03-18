package earth.terrarium.adastra.common.handlers;

import com.mojang.serialization.Codec;
import earth.terrarium.adastra.api.systems.GravityApi;
import earth.terrarium.adastra.api.systems.OxygenApi;
import earth.terrarium.adastra.api.systems.PlanetData;
import earth.terrarium.adastra.api.systems.TemperatureApi;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlanetHandler extends SavedData {

    private final Map<BlockPos, PlanetData> planetData = new HashMap<>();
    private ServerLevel level;

    public PlanetHandler() {
    }

    private PlanetHandler(ServerLevel level) {
        this.level = level;
    }

    private void loadData(CompoundTag tag) {
        var data = tag.getIntArray("").orElse(new int[0]);
        if (data.length % 3 != 0) {
            throw new RuntimeException("Invalid data length");
        }
        for (int i = 0; i < data.length; i += 3) {
            int firstPos = data[i];
            int secondPos = data[i + 1];
            planetData.put(
                BlockPos.of(((long) firstPos << 32) | (secondPos & 0xFFFFFFFFL)),
                PlanetData.unpack(data[i + 2])
            );
        }
    }

    private void saveData(CompoundTag tag) {
        if (level == null) return;
        boolean defaultOxygen = OxygenApi.API.hasOxygen(level);
        short defaultTemperature = TemperatureApi.API.getTemperature(level);
        float defaultGravity = GravityApi.API.getGravity(level);
        IntArrayList dataArray = new IntArrayList(this.planetData.size() * 3);
        var entries = planetData.entrySet();
        for (var entry : entries) {
            var data = entry.getValue();
            // Don't save positions that are identical to the default planet values.
            if (data.oxygen() != defaultOxygen || data.temperature() != defaultTemperature || data.gravity() != defaultGravity) {
                long pos = entry.getKey().asLong();
                dataArray.add((int) (pos >> 32));
                dataArray.add((int) pos);
                dataArray.add(entry.getValue().pack());
            }
        }

        tag.putIntArray("", dataArray.toIntArray());
    }

    private static Codec<PlanetHandler> createCodec(ServerLevel level) {
        return CompoundTag.CODEC.xmap(
            tag -> {
                PlanetHandler handler = new PlanetHandler(level);
                handler.loadData(tag);
                return handler;
            },
            handler -> {
                CompoundTag tag = new CompoundTag();
                handler.saveData(tag);
                return tag;
            }
        );
    }

    public static PlanetHandler read(ServerLevel level) {
        SavedDataType<PlanetHandler> type = new SavedDataType<>(
            "adastra_planet_data",
            () -> new PlanetHandler(level),
            createCodec(level),
            null
        );
        PlanetHandler handler = level.getDataStorage().computeIfAbsent(type);
        handler.level = level;
        return handler;
    }

    public static boolean hasOxygen(ServerLevel level, BlockPos pos) {
        PlanetData data = read(level).planetData.get(pos);
        return data == null ? OxygenApi.API.hasOxygen(level) : data.oxygen();
    }

    public static short getTemperature(ServerLevel level, BlockPos pos) {
        PlanetData data = read(level).planetData.get(pos);
        return data == null ? TemperatureApi.API.getTemperature(level) : data.temperature();
    }

    public static float getGravity(ServerLevel level, BlockPos pos) {
        PlanetData data = read(level).planetData.get(pos);
        return data == null ? GravityApi.API.getGravity(level) : data.gravity();
    }

    public static void setOxygen(ServerLevel level, BlockPos pos, boolean oxygen) {
        var data = read(level);
        data.planetData.computeIfAbsent(pos, p -> new PlanetData(oxygen, getTemperature(level, p), getGravity(level, p)))
            .setOxygen(oxygen);
    }

    public static void setTemperature(ServerLevel level, BlockPos pos, short temperature) {
        var data = read(level);
        data.planetData.computeIfAbsent(pos, p -> new PlanetData(hasOxygen(level, p), temperature, getGravity(level, p)))
            .setTemperature(temperature);
    }

    public static void setGravity(ServerLevel level, BlockPos pos, float gravity) {
        var data = read(level);
        data.planetData.computeIfAbsent(pos, p -> new PlanetData(hasOxygen(level, p), getTemperature(level, p), gravity))
            .setGravity(gravity);
    }

    public static void setOxygen(ServerLevel level, Collection<BlockPos> positions, boolean oxygen) {
        var data = read(level);
        for (BlockPos pos : positions) {
            data.planetData.computeIfAbsent(pos, p -> new PlanetData(oxygen, getTemperature(level, p), getGravity(level, p)))
                .setOxygen(oxygen);
        }
    }

    public static void setTemperature(ServerLevel level, Collection<BlockPos> positions, short temperature) {
        var data = read(level);
        for (BlockPos pos : positions) {
            data.planetData.computeIfAbsent(pos, p -> new PlanetData(hasOxygen(level, p), temperature, getGravity(level, p)))
                .setTemperature(temperature);
        }
    }

    public static void setGravity(ServerLevel level, Collection<BlockPos> positions, float gravity) {
        var data = read(level);
        for (BlockPos pos : positions) {
            data.planetData.computeIfAbsent(pos, p -> new PlanetData(hasOxygen(level, p), getTemperature(level, p), gravity))
                .setGravity(gravity);
        }
    }

    @Override
    public boolean isDirty() {
        return true;
    }
}
