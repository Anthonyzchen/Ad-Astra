package earth.terrarium.adastra.client.utils;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

/**
 * Replacement for botarium's ClientHooks.
 * Uses vanilla Minecraft registration methods directly.
 * NOTE: EntityRenderers.register() and ItemProperties.register() are platform-specific
 * (private/restricted in the common Architectury module). Entity renderer and item property
 * registrations must be handled by platform-specific code (Fabric/NeoForge modules).
 */
public final class ClientRegistrationHooks {

    private ClientRegistrationHooks() {}

    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> void registerBlockEntityRenderers(BlockEntityType<T> type, BlockEntityRendererProvider<T> provider) {
        BlockEntityRenderers.register(type, provider);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> void registerEntityRenderer(RegistryEntry<EntityType<T>> type, EntityRendererProvider<T> provider) {
        // TODO: Platform-specific registration - EntityRenderers.register() is not accessible in common module.
        // This must be called from Fabric/NeoForge platform code instead.
        // On Fabric: EntityRendererRegistry.register(type.get(), provider)
        // On NeoForge: handled via EntityRenderersEvent.RegisterRenderers event
    }

    public static void registerItemProperty(Item item, ResourceLocation id, ClampedItemPropertyFunction function) {
        // TODO: Platform-specific registration - ItemProperties.register() is not accessible in common module.
        // This must be called from Fabric/NeoForge platform code instead.
    }

    public static void setRenderLayer(Block block, RenderType renderType) {
        // TODO: In 1.21.1, render layers are determined by the block model JSON, not set programmatically.
        // No-op - ensure block models specify the correct render type in their JSON definitions.
    }
}
