package com.flyingfrog317.quantum_creativity.quantization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QuantizingSerializer implements RecipeSerializer<QuantizingRecipe> {
    public static final MapCodec<QuantizingRecipe> CODEC= RecordCodecBuilder.mapCodec(inst->inst.group(
            Ingredient.CODEC.fieldOf("input").forGetter(QuantizingRecipe::getInput),
            Ingredient.CODEC.listOf().fieldOf("outputs").forGetter(QuantizingRecipe::getOutputs)
    ).apply(inst,(a,b)->new QuantizingRecipe(a,NonNullList.copyOf(b))));
    public static final StreamCodec<RegistryFriendlyByteBuf, QuantizingRecipe> STREAM_CODEC=StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.inputs,Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),recipe->recipe.outputs,(a,b)->new QuantizingRecipe(a,NonNullList.copyOf(b)));
    @Override
    public MapCodec<QuantizingRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, QuantizingRecipe> streamCodec() {
        return STREAM_CODEC;
    }

//    @Override
//    public @NotNull QuantizingRecipe fromJson(@NotNull ResourceLocation resourceLocation, JsonObject jsonObject) {
//        Ingredient input = Ingredient.fromJson(jsonObject.get("input"));
//        NonNullList<Ingredient> outputs= NonNullList.create();
//        JsonArray arr = jsonObject.getAsJsonArray("outputs");
//        if (arr.isEmpty()){
//            throw new com.google.gson.JsonParseException("Outputs cannot be empty");
//        }
//        for (JsonElement obj : arr){
//            outputs.add(Ingredient.fromJson(obj));
//        }
//        return new QuantizingRecipe(resourceLocation,input,outputs);
//    }
//    @Override
//    public @Nullable QuantizingRecipe fromNetwork(@NotNull ResourceLocation resourceLocation, @NotNull FriendlyByteBuf buf) {
//        Ingredient input=Ingredient.fromNetwork(buf);
//        int output=buf.readVarInt();
//        NonNullList<Ingredient> outputs=NonNullList.create();
//        for (int i=0;i<output;i++){
//            outputs.add(Ingredient.fromNetwork(buf));
//        }
//        return new QuantizingRecipe(resourceLocation,input,outputs);
//    }
//
//    @Override
//    public void toNetwork(@NotNull FriendlyByteBuf buf, QuantizingRecipe quantizingRecipe) {
//        quantizingRecipe.inputs.toNetwork(buf);
//        buf.writeVarInt(quantizingRecipe.outputs.size());
//        for (Ingredient item : quantizingRecipe.outputs){
//            item.toNetwork(buf);
//        }
//        return;
//    }
}
