package com.flyingfrog317.quantum_creativity.tesseract.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class WireframeGeoProvider implements DataProvider {

    private final PackOutput output;

    public WireframeGeoProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> writes = new ArrayList<>();

        for (WireframeDatagenSource src :
                WireframeRegistry.all()) {

            JsonObject root = new JsonObject();
            root.addProperty("format_version", "1.12.0");

            JsonArray geometries = new JsonArray();

            JsonObject geo = new JsonObject();

            JsonObject description = new JsonObject();
            description.addProperty(
                    "identifier",
                    "geometry." + src.id()
            );
            description.addProperty("texture_width", 16);
            description.addProperty("texture_height", 16);

            geo.add("description", description);

            JsonArray bones = new JsonArray();

            int totalBones =
                    src.nodeCount()
                            + src.edgeCount();

            for (int i = 0; i < totalBones; i++) {

                JsonObject bone = getBone(i);

                bones.add(bone);
            }

            geo.add("bones", bones);
            geometries.add(geo);

            root.add("minecraft:geometry", geometries);

            Path path = output.getOutputFolder()
                    .resolve(
                            "assets/quantum_creativity/geo"
                    )
                    .resolve(
                            src.id() + ".geo.json"
                    );

            writes.add(DataProvider.saveStable(
                    cache,
                    root,
                    path
            ));
        }

        return CompletableFuture.allOf(
                writes.toArray(CompletableFuture[]::new)
        );
    }

    private static @NotNull JsonObject getBone(int i) {
        JsonObject bone = new JsonObject();
        bone.addProperty(
                "name",
                "bone" + i
        );

        JsonArray pivot = new JsonArray();
        pivot.add(0);
        pivot.add(0);
        pivot.add(0);

        bone.add("pivot", pivot);

        JsonArray cubes = getCubes();

        bone.add("cubes", cubes);
        return bone;
    }

    private static @NotNull JsonArray getCubes() {
        JsonArray cubes = new JsonArray();

        JsonObject cube = new JsonObject();

        JsonArray origin = new JsonArray();
        origin.add(-0.5);
        origin.add(-0.5);
        origin.add(-0.5);

        JsonArray size = new JsonArray();
        size.add(1);
        size.add(1);
        size.add(1);

        JsonArray uv = new JsonArray();
        uv.add(0);
        uv.add(0);

        cube.add("origin", origin);
        cube.add("size", size);
        cube.add("uv", uv);

        cubes.add(cube);
        return cubes;
    }

    @Override
    public String getName() {
        return "Wireframe Geo Generator";
    }
}
