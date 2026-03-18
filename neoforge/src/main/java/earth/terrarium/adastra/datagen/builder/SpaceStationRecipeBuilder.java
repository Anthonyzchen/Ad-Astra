package earth.terrarium.adastra.datagen.builder;

import com.teamresourceful.resourcefullib.common.datagen.CodecRecipeBuilder;
import earth.terrarium.adastra.common.recipes.SpaceStationRecipe;
import earth.terrarium.adastra.common.recipes.base.IngredientHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpaceStationRecipeBuilder extends CodecRecipeBuilder {

    private final SpaceStationRecipe recipe;

    public SpaceStationRecipeBuilder(List<IngredientHolder> ingredients, ResourceKey<Level> dimension, Identifier structure) {
        recipe = new SpaceStationRecipe(ingredients, dimension, structure);
    }

    @Override
    public @NotNull Item getResult() {
        return Items.AIR;
    }

    public void save(RecipeOutput recipeOutput, Identifier id) {
        save(recipeOutput, ResourceKey.create(Registries.RECIPE, id));
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> key) {
        Identifier id = key.identifier();
        var builder = recipeOutput.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
            .rewards(AdvancementRewards.Builder.recipe(key))
            .requirements(AdvancementRequirements.Strategy.OR);
        criteria.forEach(builder::addCriterion);
        recipeOutput.accept(key, recipe, builder
            .build(Identifier.fromNamespaceAndPath(id.getNamespace(), "recipes/space_stations/" + id.getPath())));
    }
}
