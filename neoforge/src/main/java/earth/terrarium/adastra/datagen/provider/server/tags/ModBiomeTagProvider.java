package earth.terrarium.adastra.datagen.provider.server.tags;

import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.common.tags.ModBiomeTags;
import earth.terrarium.adastra.datagen.provider.server.registry.ModBiomeDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends TagsProvider<Biome> {

    public ModBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, Registries.BIOME, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModBiomeTags.HAS_ACID_RAIN).addOptionalElement(ModBiomeDataProvider.VENUS_WASTELANDS.identifier());
        tag(ModBiomeTags.HAS_ACID_RAIN).addOptionalElement(ModBiomeDataProvider.INFERNAL_VENUS_BARRENS.identifier());

        tag(ModBiomeTags.OIL_WELL).addTag(BiomeTags.HAS_OCEAN_MONUMENT.location());
        tag(ModBiomeTags.OIL_WELL).addTag(BiomeTags.IS_OCEAN.location());
        tag(ModBiomeTags.OIL_WELL).addOptionalTag(Identifier.parse("c:ocean"));

        tag(ModBiomeTags.LUNARIAN_VILLAGE).addOptionalElement(ModBiomeDataProvider.LUNAR_WASTELANDS.identifier());
        tag(ModBiomeTags.MOON_DUNGEON).addOptionalElement(ModBiomeDataProvider.LUNAR_WASTELANDS.identifier());
        tag(ModBiomeTags.MARS_TEMPLE).addOptionalElement(ModBiomeDataProvider.MARTIAN_WASTELANDS.identifier());
        tag(ModBiomeTags.MARS_TEMPLE).addOptionalElement(ModBiomeDataProvider.MARTIAN_CANYON_CREEK.identifier());
        tag(ModBiomeTags.PYGRO_TOWER).addOptionalElement(ModBiomeDataProvider.VENUS_WASTELANDS.identifier());
        tag(ModBiomeTags.PYGRO_VILLAGE).addOptionalElement(ModBiomeDataProvider.VENUS_WASTELANDS.identifier());
        tag(ModBiomeTags.VENUS_BULLET).addOptionalElement(ModBiomeDataProvider.VENUS_WASTELANDS.identifier());
    }

    private TagBuilder tag(TagKey<Biome> tagKey) {
        return getOrCreateRawBuilder(tagKey);
    }
}
