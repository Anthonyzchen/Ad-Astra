package earth.terrarium.adastra.common.utils;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.common_storage_lib.fluid.impl.SimpleFluidSlot;
import earth.terrarium.common_storage_lib.fluid.impl.SimpleFluidStorage;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.resources.fluid.ingredient.FluidIngredient;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidUtils {

    /**
     * Returns a placeholder fluid object for the given stack (TODO: CSL migration).
     */
    public static Object getTank(ItemStack stack) {
        return null; // TODO: CSL migration - return actual fluid holder from item via FluidApi.ITEM
    }

    /**
     * Gets the fluid contained in a bucket item by checking the fluid registry.
     */
    public static Fluid getFluidFromBucket(Item item) {
        if (item == Items.WATER_BUCKET) return Fluids.WATER;
        if (item == Items.LAVA_BUCKET) return Fluids.LAVA;
        if (!(item instanceof BucketItem)) return Fluids.EMPTY;
        for (Fluid fluid : BuiltInRegistries.FLUID) {
            if (fluid.getBucket() == item && fluid != Fluids.EMPTY) {
                return fluid;
            }
        }
        return Fluids.EMPTY;
    }

    public static boolean hasFluid(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return getFluidFromBucket(stack.getItem()) != Fluids.EMPTY;
    }

    public static boolean hasFluid(ItemStack stack, int tank) {
        return hasFluid(stack);
    }

    public static long getCapacity(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        if (stack.getItem() instanceof BucketItem) {
            return 81000L;
        }
        return 0;
    }

    public static long getCapacity(ItemStack stack, int tank) {
        return getCapacity(stack);
    }

    public static ItemStack fluidFilledItem(RegistryEntry<Item> item, RegistryEntry<Fluid> fluid) {
        return ItemStack.EMPTY; // TODO: CSL migration
    }

    /**
     * Checks if two FluidResources represent the same fluid type.
     * CSL's FluidResource doesn't override equals(), so we compare the underlying Fluid type.
     */
    public static boolean isSameFluid(FluidResource a, FluidResource b) {
        if (a.isBlank() && b.isBlank()) return true;
        if (a.isBlank() || b.isBlank()) return false;
        return a.getType() == b.getType();
    }

    /**
     * Tests if a FluidIngredient matches a FluidResource.
     * CSL 0.0.7's BaseFluidIngredient.test() always returns false (stub),
     * so we check against getMatchingFluids() instead.
     */
    public static boolean ingredientMatches(FluidIngredient ingredient, FluidResource resource) {
        if (resource.isBlank()) return false;
        for (FluidResource matching : ingredient.getMatchingFluids()) {
            if (matching.getType() == resource.getType()) return true;
        }
        return false;
    }

    /**
     * Inserts fluid into a SimpleFluidSlot, working around CSL's FluidResource reference equality bug.
     * When the slot already contains the same fluid type, reuses the slot's existing FluidResource
     * instance so that SimpleFluidSlot.insert()'s equals() check passes.
     */
    public static long insertFluid(SimpleFluidSlot slot, FluidResource resource, long amount, boolean simulate) {
        FluidResource existing = slot.getResource();
        if (!existing.isBlank() && isSameFluid(existing, resource)) {
            // Reuse the slot's own FluidResource instance to bypass reference equality
            return slot.insert(existing, amount, simulate);
        }
        return slot.insert(resource, amount, simulate);
    }

    /**
     * Inserts fluid into a CommonStorage (e.g. SimpleFluidStorage), working around
     * CSL's FluidResource reference equality bug. Iterates each slot and reuses
     * the slot's existing FluidResource instance when the fluid type matches.
     */
    public static long insertFluidStorage(CommonStorage<FluidResource> storage, FluidResource resource, long amount, boolean simulate) {
        if (storage instanceof SimpleFluidStorage sfs) {
            // First pass: insert into slots that already contain the same fluid
            long remaining = amount;
            for (int i = 0; i < sfs.size(); i++) {
                if (remaining <= 0) break;
                SimpleFluidSlot slot = sfs.get(i);
                FluidResource existing = slot.getResource();
                if (!existing.isBlank() && isSameFluid(existing, resource)) {
                    long inserted = slot.insert(existing, remaining, simulate);
                    remaining -= inserted;
                }
            }
            // Second pass: insert into empty slots
            for (int i = 0; i < sfs.size(); i++) {
                if (remaining <= 0) break;
                SimpleFluidSlot slot = sfs.get(i);
                if (slot.getResource().isBlank()) {
                    long inserted = slot.insert(resource, remaining, simulate);
                    remaining -= inserted;
                }
            }
            return amount - remaining;
        }
        // Fallback for non-SimpleFluidStorage
        return storage.insert(resource, amount, simulate);
    }

    /**
     * Moves fluid from a bucket item to a fluid container.
     */
    public static void moveItemToContainer(Container container, Object fluidContainer, int slot, int resultSlot, int tank) {
        if (!(fluidContainer instanceof SimpleFluidStorage storage)) return;
        ItemStack stack = container.getItem(slot);
        if (stack.isEmpty()) return;

        Fluid fluid = getFluidFromBucket(stack.getItem());
        if (fluid != Fluids.EMPTY) {
            FluidResource resource = FluidResource.of(fluid);
            SimpleFluidSlot fluidSlot = storage.get(tank);
            long inserted = insertFluid(fluidSlot, resource, 81000L, true);

            ItemStack result = container.getItem(resultSlot);
            boolean resultSlotCanAccept = result.isEmpty() || (result.is(Items.BUCKET) && result.getCount() < result.getMaxStackSize());
            if (!resultSlotCanAccept) return;

            if (inserted >= 81000L) {
                insertFluid(fluidSlot, resource, 81000L, false);
                container.setItem(slot, ItemStack.EMPTY);
                if (result.isEmpty()) {
                    container.setItem(resultSlot, new ItemStack(Items.BUCKET));
                } else if (result.is(Items.BUCKET) && result.getCount() < result.getMaxStackSize()) {
                    result.grow(1);
                }
            }
        }
    }

    /**
     * Moves fluid from a fluid container to a bucket.
     */
    public static void moveContainerToItem(Container container, Object fluidContainer, int slot, int resultSlot, int tank) {
        if (!(fluidContainer instanceof SimpleFluidStorage storage)) return;
        ItemStack stack = container.getItem(slot);
        if (stack.isEmpty() || !stack.is(Items.BUCKET)) return;

        SimpleFluidSlot fluidSlot = storage.get(tank);
        FluidResource resource = fluidSlot.getResource();
        if (resource.isBlank()) return;
        if (fluidSlot.getAmount() < 81000L) return;

        ItemStack result = container.getItem(resultSlot);
        Item bucketItem = resource.getType().getBucket();
        if (bucketItem == Items.AIR) return;

        ItemStack filledBucket = new ItemStack(bucketItem);
        if (!result.isEmpty() && (!ItemStack.isSameItemSameComponents(result, filledBucket) || result.getCount() >= result.getMaxStackSize())) {
            return;
        }

        fluidSlot.extract(resource, 81000L, false);
        stack.shrink(1);
        if (result.isEmpty()) {
            container.setItem(resultSlot, filledBucket);
        } else {
            result.grow(1);
        }
    }
}
