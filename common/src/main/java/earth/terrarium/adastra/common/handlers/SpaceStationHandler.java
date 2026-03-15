package earth.terrarium.adastra.common.handlers;

import com.mojang.serialization.Codec;
import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.common.handlers.base.SpaceStation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.*;

public class SpaceStationHandler extends SavedData {

    private static final Codec<SpaceStationHandler> CODEC = CompoundTag.CODEC.xmap(
        tag -> {
            SpaceStationHandler handler = new SpaceStationHandler();
            handler.loadData(tag);
            return handler;
        },
        handler -> {
            CompoundTag tag = new CompoundTag();
            handler.saveData(tag);
            return tag;
        }
    );

    private static final SavedDataType<SpaceStationHandler> TYPE = new SavedDataType<>(
        "adastra_space_station_data",
        SpaceStationHandler::new,
        CODEC,
        null
    );

    private final Map<UUID, Set<SpaceStation>> spaceStationData = new HashMap<>();

    private void loadData(CompoundTag tag) {
        tag.keySet().forEach(id -> {
            ListTag stationsTag = tag.getListOrEmpty(id);
            Set<SpaceStation> stations = new HashSet<>();
            stationsTag.forEach(stationTag -> {
                CompoundTag stationCompoundTag = (CompoundTag) stationTag;
                Component name = Component.Serializer.fromJson(stationCompoundTag.getStringOr("Name", ""), AdAstra.getRegistryAccess());
                ChunkPos position = new ChunkPos(stationCompoundTag.getLongOr("Position", 0L));
                stations.add(new SpaceStation(position, name));
            });
            spaceStationData.put(UUID.fromString(id), stations);
        });
    }

    private void saveData(CompoundTag tag) {
        spaceStationData.forEach((id, stations) -> {
            ListTag ownerTag = new ListTag();
            for (var station : stations) {
                CompoundTag stationsTag = new CompoundTag();
                stationsTag.putString("Name", Component.Serializer.toJson(station.name(), AdAstra.getRegistryAccess()));
                stationsTag.putLong("Position", station.position().toLong());
                ownerTag.add(stationsTag);
            }
            tag.put(id.toString(), ownerTag);
        });
    }

    public static SpaceStationHandler read(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    public static Map<UUID, Set<SpaceStation>> getAllSpaceStations(ServerLevel level) {
        return read(level).spaceStationData;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    public static void constructSpaceStation(ServerPlayer player, ServerLevel level, Component name) {
        var data = read(level).spaceStationData;
        var stations = data.computeIfAbsent(player.getUUID(), k -> new HashSet<>());
        stations.add(new SpaceStation(player.chunkPosition(), name));
    }

    public static boolean isInSpaceStation(ServerPlayer player, ServerLevel level) {
        for (var stations : read(level).spaceStationData.values()) {
            for (var station : stations) {
                if (station.position().getChessboardDistance(player.chunkPosition()) <= 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Set<SpaceStation> getOwnedSpaceStations(ServerPlayer player, ServerLevel level) {
        return getOwnedSpaceStations(player.getUUID(), level);
    }

    public static Set<SpaceStation> getOwnedSpaceStations(UUID id, ServerLevel level) {
        return read(level).spaceStationData.getOrDefault(id, Set.of());
    }
}
