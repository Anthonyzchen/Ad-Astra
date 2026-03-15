package earth.terrarium.adastra.client.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.client.ClientPlatformUtils;
import earth.terrarium.adastra.common.blockentities.SlidingDoorBlockEntity;
import earth.terrarium.adastra.common.blocks.SlidingDoorBlock;
import earth.terrarium.adastra.common.registry.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class SlidingDoorBlockEntityRenderer implements BlockEntityRenderer<SlidingDoorBlockEntity, SlidingDoorBlockEntityRenderer.SlidingDoorRenderState> {

    public static class SlidingDoorRenderState extends BlockEntityRenderState {
        public float slide;
        public Direction direction;
        public boolean flipSecondDoor;
    }

    @Override
    public SlidingDoorRenderState createRenderState() {
        return new SlidingDoorRenderState();
    }

    @Override
    public void extractRenderState(SlidingDoorBlockEntity entity, SlidingDoorRenderState state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        state.slide = Mth.lerp(partialTick, entity.lastSlideTicks(), entity.slideTicks()) / 81.0f;
        state.direction = entity.getBlockState().getValue(SlidingDoorBlock.FACING);
        state.flipSecondDoor = ModBlocks.SIMPLE_SLIDING_DOORS.stream().noneMatch(block -> block.get().equals(entity.getBlockState().getBlock()));
    }

    @Override
    public void submit(SlidingDoorRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        // TODO: 1.21.11 - Migrate to new SubmitNodeCollector rendering pipeline.
        // The old MultiBufferSource-based rendering needs to be converted to the new
        // deferred rendering system. For now, this is a stub.
    }
}
