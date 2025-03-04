package net.kb07.kbspace.event;


import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.entity.client.ModModelLayers;
import net.kb07.kbspace.entity.client.RocketModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KBSpace.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    public void register() {
        MinecraftForge.EVENT_BUS.addListener(ModEventBusClientEvents::registerLayer);
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.ROCKET_LAYER, RocketModel::createBodyLayer);
    }



}
