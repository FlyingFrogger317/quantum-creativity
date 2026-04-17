package com.flyingfrog317.quantum_creativity.quantization;

import com.flyingfrog317.quantum_creativity.QuantumCreativity;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QuantizingRecipeCategory implements IRecipeCategory<QuantizingRecipe> {
    static public final RecipeType<QuantizingRecipe> recipeType=new RecipeType<>(QuantumCreativity.asResource("jei_quantizing"),QuantizingRecipe.class);
    private final IGuiHelper guiHelper;
    private final IDrawable background;
    private int xOffset=24;
    private int yOffset=13;
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
        builder.addInputSlot().addIngredient(VanillaTypes.ITEM_STACK,recipe.getInputItem()).setPosition(centerX,centerY);
        NonNullList<ItemStack> results=recipe.getResultItems();
        for (int i=0;i<results.size();i++){
            var offset=offsets[i];
            builder.addOutputSlot().addIngredient(VanillaTypes.ITEM_STACK, results.get(i)).setPosition(offset[0]+centerX,offset[1]+centerY).addRichTooltipCallback(new IRecipeSlotRichTooltipCallback() {
                @Override
                public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
                    tooltip.add(Component.translatable("quantum_creativity.text.random_output").withStyle(ChatFormatting.GOLD));
                }
            });
        }
    }
}
