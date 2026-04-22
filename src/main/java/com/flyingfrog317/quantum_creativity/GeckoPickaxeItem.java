package com.flyingfrog317.quantum_creativity;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class GeckoPickaxeItem extends PickaxeItem implements GeoItem {
    private final String MODID;
    private final String ITEM_NAME;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    GeckoPickaxeItem(Tier tier, int dmg, float speed, Properties  properties, String modid, String name){
        super(tier,dmg,speed,properties);
        MODID=modid;
        ITEM_NAME=name;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GenericGeckoRenderer renderer = null;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new GenericGeckoRenderer();

                return renderer;
            }
        });
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    public class GenericGeckoModel extends GeoModel<GeckoPickaxeItem> {
        @Override
        public ResourceLocation getModelResource(GeckoPickaxeItem genericGeckoItem) {
            return ResourceLocation.fromNamespaceAndPath(MODID,genericGeckoItem.getModelPath());
        }

        @Override
        public ResourceLocation getTextureResource(GeckoPickaxeItem genericGeckoItem) {
            return ResourceLocation.fromNamespaceAndPath(MODID,genericGeckoItem.getTexturePath());
        }

        @Override
        public ResourceLocation getAnimationResource(GeckoPickaxeItem genericGeckoItem) {
            return ResourceLocation.fromNamespaceAndPath(MODID, genericGeckoItem.getAnimationPath());
        }
    }

    private String getModelPath() {
        return "geo/"+ITEM_NAME+".geo.json";
    }

    private String getTexturePath() {
        return "textures/item/"+ITEM_NAME+".png";
    }
    private String getAnimationPath() {
        return "animations/"+ITEM_NAME+".animation.json";
    }
    public class GenericGeckoRenderer extends GeoItemRenderer<GeckoPickaxeItem> {
        public GenericGeckoRenderer() {
            super(new GenericGeckoModel())  ;
        }
    }
    //forwarding trash
}
