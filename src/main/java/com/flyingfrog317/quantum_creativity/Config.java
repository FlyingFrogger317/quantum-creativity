package com.flyingfrog317.quantum_creativity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = QuantumCreativity.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.BooleanValue ENABLE_AXE = BUILDER
            .comment("Enable the Quantum Axe")
            .define("axeEnabled",true);
    private static final ForgeConfigSpec.BooleanValue ENABLE_PICKAXE = BUILDER
            .comment("Enable the Quantum Axe")
            .define("pickaxeEnabled",true);
    static final ForgeConfigSpec SPEC = BUILDER.build();
    public static boolean axeEnabled;
    public static boolean pickaxeEnabled;
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        axeEnabled=ENABLE_AXE.get();
        pickaxeEnabled=ENABLE_PICKAXE.get();
    }
}
