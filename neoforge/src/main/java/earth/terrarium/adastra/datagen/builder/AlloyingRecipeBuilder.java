package earth.terrarium.adastra.datagen.builder;

import com.teamresourceful.resourcefullib.common.datagen.CodecRecipeBuilder;
import earth.terrarium.adastra.common.recipes.machines.AlloyingRecipe;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AlloyingRecipeBuilder extends CodecRecipeBuilder {

    private final AlloyingRecipe recipe;

    public AlloyingRecipeBuilder(int cookingTime, int energy, List<Ingredient> ingredients, ItemStack result) {
        recipe = new AlloyingRecipe(cookingTime, energy, ingredients, result);
    }

    @Override
    public @NotNull Item getResult() {
        return recipe.result().getItem();
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
            .build(Identifier.fromNamespaceAndPath(id.getNamespace(), "recipes/alloying/" + id.getPath())));
    }
}
