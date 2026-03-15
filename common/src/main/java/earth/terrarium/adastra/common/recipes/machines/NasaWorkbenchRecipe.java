package earth.terrarium.adastra.common.recipes.machines;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.ExtraByteCodecs;
import com.teamresourceful.resourcefullib.common.codecs.recipes.ItemStackCodec;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipeSerializer;
import earth.terrarium.adastra.common.registry.ModRecipeSerializers;
import earth.terrarium.adastra.common.registry.ModRecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record NasaWorkbenchRecipe(
    List<Ingredient> ingredients,
    ItemStack result
) implements CodecRecipe<RecipeInput> {

    private static final RecipeBookCategory BOOK_CATEGORY = new RecipeBookCategory();

    public static final MapCodec<NasaWorkbenchRecipe> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(NasaWorkbenchRecipe::ingredients),
            ItemStackCodec.CODEC.fieldOf("result").forGetter(NasaWorkbenchRecipe::result)
        ).apply(instance, NasaWorkbenchRecipe::new));

    public static final ByteCodec<NasaWorkbenchRecipe> NETWORK_CODEC = ObjectByteCodec.create(
        ExtraByteCodecs.INGREDIENT.listOf().fieldOf(NasaWorkbenchRecipe::ingredients),
        ExtraByteCodecs.ITEM_STACK.fieldOf(NasaWorkbenchRecipe::result),
        NasaWorkbenchRecipe::new
    );

    @Override
    public boolean matches(@NotNull RecipeInput container, @NotNull Level level) {
        if (container.size() < ingredients.size()) return false;
        for (int i = 0; i < ingredients.size(); i++) {
            if (!ingredients.get(i).test(container.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeInput input, HolderLookup.@NotNull Provider provider) {
        return result.copy();
    }



    @Override
    public @NotNull CodecRecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
        return ModRecipeSerializers.NASA_WORKBENCH_SERIALIZER.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull RecipeType<NasaWorkbenchRecipe> getType() {
        return (RecipeType<NasaWorkbenchRecipe>) (RecipeType<?>) ModRecipeTypes.NASA_WORKBENCH.get();
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return BOOK_CATEGORY;
    }
}
