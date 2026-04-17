package com.flyingfrog317.quantum_creativity.tesseract.datagen;

import net.minecraftforge.data.event.GatherDataEvent;

import java.util.ArrayList;
import java.util.List;

public class WireframeRegistry {

    private static final List<WireframeDatagenSource> SOURCES =
            new ArrayList<>();

    public static void register(WireframeDatagenSource src) {
        SOURCES.add(src);
    }

    public static List<WireframeDatagenSource> all() {
        return SOURCES;
    }

    // ⭐ One-line hook
    public static void registerDatagen(GatherDataEvent event) {

        var gen = event.getGenerator();
        var output = gen.getPackOutput();

        gen.addProvider(
                event.includeClient(),
                new WireframeGeoProvider(output)
        );

        gen.addProvider(
                event.includeClient(),
                new WireframeTextureProvider(output)
        );
    }
}