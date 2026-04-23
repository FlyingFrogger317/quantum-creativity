package com.flyingfrog317.quantum_creativity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class QuantumTier implements Tier {
    @Override
    public int getUses() {
        return 0;
    }

    @Override
    public float getSpeed() {
        return -2;
    }

    @Override
    public float getAttackDamageBonus() {
        return -1;
    }

    @Override
    public int getLevel() {
        return 5;
    }

    @Override
    public int getEnchantmentValue() {
        return 50;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(QuantumCreativity.registrys.getRegisteredItem("quantum_ingot").get());
    }
}
