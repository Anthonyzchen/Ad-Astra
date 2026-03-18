package earth.terrarium.adastra.common.utils.fabric;

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

    public static Supplier<Item> createSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type, int primaryColor, int secondaryColor, Item.Properties properties) {
        // 1.21.11: SpawnEggItem constructor now only takes Item.Properties.
        // Entity type, colors are now configured via data-driven components on the item.
        return () -> new SpawnEggItem(properties);
    }
}
