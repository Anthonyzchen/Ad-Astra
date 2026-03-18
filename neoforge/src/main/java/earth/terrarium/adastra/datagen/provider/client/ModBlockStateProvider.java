package earth.terrarium.adastra.datagen.provider.client;

import earth.terrarium.adastra.AdAstra;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

// TODO: 1.21.11 - NeoForge removed BlockStateProvider, ConfiguredModel, ModelFile, and ExistingFileHelper.
// Block state and model datagen needs to be reimplemented using vanilla datagen or manual JSON generation.
public class ModBlockStateProvider implements DataProvider {

    public ModBlockStateProvider(PackOutput output) {
    }

    @Override
    public CompletableFuture<?> run(@NotNull CachedOutput cache) {
        AdAstra.LOGGER.warn("ModBlockStateProvider is not yet implemented for 1.21.11 - NeoForge model generators were removed");
        return CompletableFuture.completedFuture(null);
    }

    @NotNull
    @Override
    public String getName() {
        return "Ad Astra Block States";
    }
}
