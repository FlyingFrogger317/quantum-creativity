package com.flyingfrog317.quantum_creativity;

import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

public class GeckoPickaxeItem extends PickaxeItem implements GenericGeoSystem {
    private final String MODID;
    private final String ITEM_NAME;
    GeckoPickaxeItem(Tier tier, Properties  properties, String modid, String name){
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
