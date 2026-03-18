package earth.terrarium.adastra.client.fabric;

import earth.terrarium.adastra.client.ClientPlatformUtils;
import earth.terrarium.adastra.client.dimension.ModDimensionSpecialEffects;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.Map;

public class ClientPlatformUtilsImpl {

    public static BlockStateModel getModel(ModelManager dispatcher, Identifier id) {
        // TODO: 1.21.11 - Model lookup API changed. Need to find the new approach.
        return null;
    }

    public static void registerArmor(Identifier texture, ModelLayerLocation layer, ClientPlatformUtils.ArmorFactory factory, Item... items) {
        // TODO: 1.21.11 - ArmorRenderer API changed significantly.
        // The render method now takes (PoseStack, OrderedRenderCommandQueue, ItemStack, HumanoidRenderState, EquipmentSlot, int, EntityModel)
        // instead of (PoseStack, MultiBufferSource, ItemStack, LivingEntity, EquipmentSlot, int, HumanoidModel).
        // The entity is no longer available during rendering - only the render state.
        // JetSuitItem.spawnParticles() needs the entity, so this needs rethinking.
    }

    public static void registerPlanetRenderers(Map<ResourceKey<Level>, ModDimensionSpecialEffects> renderers) {
        AdAstraClientFabric.registerDimensionEffects(renderers);
    }
}
