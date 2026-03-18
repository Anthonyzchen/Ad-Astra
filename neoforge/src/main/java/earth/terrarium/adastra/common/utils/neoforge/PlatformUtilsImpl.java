package earth.terrarium.adastra.common.utils.neoforge;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.portal.TeleportTransition;

import java.util.function.Supplier;

public class PlatformUtilsImpl {

    public static Entity teleportToDimension(Entity entity, ServerLevel level, TeleportTransition teleportTransition) {
        return entity.teleport(teleportTransition);
    }

    // TODO: 1.21.11 - SpawnEggItem is now data-driven. Entity type and colors should be set
    // via Item.Properties data components. The parameters are kept for API compatibility but
    // colors are ignored - they need to be set via data packs or Item.Properties.
    public static Supplier<Item> createSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type, int primaryColor, int secondaryColor, Item.Properties properties) {
        return () -> new SpawnEggItem(properties);
    }
}
