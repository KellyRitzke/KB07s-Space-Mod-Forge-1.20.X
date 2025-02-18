package net.kb07.kbspace.worldgen.dimension.util;

import net.kb07.kbspace.worldgen.dimension.ModDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "kbspace", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerGravityHandler {

    // Moon Gravity Settings:
    public static double moonGravityMultiplier = 0.1;   // Lower = slower falling
    public static double moonJumpBoostAmount = 0.4;     // Increase this slightly to fine-tune jump
    public static double moonMaxJumpVelocity = 0.6;     // Prevents jumping too high

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        // Ensure the player is valid
        if (player == null || !player.isAlive()) return;
        if (player.isSpectator()) return;

        // Check if the player is in the Moon dimension
        if (player.level().dimension().equals(ModDimensions.MOON_LEVEL_KEY)) {
            if (event.phase == TickEvent.Phase.END) {
                Vec3 movement = player.getDeltaMovement();
                boolean isJumping = isPlayerJumping(player);

                // **Modify Jumping** (apply a controlled boost)
                if (isJumping && movement.y > 0 && !player.getAbilities().flying) {
                    player.setNoGravity(true);
                    double boostedJump = movement.y + moonJumpBoostAmount;
                    if (boostedJump > moonMaxJumpVelocity) boostedJump = moonMaxJumpVelocity; // Prevent extreme jumps
                    player.setDeltaMovement(movement.x, boostedJump, movement.z);
                }

                // **Apply Custom Gravity**
                if (!player.onGround() && !player.getAbilities().flying) {
                    player.setNoGravity(true);
                    double newYVelocity = movement.y - (0.08 * moonGravityMultiplier); // Gravity pulls player down
                    player.setDeltaMovement(movement.x, newYVelocity, movement.z);
                }

            }

        } else {
            player.setNoGravity(false);
        }
    }

    // Helper method to detect if the player is jumping
    private static boolean isPlayerJumping(Player player) {
        if (player instanceof net.minecraft.client.player.LocalPlayer localPlayer) {
            return localPlayer.input.jumping;
        }
        return player.getDeltaMovement().y > 0 && player.fallDistance == 0;
    }

}
