package earth.terrarium.adastra.common.compat.rei.displays;

import earth.terrarium.adastra.common.compat.rei.categories.OxygenLoadingCategory;
import earth.terrarium.adastra.common.recipes.machines.OxygenLoadingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import net.minecraft.resources.Identifier;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return Optional.empty();
    }

    @Override
    public DisplaySerializer<? extends Display> getSerializer() {
        return null;
    }
}
