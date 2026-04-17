package com.flyingfrog317.quantum_creativity.tesseract;

import software.bernie.geckolib.cache.object.GeoBone;

public class GeoExt_BoneNode {
    public GeoExt_vec3 pos;
    public GeoExt_vec3 rot;
    public GeoExt_BoneNode(GeoExt_vec3 _pos, GeoExt_vec3 _rot) {
        pos=_pos;
        rot=_rot;
    }
    public void apply(GeoBone bone){
        bone.setPosX(pos.x);
        bone.setPosY(pos.y);
        bone.setPosZ(pos.z);
        bone.setRotX(rot.x);
        bone.setRotY(rot.y);
        bone.setRotZ(rot.z);
        bone.setScaleY(1);
        bone.setScaleX(1);
        bone.setScaleZ(1);
    }
}
