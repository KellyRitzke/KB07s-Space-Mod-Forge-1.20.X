package net.kb07.kbspace.worldgen.dimension;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kb07.kbspace.KBSpace;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.BiFunction;

public class ModDimensionSpecialEffects extends DimensionSpecialEffects {

    private final ModSkyRenderer skyRenderer;

    public static final DimensionSpecialEffects MOON_EFFECT = new ModDimensionSpecialEffects(
            Float.NaN, true, SkyType.NORMAL, true, false, (vec, worldTime) -> vec, (x, z) -> false);


    protected static final BiFunction<Integer, Integer, Boolean> FOGGY_FALSE = (x, z) -> false;
    protected static final BiFunction<Integer, Integer, Boolean> FOGGY_TRUE = (x, z) -> true;


    protected final BiFunction<Vec3, Float, Vec3> fogColorBrightness;
    protected final BiFunction<Integer, Integer, Boolean> foggyAtPos;

    protected ModDimensionSpecialEffects(float cloudLevel, boolean hasGround, SkyType skyType, boolean brightLightmap, boolean ambientLight, BiFunction<Vec3, Float, Vec3> fogColorBrightness, BiFunction<Integer, Integer, Boolean> foggyAtPos) {
        super(cloudLevel, hasGround, skyType, brightLightmap, ambientLight);
        this.fogColorBrightness = fogColorBrightness;
        this.foggyAtPos = foggyAtPos;
        this.skyRenderer = new ModSkyRenderer();
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 vector, float worldTime) {
        return this.fogColorBrightness.apply(vector, worldTime);
    }

    @Override
    public boolean isFoggyAt(int x, int z) {
        return this.foggyAtPos.apply(x, z);
    }

    public boolean renderSky(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        this.skyRenderer.renderSky(level, ticks, partialTick, poseStack, camera, projectionMatrix, isFoggy, setupFog);
        return true; // ✅ This enables custom sky rendering
    }

    // Manually register the custom effect in the EFFECTS map
    public static void register() {
        try {
            ResourceLocation id = new ResourceLocation(KBSpace.MOD_ID, "moon_effect");

            Field effectsField = DimensionSpecialEffects.class.getDeclaredField("EFFECTS");
            effectsField.setAccessible(true);

            @SuppressWarnings("unchecked")
            Map<ResourceLocation, DimensionSpecialEffects> effectsMap = (Map<ResourceLocation, DimensionSpecialEffects>) effectsField.get(null);

            effectsMap.put(id, MOON_EFFECT);


        } catch (Exception e) {
            System.err.println("❌ Failed to register Custom Dimension Effect!");
            e.printStackTrace();
        }
    }

}
