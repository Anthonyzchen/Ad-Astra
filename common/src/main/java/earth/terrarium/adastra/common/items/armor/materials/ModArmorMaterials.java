package earth.terrarium.adastra.common.items.armor.materials;

import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.common.tags.ModItemTags;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

/**
 * Ad Astra armor materials for MC 1.21.
 * In 1.21, ArmorMaterial is a record and must be registered.
 */
public class ModArmorMaterials {

        public static final Holder<ArmorMaterial> SPACE_SUIT = register("space_suit",
                        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                                map.put(ArmorItem.Type.BOOTS, 2);
                                map.put(ArmorItem.Type.LEGGINGS, 5);
                                map.put(ArmorItem.Type.CHESTPLATE, 6);
                                map.put(ArmorItem.Type.HELMET, 2);
                                map.put(ArmorItem.Type.BODY, 5);
                        }), 14, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F,
                        () -> Ingredient.of(ModItemTags.STEEL_INGOTS));

        public static final Holder<ArmorMaterial> NETHERITE_SPACE_SUIT = register("netherite_space_suit",
                        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                                map.put(ArmorItem.Type.BOOTS, 3);
                                map.put(ArmorItem.Type.LEGGINGS, 6);
                                map.put(ArmorItem.Type.CHESTPLATE, 8);
                                map.put(ArmorItem.Type.HELMET, 3);
                                map.put(ArmorItem.Type.BODY, 11);
                        }), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F,
                        () -> Ingredient.of(ModItemTags.STEEL_INGOTS));

        public static final Holder<ArmorMaterial> JET_SUIT = register("jet_suit",
                        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                                map.put(ArmorItem.Type.BOOTS, 3);
                                map.put(ArmorItem.Type.LEGGINGS, 6);
                                map.put(ArmorItem.Type.CHESTPLATE, 8);
                                map.put(ArmorItem.Type.HELMET, 3);
                                map.put(ArmorItem.Type.BODY, 11);
                        }), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F,
                        () -> Ingredient.of(ModItemTags.CALORITE_INGOTS));

        private static Holder<ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> defense,
                        int enchantmentValue,
                        Holder<net.minecraft.sounds.SoundEvent> equipSound, float toughness, float knockbackResistance,
                        Supplier<Ingredient> repairIngredient) {
                List<ArmorMaterial.Layer> layers = List
                                .of(new ArmorMaterial.Layer(
                                                ResourceLocation.fromNamespaceAndPath(AdAstra.MOD_ID, name)));
                ArmorMaterial material = new ArmorMaterial(defense, enchantmentValue, equipSound, repairIngredient,
                                layers,
                                toughness, knockbackResistance);
                return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL,
                                ResourceLocation.fromNamespaceAndPath(AdAstra.MOD_ID, name), material);
        }

        public static void init() {
                // Called to ensure static initialization
        }
}
