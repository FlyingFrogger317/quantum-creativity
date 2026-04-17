package com.flyingfrog317.quantum_creativity.quantization;

import com.flyingfrog317.quantum_creativity.QuantumCreativity;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class QuantizingRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    public final Ingredient inputs;
    private final NonNullList<ItemStack> outputs;

    public QuantizingRecipe(ResourceLocation id, Ingredient input, NonNullList<ItemStack> outputs) {
        this.id = id;
        this.inputs = input;
        this.outputs = outputs;
    }
    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        for (ItemStack i : inputs.getItems()){
            if (Ingredient.of(i).test(simpleContainer.getItem(0))){
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
        if (outputs.size()==1){
            return outputs.get(0).copy();
        }
        int index= ThreadLocalRandom.current().nextInt(outputs.size());
        return outputs.get(index).copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return outputs.get(0);
    }
    public NonNullList<ItemStack> getResultItems(){
        return outputs;
    }
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return QuantizingRegistries.QuantizingRecipeSerializer.get();
    }

    @Override
    public RecipeType<?> getType() {
        return QuantizingRegistries.QuantizingRecipeType.get();
    }
}
