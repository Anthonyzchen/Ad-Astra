package earth.terrarium.adastra.mixins.neoforge.common;

import earth.terrarium.adastra.common.items.rendered.RenderedBlockItem;
import earth.terrarium.adastra.common.items.rendered.RenderedItem;
import earth.terrarium.adastra.common.items.rendered.TooltipRenderedBlockItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;

// TODO: 1.21.11 - Both IClientItemExtensions and BlockEntityWithoutLevelRenderer were removed.
// Custom item rendering in NeoForge now uses RegisterClientExtensionsEvent or SpecialModelRenderer.
// This mixin is currently a no-op stub. The custom item rendering logic from
// AdAstraClientNeoForge.ITEM_RENDERERS needs to be migrated to the new NeoForge API.
// See: https://docs.neoforged.net/ for the updated item rendering approach.
@Mixin({RenderedItem.class, RenderedBlockItem.class, TooltipRenderedBlockItem.class})
public abstract class RenderedItemMixin extends Item {

    public RenderedItemMixin(Properties properties) {
        super(properties);
    }
}
