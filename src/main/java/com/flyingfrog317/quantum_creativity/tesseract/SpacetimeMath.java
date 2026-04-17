package com.flyingfrog317.quantum_creativity.tesseract;

public class SpacetimeMath {
    public static final long C = 299752458;//m/s
    public static GeoExt_vec4 transform3dto4dForPlayerMovement(GeoExt_vec3 playerSpeeds){
        double w=Math.sqrt((Math.pow(C,2)-Math.pow(playerSpeeds.x,2)-Math.pow(playerSpeeds.y,2)-Math.pow(playerSpeeds.z,2)));
        return new GeoExt_vec4(playerSpeeds.x,playerSpeeds.y,playerSpeeds.z, (float) w);
    }
    public static GeoExt_vec3 transformToGlobal(GeoExt_vec4 local) {

        double scalar = C / local.w; // singularity is intended

        return new GeoExt_vec3(
                (float) (local.x * scalar),
                (float) (local.y * scalar),
                (float) (local.z * scalar)
        );
    }
    public static GeoExt_vec4 transformToLocal(GeoExt_vec3 global) {

        double w2 = (C * C)
                - (global.x * global.x)
                - (global.y * global.y)
                - (global.z * global.z);

        float w = (float) Math.sqrt(w2);

        return new GeoExt_vec4(global.x, global.y, global.z, w);
    }
    public static double getQuantumDamage(
            GeoExt_vec4 local,
            double resilience
    ){

        double diff = C - local.w;

        if (diff <= resilience)
            return 0.0;

        double excess = diff - resilience;

        double rawDamage = excess * excess;

        double maxExcess = C - resilience*(C/100);

        double maxDamage = maxExcess * maxExcess;

        double normalized =
                (rawDamage / maxDamage) * 100.0;

        return Math.round(normalized);
    }
}
