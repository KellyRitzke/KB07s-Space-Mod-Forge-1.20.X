package net.kb07.kbspace.util.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.util.packets.PacketHandler;
import net.kb07.kbspace.util.packets.SpaceDestinationPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class SpaceSelectionScreen extends Screen {

    private static final ResourceLocation SPACE_BACKGROUND = new ResourceLocation(KBSpace.MOD_ID, "textures/gui/space_bg.png");

    private static final List<ResourceLocation> PLANETS = new ArrayList<>();
    static {
        PLANETS.add(new ResourceLocation(KBSpace.MOD_ID, "textures/environment/earth.png"));
        PLANETS.add(new ResourceLocation(KBSpace.MOD_ID, "textures/environment/moon.png"));
        PLANETS.add(new ResourceLocation(KBSpace.MOD_ID, "textures/environment/mars.png"));
    }

    private int selectedPlanet = 0;
    private float animationProgress = 1.0f; // 1.0 = fully zoomed in, 0.0 = switching
    private long lastUpdateTime;
    public SpaceSelectionScreen() {
        super(Component.literal("Select Destination"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.addRenderableWidget(
                Button.builder(Component.literal("Earth"), button -> {
                    PacketHandler.sendToServer(new SpaceDestinationPacket("earth"));
                    this.onClose();
                }).bounds(centerX - 50, centerY - 40, 100, 20).build()
        );

        this.addRenderableWidget(
                Button.builder(Component.literal("Moon"), button -> {
                    PacketHandler.sendToServer(new SpaceDestinationPacket("moon"));
                    this.onClose();
                }).bounds(centerX - 50, centerY, 100, 20).build()
        );

        /**
        this.addRenderableWidget(
                Button.builder(Component.literal("Mars"), button -> {
                    this.onClose();
                }).bounds(centerX - 50, centerY + 40, 100, 20).build()
        );
         */
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics);
        renderPlanets(guiGraphics, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    public void renderBackground(GuiGraphics guiGraphics) {
        // Scale the background dynamically
        guiGraphics.blit(SPACE_BACKGROUND, 0, 0, 0, 0, width, height, width, height);
    }

    private void renderPlanets(GuiGraphics guiGraphics, float partialTicks) {
        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 1000.0f;
        lastUpdateTime = currentTime;

        if (animationProgress < 1.0f) {
            animationProgress += deltaTime * 2; // Adjust speed of animation
            if (animationProgress > 1.0f) animationProgress = 1.0f;
        }

        int centerX = width / 2;
        int centerY = height / 2;

        float scaleFactor = Math.min(width / 1920.0f, height / 1080.0f); // Base scale on 1920x1080

        for (int i = 0; i < PLANETS.size(); i++) {
            float scale = (i == selectedPlanet) ? 1.0f + (1.0f - animationProgress) * 0.5f : 1.0f;
            scale *= scaleFactor; // Scale planets based on screen size

            int offsetX = (int) (((i - selectedPlanet) * 400) * scaleFactor - ((1.0f - animationProgress) * 200 * scaleFactor));
            int planetSize = (int) (200 * scale);

            int x = centerX + offsetX - planetSize / 2;
            int y = centerY - planetSize / 2;

            RenderSystem.setShaderTexture(0, PLANETS.get(i));
            guiGraphics.blit(PLANETS.get(i), x, y, 0, 0, planetSize, planetSize, planetSize, planetSize);
        }
    }


    /**
     * Key Codes:
     *      Left arrow: 263
     *      Right arrow: 262
     *      A key: 65
     *      D key: 68
     */


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 68) { // Right
            if (selectedPlanet < PLANETS.size() - 1) {
                selectedPlanet++;
                animationProgress = 0.0f;
            }
        } else if (keyCode == 65) { // Left
            if (selectedPlanet > 0) {
                selectedPlanet--;
                animationProgress = 0.0f;
            }
        } else if (keyCode == 256) { // Escape Key
            this.minecraft.setScreen(null); // Close screen
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }



}
