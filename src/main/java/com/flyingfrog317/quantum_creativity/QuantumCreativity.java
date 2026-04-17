package com.flyingfrog317.quantum_creativity;

import com.flyingfrog317.quantum_creativity.quantization.QuantizerBlock;
import com.flyingfrog317.quantum_creativity.quantization.QuantizingRegistries;
import com.flyingfrog317.quantum_creativity.tesseract.Tesseract;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

@Mod(QuantumCreativity.MODID)
public class QuantumCreativity
{
    //boilerplate setup
    public static final String MODID = "quantum_creativity";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Registrater registrys;

    public QuantumCreativity(FMLJavaModLoadingContext context)
    {
        registrys  = new Registrater(MODID);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        Rarity quantumRarity = Rarity.create("QUANTUM",ChatFormatting.BOLD);
        registrys.createCreativeModeTab("quantum");
        registrys.usingCreativeTab("quantum");
        QuantumTier QUANTUM = new QuantumTier();
        QuantumArmorMaterial ARMOR_QUANTUM = new QuantumArmorMaterial();
        registrys.createRawItem("quantum_axe",()->
            new GeckoAxeItem(
                    Tiers.NETHERITE,
                    100,
                    1.2f,
                    new Item.Properties().rarity(quantumRarity).stacksTo(1).durability(10000),
                    MODID,
                    "quantum_axe"
            )
        );
        registrys.createRawItem("quantum_pickaxe",()->
                new GeckoPickaxeItem(
                    Tiers.NETHERITE,
                    30,
                    0.8f,
                    new Item.Properties().rarity(quantumRarity).stacksTo(1).durability(10000),
                    MODID,
                    "quantum_pickaxe"
                )
        );
        registrys.createRawItem("quantum_sword",()->
                new GeckoSwordItem(
                        QUANTUM,
                        50,
                        3,
                        new Item.Properties().rarity(quantumRarity).stacksTo(1).durability(10000),
                        MODID,
                        "quantum_sword"
                )
        );
        registrys.createRawItem("quantum_shovel",()->
                new GeckoShovelItem(
                        QUANTUM,
                        30,
                        0.8f,
                        new Item.Properties().rarity(quantumRarity).stacksTo(1).durability(10000),
                        MODID,
                        "quantum_shovel"
                )
        );
        registrys.createRawItem("quantum_helmet",()->new GeckoArmorItem(
                ARMOR_QUANTUM,
                ArmorItem.Type.HELMET,
                new Item.Properties().rarity(quantumRarity).stacksTo(1).durability(10000),
                MODID,
                "quantum_armor"
        ));
        registrys.createRawItem("quantum_chestplate",()->new GeckoArmorItem(
                ARMOR_QUANTUM,
                ArmorItem.Type.CHESTPLATE,
                new Item.Properties().rarity(quantumRarity).stacksTo(1).durability(10000),
                MODID,
                "quantum_armor"
        ));
        registrys.createRawItem("quantum_leggings",()->new GeckoArmorItem(
                ARMOR_QUANTUM,
                ArmorItem.Type.LEGGINGS,
                new Item.Properties().rarity(quantumRarity).stacksTo(1).durability(10000),
                MODID,
                "quantum_armor"
        ));
        registrys.createRawItem("quantum_boots",()->new GeckoArmorItem(
                ARMOR_QUANTUM,
                ArmorItem.Type.BOOTS,
                new Item.Properties().rarity(quantumRarity).stacksTo(1).durability(10000),
                MODID,
                "quantum_armor"
        ));
        int quantumComponentStackSize=64;
        registrys.createItem("improvised_quantum_storage_component", new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createItem("improvised_quantum_bubble", new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createItem("improvised_hardened_metal", new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createItem("improvised_quantized_magnet", new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createItem("improvised_aquatic_trinket", new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createItem("improvised_crystallized_experience", new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createItem("improvised_bottle_o_energy", new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createBlock("improvised_quantum_casing", Block.Properties.of().destroyTime(0.2f), new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createItem("quantum_storage_component",new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createItem("quantum_bubble",new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createItem("hardened_metal",new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createItem("quantized_magnet",new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createItem("aquatic_trinket",new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createItem("crystallized_experience",new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createItem("bottle_o_energy",new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createBlock("quantum_casing",Block.Properties.of().destroyTime(0.2f),new Item.Properties().stacksTo(quantumComponentStackSize));
        registrys.createItem("quantum_mechanism",new Item.Properties().stacksTo(64));
        registrys.createFluid(
                "liquid_quantum_metal",
                FluidType.Properties.create().density(3000).viscosity(6000).temperature(1300)
        );
        registrys.createItem("quantum_ingot", new Item.Properties().stacksTo(64));
        registrys.createItem("blank_smithing_template", new Item.Properties().stacksTo(64));
        BlockBehaviour.Properties schrodingerBoxProperties = Block.Properties.of().strength(0.5f);
        registrys.createBlock("unobserved_schrodingers_box", schrodingerBoxProperties, new Item.Properties().stacksTo(16));
        registrys.createBlock("observable_schrodingers_box", schrodingerBoxProperties, new Item.Properties().stacksTo(16));
        registrys.createBlock("observed_schrodingers_box", schrodingerBoxProperties, new Item.Properties().stacksTo(1));
        registrys.createRawItem("tesseract", () ->
                new Tesseract(new Item.Properties().rarity(quantumRarity).stacksTo(1))
        );
        registrys.createRawRawBlock("quantizer",
            ()->new QuantizerBlock(BlockBehaviour.Properties.of().noOcclusion().strength(1.5f,20f).requiresCorrectToolForDrops()),
            block -> new GenericGeckoBlockItem(block,new Item.Properties().stacksTo(64),MODID,"quantizer", item -> new AnimationController<>(
                    item,
                    state -> {
                        AnimationController<?> controller=state.getController();
                        if (controller.getCurrentAnimation()==null){
                            controller.setAnimation(RawAnimation.begin().then("rotate", Animation.LoopType.LOOP));
                        }
                        controller.setAnimationSpeed(0.2f);
                        return PlayState.CONTINUE;
                    }
            ))
        );
        QuantizingRegistries.initialize(registrys);
        //transitionals
        registrys.usingCreativeTab("");
        registrys.createItem("incomplete_quantum_mechanism", new Item.Properties().stacksTo(1));
        registrys.createItem("incomplete_blank_smithing_template", new Item.Properties().stacksTo(1));
        registrys.createBlock("incomplete_schrodingers_box", schrodingerBoxProperties, new Item.Properties().stacksTo(1));
        registrys.createBlock("semiobserved_schrodingers_box", schrodingerBoxProperties, new Item.Properties().stacksTo(1));
        registrys.createItem("quantum_ingot_cast",new Item.Properties().stacksTo(1));
        registrys.register(context);
        QuantizingRegistries.register(context);
        LOGGER.info("Mod loading complete");
    }
    public static ResourceLocation asResource(String name){
        return ResourceLocation.fromNamespaceAndPath(MODID,name);
    }
}
