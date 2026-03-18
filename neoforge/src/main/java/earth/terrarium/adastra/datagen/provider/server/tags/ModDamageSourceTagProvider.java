package earth.terrarium.adastra.datagen.provider.server.tags;


import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.common.registry.ModDamageSources;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import java.util.concurrent.CompletableFuture;

public class ModDamageSourceTagProvider extends TagsProvider<DamageType> {

    public ModDamageSourceTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, Registries.DAMAGE_TYPE, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateRawBuilder(DamageTypeTags.BYPASSES_ARMOR).addOptionalElement(ModDamageSources.OXYGEN.identifier());
        getOrCreateRawBuilder(DamageTypeTags.NO_IMPACT).addOptionalElement(ModDamageSources.OXYGEN.identifier());
    }
}
