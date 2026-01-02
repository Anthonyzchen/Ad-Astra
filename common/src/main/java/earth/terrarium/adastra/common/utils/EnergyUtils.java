package earth.terrarium.adastra.common.utils;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.adastra.common.config.machines.MachineTypeConfigObject;
import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.energy.impl.ExtractOnlyEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.InsertOnlyEnergyContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.nbt.CompoundTag;

public class EnergyUtils {

    public static InsertOnlyEnergyContainer machineInsertOnlyEnergy(MachineTypeConfigObject config) {
        return new InsertOnlyEnergyContainer(config.energyCapacity, config.maxEnergyInOut);
    }

    public static ExtractOnlyEnergyContainer machineExtractOnlyEnergy(MachineTypeConfigObject config) {
        return new ExtractOnlyEnergyContainer(config.energyCapacity, config.maxEnergyInOut);
    }

    public static ItemStack energyFilledItem(RegistryEntry<Item> item) {
        return energyFilledItem(item.get().getDefaultInstance());
    }

    public static ItemStack energyFilledItem(ItemStack stack) {
        var holder = new ItemStackHolder(stack);
        var container = EnergyContainer.of(holder);
        if (container != null) {
            container.setEnergy(container.getMaxCapacity());
            CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
                CompoundTag botariumData = tag.getCompound(Botarium.BOTARIUM_DATA);
                botariumData.putLong("Energy", container.getMaxCapacity());
                tag.put(Botarium.BOTARIUM_DATA, botariumData);
            });
        }
        return stack;
    }
}
