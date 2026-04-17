package com.flyingfrog317.quantum_creativity.tesseract;

import software.bernie.geckolib.cache.object.GeoBone;

public class GeoExt_BoneLine {
    public GeoExt_vec3 pos;
    public GeoExt_vec3 rot;
    public float len;
    public float thickness;
    public GeoExt_BoneLine(GeoExt_vec3 _pos, GeoExt_vec3 _rot, float _len, float _thickness) {
        pos=_pos;
        rot=_rot;
        len=_len;
        thickness=_thickness;
    }
    public void apply(GeoBone bone){
        bone.setPosX(pos.x);
        bone.setPosY(pos.y);
        bone.setPosZ(pos.z);
        bone.setRotX(rot.x);
        bone.setRotY(rot.y);
        bone.setRotZ(rot.z);
        bone.setScaleY(len);
        bone.setScaleX(thickness);
        bone.setScaleZ(thickness);
    }
}
