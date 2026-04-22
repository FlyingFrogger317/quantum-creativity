package com.flyingfrog317.quantum_creativity.tesseract;
import com.flyingfrog317.quantum_creativity.QuantumCreativity;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class Tesseract extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public Tesseract(Item.Properties properties){
        super(properties);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private TesseractRenderer renderer = null;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new TesseractRenderer();

                return renderer;
            }
        });
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    static public class TesseractModel extends GeoExt_WireframeGeoModel<Tesseract> {
        private static final float HYPERCUBE_SIZE = 1f;
        private static final float PROJECTION_DISTANCE = 4f;
        private static final float PROJECTED_SCALE = 10f;
        private static final float ROTATION_SPEED_XW = 0.006f;
        private static final float ROTATION_SPEED_YW = 0.004f;
        private static final float ROTATION_SPEED_ZW = 0.003f;
        private static final float ROTATION_SPEED_XY = 0.002f;

        public TesseractModel() {
            super("tesseract", TesseractMath.generateEdges(), 16 + 32, QuantumCreativity.MODID);
        }

        private float angleXW = 0f;
        private float angleYW = 0f;
        private float angleZW = 0f;
        private float angleXY = 0f;

        private static final GeoExt_vec4[] BASE =
                TesseractMath.createVertices(HYPERCUBE_SIZE);

        @Override
        protected GeoExt_vec3[] updateWireframePositions() {
            angleXW += ROTATION_SPEED_XW;
            angleYW += ROTATION_SPEED_YW;
            angleZW += ROTATION_SPEED_ZW;
            angleXY += ROTATION_SPEED_XY;

            GeoExt_vec3[] result = new GeoExt_vec3[BASE.length];

            for (int i = 0; i < BASE.length; i++) {
                GeoExt_vec4 v = BASE[i];
                v = GeoExt_4drot.rotateXW(v, angleXW);
                v = GeoExt_4drot.rotateYW(v, angleYW);
                v = GeoExt_4drot.rotateZW(v, angleZW);
                v = GeoExt_4drot.rotateXY(v, angleXY);
                result[i] = GeoExt_4drot.project(v, PROJECTION_DISTANCE).scale(PROJECTED_SCALE);
            }

            return result;
        }
    }
    public static class TesseractRenderer extends GeoItemRenderer<Tesseract> {
        public TesseractRenderer() {
            super(new TesseractModel());
        }
    }
    //forwarding trash
}
