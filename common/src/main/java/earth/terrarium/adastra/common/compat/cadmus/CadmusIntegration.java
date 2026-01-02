package earth.terrarium.adastra.common.compat.cadmus;

import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfoUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

/**
 * Stub implementation for Cadmus integration.
 * Cadmus is not yet available for MC 1.21, so these methods are no-ops.
 */
public class CadmusIntegration {

    public static boolean cadmusLoaded() {
        // Cadmus is not available for 1.21 yet
        return false;
    }

    public static void claim(ServerPlayer player, ChunkPos pos) {
        // No-op: Cadmus not available
    }

    public static boolean isClaimed(ServerLevel level, ChunkPos pos) {
        // No-op: Cadmus not available
        return false;
    }

    public static void addClientListeners(ResourceKey<Level> dimension) {
        // No-op: Cadmus not available
    }

    public static void removeClientListeners(ResourceKey<Level> dimension) {
        // No-op: Cadmus not available
    }
}
