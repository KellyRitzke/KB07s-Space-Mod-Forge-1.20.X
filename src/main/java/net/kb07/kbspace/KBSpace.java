package net.kb07.kbspace;

import com.mojang.logging.LogUtils;
import net.kb07.kbspace.block.ModBlocks;
import net.kb07.kbspace.entity.ModEntities;
import net.kb07.kbspace.entity.client.RocketRenderer;
import net.kb07.kbspace.event.ModCommonEvents;
import net.kb07.kbspace.item.ModCreativeModeTabs;
import net.kb07.kbspace.item.ModItems;
import net.kb07.kbspace.util.ModKeybinds;
import net.kb07.kbspace.util.packets.PacketHandler;
import net.kb07.kbspace.worldgen.dimension.ModDimensionSpecialEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.Map;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KBSpace.MOD_ID)
public class KBSpace {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "kbspace";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public KBSpace() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModEntities.register(modEventBus);

        PacketHandler.register();

        ModCreativeModeTabs.register(modEventBus);
        ModDimensionSpecialEffects.register();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        ModCommonEvents.register();


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Manually register your custom dimension effect
        ModDimensionSpecialEffects.register();
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = KBSpace.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public class ModClientEvents {

        @SubscribeEvent
        public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
            event.register(new ResourceLocation(KBSpace.MOD_ID, "moon_effect"), ModDimensionSpecialEffects.MOON_EFFECT);
            event.register(new ResourceLocation(KBSpace.MOD_ID, "mars_effect"), ModDimensionSpecialEffects.MARS_EFFECT);
        }

        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            ModKeybinds.register(event);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.ROCKET.get(), RocketRenderer::new);
            event.enqueueWork(() -> {
                ModDimensionSpecialEffects.register(); // Ensure registration
                if (Minecraft.getInstance().level != null) {
                    Field effectsField;
                    try {
                        effectsField = DimensionSpecialEffects.class.getDeclaredField("EFFECTS");
                        effectsField.setAccessible(true);

                        @SuppressWarnings("unchecked")
                        Map<ResourceLocation, DimensionSpecialEffects> effectsMap = (Map<ResourceLocation, DimensionSpecialEffects>) effectsField.get(null);

                        effectsMap.put(new ResourceLocation(KBSpace.MOD_ID, "moon_effect"), ModDimensionSpecialEffects.MOON_EFFECT);
                        effectsMap.put(new ResourceLocation(KBSpace.MOD_ID, "mars_effect"), ModDimensionSpecialEffects.MARS_EFFECT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }

}
