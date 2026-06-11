package com.flyingfrog317.quantum_creativity.quantization;

import com.flyingfrog317.quantum_creativity.QuantumCreativity;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EMIRecipe implements EmiRecipe {
    final QuantizingRecipe recipe;
    private final int vxOffset=10;
    private final int vyOffset=10;
    public EMIRecipe(QuantizingRecipe recipe){
        this.recipe=recipe;
    }
    @Override
    public EmiRecipeCategory getCategory() {
        return EMIPlugin.CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return recipe.getId();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(EmiIngredient.of(recipe.inputs));
    }

    @Override
    public List<EmiStack> getOutputs() {
        List<EmiStack> items = new ArrayList<>();
        for (ItemStack i : recipe.getResultItems()){
            items.add(EmiStack.of(i));
        }
        return items;
    }

    @Override
    public int getDisplayWidth() {
        return 128+vxOffset*2;
    }

    @Override
    public int getDisplayHeight() {
        return 128+vyOffset*2;
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        int xOffset = 24;
        int yOffset = 13;
        EmiTexture texture = new EmiTexture(QuantumCreativity.asResource("textures/gui/container/quantizer.png"), xOffset, yOffset,128,128);
        widgetHolder.addTexture(texture,vxOffset,vyOffset);
        int centerX = 80- xOffset +vxOffset-1;
        int centerY = 69- yOffset +vyOffset-1;
        int[][] offsets = {
                {0, -51},    // Slot above center
                {26, -45},   // Slot 2
                {44, -25},   // Slot 3
                {51, 0},    // Slot 4
                {44, 25},    // Slot 5
                {26, 45},    // Slot 6
                {0, 51},    // Slot 7
                {-26, 45},   // Slot 8
                {-44, 25},   // Slot 9
                {-51, 0},   // Slot 10
                {-44, -25},  // Slot 11
                {-26, -45}   // Slot 12
        };
        widgetHolder.addSlot(EmiIngredient.of(recipe.inputs),centerX,centerY).drawBack(false);
        NonNullList<ItemStack> items=recipe.getResultItems();
        int items_per_slot= (int) Math.ceil((double)items.size()/12.0);
        int extra_items_slots= items.size()%12;
        int index=0;
        for (int[] offset : offsets) {
            List<EmiIngredient> ingredients = new ArrayList<>();
            int slots = ((extra_items_slots--) > 0) ? items_per_slot : items_per_slot - 1;
            for (int q = 0; q < slots; q++) {
                ingredients.add(EmiIngredient.of(Ingredient.of(items.get(index))));
                index++;
            }
            EmiIngredient finalized = EmiIngredient.of(ingredients);
            widgetHolder.addSlot(finalized, centerX+offset[0], centerY+offset[1]).appendTooltip(Component.translatable("quantum_creativity.text.random_output").withStyle(ChatFormatting.GOLD)).recipeContext(this).drawBack(false);
        }
    }
}
