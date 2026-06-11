package com.flyingfrog317.quantum_creativity;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public interface GenericGeoSystem extends GeoItem {
    String getModId();
    String getItemName();
    final AnimatableInstanceCache cache = null;

    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar controllers){

    };

    @Override
    default AnimatableInstanceCache getAnimatableInstanceCache(){
        return cache;
    };

    @Override
    default void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GenericGeckoRenderer<?> renderer = null;
            @Override
            public @Nullable BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (renderer==null) renderer=new GenericGeckoRenderer<>();
                return renderer;
            }
        });
    }
    default String getModelPath() {
        return "geo/"+getItemName()+".geo.json";
    }

    default String getTexturePath() {
        return "textures/item/"+getItemName()+".png";
    }
    default String getAnimationPath() {
        return "animations/"+getItemName()+".animation.json";
    }
    class GenericGeckoModel<T extends Item & GenericGeoSystem> extends GeoModel<T> {
        @Override
        public ResourceLocation getModelResource(T animatable) {
            return ResourceLocation.fromNamespaceAndPath(animatable.getModId(),animatable.getModelPath());
        }

        @Override
        public ResourceLocation getTextureResource(T animatable) {
            return ResourceLocation.fromNamespaceAndPath(animatable.getModId(),animatable.getTexturePath());
        }

        @Override
        public ResourceLocation getAnimationResource(T animatable) {
            return ResourceLocation.fromNamespaceAndPath(animatable.getModId(), animatable.getAnimationPath());
        }

    }
    public class GenericGeckoRenderer<T extends Item & GenericGeoSystem> extends GeoItemRenderer<T> {
        public GenericGeckoRenderer() {
            super(new GenericGeckoModel<>());
        }
    }
}
