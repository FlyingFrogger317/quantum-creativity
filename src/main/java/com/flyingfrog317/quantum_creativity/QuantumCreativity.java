package com.flyingfrog317.quantum_creativity;

import com.flyingfrog317.quantum_creativity.quantization.QuantizerBlock;
import com.flyingfrog317.quantum_creativity.quantization.QuantizingRegistries;
import com.flyingfrog317.quantum_creativity.tesseract.Tesseract;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.List;
import java.util.function.UnaryOperator;

@Mod(QuantumCreativity.MODID)
public class QuantumCreativity
{
    //boilerplate setup
    public static final String MODID = "quantum_creativity";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Registrater registrys;
    public static final EnumProxy<Rarity> QUANTUM_RARITY_PROXY = new EnumProxy<>(Rarity.class, -1, MODID+":quantum_rarity", (UnaryOperator<Style>) style -> style.withBold(true).withColor(ChatFormatting.DARK_PURPLE));
    private static final DeferredRegister<ArmorMaterial> ARMOR_REGISTERY = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, MODID);
    private static final Holder<ArmorMaterial> ARMOR_QUANTUM = ARMOR_REGISTERY.register("QUANTUM", ()->new ArmorMaterial(ItemBalancing.QUANTUM_ARMOR_DEFENCE_MAP,ItemBalancing.QUANTUM_ARMOR_ENCHANTABILITY, SoundEvents.ARMOR_EQUIP_GENERIC, ()-> Ingredient.of(registrys.getRegisteredItem("quantum_ingot").get()), List.of(),ItemBalancing.QUANTUM_ARMOR_TOUGHNESS, ItemBalancing.QUANTUM_ARMOR_KNOCKBACK_RESIST));
    public QuantumCreativity(ModLoadingContext context)
    {
        registrys  = new Registrater(MODID);
        registrys.createCreativeModeTab("quantum",()->new ItemStack(registrys.getRegisteredItem("quantum_ingot").get()));
        registrys.usingCreativeTab("quantum");
        QuantumTier QUANTUM = new QuantumTier();

        registrys.createRawItem("quantum_axe",()->
            new GeckoAxeItem(
                    QUANTUM,
                    new Item.Properties().rarity(QUANTUM_RARITY_PROXY.getValue()).stacksTo(ItemBalancing.SINGLE_STACK_SIZE).durability(ItemBalancing.QUANTUM_TOOL_DURABILITY).attributes(AxeItem.createAttributes(QUANTUM,ItemBalancing.QUANTUM_AXE_DAMAGE,ItemBalancing.QUANTUM_AXE_ATTACK_SPEED + ItemBalancing.ATTACK_SPEED_NORMALIZER)),
                    MODID,
                    "quantum_axe"
            )
        );
        registrys.createRawItem("quantum_pickaxe",()->
                new GeckoPickaxeItem(
                    QUANTUM,
                    new Item.Properties().rarity(QUANTUM_RARITY_PROXY.getValue()).stacksTo(ItemBalancing.SINGLE_STACK_SIZE).durability(ItemBalancing.QUANTUM_TOOL_DURABILITY).attributes(PickaxeItem.createAttributes(QUANTUM,ItemBalancing.QUANTUM_PICKAXE_DAMAGE,ItemBalancing.QUANTUM_PICKAXE_ATTACK_SPEED + ItemBalancing.ATTACK_SPEED_NORMALIZER)),
                    MODID,
                    "quantum_pickaxe"
                )
        );
        registrys.createRawItem("quantum_sword",()->
                new GeckoSwordItem(
                        QUANTUM,
                        new Item.Properties().rarity(QUANTUM_RARITY_PROXY.getValue()).stacksTo(ItemBalancing.SINGLE_STACK_SIZE).durability(ItemBalancing.QUANTUM_TOOL_DURABILITY).attributes(SwordItem.createAttributes(QUANTUM,ItemBalancing.QUANTUM_SWORD_DAMAGE,ItemBalancing.QUANTUM_SWORD_ATTACK_SPEED + ItemBalancing.ATTACK_SPEED_NORMALIZER)),
                        MODID,
                        "quantum_sword"
                )
        );
        registrys.createRawItem("quantum_shovel",()->
                new GeckoShovelItem(
                        QUANTUM,
                        new Item.Properties().rarity(QUANTUM_RARITY_PROXY.getValue()).stacksTo(ItemBalancing.SINGLE_STACK_SIZE).durability(ItemBalancing.QUANTUM_TOOL_DURABILITY).attributes(ShovelItem.createAttributes(QUANTUM,ItemBalancing.QUANTUM_SHOVEL_DAMAGE,ItemBalancing.QUANTUM_SHOVEL_ATTACK_SPEED + ItemBalancing.ATTACK_SPEED_NORMALIZER)),
                        MODID,
                        "quantum_shovel"
                )
        );
        registrys.createRawItem("quantum_hoe",() ->
                new GeckoHoeItem(
                        QUANTUM,
                        new Item.Properties().rarity(QUANTUM_RARITY_PROXY.getValue()).stacksTo(ItemBalancing.SINGLE_STACK_SIZE).durability(ItemBalancing.QUANTUM_TOOL_DURABILITY).attributes(HoeItem.createAttributes(QUANTUM,ItemBalancing.QUANTUM_HOE_DAMAGE,ItemBalancing.QUANTUM_HOE_ATTACK_SPEED + ItemBalancing.ATTACK_SPEED_NORMALIZER)),
                        MODID,
                        "quantum_hoe"
                )
        );
        registrys.createRawItem("quantum_helmet",()->new GeckoArmorItem(
                ARMOR_QUANTUM,
                ArmorItem.Type.HELMET,
                new Item.Properties().rarity(QUANTUM_RARITY_PROXY.getValue()).stacksTo(ItemBalancing.SINGLE_STACK_SIZE).durability(ItemBalancing.QUANTUM_TOOL_DURABILITY),
                MODID,
                "quantum_armor"
        ));
        registrys.createRawItem("quantum_chestplate",()->new GeckoArmorItem(
                ARMOR_QUANTUM,
                ArmorItem.Type.CHESTPLATE,
                new Item.Properties().rarity(QUANTUM_RARITY_PROXY.getValue()).stacksTo(ItemBalancing.SINGLE_STACK_SIZE).durability(ItemBalancing.QUANTUM_TOOL_DURABILITY),
                MODID,
                "quantum_armor"
        ));
        registrys.createRawItem("quantum_leggings",()->new GeckoArmorItem(
                ARMOR_QUANTUM,
                ArmorItem.Type.LEGGINGS,
                new Item.Properties().rarity(QUANTUM_RARITY_PROXY.getValue()).stacksTo(ItemBalancing.SINGLE_STACK_SIZE).durability(ItemBalancing.QUANTUM_TOOL_DURABILITY),
                MODID,
                "quantum_armor"
        ));
        registrys.createRawItem("quantum_boots",()->new GeckoArmorItem(
                ARMOR_QUANTUM,
                ArmorItem.Type.BOOTS,
                new Item.Properties().rarity(QUANTUM_RARITY_PROXY.getValue()).stacksTo(ItemBalancing.SINGLE_STACK_SIZE).durability(ItemBalancing.QUANTUM_TOOL_DURABILITY),
                MODID,
                "quantum_armor"
        ));
        registrys.createItem("damaged_quantum_storage_component", new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createItem("damaged_quantum_bubble", new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createItem("damaged_hardened_metal", new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createItem("damaged_quantized_magnet", new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createItem("damaged_aquatic_trinket", new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createItem("damaged_crystallized_experience", new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createItem("damaged_bottle_o_energy", new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createBlock("damaged_quantum_casing", Block.Properties.of().destroyTime(ItemBalancing.DAMAGED_QUANTUM_CASING_DESTROY_TIME), new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createItem("quantum_storage_component",new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createItem("quantum_bubble",new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createItem("hardened_metal",new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createItem("quantized_magnet",new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createItem("aquatic_trinket",new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createItem("crystallized_experience",new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createItem("bottle_o_energy",new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createBlock("quantum_casing",Block.Properties.of().destroyTime(ItemBalancing.QUANTUM_CASING_DESTROY_TIME),new Item.Properties().stacksTo(ItemBalancing.QUANTUM_COMPONENT_STACK_SIZE));
        registrys.createItem("quantum_mechanism",new Item.Properties().stacksTo(ItemBalancing.STANDARD_STACK_SIZE));
        registrys.createFluid(
                "liquid_quantum_metal",
                FluidType.Properties.create()
                        .density(ItemBalancing.LIQUID_QUANTUM_METAL_DENSITY)
                        .viscosity(ItemBalancing.LIQUID_QUANTUM_METAL_VISCOSITY)
                        .temperature(ItemBalancing.LIQUID_QUANTUM_METAL_TEMPERATURE)
        );
        registrys.createItem("quantum_ingot", new Item.Properties().stacksTo(ItemBalancing.STANDARD_STACK_SIZE));
        registrys.createBlock("block_of_quantum_ingot", BlockBehaviour.Properties.of(), new Item.Properties().stacksTo(ItemBalancing.STANDARD_STACK_SIZE));
        registrys.createItem("blank_smithing_template", new Item.Properties().stacksTo(ItemBalancing.STANDARD_STACK_SIZE));
        registrys.createItem("quantum_upgrade_template", new Item.Properties().stacksTo(ItemBalancing.STANDARD_STACK_SIZE));
        BlockBehaviour.Properties schrodingerBoxProperties = Block.Properties.of().strength(ItemBalancing.SCHRODINGER_BOX_STRENGTH);
        registrys.createBlock("unobserved_schrodingers_box", schrodingerBoxProperties, new Item.Properties().stacksTo(ItemBalancing.SMALL_STACK_SIZE));
        registrys.createBlock("observable_schrodingers_box", schrodingerBoxProperties, new Item.Properties().stacksTo(ItemBalancing.SMALL_STACK_SIZE));
        registrys.createBlock("observed_schrodingers_box", schrodingerBoxProperties, new Item.Properties().stacksTo(ItemBalancing.SINGLE_STACK_SIZE));
        registrys.createRawItem("tesseract", () ->
                new Tesseract(new Item.Properties().rarity(QUANTUM_RARITY_PROXY.getValue()).stacksTo(ItemBalancing.SINGLE_STACK_SIZE))
        );
        registrys.createRawRawBlock("quantizer",
            ()->new QuantizerBlock(BlockBehaviour.Properties.of().noOcclusion().strength(ItemBalancing.QUANTIZER_DESTROY_TIME, ItemBalancing.QUANTIZER_EXPLOSION_RESISTANCE).requiresCorrectToolForDrops()),
            block -> new GenericGeckoBlockItem(block,new Item.Properties().stacksTo(ItemBalancing.STANDARD_STACK_SIZE),MODID,"quantizer", item -> new AnimationController<>(
                    item,
                    state -> {
                        AnimationController<?> controller=state.getController();
                        if (controller.getCurrentAnimation()==null){
                            controller.setAnimation(RawAnimation.begin().then("rotate", Animation.LoopType.LOOP));
                        }
                        controller.setAnimationSpeed(ItemBalancing.QUANTIZER_ANIMATION_SPEED);
                        return PlayState.CONTINUE;
                    }
            ))
        );
        QuantizingRegistries.initialize(registrys);
        //transitionals
        registrys.usingCreativeTab("");
        registrys.createItem("incomplete_quantum_mechanism", new Item.Properties().stacksTo(ItemBalancing.SINGLE_STACK_SIZE));
        registrys.createBlock("incomplete_schrodingers_box", schrodingerBoxProperties, new Item.Properties().stacksTo(ItemBalancing.SINGLE_STACK_SIZE));
        registrys.createBlock("semiobserved_schrodingers_box", schrodingerBoxProperties, new Item.Properties().stacksTo(ItemBalancing.SINGLE_STACK_SIZE));
        registrys.createGeoItem("quantum_ingot_cast",new Item.Properties().stacksTo(ItemBalancing.SINGLE_STACK_SIZE));
        registrys.register(context);
        QuantizingRegistries.register(context);
        LOGGER.info("Mod loading complete");
        registrys.getBlocks().forEach((k,v) ->
                System.out.println("BLOCK KEY: " + k)
        );
    }

    public static ResourceLocation asResource(String name){
        return ResourceLocation.fromNamespaceAndPath(MODID,name);
    }
}
