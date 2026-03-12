package earth.terrarium.adastra.mixins.neoforge.common;

import earth.terrarium.adastra.client.neoforge.AdAstraClientNeoForge;
import earth.terrarium.adastra.common.items.rendered.RenderedBlockItem;
import earth.terrarium.adastra.common.items.rendered.RenderedItem;
import earth.terrarium.adastra.common.items.rendered.TooltipRenderedBlockItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

// TODO: 1.21.1 NeoForge removed IClientItemExtensions - custom item rendering now uses
// RegisterClientExtensionsEvent. This mixin needs to be reworked to use the new
// NeoForge item rendering API. Consider moving this logic to RegisterClientExtensionsEvent
// in the mod initializer instead of using a mixin.
@Mixin({RenderedItem.class, RenderedBlockItem.class, TooltipRenderedBlockItem.class})
public abstract class RenderedItemMixin extends Item {

    public RenderedItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return AdAstraClientNeoForge.ITEM_RENDERERS.get(RenderedItemMixin.this);
            }
        });
    }
}
