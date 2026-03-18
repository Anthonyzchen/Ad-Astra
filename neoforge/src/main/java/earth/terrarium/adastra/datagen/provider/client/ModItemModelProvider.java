package earth.terrarium.adastra.datagen.provider.client;

import earth.terrarium.adastra.AdAstra;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

// TODO: 1.21.11 - NeoForge removed ItemModelProvider and ExistingFileHelper.
// Item model datagen needs to be reimplemented using vanilla datagen or manual JSON generation.
public class ModItemModelProvider implements DataProvider {

    public static final Identifier RENDERED_ITEM = Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "item/rendered_item");

    public ModItemModelProvider(PackOutput output) {
    }

    @Override
    public CompletableFuture<?> run(@NotNull CachedOutput cache) {
        AdAstra.LOGGER.warn("ModItemModelProvider is not yet implemented for 1.21.11 - NeoForge model generators were removed");
        return CompletableFuture.completedFuture(null);
    }

    @NotNull
    @Override
    public String getName() {
        return "Ad Astra Item Models";
    }
}
