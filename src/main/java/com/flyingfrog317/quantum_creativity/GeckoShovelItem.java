package com.flyingfrog317.quantum_creativity;

import net.minecraft.world.item.*;

public class GeckoShovelItem extends ShovelItem implements GenericGeoSystem {
    private final String MODID;
    private final String ITEM_NAME;
    GeckoShovelItem(Tier tier, Properties  properties, String modid, String name){
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
