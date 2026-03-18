package earth.terrarium.adastra.client.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import earth.terrarium.adastra.common.blockentities.machines.EnergizerBlockEntity;
import earth.terrarium.adastra.common.blocks.base.MachineBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;


public class EnergizerBlockEntityRenderer implements BlockEntityRenderer<EnergizerBlockEntity, BlockEntityRenderState> {

    @Override
    public BlockEntityRenderState createRenderState() {
        return new BlockEntityRenderState();
    }

    @Override
    public void extractRenderState(EnergizerBlockEntity entity, BlockEntityRenderState state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        // TODO: 1.21.11 - Extract energizer-specific render data into a custom render state
    }

    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        // TODO: 1.21.11 - Migrate energizer rendering to new SubmitNodeCollector pipeline.
        // The old render method used MultiBufferSource and ItemRenderer.renderStatic
        // which needs to be adapted to the new deferred rendering system.
    }
}
