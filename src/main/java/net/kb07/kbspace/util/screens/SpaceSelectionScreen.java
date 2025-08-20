package net.kb07.kbspace.util.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.util.packets.PacketHandler;
import net.kb07.kbspace.util.packets.SpaceDestinationPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SpaceSelectionScreen extends Screen {

    private static final ResourceLocation SPACE_BACKGROUND = new ResourceLocation(KBSpace.MOD_ID, "textures/gui/space_bg.png");
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(KBSpace.MOD_ID, "textures/gui/button.png");
    private static final ResourceLocation BACK_BUTTON_TEXTURE = new ResourceLocation(KBSpace.MOD_ID, "textures/gui/back_button.png");
    private static final Logger log = LoggerFactory.getLogger(SpaceSelectionScreen.class);

    private SelectionState state = SelectionState.SOLAR_SYSTEM;
    private final List<String> solarSystems = List.of("Milky Way", "Solar System Beta");
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

        int startX, startY;

        int buttonSize = 80;
        int imageSize = 56;

        int backButtonWidth = 100;
        int backButtonHeight = 25;

        int pXTexStart = 0;
        int pYTexStart = 0;

        int backButtonX = guiGraphics.guiWidth()/2 - backButtonWidth/2;
        int backButtonY = guiGraphics.guiHeight()*3/4;

        // Solar System Screen
        if (selectedSolarSystem == null) {
            startX = guiGraphics.guiWidth()/2 - (buttonSize * solarSystems.size()/2);
            startY = guiGraphics.guiHeight()/2 - buttonSize/2;
            for (String solarSystem : solarSystems) {
                this.addRenderableWidget(new ImageButton(
                        startX, startY, buttonSize, buttonSize,
                        pXTexStart, pYTexStart, buttonSize,
                        BUTTON_TEXTURE, buttonSize, buttonSize * 3,
                        button -> {
                            selectedSolarSystem = solarSystem;
                            planets = getPlanetsForSolarSystem(solarSystem);
                            updateOptions(guiGraphics);
                        }
                ));
                this.addRenderableWidget(new ImageWidget(startX + buttonSize/2 - imageSize/2, startY + buttonSize/2 - imageSize/2, imageSize, imageSize,
                        new ResourceLocation(KBSpace.MOD_ID, "textures/gui/big_" + solarSystem.toLowerCase().replace(" ", "_") + ".png")));
                guiGraphics.drawCenteredString(this.font, solarSystem,
                        startX + (buttonSize/2), startY + 4, 0xFFFFFF);
                startX += buttonSize + 5;
            }
            guiGraphics.drawCenteredString(this.font, "Select Destination (Solar System)",
                    guiGraphics.guiWidth()/2, guiGraphics.guiHeight()/8, 0xFFFFFF);
        }

        // Planet Screen
        if (selectedSolarSystem != null && selectedPlanet == null) {
            startX = guiGraphics.guiWidth()/2 - (buttonSize * planets.size()/2);
            startY = guiGraphics.guiHeight()/2 - buttonSize/2;
            for (String planet : planets) {
                this.addRenderableWidget(new ImageButton(
                        startX, startY, buttonSize, buttonSize,
                        pXTexStart, pYTexStart, buttonSize,
                        BUTTON_TEXTURE, buttonSize, buttonSize * 3,
                        button -> {
                            selectedPlanet = planet;
                            moons = getMoonsForPlanet(planet);
                            updateOptions(guiGraphics);
                        }
                ));
                this.addRenderableWidget(new ImageWidget(startX + buttonSize/2 - imageSize/2, startY + buttonSize/2 - imageSize/2, imageSize, imageSize,
                        new ResourceLocation(KBSpace.MOD_ID, "textures/gui/big_" + planet.toLowerCase().replace(" ", "_") + ".png")));
                guiGraphics.drawCenteredString(this.font, planet,
                        startX + (buttonSize/2), startY + 4, 0xFFFFFF);
                startX += buttonSize + 5;
            }
            // Back Button
            this.addRenderableWidget(new ImageButton(
                    backButtonX, backButtonY,
                    backButtonWidth, backButtonHeight,
                    pXTexStart, pYTexStart, backButtonHeight,
                    BACK_BUTTON_TEXTURE, backButtonWidth, backButtonHeight * 3,
                    button -> {
                        selectedSolarSystem = null;
                        updateOptions(guiGraphics);
                    }
            ));
            guiGraphics.drawCenteredString(this.font, "Back",
                    backButtonX + backButtonWidth/2, backButtonY + backButtonHeight/3, 0xFFFFFF);
            guiGraphics.drawCenteredString(this.font, "Select Destination (Planet)",
                    guiGraphics.guiWidth()/2, guiGraphics.guiHeight()/8, 0xFFFFFF);
        }

        // Moon Screen
        if (selectedPlanet != null) {
            startX = guiGraphics.guiWidth()/2 - (buttonSize * (moons.size() + 1)/2);
            startY = guiGraphics.guiHeight()/2 - buttonSize/2;
            this.addRenderableWidget(new ImageButton(
                    startX, startY, buttonSize, buttonSize,
                    pXTexStart, pYTexStart, buttonSize,
                    BUTTON_TEXTURE, buttonSize, buttonSize * 3,
                    button -> teleportTo(selectedPlanet)
            ));
            this.addRenderableWidget(new ImageWidget(startX + buttonSize/2 - imageSize/2, startY + buttonSize/2 - imageSize/2, imageSize, imageSize,
                    new ResourceLocation(KBSpace.MOD_ID, "textures/gui/big_" + selectedPlanet.toLowerCase().replace(" ", "_") + ".png")));
            guiGraphics.drawCenteredString(this.font, selectedPlanet,
                    startX + (buttonSize/2), startY + 4, 0xFFFFFF);
            startX += buttonSize + 5;

            for (String moon : moons) {
                this.addRenderableWidget(new ImageButton(
                        startX, startY, buttonSize, buttonSize,
                        pXTexStart, pYTexStart, buttonSize,
                        BUTTON_TEXTURE, buttonSize, buttonSize * 3,
                        button -> teleportTo(moon)
                ));
                this.addRenderableWidget(new ImageWidget(startX + buttonSize/2 - imageSize/2, startY + buttonSize/2 - imageSize/2, imageSize, imageSize,
                        new ResourceLocation(KBSpace.MOD_ID, "textures/gui/big_" + moon.toLowerCase().replace(" ", "_") + ".png")));
                guiGraphics.drawCenteredString(this.font, moon,
                        startX + (buttonSize/2), startY + 4, 0xFFFFFF);
                startX += buttonSize + 5;
            }
            // Back Button
            this.addRenderableWidget(new ImageButton(
                    backButtonX, backButtonY,
                    backButtonWidth, backButtonHeight,
                    pXTexStart, pYTexStart, backButtonHeight,
                    BACK_BUTTON_TEXTURE, backButtonWidth, backButtonHeight * 3,
                    button -> {
                        selectedPlanet = null;
                        updateOptions(guiGraphics);
                    }
            ));
            guiGraphics.drawCenteredString(this.font, "Back",
                    backButtonX + backButtonWidth/2, backButtonY + backButtonHeight/3, 0xFFFFFF);
            guiGraphics.drawCenteredString(this.font, "Select Destination (Final)",
                    guiGraphics.guiWidth()/2, guiGraphics.guiHeight()/8, 0xFFFFFF);
        }
    }

    private void teleportTo(String destination) {
        PacketHandler.sendToServer(new SpaceDestinationPacket(destination.toLowerCase()));
        this.onClose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        renderBackground(guiGraphics);

        updateOptions(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics) {
        pGuiGraphics.blit(SPACE_BACKGROUND, 0, 0, 0, 0, width, height, width, height);
        super.renderBackground(pGuiGraphics);
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
