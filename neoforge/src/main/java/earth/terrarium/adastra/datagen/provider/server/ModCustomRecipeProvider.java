package earth.terrarium.adastra.datagen.provider.server;

import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.api.planets.Planet;
import earth.terrarium.adastra.common.recipes.base.IngredientHolder;
import earth.terrarium.adastra.common.registry.ModFluids;
import earth.terrarium.adastra.common.registry.ModItems;
import earth.terrarium.adastra.common.tags.ModFluidTags;
import earth.terrarium.adastra.common.tags.ModItemTags;
import earth.terrarium.adastra.datagen.builder.*;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.resources.fluid.ingredient.FluidIngredient;
import earth.terrarium.common_storage_lib.resources.fluid.ingredient.SizedFluidIngredient;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class ModCustomRecipeProvider {

    public static final Identifier SPACE_STATION_STRUCTURE = Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "space_station");
    private static RecipeOutput output;
    private static ModRecipeProvider provider;

    private ModCustomRecipeProvider() {}

    public static void createRecipes(ModRecipeProvider recipeProvider) {
        ModCustomRecipeProvider.provider = recipeProvider;
        ModCustomRecipeProvider.output = recipeProvider.getOutput();

        createCompressing(100, 20, Items.IRON_INGOT, ModItems.IRON_PLATE.get().getDefaultInstance());
        createCompressing(800, 20, Items.IRON_BLOCK, new ItemStack(ModItems.IRON_PLATE.get(), 9));
        createCompressing(100, 20, ModItemTags.STEEL_INGOTS, ModItems.STEEL_PLATE.get().getDefaultInstance());
        createCompressing(800, 20, ModItemTags.STEEL_BLOCKS, new ItemStack(ModItems.STEEL_PLATE.get(), 9));
        createCompressing(100, 20, ModItemTags.DESH_INGOTS, ModItems.DESH_PLATE.get().getDefaultInstance());
        createCompressing(800, 20, ModItemTags.DESH_BLOCKS, new ItemStack(ModItems.DESH_PLATE.get(), 9));
        createCompressing(100, 20, ModItemTags.OSTRUM_INGOTS, ModItems.OSTRUM_PLATE.get().getDefaultInstance());
        createCompressing(800, 20, ModItemTags.OSTRUM_BLOCKS, new ItemStack(ModItems.OSTRUM_PLATE.get(), 9));
        createCompressing(100, 20, ModItemTags.CALORITE_INGOTS, ModItems.CALORITE_PLATE.get().getDefaultInstance());
        createCompressing(800, 20, ModItemTags.CALORITE_BLOCKS, new ItemStack(ModItems.CALORITE_PLATE.get(), 9));

        createAlloying(100, 20,
            List.of(Ingredient.of(Items.IRON_INGOT), provider.tagIngredient(ItemTags.COALS)),
            ModItems.STEEL_INGOT.get().getDefaultInstance(),
            List.of("iron_ingot", "coals")
        );

        createOxygenLoading(1, 30,
            new SizedFluidIngredient(FluidIngredient.of(FluidTags.WATER), 100),
            FluidResource.of(ModFluids.OXYGEN.get()), 4, "water");

        createOxygenLoading(1, 30,
            new SizedFluidIngredient(FluidIngredient.of(ModFluidTags.OXYGEN), 25),
            FluidResource.of(ModFluids.OXYGEN.get()), 25, "oxygen");

        createRefining(1, 30,
            new SizedFluidIngredient(FluidIngredient.of(ModFluidTags.OIL), 5),
            FluidResource.of(ModFluids.FUEL.get()), 5, "oil");

        createCryoFreezing(60, 40,
            ModItems.ICE_SHARD.get().getDefaultInstance(),
            FluidResource.of(ModFluids.CRYO_FUEL.get()), 25
        );

        createCryoFreezing(120, 40,
            Items.ICE.getDefaultInstance(),
            FluidResource.of(ModFluids.CRYO_FUEL.get()), 1
        );

        createCryoFreezing(120, 40,
            Items.PACKED_ICE.getDefaultInstance(),
            FluidResource.of(ModFluids.CRYO_FUEL.get()), 2
        );

        createCryoFreezing(120, 40,
            Items.BLUE_ICE.getDefaultInstance(),
            FluidResource.of(ModFluids.CRYO_FUEL.get()), 10
        );

        createNasaWorkbench(List.of(
                Ingredient.of(ModItems.ROCKET_NOSE_CONE.get()),
                provider.tagIngredient(ModItemTags.STEEL_BLOCKS),
                provider.tagIngredient(ModItemTags.STEEL_BLOCKS),
                provider.tagIngredient(ModItemTags.STEEL_BLOCKS),
                provider.tagIngredient(ModItemTags.STEEL_BLOCKS),
                provider.tagIngredient(ModItemTags.STEEL_BLOCKS),
                provider.tagIngredient(ModItemTags.STEEL_BLOCKS),
                Ingredient.of(ModItems.ROCKET_FIN.get()),
                Ingredient.of(ModItems.STEEL_TANK.get()),
                Ingredient.of(ModItems.STEEL_TANK.get()),
                Ingredient.of(ModItems.ROCKET_FIN.get()),
                Ingredient.of(ModItems.ROCKET_FIN.get()),
                Ingredient.of(ModItems.STEEL_ENGINE.get()),
                Ingredient.of(ModItems.ROCKET_FIN.get())
            ),
            ModItems.TIER_1_ROCKET.get().getDefaultInstance());

        createNasaWorkbench(List.of(
                Ingredient.of(ModItems.ROCKET_NOSE_CONE.get()),
                provider.tagIngredient(ModItemTags.DESH_BLOCKS),
                provider.tagIngredient(ModItemTags.DESH_BLOCKS),
                provider.tagIngredient(ModItemTags.DESH_BLOCKS),
                provider.tagIngredient(ModItemTags.DESH_BLOCKS),
                provider.tagIngredient(ModItemTags.DESH_BLOCKS),
                provider.tagIngredient(ModItemTags.DESH_BLOCKS),
                Ingredient.of(ModItems.ROCKET_FIN.get()),
                Ingredient.of(ModItems.DESH_TANK.get()),
                Ingredient.of(ModItems.DESH_TANK.get()),
                Ingredient.of(ModItems.ROCKET_FIN.get()),
                Ingredient.of(ModItems.ROCKET_FIN.get()),
                Ingredient.of(ModItems.DESH_ENGINE.get()),
                Ingredient.of(ModItems.ROCKET_FIN.get())
            ),
            ModItems.TIER_2_ROCKET.get().getDefaultInstance());

        createNasaWorkbench(List.of(
                Ingredient.of(ModItems.ROCKET_NOSE_CONE.get()),
                provider.tagIngredient(ModItemTags.OSTRUM_BLOCKS),
                provider.tagIngredient(ModItemTags.OSTRUM_BLOCKS),
                provider.tagIngredient(ModItemTags.OSTRUM_BLOCKS),
                provider.tagIngredient(ModItemTags.OSTRUM_BLOCKS),
                provider.tagIngredient(ModItemTags.OSTRUM_BLOCKS),
                provider.tagIngredient(ModItemTags.OSTRUM_BLOCKS),
                Ingredient.of(ModItems.ROCKET_FIN.get()),
                Ingredient.of(ModItems.OSTRUM_TANK.get()),
                Ingredient.of(ModItems.OSTRUM_TANK.get()),
                Ingredient.of(ModItems.ROCKET_FIN.get()),
                Ingredient.of(ModItems.ROCKET_FIN.get()),
                Ingredient.of(ModItems.OSTRUM_ENGINE.get()),
                Ingredient.of(ModItems.ROCKET_FIN.get())
            ),
            ModItems.TIER_3_ROCKET.get().getDefaultInstance());

        createNasaWorkbench(List.of(
                Ingredient.of(ModItems.ROCKET_NOSE_CONE.get()),
                provider.tagIngredient(ModItemTags.CALORITE_BLOCKS),
                provider.tagIngredient(ModItemTags.CALORITE_BLOCKS),
                provider.tagIngredient(ModItemTags.CALORITE_BLOCKS),
                provider.tagIngredient(ModItemTags.CALORITE_BLOCKS),
                provider.tagIngredient(ModItemTags.CALORITE_BLOCKS),
                provider.tagIngredient(ModItemTags.CALORITE_BLOCKS),
                Ingredient.of(ModItems.ROCKET_FIN.get()),
                Ingredient.of(ModItems.CALORITE_TANK.get()),
                Ingredient.of(ModItems.CALORITE_TANK.get()),
                Ingredient.of(ModItems.ROCKET_FIN.get()),
                Ingredient.of(ModItems.ROCKET_FIN.get()),
                Ingredient.of(ModItems.CALORITE_ENGINE.get()),
                Ingredient.of(ModItems.ROCKET_FIN.get())
            ),
            ModItems.TIER_4_ROCKET.get().getDefaultInstance());

        createSpaceStation(List.of(
                IngredientHolder.of(provider.tagIngredient(ModItemTags.IRON_PLATES), 64),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.STEEL_INGOTS), 32),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.DESH_INGOTS), 32),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.DESH_PLATES), 32)
            ),
            Planet.EARTH_ORBIT,
            SPACE_STATION_STRUCTURE);

        createSpaceStation(List.of(
                IngredientHolder.of(provider.tagIngredient(ModItemTags.IRON_PLATES), 64),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.STEEL_INGOTS), 32),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.DESH_INGOTS), 32),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.DESH_PLATES), 32)
            ),
            Planet.MOON_ORBIT,
            SPACE_STATION_STRUCTURE);

        createSpaceStation(List.of(
                IngredientHolder.of(provider.tagIngredient(ModItemTags.STEEL_INGOTS), 32),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.STEEL_PLATES), 64),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.OSTRUM_INGOTS), 32),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.OSTRUM_PLATES), 32)
            ),
            Planet.MARS_ORBIT,
            SPACE_STATION_STRUCTURE);

        createSpaceStation(List.of(
                IngredientHolder.of(provider.tagIngredient(ModItemTags.STEEL_INGOTS), 64),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.STEEL_PLATES), 64),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.CALORITE_INGOTS), 32),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.CALORITE_PLATES), 32)
            ),
            Planet.VENUS_ORBIT,
            SPACE_STATION_STRUCTURE);

        createSpaceStation(List.of(
                IngredientHolder.of(provider.tagIngredient(ModItemTags.STEEL_INGOTS), 64),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.STEEL_PLATES), 64),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.CALORITE_INGOTS), 32),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.CALORITE_PLATES), 32)
            ),
            Planet.MERCURY_ORBIT,
            SPACE_STATION_STRUCTURE);


        createSpaceStation(List.of(
                IngredientHolder.of(provider.tagIngredient(ModItemTags.STEEL_PLATES), 32),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.DESH_INGOTS), 32),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.OSTRUM_PLATES), 32),
                IngredientHolder.of(provider.tagIngredient(ModItemTags.CALORITE_PLATES), 32)
            ),
            Planet.GLACIO_ORBIT,
            SPACE_STATION_STRUCTURE);
    }

    public static void createCompressing(int cookingtime, int energy, TagKey<Item> ingredient, ItemStack result) {
        Identifier ingredientId = ingredient.location();
        Identifier resultId = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(result.getItem()));

        new CompressingRecipeBuilder(cookingtime, energy, provider.tagIngredient(ingredient), result)
            .unlockedBy("has_item", provider.hasTag(ingredient))
            .save(output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "compressing/%s_from_compressing_%s".formatted(resultId.getPath(), ingredientId.getPath()))));
    }

    public static void createCompressing(int cookingtime, int energy, Item ingredient, ItemStack result) {
        Identifier ingredientId = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(ingredient));
        Identifier resultId = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(result.getItem()));

        new CompressingRecipeBuilder(cookingtime, energy, Ingredient.of(ingredient), result)
            .unlockedBy("has_item", provider.hasItem(ingredient))
            .save(output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "compressing/%s_from_compressing_%s".formatted(resultId.getPath(), ingredientId.getPath()))));
    }

    public static void createAlloying(int cookingtime, int energy, List<Ingredient> ingredients, ItemStack result, List<String> ingredientNames) {
        Identifier resultId = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(result.getItem()));

        var builder = new AlloyingRecipeBuilder(cookingtime, energy, ingredients, result)
            .unlockedBy("has_item", provider.hasItem(ModItems.ETRIONIC_BLAST_FURNACE.get()));

        builder.save(output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(
            AdAstra.MOD_ID,
            "alloying/%s_from_alloying_%s".formatted(resultId.getPath(), String.join("_and_", ingredientNames))
        )));
    }

    public static void createOxygenLoading(int cookingtime, int energy, SizedFluidIngredient ingredient, FluidResource resultFluid, long resultAmount, String name) {
        Identifier resultFluidId = Objects.requireNonNull(BuiltInRegistries.FLUID.getKey(resultFluid.getType()));

        new OxygenLoadingRecipeBuilder(cookingtime, energy, ingredient, resultFluid, resultAmount)
            .save(output, Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "oxygen_loading/%s_from_oxygen_loading_%s".formatted(resultFluidId.getPath(), name)));
    }

    public static void createRefining(int cookingtime, int energy, SizedFluidIngredient ingredient, FluidResource resultFluid, long resultAmount, String name) {
        Identifier resultFluidId = Objects.requireNonNull(BuiltInRegistries.FLUID.getKey(resultFluid.getType()));

        new RefiningRecipeBuilder(cookingtime, energy, ingredient, resultFluid, resultAmount)
            .save(output, Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "refining/%s_from_refining_%s".formatted(resultFluidId.getPath(), name)));
    }

    public static void createCryoFreezing(int cookingtime, int energy, ItemStack ingredient, FluidResource resultFluid, long resultAmount) {
        Identifier ingredientId = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(ingredient.getItem()));
        Identifier resultFluidId = Objects.requireNonNull(BuiltInRegistries.FLUID.getKey(resultFluid.getType()));

        new CryoFreezingRecipeBuilder(cookingtime, energy, Ingredient.of(ingredient.getItem()), resultFluid, resultAmount)
            .save(output, Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "cryo_freezing/%s_from_cryo_freezing_%s".formatted(resultFluidId.getPath(), ingredientId.getPath())));
    }

    public static void createNasaWorkbench(List<Ingredient> ingredients, ItemStack result) {
        Identifier resultId = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(result.getItem()));

        new NasaWorkbenchRecipeBuilder(ingredients, result)
            .unlockedBy("has_item", provider.hasItem(ModItems.NASA_WORKBENCH.get()))
            .save(output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "nasa_workbench/%s_from_nasa_workbench".formatted(resultId.getPath()))));
    }

    public static void createSpaceStation(List<IngredientHolder> ingredients, ResourceKey<Level> dimension, Identifier structure) {
        new SpaceStationRecipeBuilder(ingredients, dimension, structure)
            .save(output, Identifier.fromNamespaceAndPath(AdAstra.MOD_ID, "space_station/%s_space_station".formatted(dimension.identifier().getPath())));
    }
}
