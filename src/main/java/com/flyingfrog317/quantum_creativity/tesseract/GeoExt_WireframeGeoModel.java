package com.flyingfrog317.quantum_creativity.tesseract;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import java.util.NoSuchElementException;

public abstract class GeoExt_WireframeGeoModel<T extends GeoAnimatable> extends GeoModel<T>  {

    private final Tuple<Integer, Integer>[] wireframeConnections;
    private final String MODID;
    private final String id;
    private final GeoBone[] cachedBones;

    public GeoExt_WireframeGeoModel(String id,
                                    Tuple<Integer, Integer>[] connections,
                                    int size,
                                    String MODID) {
        this.id = id;
        this.wireframeConnections = connections;
        this.MODID = MODID;

        // NOTE: size = total bones expected (nodes + edges)
        this.cachedBones = new GeoBone[size];
    }

    // -------------------------
    // Bone loading (cached once)
    // -------------------------
    private GeoBone[] getGeoBones() {
        if (cachedBones[0] != null) return cachedBones;

        for (int i = 0; i < cachedBones.length; i++) {
            int finalI = i;
            cachedBones[i] = this.getBone("bone" + i)
                    .orElseThrow(() -> new NoSuchElementException(
                            "Missing bone: bone" + finalI + " (expected " + cachedBones.length + ")"
                    ));
        }

        return cachedBones;
    }

    // -------------------------
    // Animation tick
    // -------------------------
    @Override
    public void setCustomAnimations(T animatable,
                                    long instanceId,
                                    AnimationState<T> animationState) {

        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone[] bones = getGeoBones();

        GeoExt_vec3[] wireframePositions = updateWireframePositions();

        if (wireframePositions == null) return;

        int currentBone = 0;

        // nodes
        for (GeoExt_vec3 pos : wireframePositions) {
            applyBoneNode(bones[currentBone++], pos);
        }

        // edges
        for (Tuple<Integer, Integer> connection : wireframeConnections) {
            GeoExt_vec3 origin = wireframePositions[connection.getA()];
            GeoExt_vec3 target = wireframePositions[connection.getB()];
            GeoExt_vec3 delta = target.subtract(origin);
            float len = delta.length();
            if (len == 0f) {
                applyBoneNode(bones[currentBone++], origin);
                continue;
            }

            GeoExt_vec3 dir = delta.scale(1.0f / len);
            GeoExt_vec3 midpoint = origin.add(target).scale(0.5f);

            float horizontalLength = (float) Math.sqrt(dir.x() * dir.x() + dir.y() * dir.y());
            float rotX = (float) Math.atan2(dir.z(), horizontalLength);
            float rotY = 0f;
            float rotZ = (float) Math.atan2(dir.x(), dir.y());

            applyBoneLine(
                    bones[currentBone++],
                    midpoint,
                    new GeoExt_vec3(rotX, rotY, rotZ),
                    len,
                    0.1f
            );
        }
    }

    // -------------------------
    // Bone transforms
    // -------------------------
    private void applyBoneLine(GeoBone bone,
                               GeoExt_vec3 pos,
                               GeoExt_vec3 rot,
                               float len,
                               @SuppressWarnings("SameParameterValue") float thickness) {

        bone.setPosX(pos.x());
        bone.setPosY(pos.y());
        bone.setPosZ(pos.z());

        bone.setRotX(rot.x());
        bone.setRotY(rot.y());
        bone.setRotZ(rot.z());

        bone.setScaleY(len);
        bone.setScaleX(thickness);
        bone.setScaleZ(thickness);
    }

    private void applyBoneNode(GeoBone bone, GeoExt_vec3 pos) {
        bone.setPosX(pos.x());
        bone.setPosY(pos.y());
        bone.setPosZ(pos.z());

        bone.setRotX(0);
        bone.setRotY(0);
        bone.setRotZ(0);

        bone.setScaleX(1);
        bone.setScaleY(1);
        bone.setScaleZ(1);
    }

    // -------------------------
    // Required by subclass
    // -------------------------
    protected abstract GeoExt_vec3[] updateWireframePositions();

    // -------------------------
    // Resources
    // -------------------------
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return ResourceLocation.fromNamespaceAndPath(MODID,
                "geo/" + id + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return ResourceLocation.fromNamespaceAndPath(MODID,
                "textures/wireframes/" + id + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return ResourceLocation.fromNamespaceAndPath(MODID, "animations/empty.animation.json");
    }
}
