package com.flyingfrog317.quantum_creativity;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class QuantumTier implements Tier {

    @Override
    public int getUses() {
        return 0;
    }

    @Override
    public float getSpeed() {
        return 100.0f;
    }

    @Override
    public float getAttackDamageBonus() {
        return -1;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return null;
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
