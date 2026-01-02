package earth.terrarium.adastra.common.recipes.machines;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.ExtraByteCodecs;
import com.teamresourceful.resourcefullib.common.codecs.recipes.ItemStackCodec;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipeSerializer;
import earth.terrarium.adastra.common.blockentities.machines.EtrionicBlastFurnaceBlockEntity;
import earth.terrarium.adastra.common.registry.ModRecipeSerializers;
import earth.terrarium.adastra.common.registry.ModRecipeTypes;
import earth.terrarium.adastra.common.utils.ItemUtils;
import earth.terrarium.adastra.common.recipes.ContainerRecipeInput;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record AlloyingRecipe(
        int cookingTime, int energy,
        List<Ingredient> ingredients, ItemStack result) implements CodecRecipe<ContainerRecipeInput> {

    public static final MapCodec<AlloyingRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("cookingtime").forGetter(AlloyingRecipe::cookingTime),
                    Codec.INT.fieldOf("energy").forGetter(AlloyingRecipe::energy),
                    Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(AlloyingRecipe::ingredients),
                    ItemStackCodec.CODEC.fieldOf("result").forGetter(AlloyingRecipe::result))
                    .apply(instance, AlloyingRecipe::new));

    public static final ByteCodec<AlloyingRecipe> NETWORK_CODEC = ObjectByteCodec.create(
            ByteCodec.INT.fieldOf(AlloyingRecipe::cookingTime),
            ByteCodec.INT.fieldOf(AlloyingRecipe::energy),
            ExtraByteCodecs.INGREDIENT.listOf().fieldOf(AlloyingRecipe::ingredients),
            ExtraByteCodecs.ITEM_STACK.fieldOf(AlloyingRecipe::result),
            AlloyingRecipe::new);

    @Override
    public boolean matches(@NotNull ContainerRecipeInput input, @NotNull Level level) {
        Container container = input.container();
        if (container.getContainerSize() < ingredients.size())
            return false;
        for (int i = 0; i < Math.min(4, ingredients.size()); i++) {
            boolean found = false;
            for (int j = 0; j < 4; j++) {
                if (ingredients.get(i).test(container.getItem(j + 1))) {
                    found = true;
                    break;
                }
            }
            if (!found)
                return false;
        }

        if (!(container instanceof EtrionicBlastFurnaceBlockEntity entity))
            return true;
        if (entity.getEnergyStorage().internalExtract(energy, true) < energy)
            return false;
        return ItemUtils.canAddItem(container, result, 5, 6, 7, 8);
    }

    @Override
    public CodecRecipeSerializer<? extends CodecRecipe<ContainerRecipeInput>> serializer() {
        return ModRecipeSerializers.ALLOYING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.ALLOYING.get();
    }
}
