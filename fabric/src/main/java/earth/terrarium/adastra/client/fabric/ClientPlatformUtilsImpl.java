package earth.terrarium.adastra.client.fabric;

import earth.terrarium.adastra.client.ClientPlatformUtils;
import earth.terrarium.adastra.client.dimension.ModDimensionSpecialEffects;
import earth.terrarium.adastra.common.items.armor.JetSuitItem;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelManager;
// TODO: 1.21.11 - ModelIdentifier may have been removed or renamed
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import com.mojang.blaze3d.vertex.VertexConsumer;

import java.util.Map;

public class ClientPlatformUtilsImpl {

    public static BlockStateModel getModel(ModelManager dispatcher, Identifier id) {
        // TODO: 1.21.11 - BakedModel is now BlockStateModel. FabricBakedModelManager may not exist.
        // Need to find the new Fabric API for model lookup or use vanilla ModelManager methods.
        return null; // Placeholder - needs Fabric API update
    }

    public static void registerArmor(Identifier texture, ModelLayerLocation layer, ClientPlatformUtils.ArmorFactory factory, Item... items) {
        ArmorRenderer.register((poseStack, buffer, stack, entity, slot, packedLight, original) -> {
            var root = Minecraft.getInstance().getEntityModels().bakeLayer(layer);
            var model = factory.create(root, slot, stack, original);

            if (stack.getItem() instanceof JetSuitItem suit) {
                suit.spawnParticles(entity.level(), entity, original, stack);
            }

            VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(texture));
            model.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, -1);
        }, items);
    }

    public static void registerPlanetRenderers(Map<ResourceKey<Level>, ModDimensionSpecialEffects> renderers) {
        AdAstraClientFabric.registerDimensionEffects(renderers);
    }
}
