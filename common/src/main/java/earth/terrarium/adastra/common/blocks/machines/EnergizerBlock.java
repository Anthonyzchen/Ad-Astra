package earth.terrarium.adastra.common.blocks.machines;

import earth.terrarium.adastra.common.blockentities.machines.EnergizerBlockEntity;
import earth.terrarium.adastra.common.blocks.base.MachineBlock;
import earth.terrarium.adastra.common.registry.ModItems;
import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

@SuppressWarnings("deprecation")
public class EnergizerBlock extends MachineBlock {

    public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 5);

    public EnergizerBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(LIT, false)
                .setValue(POWER, 0));
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide)
            return ItemInteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof EnergizerBlockEntity entity)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (entity.getItem(0).isEmpty() && !stack.isEmpty() && stack.getCount() == 1) {
            player.setItemInHand(hand, ItemStack.EMPTY);
            entity.setItem(0, stack);
            return ItemInteractionResult.SUCCESS;
        } else if (stack.isEmpty()) {
            player.setItemInHand(hand, entity.getItem(0));
            entity.clearContent();
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWER);
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof EnergizerBlockEntity entity) {
            return (int) (entity.getEnergyStorage().getStoredEnergy()
                    / (float) entity.getEnergyStorage().getMaxCapacity() * 15);
        }
        return 0;
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (!(blockEntity instanceof EnergizerBlockEntity entity))
            return super.getDrops(blockState, builder);
        ItemStackHolder stack = new ItemStackHolder(ModItems.ENERGIZER.get().getDefaultInstance());
        EnergyContainer itemEnergyContainer = EnergyContainer.of(stack);
        if (itemEnergyContainer == null)
            return super.getDrops(blockState, builder);
        itemEnergyContainer.setEnergy(entity.getEnergyStorage().getStoredEnergy());
        CustomData.update(DataComponents.CUSTOM_DATA, stack.getStack(), tag -> {
            CompoundTag botariumTag;
            if (tag.contains(Botarium.BOTARIUM_DATA, Tag.TAG_COMPOUND)) {
                botariumTag = tag.getCompound(Botarium.BOTARIUM_DATA);
            } else {
                botariumTag = new CompoundTag();
                tag.put(Botarium.BOTARIUM_DATA, botariumTag);
            }
            botariumTag.putLong("Energy", entity.getEnergyStorage().getStoredEnergy());
        });
        return List.of(stack.getStack());
    }
}