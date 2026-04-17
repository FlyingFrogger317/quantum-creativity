package com.flyingfrog317.quantum_creativity.quantization;

import com.flyingfrog317.quantum_creativity.QuantumCreativity;
import com.flyingfrog317.quantum_creativity.Registrater;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
@Mod.EventBusSubscriber(modid = QuantumCreativity.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class QuantizingRegistries {
    static private Registrater registrater;
    static private final DeferredRegister<RecipeSerializer<?>> RECIPE_REGISTERY = DeferredRegister.create(ForgeRegistries.Keys.RECIPE_SERIALIZERS, QuantumCreativity.MODID);
    static private final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, QuantumCreativity.MODID);
    static private final DeferredRegister<MenuType<?>> MENUTYPE_REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.MENU_TYPES,QuantumCreativity.MODID);
    static private final DeferredRegister<RecipeType<?>> RECIPETYPE_REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.RECIPE_TYPES,QuantumCreativity.MODID);
    static public RegistryObject<RecipeType<QuantizingRecipe>> QuantizingRecipeType;
    static public RegistryObject<Block> QuantizerBlockReg;
    static public RegistryObject<Item> QuantizerItemReg;
    static public RegistryObject<RecipeSerializer<QuantizingRecipe>> QuantizingRecipeSerializer;
    static public RegistryObject<BlockEntityType<QuantizerBlockEntity>> QuantizerBlockEntityReg;
    static public RegistryObject<MenuType<QuantizerBlockMenu>> QuantizerBlockMenuReg;
    public static void initialize(Registrater reg){
        registrater=reg;
        QuantizingRecipeType = RECIPETYPE_REGISTRY.register("quantizing", ()-> new RecipeType<QuantizingRecipe>() {});
        QuantizingRecipeSerializer = RECIPE_REGISTERY.register("quantizing", QuantizingSerializer::new);
        QuantizerBlockReg=reg.getRegisteredBlock("quantizer");
        QuantizerItemReg=reg.getRegisteredItem("quantizer");
        QuantizerBlockEntityReg = BLOCK_ENTITY_REGISTRY.register("quantizer_entity",()->BlockEntityType.Builder.of(QuantizerBlockEntity::new,QuantizerBlockReg.get()).build(null));
        QuantizerBlockMenuReg = MENUTYPE_REGISTRY.register("quantizer_menu",()-> IForgeMenuType.create(QuantizerBlockMenu::new));
    }
    public static void register(FMLJavaModLoadingContext ctx){
        IEventBus bus= ctx.getModEventBus();
        RECIPETYPE_REGISTRY.register(bus);
        MENUTYPE_REGISTRY.register(bus);
        BLOCK_ENTITY_REGISTRY.register(bus);
        RECIPE_REGISTERY.register(bus);
    }
    @SubscribeEvent
    public static void registerScreen(FMLClientSetupEvent ctx){
        QuantumCreativity.LOGGER.info("registering pelase");
        MenuScreens.register(QuantizerBlockMenuReg.get(),QuantizerBlockScreen::new);
    }
}
