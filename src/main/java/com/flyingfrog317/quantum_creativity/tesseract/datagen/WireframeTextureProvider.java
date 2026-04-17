package com.flyingfrog317.quantum_creativity.tesseract.datagen;

import com.google.common.hash.Hashing;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WireframeTextureProvider implements DataProvider {

    private final PackOutput output;

    public WireframeTextureProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> writes = new ArrayList<>();

        for (WireframeDatagenSource src :
                WireframeRegistry.all()) {
            writes.add(CompletableFuture.runAsync(() -> {
                try {
                    int color = src.colorARGB();

                    BufferedImage img =
                            new BufferedImage(
                                    16,
                                    16,
                                    BufferedImage.TYPE_INT_ARGB
                            );

                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            img.setRGB(x, y, color);
                        }
                    }

                    Path path = output.getOutputFolder()
                            .resolve(
                                    "assets/quantum_creativity/textures/wireframes"
                            )
                            .resolve(
                                    src.id() + ".png"
                            );

                    Files.createDirectories(path.getParent());

                    ByteArrayOutputStream buffer =
                            new ByteArrayOutputStream();
                    ImageIO.write(img, "png", buffer);

                    byte[] bytes = buffer.toByteArray();
                    cache.writeIfNeeded(
                            path,
                            bytes,
                            Hashing.sha1().hashBytes(bytes)
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
        }

        return CompletableFuture.allOf(
                writes.toArray(CompletableFuture[]::new)
        );
    }

    @Override
    public String getName() {
        return "Wireframe Texture Generator";
    }
}
