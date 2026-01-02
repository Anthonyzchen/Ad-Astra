package earth.terrarium.adastra.common.compat.argonauts;

import com.mojang.authlib.GameProfile;

import java.util.List;
import java.util.UUID;

/**
 * Stub implementation for Argonauts integration.
 * Argonauts is not yet available for MC 1.21, so these methods return
 * empty/false.
 */
public class ArgonautsIntegration {

    public static boolean argonautsLoaded() {
        // Argonauts is not available for 1.21 yet
        return false;
    }

    public static List<GameProfile> getClientPartyMembers(UUID player) {
        // No-op: Argonauts not available
        return List.of();
    }

    public static List<GameProfile> getClientGuildMembers(UUID player) {
        // No-op: Argonauts not available
        return List.of();
    }
}
