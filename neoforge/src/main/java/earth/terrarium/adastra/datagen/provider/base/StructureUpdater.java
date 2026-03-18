/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense"
 *  Details can be found in the license file in the root folder of this project
 *
 * Methods below are taken from Immersive Engineering and modified to fit Ad Astra -> https://github.com/BluSunrize/ImmersiveEngineering
 */

package earth.terrarium.adastra.datagen.provider.base;

import com.google.common.hash.Hashing;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerUpper;
import earth.terrarium.adastra.AdAstra;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

// TODO: 1.21.11 - ExistingFileHelper and MultiPackResourceManager are no longer available from NeoForge datagen.
// This provider needs to be reimplemented to load structure files differently.
public class StructureUpdater implements DataProvider {

    private final String basePath;
    private final String modid;
    private final PackOutput output;

    public StructureUpdater(
        String basePath, String modid, PackOutput output
    ) {
        this.basePath = basePath;
        this.modid = modid;
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(@NotNull CachedOutput cache) {
        AdAstra.LOGGER.warn("StructureUpdater is not yet fully implemented for 1.21.11 - ExistingFileHelper was removed");
        return CompletableFuture.completedFuture(null);
    }

    private static CompoundTag updateNBT(CompoundTag nbt) {
        final CompoundTag updatedNBT = DataFixTypes.STRUCTURE.updateToCurrentVersion(
            DataFixers.getDataFixer(), nbt, nbt.getIntOr("DataVersion", 0)
        );
        StructureTemplate template = new StructureTemplate();
        template.load(BuiltInRegistries.BLOCK, updatedNBT);
        return template.save(new CompoundTag());
    }

    @NotNull
    @Override
    public String getName() {
        return "Update structure files in " + basePath;
    }
}
