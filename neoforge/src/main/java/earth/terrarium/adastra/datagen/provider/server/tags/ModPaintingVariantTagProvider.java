package earth.terrarium.adastra.datagen.provider.server.tags;


import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.common.tags.ModPaintingVariantTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagEntry;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;

import java.util.concurrent.CompletableFuture;

public class ModPaintingVariantTagProvider extends TagsProvider<PaintingVariant> {

    private static final String[] PAINTING_VARIANT_NAMES = {
        "mercury", "moon", "pluto", "earth", "glacio", "mars", "venus",
        "jupiter", "neptune", "uranus", "saturn", "the_milky_way", "alpha_centauri", "sun"
    };

    public ModPaintingVariantTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, Registries.PAINTING_VARIANT, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        var builder = getOrCreateRawBuilder(ModPaintingVariantTags.SPACE_PAINTINGS);
        for (String name : PAINTING_VARIANT_NAMES) {
            builder.add(TagEntry.element(Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, name)));
        }
    }
}
