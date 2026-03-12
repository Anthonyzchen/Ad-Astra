package earth.terrarium.adastra.common.blockentities.pipes;

import earth.terrarium.adastra.common.blocks.properties.PipeProperty;
import earth.terrarium.adastra.common.utils.FluidUtils;
import earth.terrarium.common_storage_lib.fluid.FluidApi;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FluidPipeBlockEntity extends PipeBlockEntity {

    public FluidPipeBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void addNode(@NotNull BlockEntity entity, PipeProperty pipeProperty, Direction direction, BlockPos pos) {
        // TODO: CSL migration - rework fluid pipe node detection for CSL API
        if (pipeProperty.isNone()) return;
        if (pipeProperty.isNormal() || pipeProperty.isInsert()) {
            consumers.put(pos, direction);
        } else if (pipeProperty.isExtract()) {
            sources.put(pos, direction);
        }
    }

    @Override
    public void moveContents(long transferRate, @NotNull BlockEntity source, @NotNull BlockEntity consumer, Direction sourceDirection, Direction consumerDirection) {
        CommonStorage<FluidResource> sourceStorage = FluidApi.BLOCK.find(source.getLevel(), source.getBlockPos(), sourceDirection);
        CommonStorage<FluidResource> consumerStorage = FluidApi.BLOCK.find(consumer.getLevel(), consumer.getBlockPos(), consumerDirection);
        if (sourceStorage == null || consumerStorage == null) return;

        for (int i = 0; i < sourceStorage.size(); i++) {
            FluidResource resource = sourceStorage.get(i).getResource();
            if (resource.isBlank()) continue;
            long extracted = sourceStorage.extract(resource, transferRate, true);
            if (extracted > 0) {
                long inserted = FluidUtils.insertFluidStorage(consumerStorage, resource, extracted, false);
                if (inserted > 0) {
                    sourceStorage.extract(resource, inserted, false);
                }
            }
            break; // Only transfer first non-blank fluid per tick
        }
    }

    @Override
    public boolean isValid(@NotNull BlockEntity entity, Direction direction) {
        return FluidApi.BLOCK.find(entity.getLevel(), entity.getBlockPos(), direction) != null;
    }
}
