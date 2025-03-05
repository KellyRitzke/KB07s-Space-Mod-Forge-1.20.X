package net.kb07.kbspace.util.packets;

import net.kb07.kbspace.KBSpace;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static int id = 0;
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(KBSpace.MOD_ID, "main/channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static final SimpleChannel SLOWDOWN_INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(KBSpace.MOD_ID, "main/instance/slowdown_rocket"),
            () -> "1.0",
            s -> true,
            s -> true
    );

    public static void register() {
        CHANNEL.registerMessage(0, LaunchRocketPacket.class,
                LaunchRocketPacket::encode,
                LaunchRocketPacket::decode,
                LaunchRocketPacket::handle);
        SLOWDOWN_INSTANCE.registerMessage(id++, RocketSlowdownPacket.class,
                RocketSlowdownPacket::encode,
                RocketSlowdownPacket::decode,
                RocketSlowdownPacket::handle);
        CHANNEL.registerMessage(id++, SpaceDestinationPacket.class,
                SpaceDestinationPacket::toBytes,
                SpaceDestinationPacket::new,
                SpaceDestinationPacket::handle);
        CHANNEL.registerMessage(id++, OpenSpaceDestinationPacket.class,
                OpenSpaceDestinationPacket::toBytes,
                OpenSpaceDestinationPacket::new,
                OpenSpaceDestinationPacket::handle);

    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }
}
