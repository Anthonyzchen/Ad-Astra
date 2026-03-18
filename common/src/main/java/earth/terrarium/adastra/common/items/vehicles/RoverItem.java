package earth.terrarium.adastra.common.items.vehicles;

import earth.terrarium.adastra.common.constants.ConstantComponents;
import earth.terrarium.adastra.common.entities.vehicles.Rover;
import earth.terrarium.adastra.common.utils.TooltipUtils;
// TODO: Migrate to CSL
// import earth.terrarium.botarium.common.fluid.FluidApi;
// import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RoverItem extends VehicleItem {

    public RoverItem(Supplier<EntityType<?>> type, Properties properties) {
        super(type, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        if (level.isClientSide()) return InteractionResult.CONSUME;
        var pos = context.getClickedPos();
        var stack = context.getItemInHand();

        level.playSound(context.getPlayer(), pos, SoundEvents.LODESTONE_PLACE, SoundSource.BLOCKS, 1, 1);
        var vehicle = type().create(level, EntitySpawnReason.SPAWN_ITEM_USE);
        if (vehicle == null) return InteractionResult.PASS;
        vehicle.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
        vehicle.setYRot(context.getRotation() + 270);
        level.addFreshEntity(vehicle);

        if (vehicle instanceof Rover rover) {
            // TODO: Migrate to CSL - re-implement fluid transfer from item to entity
        }

        stack.shrink(1);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, context, tooltipDisplay, consumer, isAdvanced);
        TooltipUtils.addDescriptionComponent(consumer, ConstantComponents.ROVER_INFO);
    }
}
