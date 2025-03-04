package net.kb07.kbspace.util;

import net.kb07.kbspace.entity.custom.RocketEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LaunchRocketPacket {
    public LaunchRocketPacket() {}

    public static void encode(LaunchRocketPacket msg, FriendlyByteBuf buf) {}

    public static LaunchRocketPacket decode(FriendlyByteBuf buf) {
        return new LaunchRocketPacket();
    }

    public static void handle(LaunchRocketPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && player.getVehicle() instanceof RocketEntity rocket) {
                rocket.startLaunch();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
