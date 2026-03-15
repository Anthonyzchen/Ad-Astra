package earth.terrarium.adastra.mixins.neoforge.common;

import earth.terrarium.adastra.common.items.armor.base.CustomDyeableArmorItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;

// TODO: 1.21.11 - NeoForge removed IClientItemExtensions. Armor rendering now uses
// RegisterClientExtensionsEvent or the new armor layer system. This mixin MUST be
// reworked to use the new NeoForge armor rendering API. The IClientItemExtensions
// interface and initializeClient method no longer exist. Consider moving this logic
// to RegisterClientExtensionsEvent in the mod initializer instead of using a mixin.
//
// The following methods were removed because they reference APIs that no longer exist:
// - initializeClient(Consumer<IClientItemExtensions>)
// - getArmorTexture(ItemStack, Entity, EquipmentSlot, String)
@Mixin(CustomDyeableArmorItem.class)
public abstract class CustomDyeableArmorItemMixin extends Item {

    public CustomDyeableArmorItemMixin(Properties properties) {
        super(properties);
    }
}
