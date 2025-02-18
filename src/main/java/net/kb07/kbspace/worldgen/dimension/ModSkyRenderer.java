package net.kb07.kbspace.worldgen.dimension;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.kb07.kbspace.KBSpace;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.lwjgl.system.linux.XGenericEvent;

import javax.annotation.Nullable;
import javax.management.Attribute;
import javax.swing.text.html.parser.Entity;


public class ModSkyRenderer {

    private static final ResourceLocation SUN_LOCATION = new ResourceLocation("textures/environment/sun.png");
    private static final ResourceLocation EARTH_LOCATION = new ResourceLocation(KBSpace.MOD_ID, "textures/environment/earth.png");

    @Nullable
    private VertexBuffer starBuffer;
    @Nullable
    private VertexBuffer skyBuffer;
    @Nullable
    private VertexBuffer darkBuffer;

    @Nullable
    private ClientLevel level;

    public ModSkyRenderer() {
    }

    public void renderSky(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {

        if (level != null && level.dimensionType().effectsLocation().equals(new ResourceLocation(KBSpace.MOD_ID, "moon_effect"))) {

            // Inject your custom sky rendering logic here
            // For example, make sure the sky is rendered with your custom properties

            if (skyBuffer == null || starBuffer == null || darkBuffer == null) {
                System.out.println("❌ One or more buffers are NULL! Creating buffers...");
                createSkyBuffers();
                createStars();
                createDarkBuffer();
            }

            if (!isFoggy) {
                setupFog.run(); // Ensures fog settings are applied only when needed
            }
            Vec3 vec3 = level.getSkyColor(camera.getPosition(), partialTick);
            float f = (float)vec3.x;
            float f1 = (float)vec3.y;
            float f2 = (float)vec3.z;
            FogRenderer.levelFogColor();
            BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
            RenderSystem.depthMask(false);
            RenderSystem.setShaderColor(f, f1, f2, 1.0F);
            ShaderInstance shaderinstance = RenderSystem.getShader();
            this.skyBuffer.bind();
            assert shaderinstance != null;
            this.skyBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderinstance);
            VertexBuffer.unbind();

            RenderSystem.enableBlend();
            float[] afloat = level.effects().getSunriseColor(level.getTimeOfDay(partialTick), partialTick);
            if (afloat != null) {
                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                poseStack.pushPose();

                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                float f3 = Mth.sin(level.getSunAngle(partialTick)) < 0.0F ? 180.0F : 0.0F;
                poseStack.mulPose(Axis.ZP.rotationDegrees(f3));
                poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));

                float f4 = afloat[0];
                float f5 = afloat[1];
                float f6 = afloat[2];
                Matrix4f matrix4f = poseStack.last().pose();

                bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
                bufferbuilder.vertex(matrix4f, 0.0F, 100.0F, 0.0F).color(f4, f5, f6, afloat[3]).endVertex();

                int i = 16;
                for(int j = 0; j <= 16; ++j) {
                    float f7 = (float)j * ((float)Math.PI * 2F) / 16.0F;
                    float f8 = Mth.sin(f7);
                    float f9 = Mth.cos(f7);
                    bufferbuilder.vertex(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
                }

                BufferUploader.drawWithShader(bufferbuilder.end());

                poseStack.popPose();
            }

            float f10 = 1.0F;
            if (starBuffer != null) {
                // Set shader for drawing stars
                RenderSystem.setShader(GameRenderer::getPositionColorShader);  // Ensure this shader is valid
                RenderSystem.setShaderColor(f10, f10, f10, f10);
                FogRenderer.setupNoFog();  // Optional, if you want no fog for stars
                this.starBuffer.bind();  // Bind the star buffer
                assert GameRenderer.getPositionShader() != null;  // Ensure shader is correctly set
                this.starBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, GameRenderer.getPositionShader());  // Draw the stars
                VertexBuffer.unbind();  // Unbind the buffer
                setupFog.run();  // Restore fog settings
            }

            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            poseStack.pushPose(); // Sun
            float f11 = 1.0F - level.getRainLevel(partialTick);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));

            Matrix4f matrix4f1 = poseStack.last().pose();
            float f12 = 30.0F;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, SUN_LOCATION);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(matrix4f1, -f12, 100.0F, -f12).uv(0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(matrix4f1, f12, 100.0F, -f12).uv(1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(matrix4f1, f12, 100.0F, f12).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(matrix4f1, -f12, 100.0F, f12).uv(0.0F, 1.0F).endVertex();
            BufferUploader.drawWithShader(bufferbuilder.end());
            poseStack.popPose();

            poseStack.pushPose(); // Earth
            poseStack.mulPose(Axis.XP.rotationDegrees(15.0f));

            float earthSize = 40.0F;
            Matrix4f earthMatrix = poseStack.last().pose();

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, EARTH_LOCATION);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(earthMatrix, -earthSize, 100.0F, -earthSize).uv(0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(earthMatrix, earthSize, 100.0F, -earthSize).uv(1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(earthMatrix, earthSize, 100.0F, earthSize).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(earthMatrix, -earthSize, 100.0F, earthSize).uv(0.0F, 1.0F).endVertex();
            BufferUploader.drawWithShader(bufferbuilder.end());

            poseStack.popPose();

            RenderSystem.setShaderColor(f1, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
            double d0 = camera.getEntity().getEyePosition(partialTick).y - level.getLevelData().getHorizonHeight(level);
            if (d0 < 0.0D) {
                if (darkBuffer != null) {
                    poseStack.pushPose();
                    poseStack.translate(0.0F, 12.0F, 0.0F);
                    this.darkBuffer.bind();
                    this.darkBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderinstance);
                    VertexBuffer.unbind();
                    poseStack.popPose();
                }
            }

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.depthMask(true);

        }

    }

    private void createSkyBuffers() {
        if (skyBuffer != null) {
            skyBuffer.close();
        }

        skyBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        bufferbuilder.vertex(-100.0, 100.0, -100.0).endVertex();
        bufferbuilder.vertex(100.0, 100.0, -100.0).endVertex();
        bufferbuilder.vertex(100.0, 100.0, 100.0).endVertex();
        bufferbuilder.vertex(-100.0, 100.0, 100.0).endVertex();

        BufferBuilder.RenderedBuffer buffer = bufferbuilder.end();
        skyBuffer.bind();
        skyBuffer.upload(buffer);
        VertexBuffer.unbind();
    }

    private void createStarBuffer() {
        if (starBuffer != null) {
            starBuffer.close();
        }

        starBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        RandomSource random = RandomSource.create(10842L);

        for (int i = 0; i < 1500; ++i) {
            double x = (random.nextFloat() * 2.0F - 1.0F);
            double y = (random.nextFloat() * 2.0F - 1.0F);
            double z = (random.nextFloat() * 2.0F - 1.0F);
            double size = 0.15F + random.nextFloat() * 0.1F;
            double d = x * x + y * y + z * z;

            if (d < 1.0D && d > 0.01D) {
                d = 1.0D / Math.sqrt(d);
                x *= d;
                y *= d;
                z *= d;

                bufferbuilder.vertex(x * 100.0D, y * 100.0D, z * 100.0D).endVertex();
            }
        }

        BufferBuilder.RenderedBuffer buffer = bufferbuilder.end();
        starBuffer.bind();
        starBuffer.upload(buffer);
        VertexBuffer.unbind();
    }

    private void createDarkBuffer() {
        if (darkBuffer != null) {
            darkBuffer.close();
        }

        darkBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();

        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        bufferbuilder.vertex(-100.0, -100.0, -100.0).endVertex();
        bufferbuilder.vertex(100.0, -100.0, -100.0).endVertex();
        bufferbuilder.vertex(100.0, -100.0, 100.0).endVertex();
        bufferbuilder.vertex(-100.0, -100.0, 100.0).endVertex();

        BufferBuilder.RenderedBuffer buffer = bufferbuilder.end();
        darkBuffer.bind();
        darkBuffer.upload(buffer);
        VertexBuffer.unbind();
    }

    private void createStars() {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        if (this.starBuffer != null) {
            this.starBuffer.close();
        }

        this.starBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        BufferBuilder.RenderedBuffer bufferbuilder$renderedbuffer = this.drawStars(bufferbuilder);
        this.starBuffer.bind();
        this.starBuffer.upload(bufferbuilder$renderedbuffer);
        VertexBuffer.unbind();
    }

    private BufferBuilder.RenderedBuffer drawStars(BufferBuilder pBuilder) {
        RandomSource randomsource = RandomSource.create(10842L);
        pBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        for(int i = 0; i < 1500; ++i) {
            double d0 = (double)(randomsource.nextFloat() * 2.0F - 1.0F);
            double d1 = (double)(randomsource.nextFloat() * 2.0F - 1.0F);
            double d2 = (double)(randomsource.nextFloat() * 2.0F - 1.0F);
            double d3 = (double)(0.15F + randomsource.nextFloat() * 0.1F);
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d4 < 1.0D && d4 > 0.01D) {
                d4 = 1.0D / Math.sqrt(d4);
                d0 *= d4;
                d1 *= d4;
                d2 *= d4;
                double d5 = d0 * 100.0D;
                double d6 = d1 * 100.0D;
                double d7 = d2 * 100.0D;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = randomsource.nextDouble() * Math.PI * 2.0D;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for(int j = 0; j < 4; ++j) {
                    double d17 = 0.0D;
                    double d18 = (double)((j & 2) - 1) * d3;
                    double d19 = (double)((j + 1 & 2) - 1) * d3;
                    double d20 = 0.0D;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d21 * d12 + 0.0D * d13;
                    double d24 = 0.0D * d12 - d21 * d13;
                    double d25 = d24 * d9 - d22 * d10;
                    double d26 = d22 * d9 + d24 * d10;
                    pBuilder.vertex(d5 + d25, d6 + d23, d7 + d26).endVertex();
                }
            }
        }

        return pBuilder.end();
    }

}
