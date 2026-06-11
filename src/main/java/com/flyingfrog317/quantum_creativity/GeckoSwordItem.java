package com.flyingfrog317.quantum_creativity;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class GeckoSwordItem extends SwordItem implements GenericGeoSystem {
    private final String MODID;
    private final String ITEM_NAME;
    GeckoSwordItem(Tier tier, Item.Properties  properties, String modid, String name){
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
