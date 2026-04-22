package com.flyingfrog317.quantum_creativity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public class QuantumArmorMaterial implements ArmorMaterial {
    @Override
    public int getDurabilityForType(ArmorItem.Type pType) {
        return 10000;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type pType) {
        return 5;
    }

    @Override
    public int getEnchantmentValue() {
        return 50;
    }

    @Override
    public @Nullable SoundEvent getEquipSound() {
        return null;
    }

    @Override
    public @Nullable Ingredient getRepairIngredient() {
        return null;
    }

    @Override
    public String getName() {
        return QuantumCreativity.asResource("quantum").toString();
    }

    @Override
    public float getToughness() {
        return 5;
    }

    @Override
    public float getKnockbackResistance() {
        return 1;
    }
}
