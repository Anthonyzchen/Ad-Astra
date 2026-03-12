package earth.terrarium.adastra.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.ExtraByteCodecs;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipeSerializer;
import earth.terrarium.adastra.common.recipes.base.IngredientHolder;
import earth.terrarium.adastra.common.registry.ModRecipeSerializers;
import earth.terrarium.adastra.common.registry.ModRecipeTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public record SpaceStationRecipe(
    List<IngredientHolder> ingredients,
    ResourceKey<Level> dimension,
    ResourceLocation structure
) implements CodecRecipe<RecipeInput> {

    public static final MapCodec<SpaceStationRecipe> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            IngredientHolder.CODEC.listOf().fieldOf("ingredients").forGetter(SpaceStationRecipe::ingredients),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(SpaceStationRecipe::dimension),
            ResourceLocation.CODEC.fieldOf("structure").forGetter(SpaceStationRecipe::structure)
        ).apply(instance, SpaceStationRecipe::new));

    public static final ByteCodec<SpaceStationRecipe> NETWORK_CODEC = ObjectByteCodec.create(
        IngredientHolder.NETWORK_CODEC.listOf().fieldOf(SpaceStationRecipe::ingredients),
        ExtraByteCodecs.DIMENSION.fieldOf(SpaceStationRecipe::dimension),
        ExtraByteCodecs.RESOURCE_LOCATION.fieldOf(SpaceStationRecipe::structure),
        SpaceStationRecipe::new
    );

    @Override
    public boolean matches(@NotNull RecipeInput container, @NotNull Level level) {
        return false;
    }

    @Override
    public CodecRecipeSerializer<? extends CodecRecipe<RecipeInput>> serializer() {
        return ModRecipeSerializers.SPACE_STATION_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.SPACE_STATION_RECIPE.get();
    }

    public static Optional<RecipeHolder<SpaceStationRecipe>> getSpaceStation(Level level, ResourceKey<Level> dimension) {
        return level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.SPACE_STATION_RECIPE.get())
            .stream()
            .filter(recipe -> recipe.value().dimension().equals(dimension))
            .findFirst();
    }

    public static boolean hasIngredients(Player player, Level level, SpaceStationRecipe recipe) {
        for (IngredientHolder holder : recipe.ingredients()) {
            int count = 0;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                var stack = player.getInventory().getItem(i);
                if (holder.ingredient().test(stack)) {
                    count += stack.getCount();
                }
            }
            if (count < holder.count()) return false;
        }
        return true;
    }

    public static void consumeIngredients(Player player, Level level) {
        getSpaceStation(level, level.dimension()).ifPresent(recipe -> {
            for (IngredientHolder holder : recipe.value().ingredients()) {
                int remaining = holder.count();
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    var stack = player.getInventory().getItem(i);
                    if (holder.ingredient().test(stack)) {
                        int toRemove = Math.min(remaining, stack.getCount());
                        stack.shrink(toRemove);
                        remaining -= toRemove;
                        if (remaining <= 0) break;
                    }
                }
            }
        });
    }
}
