package net.kb07.kbspace.worldgen.dimension.util;

import net.kb07.kbspace.worldgen.dimension.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ModGravityHandler {

    public void entityTick(Level level, LivingEntity entity, Vec3 travelVector, BlockPos movementAffectingPos) {
        if (level.dimension().equals(ModDimensions.MOON_LEVEL_KEY)) {
            if (entity.onGround()) {
                boolean touchingSomething = false;
                Direction[] var6 = Direction.values();
                int var7 = var6.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    Direction direction = var6[var8];
                    BlockPos pos = movementAffectingPos.relative(direction);
                    if (!level.getBlockState(pos).isAir()) {
                        touchingSomething = true;
                        break;
                    }
                }

                float ySpeed = 0.0F;
                if ((touchingSomething || level.getGameTime() % 10L == 0L) && entity instanceof Player) {
                    Player player = (Player)entity;
                    if (player.isShiftKeyDown()) {
                        ySpeed = -0.01F;
                    } else if (isPlayerJumping(player)) {
                        ySpeed = 0.01F;
                    }
                }

                if (!touchingSomething) {
                    travelVector = travelVector.scale(0.11999999731779099);
                }

                float friction = level.getBlockState(movementAffectingPos).getBlock().getFriction();
                float speed = touchingSomething ? (entity.isSprinting() ? friction * 0.91F : 0.91F) : (entity.isSprinting() ? friction * 0.999F : 0.999F);

                Vec3 movementVector = new Vec3(travelVector.x * friction, travelVector.y, travelVector.z * friction);
                double downSpeed = movementVector.y;

                if (entity instanceof Player && !level.getBlockState(movementAffectingPos).isAir()) {
                    if (entity.getY() > movementAffectingPos.getY()) {
                        downSpeed = -0.1;
                    } else {
                        downSpeed = 0.0;
                    }
                }

                downSpeed += (double)ySpeed;
                if (entity.isNoGravity()) {
                    entity.setDeltaMovement(movementVector.x, downSpeed, movementVector.z);
                } else {
                    entity.setDeltaMovement(movementVector.x * (double)speed, downSpeed * 0.98, movementVector.z * (double)speed);
                }

                entity.setNoGravity(this instanceof FlyingAnimal);
            }
        }
    }

    private static boolean isPlayerJumping(Player player) {
        if (player instanceof net.minecraft.client.player.LocalPlayer localPlayer) {
            return localPlayer.input.jumping;
        }
        return player.getDeltaMovement().y > 0 && player.fallDistance == 0;
    }

}
