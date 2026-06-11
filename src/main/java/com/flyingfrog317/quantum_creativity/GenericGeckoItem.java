package com.flyingfrog317.quantum_creativity;

import net.minecraft.world.item.Item;

public class GenericGeckoItem extends Item implements GenericGeoSystem {
    private final String MODID;
    private final String ITEM_NAME;
    GenericGeckoItem(Item.Properties properties, String modid, String name){
        super(properties);
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
