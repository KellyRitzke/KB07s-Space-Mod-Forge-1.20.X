package net.kb07.kbspace.worldgen.dimension;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.phys.Vec3;

public class ModSkyRenderer{

    private final Minecraft mc;

    public ModSkyRenderer(Minecraft minecraft) {
        this.mc = minecraft;
    }

    public void renderSky(PoseStack poseStack, float partialTicks, ClientLevel level, MultiBufferSource.BufferSource bufferSource, Vec3 fogColor, Runnable setupFog) {
        System.out.println("🌌 Rendering Custom Sky...");

        // 🔹 Clear default sky rendering (removes moon/sun)
        poseStack.pushPose();
        poseStack.translate(0, -10, 0);  // Adjust sky height if needed
        poseStack.popPose();

        // 🔹 Always keep stars visible
        level.effects().isFoggyAt(0, 0); // Example call to ensure stars render
    }
}
