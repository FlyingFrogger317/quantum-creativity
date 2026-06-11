package com.flyingfrog317.quantum_creativity.quantization;

import com.flyingfrog317.quantum_creativity.QuantumCreativity;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QuantizingRecipeCategory implements IRecipeCategory<QuantizingRecipe> {
    static public final RecipeType<QuantizingRecipe> recipeType=new RecipeType<>(QuantumCreativity.asResource("jei_quantizing"),QuantizingRecipe.class);
    private final IGuiHelper guiHelper;
    private final IDrawable background;
    private final int xOffset=24;
    private final int yOffset=13;
    public QuantizingRecipeCategory(IGuiHelper helper){
        this.background = helper.createDrawable(QuantumCreativity.asResource("textures/gui/container/quantizer.png"),xOffset,yOffset,128,128);
        guiHelper=helper;
    }
    @Override
    @NotNull
    public RecipeType<QuantizingRecipe> getRecipeType() {
        return recipeType;
    }
    @Override
    @NotNull
    public Component getTitle() {
        return Component.translatable("recipe.quantum_creativity.quantizing");
    }
    @Override
    public @Nullable IDrawable getIcon() {
        return guiHelper.createDrawableItemStack(new ItemStack(QuantizingRegistries.QuantizerItemReg.get()));
    }
    @SuppressWarnings("removal")
    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, QuantizingRecipe recipe, IFocusGroup iFocusGroup) {
        int centerX = 80-xOffset;
        int centerY = 69-yOffset;
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
        builder.addInputSlot().addIngredients(recipe.inputs).setPosition(centerX,centerY);
        NonNullList<ItemStack> results=recipe.getResultItems();
        int items_per_slot= (int) Math.ceil((double)results.size()/12.0);
        int extra_items_slots= results.size()%12;
        int index=0;
        for (int[] offset : offsets) {
            IRecipeSlotBuilder slot = builder.addOutputSlot(centerX + offset[0], centerY + offset[1]);
            int slots = ((extra_items_slots--) > 0) ? items_per_slot : items_per_slot - 1;
            for (int q = 0; q < slots; q++) {
                slot.addIngredient(VanillaTypes.ITEM_STACK, results.get(index));
                index++;
            }
            slot.addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable("quantum_creativity.text.random_output").withStyle(ChatFormatting.GOLD)));
        }
    }
}
