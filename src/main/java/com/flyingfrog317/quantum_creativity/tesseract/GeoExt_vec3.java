package com.flyingfrog317.quantum_creativity.tesseract;
@SuppressWarnings("unused")
public record GeoExt_vec3(float x, float y, float z) {

    public static final GeoExt_vec3 ZERO = new GeoExt_vec3(0, 0, 0);
    public static final GeoExt_vec3 ONE = new GeoExt_vec3(1, 1, 1);

    // -------------------------
    // Basic math operations
    // -------------------------

    public GeoExt_vec3 add(GeoExt_vec3 v) {
        return new GeoExt_vec3(x + v.x, y + v.y, z + v.z);
    }

    public GeoExt_vec3 subtract(GeoExt_vec3 v) {
        return new GeoExt_vec3(x - v.x, y - v.y, z - v.z);
    }

    public GeoExt_vec3 scale(float s) {
        return new GeoExt_vec3(x * s, y * s, z * s);
    }

    public GeoExt_vec3 negate() {
        return new GeoExt_vec3(-x, -y, -z);
    }

    // -------------------------
    // Math utilities
    // -------------------------

    public float dot(GeoExt_vec3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public GeoExt_vec3 cross(GeoExt_vec3 v) {
        return new GeoExt_vec3(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
        );
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    public GeoExt_vec3 normalize() {
        float len = length();
        if (len == 0) return ZERO;
        return new GeoExt_vec3(x / len, y / len, z / len);
    }

    // -------------------------
    // Interpolation (VERY useful for animation)
    // -------------------------

    public GeoExt_vec3 lerp(GeoExt_vec3 target, float t) {
        return new GeoExt_vec3(
                x + (target.x - x) * t,
                y + (target.y - y) * t,
                z + (target.z - z) * t
        );
    }

    // -------------------------
    // Object overrides
    // -------------------------

    @Override
    public String toString() {
        return "GeoExt_vec3(" + x + ", " + y + ", " + z + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeoExt_vec3 v)) return false;
        return Float.compare(v.x, x) == 0
                && Float.compare(v.y, y) == 0
                && Float.compare(v.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.hashCode(x);
        result = 31 * result + Float.hashCode(y);
        result = 31 * result + Float.hashCode(z);
        return result;
    }
}