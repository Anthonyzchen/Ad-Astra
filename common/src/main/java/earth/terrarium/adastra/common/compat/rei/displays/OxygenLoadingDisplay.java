package earth.terrarium.adastra.common.compat.rei.displays;

import earth.terrarium.adastra.common.compat.rei.categories.OxygenLoadingCategory;
import earth.terrarium.adastra.common.recipes.machines.OxygenLoadingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public record OxygenLoadingDisplay(OxygenLoadingRecipe recipe) implements Display {

    public OxygenLoadingDisplay(RecipeHolder<OxygenLoadingRecipe> recipe) {
        this(recipe.value());
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        // TODO: CSL migration - fluid input was removed from recipe, re-add when CSL FluidResource is available
        return List.of();
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        // TODO: CSL migration - fluid result was removed from recipe, re-add when CSL FluidResource is available
        return List.of();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return OxygenLoadingCategory.ID;
    }
}
