package net.kb07.kbspace.util.packets;

import net.kb07.kbspace.util.screens.SpaceSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenSpaceDestinationPacket {
    public OpenSpaceDestinationPacket() {}

    public OpenSpaceDestinationPacket(FriendlyByteBuf buf) {
        // No data needed
    }

    public void toBytes(FriendlyByteBuf buf) {
        // No data needed
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Minecraft.getInstance().setScreen(new SpaceSelectionScreen());
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
