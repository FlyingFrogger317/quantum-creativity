package com.flyingfrog317.quantum_creativity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.keyframe.event.data.CustomInstructionKeyframeData;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class GeckoArmorItem extends ArmorItem implements GeoItem {
    private String MODID;
    private String ITEM_NAME;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public GeckoArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties, String modid, String name) {
        super(pMaterial, pType, pProperties);
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
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (renderer == null)
                    renderer=new GenericGeckoRenderer();
                this.renderer.prepForRender(livingEntity,itemStack,equipmentSlot,original);
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

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return ResourceLocation.fromNamespaceAndPath(MODID,getTexturePath()).toString();
    }
    //forwarding trash
}
