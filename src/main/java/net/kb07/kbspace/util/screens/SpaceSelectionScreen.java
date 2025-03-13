package net.kb07.kbspace.util.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.util.packets.PacketHandler;
import net.kb07.kbspace.util.packets.SpaceDestinationPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class SpaceSelectionScreen extends Screen {

    private static final ResourceLocation SPACE_BACKGROUND = new ResourceLocation(KBSpace.MOD_ID, "textures/gui/space_bg.png");
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(KBSpace.MOD_ID, "textures/gui/button.png");
    private static final ResourceLocation LIST_BACKGROUND_TEXTURE = new ResourceLocation(KBSpace.MOD_ID, "textures/gui/list_background.png");

    private SelectionState state = SelectionState.SOLAR_SYSTEM;
    private final List<String> solarSystems = List.of("Milky Way", "Solar System Beta", "Solar System Gamma");
    private List<String> planets = new ArrayList<>();
    private List<String> moons = new ArrayList<>();
    private String selectedSolarSystem;
    private String selectedPlanet;

    public SpaceSelectionScreen() {
        super(Component.literal("Select Destination"));
    }

    @Override
    protected void init() {
    }

    private void updateOptions(GuiGraphics guiGraphics) {
        this.clearWidgets();
        int startX = 11;
        int startY = 41;

        int buttonWidth = 80;
        int buttonHeight = 20;
        int pXTexStart = 0;
        int pYTexStart = 0;
        int pYDiffTex = 20;

        // Solar System List
        for (String solarSystem : solarSystems) {
            this.addRenderableWidget(new ImageButton(
                    startX, startY, buttonWidth, buttonHeight,
                    pXTexStart, pYTexStart, pYDiffTex,
                    BUTTON_TEXTURE, 80, 60,
                    button -> {
                        selectedSolarSystem = solarSystem;
                        planets = getPlanetsForSolarSystem(solarSystem);
                        moons.clear();
                        updateOptions(guiGraphics);
                    }
            ));
            guiGraphics.drawCenteredString(this.font, solarSystem,
                    startX + (buttonWidth/2), startY + (buttonHeight/2) - 4, 0xFFFFFF);
            startY += buttonHeight;
        }

        // Planet List (Next Column)
        if (selectedSolarSystem != null) {
            startX += 81;
            startY = 41;
            for (String planet : planets) {
                this.addRenderableWidget(new ImageButton(
                        startX, startY, buttonWidth, buttonHeight,
                        pXTexStart, pYTexStart, pYDiffTex,
                        BUTTON_TEXTURE, 80, 60,
                        button -> {
                            selectedPlanet = planet;
                            moons = getMoonsForPlanet(planet);
                            updateOptions(guiGraphics);
                        }
                ));
                guiGraphics.drawCenteredString(this.font, planet,
                        startX + (buttonWidth/2), startY + (buttonHeight/2) - 4, 0xFFFFFF);
                startY += buttonHeight;
            }
        }

        // Moon List (Next Column)
        if (selectedPlanet != null) {
            startX += 81;
            startY = 41;
            this.addRenderableWidget(new ImageButton(
                    startX, startY, buttonWidth, buttonHeight,
                    pXTexStart, pYTexStart, pYDiffTex,
                    BUTTON_TEXTURE, 80, 60,
                    button -> teleportTo(selectedPlanet)
            ));
            guiGraphics.drawCenteredString(this.font, "Land",
                    startX + (buttonWidth/2), startY + (buttonHeight/2) - 4, 0xFFFFFF);
            startY += buttonHeight;

            for (String moon : moons) {
                this.addRenderableWidget(new ImageButton(
                        startX, startY, buttonWidth, buttonHeight,
                        pXTexStart, pYTexStart, pYDiffTex,
                        BUTTON_TEXTURE, 80, 60,
                        button -> teleportTo(moon)
                ));
                guiGraphics.drawCenteredString(this.font, moon,
                        startX + (buttonWidth/2), startY + (buttonHeight/2) - 4, 0xFFFFFF);
                startY += buttonHeight;
            }
        }
    }

    private void teleportTo(String destination) {
        PacketHandler.sendToServer(new SpaceDestinationPacket(destination.toLowerCase()));
        this.onClose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int imageX = 82;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        renderBackground(guiGraphics);

        renderListBackground(guiGraphics, 10, 40); // Solar System List
        if (selectedSolarSystem != null) renderListBackground(guiGraphics, imageX + 9, 40); // Planet List
        if (selectedPlanet != null) renderListBackground(guiGraphics, (imageX * 2) + 8, 40); // Moon List

        updateOptions(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics) {
        pGuiGraphics.blit(SPACE_BACKGROUND, 0, 0, 0, 0, width, height, width, height);
        super.renderBackground(pGuiGraphics);
    }

    private void renderListBackground(GuiGraphics guiGraphics, int x, int y) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(LIST_BACKGROUND_TEXTURE, x, y, 0, 0, 82, 202, 82, 202);
    }

    private List<String> getPlanetsForSolarSystem(String solarSystem) {
        return switch (solarSystem) {
            case "Milky Way" -> List.of("Earth", "Mars");
            case "Solar System Beta" -> List.of("Jupiter", "Saturn");
            default -> List.of();
        };
    }

    private List<String> getMoonsForPlanet(String planet) {
        return switch (planet) {
            case "Earth" -> List.of("Moon");
            case "Mars" -> List.of("Phobos", "Deimos");
            default -> List.of();
        };
    }

    private enum SelectionState {
        SOLAR_SYSTEM, PLANET, MOON
    }
}
