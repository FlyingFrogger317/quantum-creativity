package com.flyingfrog317.quantum_creativity.tesseract.datagen;

public class WireframeDatagenSource {
    private static final int DEFAULT_COLOR_ARGB = 0xFF00FFFF;

    private final int nodeCount;
    private final int edgeCount;
    private final String id;
    private final int colorARGB;

    public WireframeDatagenSource(int nodeCount, int edgeCount, String id) {
        this(nodeCount, edgeCount, id, DEFAULT_COLOR_ARGB);
    }

    public WireframeDatagenSource(int nodeCount, int edgeCount, String id, int colorARGB) {
        this.nodeCount = nodeCount;
        this.edgeCount = edgeCount;
        this.id = id;
        this.colorARGB = colorARGB;
    }

    int nodeCount() {
        return nodeCount;
    }

    int edgeCount() {
        return edgeCount;
    }

    String id() {
        return id;
    }

    int colorARGB() {
        return colorARGB;
    }
}
