package com.flyingfrog317.quantum_creativity;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import java.util.function.Consumer;

public class GeckoAxeItem extends AxeItem implements GenericGeoSystem {
    private final String MODID;
    private final String ITEM_NAME;
    GeckoAxeItem(Tier tier, Item.Properties  properties, String modid, String name){
        super(tier,properties);
        MODID=modid;
        ITEM_NAME=name;
    }

    @Override
    public String getModId() {
        return MODID;
    }

    @Override
    public String getItemName() {
        return ITEM_NAME;
    }
}
