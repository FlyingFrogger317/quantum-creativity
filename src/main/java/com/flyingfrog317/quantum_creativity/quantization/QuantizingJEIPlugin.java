package com.flyingfrog317.quantum_creativity.quantization;

import com.flyingfrog317.quantum_creativity.QuantumCreativity;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class QuantizingJEIPlugin implements IModPlugin {
    private static final ResourceLocation uid = QuantumCreativity.asResource("jei_plugin");
    @Override
    public ResourceLocation getPluginUid() {
        return uid;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        registration.addRecipeCategories(new QuantizingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(
                QuantizingRecipeCategory.recipeType,
                Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(QuantizingRegistries.QuantizingRecipeType.get())
        );
    }
}
