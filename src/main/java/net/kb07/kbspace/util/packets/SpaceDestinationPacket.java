package net.kb07.kbspace.util.packets;

import net.kb07.kbspace.entity.custom.RocketEntity;
import net.kb07.kbspace.util.registries.DimensionRegistry;
import net.kb07.kbspace.worldgen.portal.ModTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class SpaceDestinationPacket {
    private final String destination;

    public SpaceDestinationPacket(String destination) {
        this.destination = destination;
    }

    public SpaceDestinationPacket(FriendlyByteBuf buf) {
        this.destination = buf.readUtf();
    }

    public static void encode(SpaceDestinationPacket packet, FriendlyByteBuf buf) {
        // Serialize the packet data (e.g., coordinates, dimension ID, etc.)
        buf.writeInt(Integer.parseInt(packet.destination));
    }

    public static SpaceDestinationPacket decode(FriendlyByteBuf buf) {
        return new SpaceDestinationPacket(buf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(destination);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && player.getVehicle() instanceof RocketEntity rocket) {
                ServerLevel targetWorld = DimensionRegistry.getDimension(destination, player.server);
                if (targetWorld != null) {
                    rocket.teleportRocketAndRider(targetWorld);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
