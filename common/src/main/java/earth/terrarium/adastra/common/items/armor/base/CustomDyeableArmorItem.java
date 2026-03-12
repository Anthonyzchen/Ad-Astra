package earth.terrarium.adastra.common.items.armor.base;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public class CustomDyeableArmorItem extends ArmorItem {

    public CustomDyeableArmorItem(Holder<ArmorMaterial> armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }

    // Makes the default color white instead of brown
    public static int getColor(ItemStack stack) {
        return DyedItemColor.getOrDefault(stack, 0xFFFFFFFF);
    }
}
