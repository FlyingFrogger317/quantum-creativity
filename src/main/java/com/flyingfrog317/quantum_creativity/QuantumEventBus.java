package com.flyingfrog317.quantum_creativity;

import com.flyingfrog317.quantum_creativity.tesseract.datagen.WireframeDatagenSource;
import com.flyingfrog317.quantum_creativity.tesseract.datagen.WireframeRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = QuantumCreativity.MODID,bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class QuantumEventBus {
    private static final WireframeDatagenSource TESSERACT_WIREFRAME =
            new WireframeDatagenSource(16, 32, "tesseract");

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        WireframeRegistry.register(TESSERACT_WIREFRAME);
        WireframeRegistry.registerDatagen(event);
    }
}
