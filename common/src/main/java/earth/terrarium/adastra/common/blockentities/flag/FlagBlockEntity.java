package earth.terrarium.adastra.common.blockentities.flag;

import com.mojang.authlib.GameProfile;
import dev.architectury.injectables.annotations.PlatformOnly;
import earth.terrarium.adastra.common.blockentities.flag.content.FlagContent;
import earth.terrarium.adastra.common.blockentities.flag.content.UrlContent;
import earth.terrarium.adastra.common.registry.ModBlockEntityTypes;
import earth.terrarium.adastra.mixins.common.SkullBlockEntityInvoker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlagBlockEntity extends BlockEntity {

    @Nullable
    private GameProfile owner;

    @Nullable
    private FlagContent content;

    public FlagBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.FLAG.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        if (owner != null) {
            ResolvableProfile.CODEC.encodeStart(NbtOps.INSTANCE, new ResolvableProfile(owner))
                    .result().ifPresent(t -> tag.put("FlagOwner", t));
        }
        if (content != null) {
            tag.put("FlagContent", content.toFullTag());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("FlagOwner", Tag.TAG_COMPOUND)) {
            ResolvableProfile.CODEC.parse(NbtOps.INSTANCE, tag.getCompound("FlagOwner"))
                    .result().ifPresent(p -> setOwner(p.gameProfile()));
        }
        if (tag.contains("FlagUrl", Tag.TAG_STRING)) {
            this.content = UrlContent.of("https://imgur.com/" + tag.getString("FlagUrl"));
        }
        if (tag.contains("FlagContent", Tag.TAG_COMPOUND)) {
            this.content = FlagContent.fromTag(tag.getCompound("FlagContent"));
        }
    }

    @Nullable
    public GameProfile getOwner() {
        return this.owner;
    }

    public void setOwner(GameProfile profile) {
        synchronized (this) {
            this.owner = profile;
        }
        this.loadOwnerProperties();
    }

    private void loadOwnerProperties() {
        if (owner == null)
            return;
        SkullBlockEntityInvoker.invokeFetchGameProfile(this.owner.getName()).thenAccept(owner -> {
            if (owner.isPresent()) {
                this.owner = owner.get();
                this.setChanged();
            }
        });
    }

    @Nullable
    public FlagContent getContent() {
        return this.content;
    }

    public void setContent(@Nullable FlagContent content) {
        this.content = content;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @PlatformOnly("neoforge")
    @SuppressWarnings("unused")
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(2);
    }
}
