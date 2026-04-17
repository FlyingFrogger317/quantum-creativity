package com.flyingfrog317.quantum_creativity.tesseract;

public final class GeoExt_vec4 {

    public final float x;
    public final float y;
    public final float z;
    public final float w;

    public static final GeoExt_vec4 ZERO = new GeoExt_vec4(0, 0, 0, 0);
    public static final GeoExt_vec4 ONE = new GeoExt_vec4(1, 1, 1, 1);

    public GeoExt_vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public GeoExt_vec4 add(GeoExt_vec4 v) {
        return new GeoExt_vec4(x + v.x, y + v.y, z + v.z, w + v.w);
    }

    public GeoExt_vec4 subtract(GeoExt_vec4 v) {
        return new GeoExt_vec4(x - v.x, y - v.y, z - v.z, w - v.w);
    }

    public GeoExt_vec4 scale(float s) {
        return new GeoExt_vec4(x * s, y * s, z * s, w * s);
    }

    public GeoExt_vec4 negate() {
        return new GeoExt_vec4(-x, -y, -z, -w);
    }

    public float dot(GeoExt_vec4 v) {
        return x * v.x + y * v.y + z * v.z + w * v.w;
    }

    public GeoExt_vec4 cross(GeoExt_vec4 v) {
        throw new UnsupportedOperationException(
                "A binary cross product is not defined for 4D vectors."
        );
    }

    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public float lengthSquared() {
        return x * x + y * y + z * z + w * w;
    }

    public GeoExt_vec4 normalize() {
        float len = length();
        if (len == 0f) return ZERO;
        return scale(1.0f / len);
    }

    public GeoExt_vec4 lerp(GeoExt_vec4 target, float t) {
        return new GeoExt_vec4(
                x + (target.x - x) * t,
                y + (target.y - y) * t,
                z + (target.z - z) * t,
                w + (target.w - w) * t
        );
    }

    @Override
    public String toString() {
        return "GeoExt_vec4(" + x + ", " + y + ", " + z + ", " + w + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeoExt_vec4)) return false;
        GeoExt_vec4 v = (GeoExt_vec4) o;
        return Float.compare(v.x, x) == 0
                && Float.compare(v.y, y) == 0
                && Float.compare(v.z, z) == 0
                && Float.compare(v.w, w) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.hashCode(x);
        result = 31 * result + Float.hashCode(y);
        result = 31 * result + Float.hashCode(z);
        result = 31 * result + Float.hashCode(w);
        return result;
    }
}
