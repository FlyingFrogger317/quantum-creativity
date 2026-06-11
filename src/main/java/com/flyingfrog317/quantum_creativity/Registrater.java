package com.flyingfrog317.quantum_creativity;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;


public class Registrater {
    final String MODID;
    Registrater(String modid){
        MODID=modid;
        block_registry = DeferredRegister.createBlocks(MODID);
        item_registry = DeferredRegister.createItems(MODID);
        fluid_type_registry = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, MODID);
        fluid_registry = DeferredRegister.create(Registries.FLUID, MODID);
        creative_mode_tab_registry = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    }
    public final DeferredRegister<Block> block_registry;
    public final DeferredRegister<Item> item_registry;
    public final DeferredRegister<FluidType> fluid_type_registry;
    public final DeferredRegister<Fluid> fluid_registry;
    public final DeferredRegister<CreativeModeTab> creative_mode_tab_registry;
    private String currentCreativeTab;
    private final Map<String,Collection<Supplier<Item>>> creativeModeTabs = new HashMap<>();
    private final Map<String,Supplier<Item>> items = new HashMap<>();
    private final Map<String,Supplier<Block>> blocks = new HashMap<>();
    public void register(ModLoadingContext context){
        IEventBus modEventBus=context.getActiveContainer().getEventBus();
        block_registry.register(modEventBus);
        item_registry.register(modEventBus);
        fluid_type_registry.register(modEventBus);
        fluid_registry.register(modEventBus);
        creative_mode_tab_registry.register(modEventBus);
        return;
    }
    private Supplier<Item> registerItem(String name, Item.Properties item) {
        return item_registry.register(name,()->new Item(item));
    }
    private Supplier<Item> registerGeoItem(String name, Item.Properties item) {
        return item_registry.register(name,() -> new GenericGeckoItem(item,MODID,name));
    }
    private void registerItemToCreativeTab(Supplier<Item> item){
        if (currentCreativeTab.isEmpty()) return;
        creativeModeTabs.get(currentCreativeTab).add(item);
    }
    private Supplier<Block> registerBlock(String name, BlockBehaviour.Properties block){
        return block_registry.register(name,() -> new Block(block));
    }
    private Supplier<Block> registerRawBlock(String name, Supplier<? extends Block> block){
        return block_registry.register(name,block);
    }
    private Supplier<Item> registerBlockItem(String name, Supplier<Block> block, Item.Properties properties){
        return item_registry.register(name,() -> new BlockItem(block.get(),properties));
    }
    private Supplier<Item> registerRawBlockItem(String name, Supplier<Block> block, Function<Block,? extends BlockItem> fn){
        return item_registry.register(name,() -> fn.apply(block.get()));
    }
    private Supplier<Item> registerRawItem(String name, Supplier<? extends Item> item){
        return item_registry.register(name,item);
    }
    @SuppressWarnings("UnusedReturnValue")
    public Supplier<Item> createItem(String name, Item.Properties item){
        Supplier<Item> itemO = registerItem(name, item);
        items.put(name,itemO);
        registerItemToCreativeTab(itemO);
        return  itemO;
    }
    public void createBlock(String name, BlockBehaviour.Properties block, Item.Properties properties){
        Supplier<Block> regBlock = registerBlock(name,block);
        Supplier<Item> item=registerBlockItem(name,regBlock,properties);
        items.put(name,item);
        blocks.put(name,regBlock);
        registerItemToCreativeTab(item);
    }
    public void createCreativeModeTab(String name, Supplier<ItemStack> icon){
        creative_mode_tab_registry.register(name,() -> CreativeModeTab.builder().displayItems((itemDisplayParameters, output) -> creativeModeTabs.get(name).forEach(item -> output.accept(item.get()))).title(Component.translatable("itemGroup.quantum_creativity."+name)).icon(icon).build());
        creativeModeTabs.put(name,new ArrayList<>());
    }
    public void usingCreativeTab(String name){
        currentCreativeTab=name;
    }
    @SuppressWarnings("unused")
    public void createGeoItem(String name, Item.Properties properties){
        Supplier<Item> item=registerGeoItem(name,properties);
        items.put(name,item);
        registerItemToCreativeTab(item);
    }
    public void createRawItem(String name, Supplier<? extends Item> item){
        Supplier<Item> itemO=registerRawItem(name,item);
        items.put(name,itemO);
        registerItemToCreativeTab(itemO);
    }
    @SuppressWarnings("UnusedReturnValue")
    public Supplier<FlowingFluid> createFluid(String name, FluidType.Properties properties) {
        BaseFlowingFluid.Properties[] props = new BaseFlowingFluid.Properties[1];
        Supplier<FluidType> fluidType = fluid_type_registry.register(name, () -> new FluidType(properties){

//            @Override
//            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
//                consumer.accept(new IClientFluidTypeExtensions() {
//                    @Override
//                    public ResourceLocation getStillTexture() {
//                        return ResourceLocation.fromNamespaceAndPath(MODID,"block/"+name+"_still");
//                    }
//
//                    @Override
//                    public ResourceLocation getFlowingTexture() {
//                        return ResourceLocation.fromNamespaceAndPath(MODID,"block/"+name+"_still");
//                    }
//                });
//            }
        });
        Supplier<FlowingFluid> fluid = fluid_registry.register(name,()-> new BaseFlowingFluid.Source(props[0]));
        Supplier<FlowingFluid> flowingFluid = fluid_registry.register("flowing_"+name,()-> new BaseFlowingFluid.Flowing(props[0]));
        Supplier<LiquidBlock> block = block_registry.register(name+"_block", ()->new LiquidBlock(fluid.get(),BlockBehaviour.Properties.of().replaceable()));
        Supplier<Item> bucket_item = registerRawItem(name+"_bucket",()->new BucketItem(fluid.get(), new Item.Properties().stacksTo(1)));
        items.put(name+"_bucket",bucket_item);
        registerItemToCreativeTab(bucket_item);
        props[0] = new BaseFlowingFluid.Properties(fluidType,fluid,flowingFluid).bucket(bucket_item).block(block);
        return fluid;
    }
    @SuppressWarnings("unused")
    public Supplier<Block> createRawBlock(String name, Supplier<? extends Block> block, Item.Properties properties){
        Supplier<Block> regBlock = registerRawBlock(name,block);
        Supplier<Item> item=registerBlockItem(name,regBlock,properties);
        items.put(name,item);
        blocks.put(name,regBlock);
        registerItemToCreativeTab(item);
        return regBlock;
    }
    @SuppressWarnings("UnusedReturnValue")
    public Supplier<Block> createRawRawBlock(String name, Supplier<? extends Block> block, Function<Block,? extends BlockItem> fn){
        Supplier<Block> regBlock = registerRawBlock(name,block);
        Supplier<Item> item=registerRawBlockItem(name,regBlock,fn);
        items.put(name,item);
        blocks.put(name,regBlock);
        registerItemToCreativeTab(item);
        return regBlock;
    }
    public Supplier<Item> getRegisteredItem(String name){
        return items.get(name);
    }
    public Supplier<Block> getRegisteredBlock(String name){
        return blocks.get(name);
    }
    public Map<String,Supplier<Block>> getBlocks(){
        return blocks;
    }
}
