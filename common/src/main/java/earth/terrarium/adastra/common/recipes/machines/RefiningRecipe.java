package earth.terrarium.adastra.common.recipes.machines;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipeSerializer;
import earth.terrarium.adastra.common.registry.ModRecipeSerializers;
import earth.terrarium.adastra.common.registry.ModRecipeTypes;
import earth.terrarium.common_storage_lib.resources.ResourceStack;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.resources.fluid.ingredient.SizedFluidIngredient;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record RefiningRecipe(
    int cookingTime, int energy,
    SizedFluidIngredient input,
    FluidResource result,
    long resultAmount
) implements CodecRecipe<RecipeInput> {

    public static final MapCodec<RefiningRecipe> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            Codec.INT.fieldOf("cookingtime").forGetter(RefiningRecipe::cookingTime),
            Codec.INT.fieldOf("energy").forGetter(RefiningRecipe::energy),
            SizedFluidIngredient.NESTED_MB_CODEC.codec().fieldOf("input").forGetter(RefiningRecipe::input),
            ResourceStack.FLUID_MB_CODEC.fieldOf("result").forGetter(r -> new ResourceStack<>(r.result(), r.resultAmount()))
        ).apply(instance, (cookingTime, energy, input, resultStack) ->
            new RefiningRecipe(cookingTime, energy, input, resultStack.resource(), resultStack.amount())));

    private static final ByteCodec<FluidResource> FLUID_RESOURCE_BYTE_CODEC = ByteCodec.STRING.map(
        str -> FluidResource.of(BuiltInRegistries.FLUID.get(ResourceLocation.parse(str))),
        res -> BuiltInRegistries.FLUID.getKey(res.getType()).toString()
    );

    private static final ByteCodec<SizedFluidIngredient> SIZED_FLUID_INGREDIENT_BYTE_CODEC = ObjectByteCodec.create(
        ByteCodec.STRING.fieldOf(i -> BuiltInRegistries.FLUID.getKey(i.getFluids().getFirst().resource().getType()).toString()),
        ByteCodec.LONG.fieldOf(SizedFluidIngredient::getAmount),
        (fluidStr, amount) -> SizedFluidIngredient.of(FluidResource.of(BuiltInRegistries.FLUID.get(ResourceLocation.parse(fluidStr))), (int) (long) amount)
    );

    public static final ByteCodec<RefiningRecipe> NETWORK_CODEC = ObjectByteCodec.create(
        ByteCodec.INT.fieldOf(RefiningRecipe::cookingTime),
        ByteCodec.INT.fieldOf(RefiningRecipe::energy),
        SIZED_FLUID_INGREDIENT_BYTE_CODEC.fieldOf(RefiningRecipe::input),
        FLUID_RESOURCE_BYTE_CODEC.fieldOf(RefiningRecipe::result),
        ByteCodec.LONG.fieldOf(RefiningRecipe::resultAmount),
        RefiningRecipe::new
    );

    @Override
    public boolean matches(@NotNull RecipeInput container, @NotNull Level level) {
        // Recipe matching is handled by the block entity checking fluid contents
        return true;
    }

    @Override
    public CodecRecipeSerializer<? extends CodecRecipe<RecipeInput>> serializer() {
        return ModRecipeSerializers.REFINING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.REFINING.get();
    }
}
