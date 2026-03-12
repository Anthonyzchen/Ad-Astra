package earth.terrarium.adastra.common.items;

import earth.terrarium.adastra.common.constants.ConstantComponents;
import earth.terrarium.adastra.common.utils.FluidUtils;
import earth.terrarium.adastra.common.utils.TooltipUtils;
import earth.terrarium.common_storage_lib.fluid.impl.SimpleFluidStorage;
// TODO: Migrate to CSL
// import earth.terrarium.botarium.common.fluid.FluidApi;
// import earth.terrarium.botarium.common.fluid.base.FluidHolder;
// import earth.terrarium.botarium.common.fluid.utils.ClientFluidHooks;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GasTankItem extends Item {

    private static final long BUCKET = 81000L;

    private final long tankSize;
    private final long distributionAmount;

    public GasTankItem(Properties properties, long tankSize, long distributionAmount) {
        super(properties);
        this.tankSize = tankSize;
        this.distributionAmount = distributionAmount;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (FluidUtils.hasFluid(stack)) {
            player.startUsingItem(usedHand);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int remainingUseDuration) {
        // TODO: Migrate to CSL - re-implement fluid distribution from gas tank to inventory items
        if (level.isClientSide()) return;
        if (!(entity instanceof Player player)) return;
        var container = getFluidContainer(stack);
        if (container == null || container.get(0).getAmount() == 0) return;
        if (entity.tickCount % 4 == 0) {
            level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_DRINK, player.getSoundSource(), 1.0F, 1.0F);
        }
    }

    // TODO: Migrate to CSL - re-implement fluid distribution
    public boolean distributeSequential(ItemStack from, Object container, Inventory inventory) {
        return false;
    }

    public SimpleFluidStorage getFluidContainer(ItemStack holder) {
        return new SimpleFluidStorage(1, tankSize * BUCKET / 1000L);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(TooltipUtils.getFluidComponent(FluidUtils.getTank(stack), FluidUtils.getCapacity(stack)));
        tooltipComponents.add(TooltipUtils.getMaxFluidOutComponent(distributionAmount * BUCKET / 1000L));
        TooltipUtils.addDescriptionComponent(tooltipComponents, ConstantComponents.GAS_TANK_INFO);
    }

    public int getUseDuration(@NotNull ItemStack stack) {
        return 72_000;
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return FluidUtils.hasFluid(stack);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        var fluidContainer = getFluidContainer(stack);
        return (int) (((double) fluidContainer.get(0).getAmount() / (double) fluidContainer.get(0).getLimit(fluidContainer.get(0).getResource())) * 13);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        // TODO: Migrate to CSL - replace ClientFluidHooks.getFluidColor
        return 0xFFFFFF;
    }
}
