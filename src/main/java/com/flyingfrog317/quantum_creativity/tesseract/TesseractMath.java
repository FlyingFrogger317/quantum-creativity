package com.flyingfrog317.quantum_creativity.tesseract;

import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.List;

public class TesseractMath {
    private static final Tuple<Integer, Integer>[] CUBE_EDGE_TEMPLATE = new Tuple[] {
            new Tuple<>(0, 1),
            new Tuple<>(0, 2),
            new Tuple<>(0, 4),
            new Tuple<>(1, 3),
            new Tuple<>(1, 5),
            new Tuple<>(2, 3),
            new Tuple<>(2, 6),
            new Tuple<>(3, 7),
            new Tuple<>(4, 5),
            new Tuple<>(4, 6),
            new Tuple<>(5, 7),
            new Tuple<>(6, 7)
    };

    public static GeoExt_vec3[] createOuterCubeVertices(float size) {
        GeoExt_vec3[] points = new GeoExt_vec3[8];

        int index = 0;

        for (int x = -1; x <= 1; x += 2)
            for (int y = -1; y <= 1; y += 2)
                for (int z = -1; z <= 1; z += 2)
                    points[index++] = new GeoExt_vec3(
                            x * size,
                            y * size,
                            z * size
                    );

        return points;
    }

    public static Tuple<Integer, Integer>[] generateOuterCubeEdges() {
        return CUBE_EDGE_TEMPLATE;
    }

    public static GeoExt_vec3[] createOuterAndInnerCubeVertices(float outerSize, float innerSize) {
        GeoExt_vec3[] outer = createOuterCubeVertices(outerSize);
        GeoExt_vec3[] inner = createOuterCubeVertices(innerSize);
        GeoExt_vec3[] points = new GeoExt_vec3[outer.length + inner.length];

        System.arraycopy(outer, 0, points, 0, outer.length);
        System.arraycopy(inner, 0, points, outer.length, inner.length);

        return points;
    }

    public static Tuple<Integer, Integer>[] generateOuterAndInnerCubeEdges() {
        Tuple<Integer, Integer>[] edges = new Tuple[(CUBE_EDGE_TEMPLATE.length * 2) + 8];

        for (int i = 0; i < CUBE_EDGE_TEMPLATE.length; i++) {
            Tuple<Integer, Integer> edge = CUBE_EDGE_TEMPLATE[i];
            edges[i] = edge;
            edges[i + CUBE_EDGE_TEMPLATE.length] = new Tuple<>(edge.getA() + 8, edge.getB() + 8);
        }

        for (int i = 0; i < 8; i++) {
            edges[(CUBE_EDGE_TEMPLATE.length * 2) + i] = new Tuple<>(i, i + 8);
        }

        return edges;
    }

    public static GeoExt_vec4[] createVertices(float size) {

        GeoExt_vec4[] points = new GeoExt_vec4[16];

        int index = 0;

        for (int x = -1; x <= 1; x += 2)
            for (int y = -1; y <= 1; y += 2)
                for (int z = -1; z <= 1; z += 2)
                    for (int w = -1; w <= 1; w += 2) {

                        points[index++] = new GeoExt_vec4(
                                x * size,
                                y * size,
                                z * size,
                                w * size
                        );
                    }

        return points;
    }
    public static Tuple<Integer,Integer>[] generateEdges() {

        List<Tuple<Integer,Integer>> edges = new ArrayList<>();

        for (int i = 0; i < 16; i++) {
            for (int j = i + 1; j < 16; j++) {

                int diff = i ^ j;

                // Exactly one bit difference
                if (Integer.bitCount(diff) == 1) {
                    edges.add(new Tuple<>(i, j));
                }
            }
        }

        return edges.toArray(new Tuple[0]);
    }
}
