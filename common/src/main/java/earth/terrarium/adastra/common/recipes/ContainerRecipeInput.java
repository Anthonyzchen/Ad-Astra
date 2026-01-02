package earth.terrarium.adastra.common.recipes;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

/**
 * A RecipeInput wrapper for Container to maintain compatibility with MC 1.21+
 * recipes.
 * In MC 1.21, recipes use RecipeInput instead of Container directly.
 */
public record ContainerRecipeInput(Container container) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        return container.getItem(index);
    }

    @Override
    public int size() {
        return container.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (!container.getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
