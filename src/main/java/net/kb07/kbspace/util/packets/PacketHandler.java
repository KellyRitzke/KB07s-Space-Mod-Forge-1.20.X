package net.kb07.kbspace.util.packets;

import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.entity.custom.RocketEntity;
import net.kb07.kbspace.util.LaunchRocketPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static int id = 0;
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(KBSpace.MOD_ID, "main/channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(KBSpace.MOD_ID, "main/instance"),
            () -> "1.0",
            s -> true,
            s -> true
    );

    public static void register() {
        CHANNEL.registerMessage(0, LaunchRocketPacket.class,
                LaunchRocketPacket::encode,
                LaunchRocketPacket::decode,
                LaunchRocketPacket::handle);
        INSTANCE.registerMessage(id++, RocketSlowdownPacket.class,
                RocketSlowdownPacket::encode,
                RocketSlowdownPacket::decode,
                RocketSlowdownPacket::handle);

    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }
}
