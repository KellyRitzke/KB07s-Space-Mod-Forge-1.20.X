package net.kb07.kbspace;

import com.mojang.logging.LogUtils;
import net.kb07.kbspace.block.ModBlocks;
import net.kb07.kbspace.item.ModCreativeModeTabs;
import net.kb07.kbspace.item.ModItems;
import net.kb07.kbspace.worldgen.dimension.ModDimensionSpecialEffects;
import net.kb07.kbspace.worldgen.dimension.ModDimensions;
import net.kb07.kbspace.worldgen.dimension.util.PlayerGravityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
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
        System.out.println("✅ Manual registration of custom dimension effect");
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
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }

    public class ModCommonEvents {

        public static void register() {
            MinecraftForge.EVENT_BUS.addListener(ModCommonEvents::onLevelTick);
            MinecraftForge.EVENT_BUS.addListener(PlayerGravityHandler::onPlayerTick);
        }

        @SubscribeEvent
        public static void onLevelTick(TickEvent.LevelTickEvent event) {
            Level level = event.level;
            if (!level.dimension().equals(ModDimensions.MOON_LEVEL_KEY)) return;

            AABB boundingBox = new AABB(
                    -5000, level.getMinBuildHeight(), -5000,
                    5000, level.getMaxBuildHeight(), 5000
            );

            for (Entity entity : level.getEntitiesOfClass((Entity.class), boundingBox, e -> !(e instanceof Player))) {
                if (level.dimension().equals(ModDimensions.MOON_LEVEL_KEY)) {

                    Vec3 movement = entity.getDeltaMovement();

                    if (entity instanceof LivingEntity livingEntity) {
                        if (!livingEntity.onGround() && !livingEntity.isInWater() && !livingEntity.isInLava()) {
                            entity.setNoGravity(true);
                            double newYVelocity = movement.y - (0.08 * PlayerGravityHandler.moonGravityMultiplier);
                            livingEntity.setDeltaMovement(movement.x, newYVelocity, movement.z);
                        } else {
                            entity.setNoGravity(false);
                        }
                    }

                    if (entity instanceof ItemEntity itemEntity) {

                        if (!itemEntity.onGround() || !itemEntity.isInWater() || !itemEntity.isInLava()) {
                            itemEntity.setNoGravity(true); // Disable default gravity

                            Vec3 velocity = itemEntity.getDeltaMovement();
                            double gravity = 0.08 * PlayerGravityHandler.moonGravityMultiplier;

                            Vec3 newVelocity = velocity.add(0, -gravity, 0);
                            itemEntity.setDeltaMovement(newVelocity);
                        } else {
                            itemEntity.setNoGravity(false);
                        }

                    }

                } else {
                    entity.setNoGravity(false);
                }
            }
        }

    }

}
