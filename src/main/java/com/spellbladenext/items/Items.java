package com.spellbladenext.items;

import com.spellbladenext.Spellblades;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

public class Items {
    public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();

    private static Weapon.Entry entry(String name, Weapon.CustomMaterial material, Item item, ItemConfig.Weapon defaults) {
        return entry(null, name, material, item, defaults);
    }

    private static Weapon.Entry entry(String requiredMod, String name, Weapon.CustomMaterial material, Item item, ItemConfig.Weapon defaults) {
        var entry = new Weapon.Entry(Spellblades.MOD_ID, name, material, item, defaults, null);
        if (entry.isRequiredModInstalled()) {
            entries.add(entry);
        }
        return entry;
    }

    private static Supplier<Ingredient> ingredient(String idString) {
        return ingredient(idString, net.minecraft.item.Items.DIAMOND);
    }

    private static Supplier<Ingredient> ingredient(String idString, Item fallback) {
        var id = new Identifier(idString);
        return () -> {
            var item = Registries.ITEM.get(id);
            var ingredient = item != null ? item : fallback;
            return Ingredient.ofItems(ingredient);
        };
    }

    private static final float bladeValue = 2F;
    private static final float bladeDamage = 2;
    private static final float claymoreDamage = 4F;
    private static final float bladeSpeed = -3;
    private static final float claymoreSpeed = -3;

    private static Weapon.Entry blade(String name, Weapon.CustomMaterial material, float damage, MagicSchool school) {
        return blade(null, name, material, damage, school );
    }

    private static Weapon.Entry blade(String requiredMod, String name, Weapon.CustomMaterial material, float damage,MagicSchool school) {
        var settings = new Item.Settings();
        var item = new Spellblade(material, settings, 1, -2.4F, school);
        return entry(requiredMod, name, material, item, new ItemConfig.Weapon(damage, -2.4F));
    }


    public static final Weapon.Entry frost_blade = blade("frost_blade",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(net.minecraft.item.Items.PRISMARINE_SHARD)), 3F,MagicSchool.FROST)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.FROST), bladeValue));
    public static final Weapon.Entry fire_blade = blade("fire_blade",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(net.minecraft.item.Items.BLAZE_ROD)), 3F,MagicSchool.FIRE)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.FIRE), bladeValue));
    public static final Weapon.Entry arcane_blade = blade("arcane_blade",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(net.minecraft.item.Items.AMETHYST_SHARD)), 3F,MagicSchool.ARCANE)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.ARCANE), bladeValue));
    public static final Weapon.Entry glacial_gladius = blade("glacial_gladius",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Spellblades.RUNEFROST)), 5F,MagicSchool.FROST)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.FROST), 3));
    public static final Weapon.Entry flaming_falchion = blade("flaming_falchion",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Spellblades.RUNEBLAZE)), 5F,MagicSchool.FIRE)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.FIRE), 3));
    public static final Weapon.Entry crystal_cutlass = blade("crystal_cutlass",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(Spellblades.RUNEGLEAM)), 5F,MagicSchool.ARCANE)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.ARCANE), 3));


    private static Weapon.Entry orb(String name, Weapon.CustomMaterial material, float damage, MagicSchool school) {
        return orb(null, name, material, damage, school );
    }
    private static Weapon.Entry orb(String requiredMod, String name, Weapon.CustomMaterial material, float damage,MagicSchool school) {
        var settings = new Item.Settings();
        var item = new Orb(material, settings, school);
        return entry(requiredMod, name, material, item, new ItemConfig.Weapon(damage, -3F));
    }
    public static final Weapon.Entry frost_orb = orb("frost_orb",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(net.minecraft.item.Items.PRISMARINE_CRYSTALS)), 1F,MagicSchool.FROST)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.FROST), bladeValue))
            .attribute(ItemConfig.Attribute.multiply(SpellAttributes.HASTE,0.25F));
    public static final Weapon.Entry fire_orb = orb("fire_orb",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(net.minecraft.item.Items.BLAZE_ROD)), 1F,MagicSchool.FIRE)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.FIRE), bladeValue))
            .attribute(ItemConfig.Attribute.multiply(SpellAttributes.HASTE,0.25F));

    public static final Weapon.Entry arcane_orb = orb("arcane_orb",
            Weapon.CustomMaterial.matching(ToolMaterials.DIAMOND, () -> Ingredient.ofItems(net.minecraft.item.Items.AMETHYST_SHARD)), 1F,MagicSchool.ARCANE)
            .attribute(ItemConfig.Attribute.bonus(SpellAttributes.POWER.get(MagicSchool.ARCANE), bladeValue))
            .attribute(ItemConfig.Attribute.multiply(SpellAttributes.HASTE,0.25F));

    public static void register(Map<String, ItemConfig.Weapon> configs) {
        Weapon.register(configs, entries, RegistryKey.of(Registries.ITEM_GROUP.getKey(),new Identifier(Spellblades.MOD_ID,"generic")));
    }
}

