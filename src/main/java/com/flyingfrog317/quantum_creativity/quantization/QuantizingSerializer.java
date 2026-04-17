package com.flyingfrog317.quantum_creativity.quantization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.stream.Collectors;

public class QuantizingSerializer implements RecipeSerializer<QuantizingRecipe> {
    @Override
    public QuantizingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
        Ingredient input = Ingredient.fromJson(jsonObject.get("input"));
        NonNullList<ItemStack> outputs= NonNullList.create();
        JsonArray arr = jsonObject.getAsJsonArray("outputs");
        for (JsonElement obj : arr){
            outputs.addAll(Ingredient.valueFromJson(obj.getAsJsonObject()).getItems());
        }
        return new QuantizingRecipe(resourceLocation,input,outputs);
    }
    @Override
    public @Nullable QuantizingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
        Ingredient input=Ingredient.fromNetwork(buf);
        int output=buf.readVarInt();
        NonNullList<ItemStack> outputs=NonNullList.createWithCapacity(output);
        for (int i=0;i<outputs.size();i++){
            outputs.add(buf.readItem());
        }
        return new QuantizingRecipe(resourceLocation,input,outputs);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, QuantizingRecipe quantizingRecipe) {
        buf.writeVarInt(quantizingRecipe.getIngredients().size());
        for (Ingredient i : quantizingRecipe.getIngredients()){
            i.toNetwork(buf);
        }
        buf.writeVarInt(quantizingRecipe.getResultItems().size());
        for (ItemStack item : quantizingRecipe.getResultItems()){
            buf.writeItem(item);
        }
        return;
    }
}
