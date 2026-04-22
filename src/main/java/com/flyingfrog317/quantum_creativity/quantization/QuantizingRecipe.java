package com.flyingfrog317.quantum_creativity.quantization;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import java.util.stream.Collector;

public class QuantizingRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    public final Ingredient inputs;
    public final NonNullList<Ingredient> outputs;

    public QuantizingRecipe(ResourceLocation id, Ingredient input, NonNullList<Ingredient> outputs) {
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
    /**
     * @deprecated Required by the Recipe interface but not supported for this recipe type. Always throws.
     * Use {@link #resolve(RandomSource)} instead.
     * @throws UnsupportedOperationException always
     */
    @Deprecated
    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
        throw new UnsupportedOperationException("QuantizingRecipe.assemble() is unsupported, use QuantizingRecipe.resolve(RandomSource) instead");
    }
    public ItemStack resolve(RandomSource source){
        NonNullList<ItemStack> items=getResultItems();
        if (items.size()==1){
            return items.get(0).copy();
        }
        int rand=source.nextInt(items.size());//the constructor prevents it from being 0
        return items.get(rand).copy();
    }
    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return outputs.get(0).getItems()[0];
    }
    public NonNullList<ItemStack> getResultItems(){
        return outputs.stream().map(Ingredient::getItems).collect(Collector.of(
                NonNullList::create, // supplier
                (list, array) -> {
                    if (array != null) {
                        for (ItemStack stack : array) {
                            if (stack != null && !stack.isEmpty()) {
                                list.add(stack);
                            }
                        }
                    }
                },
                (left, right) -> {
                    left.addAll(right);
                    return left;
                }
        ));
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
