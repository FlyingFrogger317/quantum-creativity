package com.flyingfrog317.quantum_creativity.tesseract;

public class GeoExt_4drot {
    public static GeoExt_vec4 rotateXY(GeoExt_vec4 v, float a) {
        float c = (float) Math.cos(a);
        float s = (float) Math.sin(a);

        return new GeoExt_vec4(
                v.x() * c - v.y() * s,
                v.x() * s + v.y() * c,
                v.z(),
                v.w()
        );
    }

    @SuppressWarnings("unused")
    public static GeoExt_vec4 rotateXZ(GeoExt_vec4 v, float a) {
        float c = (float) Math.cos(a);
        float s = (float) Math.sin(a);

        return new GeoExt_vec4(
                v.x() * c - v.z() * s,
                v.y(),
                v.x() * s + v.z() * c,
                v.w()
        );
    }

    @SuppressWarnings("unused")
    public static GeoExt_vec4 rotateYZ(GeoExt_vec4 v, float a) {
        float c = (float) Math.cos(a);
        float s = (float) Math.sin(a);

        return new GeoExt_vec4(
                v.x(),
                v.y() * c - v.z() * s,
                v.y() * s + v.z() * c,
                v.w()
        );
    }

    public static GeoExt_vec4 rotateXW(GeoExt_vec4 v, float a) {
        float c = (float) Math.cos(a);
        float s = (float) Math.sin(a);

        return new GeoExt_vec4(
                v.x() * c - v.w() * s,
                v.y(),
                v.z(),
                v.x() * s + v.w() * c
        );
    }

    public static GeoExt_vec4 rotateYW(GeoExt_vec4 v, float a) {
        float c = (float) Math.cos(a);
        float s = (float) Math.sin(a);

        return new GeoExt_vec4(
                v.x(),
                v.y() * c - v.w() * s,
                v.z(),
                v.y() * s + v.w() * c
        );
    }

    public static GeoExt_vec4 rotateZW(GeoExt_vec4 v, float a) {
        float c = (float) Math.cos(a);
        float s = (float) Math.sin(a);

        return new GeoExt_vec4(
                v.x(),
                v.y(),
                v.z() * c - v.w() * s,
                v.z() * s + v.w() * c
        );
    }
    public static GeoExt_vec3 project(GeoExt_vec4 v, float distance) {

        float wFactor = 1f / (distance - v.w());

        return new GeoExt_vec3(
                v.x() * wFactor,
                v.y() * wFactor,
                v.z() * wFactor
        );
    }
}