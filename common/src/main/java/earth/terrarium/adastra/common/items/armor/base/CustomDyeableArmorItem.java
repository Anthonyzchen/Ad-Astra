package earth.terrarium.adastra.common.items.armor.base;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

// import net.minecraft.world.item.DyeableLeatherItem;

/**
 * Custom dyeable armor item for MC 1.21.
 * In 1.21, DyeableArmorItem was removed and dyeable behavior is handled via
 * data components.
 */
public class CustomDyeableArmorItem extends ArmorItem /* implements DyeableLeatherItem */ {

    public CustomDyeableArmorItem(Holder<ArmorMaterial> armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties.component(DataComponents.DYED_COLOR, new DyedItemColor(0xFFFFFF, true)));
    }

    // Makes the default color white instead of brown
    // @Override
    public int getColor(ItemStack stack) {
        DyedItemColor dyedColor = stack.get(DataComponents.DYED_COLOR);
        if (dyedColor != null) {
            int color = dyedColor.rgb();
            return color == 0xa06540 ? 0xFFFFFF : color;
        }
        return 0xFFFFFF;
    }
}
