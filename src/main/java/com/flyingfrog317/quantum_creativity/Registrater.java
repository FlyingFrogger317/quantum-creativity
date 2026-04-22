package com.flyingfrog317.quantum_creativity;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


public class Registrater {
    String MODID;
    Registrater(String modid){
        MODID=modid;
        block_registry = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
        item_registry = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
        fluid_type_registry = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MODID);
        fluid_registry = DeferredRegister.create(ForgeRegistries.FLUIDS, MODID);
        creative_mode_tab_registry = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    }
    public final DeferredRegister<Block> block_registry;
    public final DeferredRegister<Item> item_registry;
    public final DeferredRegister<FluidType> fluid_type_registry;
    public final DeferredRegister<Fluid> fluid_registry;
    public final DeferredRegister<CreativeModeTab> creative_mode_tab_registry;
    private String currentCreativeTab;
    private final Map<String,Collection<RegistryObject<Item>>> creativeModeTabs = new HashMap<>();
    private final Map<String,RegistryObject<Item>> items = new HashMap<>();
    private final Map<String,RegistryObject<Block>> blocks = new HashMap<>();
    public void register(FMLJavaModLoadingContext context){
        IEventBus modEventBus=context.getModEventBus();
        block_registry.register(modEventBus);
        item_registry.register(modEventBus);
        fluid_type_registry.register(modEventBus);
        fluid_registry.register(modEventBus);
        creative_mode_tab_registry.register(modEventBus);
        return;
    }
    private RegistryObject<Item> registerItem(String name, Item.Properties item) {
        return item_registry.register(name,()->new Item(item));
    }
    private RegistryObject<Item> registerGeoItem(String name, Item.Properties item) {
        return item_registry.register(name,() -> new GenericGeckoItem(item,MODID,name));
    }
    private void registerItemToCreativeTab(RegistryObject<Item> item){
        if (currentCreativeTab.isEmpty()) return;
        creativeModeTabs.get(currentCreativeTab).add(item);
    }
    private RegistryObject<Block> registerBlock(String name, BlockBehaviour.Properties block){
        return block_registry.register(name,() -> new Block(block));
    }
    private RegistryObject<Block> registerRawBlock(String name, Supplier<? extends Block> block){
        return block_registry.register(name,block);
    }
    private RegistryObject<Item> registerBlockItem(String name, RegistryObject<Block> block, Item.Properties properties){
        return item_registry.register(name,() -> new BlockItem(block.get(),properties));
    }
    private RegistryObject<Item> registerRawBlockItem(String name, RegistryObject<Block> block, Function<Block,? extends BlockItem> fn){
        return item_registry.register(name,() -> fn.apply(block.get()));
    }
    private RegistryObject<Item> registerRawItem(String name, Supplier<? extends Item> item){
        return item_registry.register(name,item);
    }
    @SuppressWarnings("UnusedReturnValue")
    public RegistryObject<Item> createItem(String name, Item.Properties item){
        RegistryObject<Item> itemO = registerItem(name, item);
        items.put(name,itemO);
        registerItemToCreativeTab(itemO);
        return  itemO;
    }
    public void createBlock(String name, BlockBehaviour.Properties block, Item.Properties properties){
        RegistryObject<Block> regBlock = registerBlock(name,block);
        RegistryObject<Item> item=registerBlockItem(name,regBlock,properties);
        items.put(name,item);
        blocks.put(name,regBlock);
        registerItemToCreativeTab(item);
    }
    public void createCreativeModeTab(String name){
        creative_mode_tab_registry.register(name,() -> CreativeModeTab.builder().displayItems((itemDisplayParameters, output) -> creativeModeTabs.get(name).forEach(item -> output.accept(item.get()))).title(Component.translatable("itemGroup.quantum_creativity."+name)).build());
        creativeModeTabs.put(name,new ArrayList<>());
    }
    public void usingCreativeTab(String name){
        currentCreativeTab=name;
    }
    @SuppressWarnings("unused")
    public void createGeoItem(String name, Item.Properties properties){
        RegistryObject<Item> item=registerGeoItem(name,properties);
        items.put(name,item);
        registerItemToCreativeTab(item);
    }
    public void createRawItem(String name, Supplier<? extends Item> item){
        RegistryObject<Item> itemO=registerRawItem(name,item);
        items.put(name,itemO);
        registerItemToCreativeTab(itemO);
    }
    @SuppressWarnings("UnusedReturnValue")
    public RegistryObject<FlowingFluid> createFluid(String name, FluidType.Properties properties) {
        ForgeFlowingFluid.Properties[] props = new ForgeFlowingFluid.Properties[1];
        RegistryObject<FluidType> fluidType = fluid_type_registry.register(name, () -> new FluidType(properties){
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {
                    @Override
                    public ResourceLocation getStillTexture() {
                        return ResourceLocation.fromNamespaceAndPath(MODID,"block/"+name+"_still");
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return ResourceLocation.fromNamespaceAndPath(MODID,"block/"+name+"_still");
                    }
                });
            }
        });
        RegistryObject<FlowingFluid> fluid = fluid_registry.register(name,()-> new ForgeFlowingFluid.Source(props[0]));
        RegistryObject<FlowingFluid> flowingFluid = fluid_registry.register("flowing_"+name,()-> new ForgeFlowingFluid.Flowing(props[0]));
        @SuppressWarnings("deprecation")
        RegistryObject<LiquidBlock> block = block_registry.register(name+"_block", ()->new LiquidBlock(fluid.get(),BlockBehaviour.Properties.of().replaceable()));
        @SuppressWarnings("deprecation")
        RegistryObject<Item> bucket_item = registerRawItem(name+"_bucket",()->new BucketItem(fluid.get(), new Item.Properties().stacksTo(1)));
        items.put(name+"_bucket",bucket_item);
        registerItemToCreativeTab(bucket_item);
        props[0] = new ForgeFlowingFluid.Properties(fluidType,fluid,flowingFluid).bucket(bucket_item).block(block);
        return fluid;
    }
    @SuppressWarnings("unused")
    public RegistryObject<Block> createRawBlock(String name, Supplier<? extends Block> block, Item.Properties properties){
        RegistryObject<Block> regBlock = registerRawBlock(name,block);
        RegistryObject<Item> item=registerBlockItem(name,regBlock,properties);
        items.put(name,item);
        blocks.put(name,regBlock);
        registerItemToCreativeTab(item);
        return regBlock;
    }
    @SuppressWarnings("UnusedReturnValue")
    public RegistryObject<Block> createRawRawBlock(String name, Supplier<? extends Block> block, Function<Block,? extends BlockItem> fn){
        RegistryObject<Block> regBlock = registerRawBlock(name,block);
        RegistryObject<Item> item=registerRawBlockItem(name,regBlock,fn);
        items.put(name,item);
        blocks.put(name,regBlock);
        registerItemToCreativeTab(item);
        return regBlock;
    }
    public RegistryObject<Item> getRegisteredItem(String name){
        return items.get(name);
    }
    public RegistryObject<Block> getRegisteredBlock(String name){
        return blocks.get(name);
    }
}
