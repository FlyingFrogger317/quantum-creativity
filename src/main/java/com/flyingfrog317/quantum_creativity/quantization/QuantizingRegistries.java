package com.flyingfrog317.quantum_creativity.quantization;

import com.flyingfrog317.quantum_creativity.QuantumCreativity;
import com.flyingfrog317.quantum_creativity.Registrater;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@EventBusSubscriber(modid = QuantumCreativity.MODID, value = Dist.CLIENT)
public class QuantizingRegistries {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    static private Registrater registrater;
    static private final DeferredRegister<RecipeSerializer<?>> RECIPE_REGISTERY = DeferredRegister.create(Registries.RECIPE_SERIALIZER, QuantumCreativity.MODID);
    static private final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, QuantumCreativity.MODID);
    static private final DeferredRegister<MenuType<?>> MENUTYPE_REGISTRY = DeferredRegister.create(Registries.MENU,QuantumCreativity.MODID);
    static private final DeferredRegister<RecipeType<?>> RECIPETYPE_REGISTRY = DeferredRegister.create(Registries.RECIPE_TYPE,QuantumCreativity.MODID);
    static public DeferredHolder<RecipeType<?>,RecipeType<QuantizingRecipe>> QuantizingRecipeType;
    static public Supplier<Block> QuantizerBlockReg;
    static public Supplier<Item> QuantizerItemReg;
    static public DeferredHolder<RecipeSerializer<?>,RecipeSerializer<QuantizingRecipe>> QuantizingRecipeSerializer;
    static public DeferredHolder<BlockEntityType<?>,BlockEntityType<QuantizerBlockEntity>> QuantizerBlockEntityReg;
    static public Supplier<MenuType<QuantizerBlockMenu>> QuantizerBlockMenuReg;
    static public BlockCapability<IItemHandler, Direction> ItemHandlerCap = BlockCapability.createSided(QuantumCreativity.asResource("item_handler"),IItemHandler.class);
    public static void initialize(Registrater reg){
        registrater=reg;
        //noinspection Convert2Diamond
        QuantizingRecipeType = RECIPETYPE_REGISTRY.register("quantizing", ()-> new RecipeType<QuantizingRecipe>() {});
        QuantizingRecipeSerializer = RECIPE_REGISTERY.register("quantizing", QuantizingSerializer::new);
        QuantizerBlockReg=reg.getRegisteredBlock("quantizer");
        QuantizerItemReg=reg.getRegisteredItem("quantizer");
        //noinspection DataFlowIssue
        QuantizerBlockEntityReg = BLOCK_ENTITY_REGISTRY.register("quantizer_entity",()->BlockEntityType.Builder.of(QuantizerBlockEntity::new,QuantizerBlockReg.get()).build(null));
        QuantizerBlockMenuReg = MENUTYPE_REGISTRY.register("quantizer_menu",()-> IMenuTypeExtension.create(QuantizerBlockMenu::new));
    }
    public static void register(ModLoadingContext ctx){
        IEventBus bus= ctx.getActiveContainer().getEventBus();
        RECIPETYPE_REGISTRY.register(bus);
        MENUTYPE_REGISTRY.register(bus);
        BLOCK_ENTITY_REGISTRY.register(bus);
        RECIPE_REGISTERY.register(bus);
    }
    @SubscribeEvent
    public static void registerScreen(RegisterMenuScreensEvent ctx){
        ctx.register(QuantizerBlockMenuReg.get(),QuantizerBlockScreen::new);
    }
}
