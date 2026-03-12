package earth.terrarium.adastra.common.recipes.machines;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.ExtraByteCodecs;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipeSerializer;
import earth.terrarium.adastra.common.registry.ModRecipeSerializers;
import earth.terrarium.adastra.common.registry.ModRecipeTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import earth.terrarium.common_storage_lib.resources.ResourceStack;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record CryoFreezingRecipe(
    int cookingTime, int energy,
    Ingredient input,
    FluidResource result,
    long resultAmount
) implements CodecRecipe<RecipeInput> {

    public static final MapCodec<CryoFreezingRecipe> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            Codec.INT.fieldOf("cookingtime").forGetter(CryoFreezingRecipe::cookingTime),
            Codec.INT.fieldOf("energy").forGetter(CryoFreezingRecipe::energy),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(CryoFreezingRecipe::input),
            ResourceStack.FLUID_MB_CODEC.fieldOf("result").forGetter(r -> new ResourceStack<>(r.result(), r.resultAmount()))
        ).apply(instance, (cookingTime, energy, input, resultStack) ->
            new CryoFreezingRecipe(cookingTime, energy, input, resultStack.resource(), resultStack.amount())));

    private static final ByteCodec<FluidResource> FLUID_RESOURCE_BYTE_CODEC = ByteCodec.STRING.map(
        str -> FluidResource.of(BuiltInRegistries.FLUID.get(ResourceLocation.parse(str))),
        res -> BuiltInRegistries.FLUID.getKey(res.getType()).toString()
    );

    public static final ByteCodec<CryoFreezingRecipe> NETWORK_CODEC = ObjectByteCodec.create(
        ByteCodec.INT.fieldOf(CryoFreezingRecipe::cookingTime),
        ByteCodec.INT.fieldOf(CryoFreezingRecipe::energy),
        ExtraByteCodecs.INGREDIENT.fieldOf(CryoFreezingRecipe::input),
        FLUID_RESOURCE_BYTE_CODEC.fieldOf(CryoFreezingRecipe::result),
        ByteCodec.LONG.fieldOf(CryoFreezingRecipe::resultAmount),
        CryoFreezingRecipe::new
    );

    @Override
    public boolean matches(@NotNull RecipeInput container, @NotNull Level level) {
        return input.test(container.getItem(1));
    }

    @Override
    public CodecRecipeSerializer<? extends CodecRecipe<RecipeInput>> serializer() {
        return ModRecipeSerializers.CRYO_FREEZING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.CRYO_FREEZING.get();
    }
}
