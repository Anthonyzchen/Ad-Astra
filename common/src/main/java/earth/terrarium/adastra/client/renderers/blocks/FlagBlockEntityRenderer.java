package earth.terrarium.adastra.client.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import earth.terrarium.adastra.client.renderers.textures.FlagImageTexture;
import earth.terrarium.adastra.client.renderers.textures.FlagUrlTexture;
import earth.terrarium.adastra.common.blockentities.flag.FlagBlockEntity;
import earth.terrarium.adastra.common.blockentities.flag.content.FlagContent;
import earth.terrarium.adastra.common.blockentities.flag.content.ImageContent;
import earth.terrarium.adastra.common.blockentities.flag.content.UrlContent;
import earth.terrarium.adastra.common.blocks.FlagBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.object.skull.SkullModel;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class FlagBlockEntityRenderer implements BlockEntityRenderer<FlagBlockEntity, BlockEntityRenderState> {

    @Override
    public void extractRenderState(FlagBlockEntity entity, BlockEntityRenderState state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        // TODO: 1.21.11 - Extract flag-specific render data into a custom render state
    }

    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        // TODO: 1.21.11 - Migrate flag rendering to new SubmitNodeCollector pipeline.
        // The old render method used MultiBufferSource for VertexConsumer-based rendering
        // which needs to be adapted to the new deferred rendering system.
    }

    private static void renderQuad(Matrix4f pose, PoseStack.Pose poseNormal, Vec3i normal, VertexConsumer consumer, float width, float height, float u, float v, float uWidth, float vHeight, int light, int overlay) {
        consumer.addVertex(pose, 0, 0, 0).setColor(255, 255, 255, 255).setUv(u, v).setOverlay(overlay).setUv2(light & 0xFFFF, light >> 16 & 0xFFFF).setNormal(poseNormal, normal.getX(), normal.getY(), normal.getZ());
        consumer.addVertex(pose, 0, height, 0).setColor(255, 255, 255, 255).setUv(u, v + vHeight).setOverlay(overlay).setUv2(light & 0xFFFF, light >> 16 & 0xFFFF).setNormal(poseNormal, normal.getX(), normal.getY(), normal.getZ());
        consumer.addVertex(pose, width, height, 0).setColor(255, 255, 255, 255).setUv(u + uWidth, v + vHeight).setOverlay(overlay).setUv2(light & 0xFFFF, light >> 16 & 0xFFFF).setNormal(poseNormal, normal.getX(), normal.getY(), normal.getZ());
        consumer.addVertex(pose, width, 0, 0).setColor(255, 255, 255, 255).setUv(u + uWidth, v).setOverlay(overlay).setUv2(light & 0xFFFF, light >> 16 & 0xFFFF).setNormal(poseNormal, normal.getX(), normal.getY(), normal.getZ());
    }

    private static RenderType getFlagImage(FlagContent content) {
        Identifier id = content.toTexture();
        TextureManager manager = Minecraft.getInstance().getTextureManager();
        AbstractTexture texture = manager.getTexture(id, MissingTextureAtlasSprite.getTexture());
        if (texture == MissingTextureAtlasSprite.getTexture()) {
            if (content instanceof UrlContent url) {
                manager.register(id, new FlagUrlTexture(url.url()));
            } else if (content instanceof ImageContent image) {
                manager.register(id, new FlagImageTexture(image.data()));
            }
        }
        return RenderType.entitySolid(id);
    }
}
