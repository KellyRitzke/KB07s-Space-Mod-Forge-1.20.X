package net.kb07.kbspace.util.packets;

import net.kb07.kbspace.entity.custom.RocketEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RocketSlowdownPacket {
    public RocketSlowdownPacket() {}

    public static void encode(RocketSlowdownPacket msg, FriendlyByteBuf buffer) {}

    public static RocketSlowdownPacket decode(FriendlyByteBuf buffer) {
        return new RocketSlowdownPacket();
    }

    public static void handle(RocketSlowdownPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ServerPlayer player = contextSupplier.get().getSender();
            if (player != null && player.getVehicle() instanceof RocketEntity rocket) {
                rocket.slowDown();
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
