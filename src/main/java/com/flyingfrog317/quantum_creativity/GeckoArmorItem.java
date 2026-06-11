package com.flyingfrog317.quantum_creativity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class GeckoArmorItem extends ArmorItem implements GeoItem, Equipable {
    private final String MODID;
    private final String ITEM_NAME;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public GeckoArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties, String modid, String name) {
        super(material, type, properties);
        MODID=modid;
        ITEM_NAME=name;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer = null;
            @Override
            public @Nullable <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (this.renderer == null){
                    this.renderer = new GenericGeckoRenderer();
                }
                return renderer;
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    public class GenericGeckoModel extends GeoModel<GeckoArmorItem> {
        @Override
        public ResourceLocation getModelResource(GeckoArmorItem genericGeckoItem) {
            return ResourceLocation.fromNamespaceAndPath(MODID,genericGeckoItem.getModelPath());
        }

        @Override
        public ResourceLocation getTextureResource(GeckoArmorItem genericGeckoItem) {
            return ResourceLocation.fromNamespaceAndPath(MODID,genericGeckoItem.getTexturePath());
        }

        @Override
        public ResourceLocation getAnimationResource(GeckoArmorItem genericGeckoItem) {
            return ResourceLocation.fromNamespaceAndPath(MODID, genericGeckoItem.getAnimationPath());
        }
    }

    private String getModelPath() {
        return "geo/"+ITEM_NAME+".geo.json";
    }

    private String getTexturePath() {
        return "textures/armor/"+ITEM_NAME+".png";
    }
    private String getAnimationPath() {
        return "animations/"+ITEM_NAME+".animation.json";
    }
    public class GenericGeckoRenderer extends GeoArmorRenderer<GeckoArmorItem> {
        public GenericGeckoRenderer() {
            super(new GenericGeckoModel())  ;
        }
    }
}
