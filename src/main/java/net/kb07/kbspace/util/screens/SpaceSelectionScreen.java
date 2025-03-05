package net.kb07.kbspace.util.screens;

import net.kb07.kbspace.util.packets.PacketHandler;
import net.kb07.kbspace.util.packets.SpaceDestinationPacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SpaceSelectionScreen extends Screen {
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



}
