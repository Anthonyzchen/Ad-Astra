package earth.terrarium.adastra.mixins.fabric.common;

import earth.terrarium.adastra.common.items.armor.JetSuitItem;
import org.spongepowered.asm.mixin.Mixin;

/**
 * 1.21.11: FabricElytraItem was removed from Fabric API.
 * Custom elytra behavior is now registered via EntityElytraEvents.CUSTOM in AdAstraFabric.init().
 * This mixin is kept as a placeholder and can be removed if no longer needed.
 */
@SuppressWarnings("DataFlowIssue")
@Mixin(JetSuitItem.class)
public abstract class JetSuitItemMixin {
}
