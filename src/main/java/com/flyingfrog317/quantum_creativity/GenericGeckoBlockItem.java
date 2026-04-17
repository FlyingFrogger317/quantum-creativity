package com.flyingfrog317.quantum_creativity;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Function;

public class GenericGeckoBlockItem extends BlockItem implements GeoItem {
    private String MODID;
    private String ITEM_NAME;
    private final Function<GenericGeckoBlockItem, AnimationController<GenericGeckoBlockItem>> controllerBuilder;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public GenericGeckoBlockItem(Block block, Properties properties, String modid, String name){
        this(block,properties,modid,name,null);
    }
    public GenericGeckoBlockItem(Block block, Properties properties, String modid, String name, Function<GenericGeckoBlockItem, AnimationController<GenericGeckoBlockItem>> controllerFactoryp){
        super(block,properties);
        MODID=modid;
        ITEM_NAME=name;
        controllerBuilder=controllerFactoryp;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        if (controllerBuilder!=null){
            controllerRegistrar.add(controllerBuilder.apply(this));
        }
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
    public class GenericGeckoModel extends GeoModel<GenericGeckoBlockItem> {
        @Override
        public ResourceLocation getModelResource(GenericGeckoBlockItem genericGeckoItem) {
            return ResourceLocation.fromNamespaceAndPath(MODID,genericGeckoItem.getModelPath());
        }

        @Override
        public ResourceLocation getTextureResource(GenericGeckoBlockItem genericGeckoItem) {
            return ResourceLocation.fromNamespaceAndPath(MODID,genericGeckoItem.getTexturePath());
        }

        @Override
        public ResourceLocation getAnimationResource(GenericGeckoBlockItem genericGeckoItem) {
            return ResourceLocation.fromNamespaceAndPath(MODID, genericGeckoItem.getAnimationPath());
        }
    }

    private String getModelPath() {
        return "geo/"+ITEM_NAME+".geo.json";
    }

    private String getTexturePath() {
        return "textures/block/"+ITEM_NAME+".png";
    }
    private String getAnimationPath() {
        return "animations/"+ITEM_NAME+".animation.json";
    }
    public class GenericGeckoRenderer extends GeoItemRenderer<GenericGeckoBlockItem> {
        public GenericGeckoRenderer() {
            super(new GenericGeckoModel())  ;
        }

        @Override
        public RenderType getRenderType(GenericGeckoBlockItem animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
            return RenderType.entityTranslucentEmissive(texture);
        }
    }
    //forwarding trash
}
