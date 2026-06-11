package com.flyingfrog317.quantum_creativity;

import com.flyingfrog317.quantum_creativity.tesseract.datagen.WireframeDatagenSource;
import com.flyingfrog317.quantum_creativity.tesseract.datagen.WireframeRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
@EventBusSubscriber(modid = QuantumCreativity.MODID, value = Dist.CLIENT)
public class QuantumEventBus {
    private static final WireframeDatagenSource TESSERACT_WIREFRAME =
            new WireframeDatagenSource(16, 32, "tesseract");

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        WireframeRegistry.register(TESSERACT_WIREFRAME);
        WireframeRegistry.registerDatagen(event);
    }
}
