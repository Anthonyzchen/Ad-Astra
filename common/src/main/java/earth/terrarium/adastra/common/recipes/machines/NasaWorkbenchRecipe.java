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
import earth.terrarium.adastra.common.registry.ModRecipeSerializers;
import earth.terrarium.adastra.common.registry.ModRecipeTypes;
import earth.terrarium.adastra.common.recipes.ContainerRecipeInput;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record NasaWorkbenchRecipe(
        List<Ingredient> ingredients,
        ItemStack result) implements CodecRecipe<ContainerRecipeInput> {

    public static final MapCodec<NasaWorkbenchRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(NasaWorkbenchRecipe::ingredients),
                    ItemStackCodec.CODEC.fieldOf("result").forGetter(NasaWorkbenchRecipe::result))
                    .apply(instance, NasaWorkbenchRecipe::new));

    public static final ByteCodec<NasaWorkbenchRecipe> NETWORK_CODEC = ObjectByteCodec.create(
            ExtraByteCodecs.INGREDIENT.listOf().fieldOf(NasaWorkbenchRecipe::ingredients),
            ExtraByteCodecs.ITEM_STACK.fieldOf(NasaWorkbenchRecipe::result),
            NasaWorkbenchRecipe::new);

    @Override
    public boolean matches(@NotNull ContainerRecipeInput input, @NotNull Level level) {
        Container container = input.container();
        if (container.getContainerSize() < ingredients.size())
            return false;
        for (int i = 0; i < ingredients.size(); i++) {
            if (!ingredients.get(i).test(container.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public CodecRecipeSerializer<? extends CodecRecipe<ContainerRecipeInput>> serializer() {
        return ModRecipeSerializers.NASA_WORKBENCH_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.NASA_WORKBENCH.get();
    }
}