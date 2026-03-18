package earth.terrarium.adastra.datagen;

import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.datagen.provider.base.ModRegistryProvider;
import earth.terrarium.adastra.datagen.provider.base.StructureUpdater;
import earth.terrarium.adastra.datagen.provider.client.*;
import earth.terrarium.adastra.datagen.provider.server.ModAdvancementProvider;
import earth.terrarium.adastra.datagen.provider.server.ModLootTableProvider;
import earth.terrarium.adastra.datagen.provider.server.ModPlanetProvider;
import earth.terrarium.adastra.datagen.provider.server.ModRecipeProvider;
import earth.terrarium.adastra.datagen.provider.server.tags.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public final class AdAstraDataGenerator {

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        PackOutput packOutput = generator.getPackOutput();

        event.addProvider(new ModLangProvider(packOutput));
        event.addProvider(new ModBlockStateProvider(packOutput));
        event.addProvider(new ModHighlightBlockStateProvider(packOutput));
        event.addProvider(new ModItemModelProvider(packOutput));
        event.addProvider(new ModPlanetRendererProvider(packOutput));

        event.addProvider(new ModRegistryProvider(packOutput, lookupProvider));
        event.addProvider(new ModPlanetProvider(packOutput));
        event.addProvider(new ModRecipeProvider.Runner(packOutput, lookupProvider));
        event.addProvider(new ModLootTableProvider(packOutput, lookupProvider));
        event.addProvider(new ModAdvancementProvider(packOutput, lookupProvider));

        event.addProvider(new ModBlockTagProvider(packOutput, lookupProvider));
        event.addProvider(new ModItemTagProvider(packOutput, lookupProvider));
        event.addProvider(new ModFluidTagProvider(packOutput, lookupProvider));
        event.addProvider(new ModEntityTypeTagProvider(packOutput, lookupProvider));
        event.addProvider(new ModPaintingVariantTagProvider(packOutput, lookupProvider));
        event.addProvider(new ModDamageSourceTagProvider(packOutput, lookupProvider));
        event.addProvider(new ModBiomeTagProvider(packOutput, lookupProvider));

        event.addProvider(new StructureUpdater("structures", AdAstra.MOD_ID, packOutput));
    }
}
