package net.kb07.kbspace.util;

import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.util.packets.PacketHandler;
import net.kb07.kbspace.util.packets.RocketSlowdownPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KBSpace.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyInputHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (ModKeybinds.LAUNCH_ROCKET.isDown()) {
                PacketHandler.sendToServer(new LaunchRocketPacket());
                PacketHandler.INSTANCE.sendToServer(new RocketSlowdownPacket());
            }
        }
    }
}
