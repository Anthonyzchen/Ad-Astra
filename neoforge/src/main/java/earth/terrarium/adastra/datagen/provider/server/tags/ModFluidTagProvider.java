package earth.terrarium.adastra.datagen.provider.server.tags;

import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.common.registry.ModFluids;
import earth.terrarium.adastra.common.tags.ModFluidTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import java.util.concurrent.CompletableFuture;

public class ModFluidTagProvider extends TagsProvider<Fluid> {

    public ModFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, Registries.FLUID, completableFuture);
    }

    private net.minecraft.tags.TagBuilder tag(TagKey<Fluid> tagKey) {
        return getOrCreateRawBuilder(tagKey);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        add(ModFluidTags.OXYGEN, ModFluids.OXYGEN.get(), "oxygen", "oxygen");
        add(ModFluidTags.HYDROGEN, ModFluids.HYDROGEN.get(), "hydrogen", "hydrogen");
        add(ModFluidTags.OIL, ModFluids.OIL.get(), "oil", "oil");
        add(ModFluidTags.FUEL, ModFluids.FUEL.get(), "fuel", "fuel");
        add(ModFluidTags.FUEL, ModFluids.CRYO_FUEL.get());
        add(ModFluidTags.EFFICIENT_FUEL, ModFluids.CRYO_FUEL.get());

        add(ModFluidTags.TIER_1_ROCKET_FUEL, ModFluidTags.FUEL);
        add(ModFluidTags.TIER_2_ROCKET_FUEL, ModFluidTags.FUEL);
        add(ModFluidTags.TIER_3_ROCKET_FUEL, ModFluidTags.FUEL);
        add(ModFluidTags.TIER_4_ROCKET_FUEL, ModFluidTags.FUEL);
        add(ModFluidTags.TIER_1_ROVER_FUEL, ModFluidTags.FUEL);

        add(ModFluidTags.ZIP_GUN_PROPELLANTS, ModFluidTags.OXYGEN);
        add(ModFluidTags.ZIP_GUN_PROPELLANTS, ModFluidTags.HYDROGEN);

        add(ModFluidTags.FREEZES_IN_SPACE, Fluids.WATER);
        add(ModFluidTags.EVAPORATES_IN_SPACE, Fluids.WATER);

        tag(ModFluidTags.FUEL).add(TagEntry.optionalTag(Identifier.parse("c:diesel")));
        tag(ModFluidTags.FUEL).add(TagEntry.optionalTag(Identifier.parse("forge:diesel")));
        tag(ModFluidTags.FUEL).add(TagEntry.optionalTag(Identifier.parse("c:biodiesel")));
        tag(ModFluidTags.FUEL).add(TagEntry.optionalTag(Identifier.parse("forge:biodiesel")));
        tag(ModFluidTags.FUEL).add(TagEntry.optionalTag(Identifier.parse("forge:biodiesel")));

        tag(ModFluidTags.OIL).add(TagEntry.optionalElement(Identifier.parse("techreborn:oil")));
        tag(ModFluidTags.OIL).add(TagEntry.optionalTag(Identifier.parse("forge:crude_oil")));
        tag(ModFluidTags.OIL).add(TagEntry.optionalTag(Identifier.parse("c:crude_oil")));
    }

    private void add(TagKey<Fluid> tag, Fluid fluid) {
        tag(tag).add(element(fluid));
    }

    private void add(TagKey<Fluid> tag, TagKey<Fluid> fluid) {
        tag(tag).addTag(fluid.location());
    }

    private void add(TagKey<Fluid> tag, Fluid fluid, String fabricCommonTag, String forgeCommonTag) {
        add(tag, fluid);
        addFabricTag(fluid, tag, fabricCommonTag);
        addForgeTag(fluid, tag, forgeCommonTag);
    }

    private void addFabricTag(Fluid fluid, TagKey<Fluid> tag, String fabricCommonTag) {
        tag(tag).add(TagEntry.optionalTag(Identifier.fromNamespaceAndPath("c", fabricCommonTag)));

        var commonTag = TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath("c", fabricCommonTag));
        tag(commonTag).add(element(fluid));
    }

    private void addForgeTag(Fluid fluid, TagKey<Fluid> tag, String forgeCommonTag) {
        tag(tag).add(TagEntry.optionalTag(Identifier.fromNamespaceAndPath("forge", forgeCommonTag)));

        var commonTag = TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath("forge", forgeCommonTag));
        tag(commonTag).add(element(fluid));
    }

    private static TagEntry element(Fluid fluid) {
        return TagEntry.element(loc(fluid));
    }

    private static Identifier loc(Fluid fluid) {
        return BuiltInRegistries.FLUID.getKey(fluid);
    }
}
