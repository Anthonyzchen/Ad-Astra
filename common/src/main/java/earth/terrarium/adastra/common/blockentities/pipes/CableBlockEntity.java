package earth.terrarium.adastra.common.blockentities.pipes;

import earth.terrarium.adastra.common.blocks.properties.PipeProperty;
import earth.terrarium.common_storage_lib.energy.EnergyApi;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CableBlockEntity extends PipeBlockEntity {

    public CableBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void addNode(@NotNull BlockEntity entity, PipeProperty pipeProperty, Direction direction, BlockPos pos) {
        if (pipeProperty.isNone()) return;
        // direction is from the pipe toward the entity; the entity's face is the opposite
        var container = EnergyApi.BLOCK.find(entity.getLevel(), pos, direction.getOpposite());
        if (container == null) return;

        if (!pipeProperty.isInsert() && (pipeProperty.isExtract() || container.extract(container.getStoredAmount(), true) > 0)) {
            sources.put(pos, direction);
        } else if (pipeProperty.isNormal() || pipeProperty.isInsert()) {
            consumers.put(pos, direction);
        }
    }

    @Override
    public void moveContents(long transferRate, @NotNull BlockEntity source, @NotNull BlockEntity consumer, Direction sourceDirection, Direction consumerDirection) {
        // sourceDirection/consumerDirection are from the pipe toward the entity; use opposite to get the entity's face
        var sourceContainer = EnergyApi.BLOCK.find(source.getLevel(), source.getBlockPos(), sourceDirection.getOpposite());
        if (sourceContainer == null) return;
        var consumerContainer = EnergyApi.BLOCK.find(consumer.getLevel(), consumer.getBlockPos(), consumerDirection.getOpposite());
        if (consumerContainer == null) return;
        long toTransfer = Math.min(transferRate, sourceContainer.getStoredAmount());
        long extracted = sourceContainer.extract(toTransfer, true);
        if (extracted > 0) {
            long inserted = consumerContainer.insert(extracted, false);
            if (inserted > 0) {
                sourceContainer.extract(inserted, false);
            }
        }
    }

    @Override
    public boolean isValid(@NotNull BlockEntity entity, Direction direction) {
        // direction is from the pipe toward the entity; the entity's face is the opposite
        return EnergyApi.BLOCK.find(entity.getLevel(), entity.getBlockPos(), direction.getOpposite()) != null;
    }
}
