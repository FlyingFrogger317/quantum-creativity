package com.flyingfrog317.quantum_creativity.quantization;

import com.flyingfrog317.quantum_creativity.QuantumCreativity;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.crafting.RecipeManager;

@EmiEntrypoint
public class EMIPlugin implements EmiPlugin {
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(QuantumCreativity.asResource("quantizing"), EmiStack.of(QuantizingRegistries.QuantizerBlockReg.get()));
    @Override
    public void register(EmiRegistry emiRegistry) {
        emiRegistry.addCategory(CATEGORY);
        emiRegistry.addWorkstation(CATEGORY,EmiStack.of(QuantizingRegistries.QuantizerBlockReg.get()));
        RecipeManager manager=emiRegistry.getRecipeManager();
        for (QuantizingRecipe recipe : manager.getAllRecipesFor(QuantizingRegistries.QuantizingRecipeType.get())){
            emiRegistry.addRecipe(new EMIRecipe(recipe));
        }
        QuantumCreativity.LOGGER.info("EMI registered");
    }
}
