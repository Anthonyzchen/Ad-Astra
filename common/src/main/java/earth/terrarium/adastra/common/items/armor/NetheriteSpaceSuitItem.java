package earth.terrarium.adastra.common.items.armor;

import earth.terrarium.adastra.common.constants.ConstantComponents;
import earth.terrarium.adastra.common.registry.ModFluids;
import earth.terrarium.adastra.common.utils.FluidUtils;
import earth.terrarium.adastra.common.utils.TooltipUtils;
// FluidConstants removed - using BUCKET from parent class
import net.minecraft.network.chat.Component;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NetheriteSpaceSuitItem extends SpaceSuitItem {

    public NetheriteSpaceSuitItem(Holder<ArmorMaterial> material, Type type, long tankSize, Properties properties) {
        super(material, type, tankSize, properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
        tooltipComponents.add(TooltipUtils.getFluidComponent(
            FluidUtils.getTank(stack),
            tankSize * BUCKET / 1000L,
            ModFluids.OXYGEN.get()));
        TooltipUtils.addDescriptionComponent(tooltipComponents, ConstantComponents.NETHERITE_SPACE_SUIT_INFO);
    }
}
