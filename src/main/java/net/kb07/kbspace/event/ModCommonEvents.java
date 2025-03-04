package net.kb07.kbspace.event;

import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.entity.custom.RocketEntity;
import net.kb07.kbspace.worldgen.dimension.ModDimensions;
import net.kb07.kbspace.worldgen.dimension.util.PlayerGravityHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModCommonEvents {

    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(ModCommonEvents::onLevelTick);
        MinecraftForge.EVENT_BUS.addListener(PlayerGravityHandler::onPlayerTick);
        MinecraftForge.EVENT_BUS.addListener(ModCommonEvents::onLivingFall);
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();

        if (level.dimension().equals(ModDimensions.MOON_LEVEL_KEY)) {
            float originalDistance = event.getDistance();

            // Scale fall damage based on moon gravity (adjust multiplier as needed)
            float newDistance = originalDistance * 0.4f; // Example: Reduce fall height by 60%
            event.setDistance(newDistance);
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        Level level = event.level;
        if (!level.dimension().equals(ModDimensions.MOON_LEVEL_KEY)) return;

        AABB boundingBox = new AABB(
                -5000, level.getMinBuildHeight(), -5000,
                5000, 800, 5000
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
