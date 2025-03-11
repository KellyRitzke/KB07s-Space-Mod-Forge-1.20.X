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

    public static double marsGravityMultiplier = 0.2;   // Lower = slower falling
    public static double marsJumpBoostAmount = 0.6;     // Increase this slightly to fine-tune jump
    public static double marsMaxJumpVelocity = 0.6;     // Prevents jumping too high

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        // Ensure the player is valid
        if (player == null || !player.isAlive()) return;
        if (player.isSpectator()) return;

        // Check if the player is in the Moon dimension
        if (player.level().dimension().equals(ModDimensions.MOON_LEVEL_KEY)) {
            playerGravity(event, moonGravityMultiplier, moonJumpBoostAmount, moonMaxJumpVelocity);

        } else if (player.level().dimension().equals(ModDimensions.MARS_LEVEL_KEY)) {
            playerGravity(event, marsGravityMultiplier, marsJumpBoostAmount, marsMaxJumpVelocity);

        }
        else {
            player.setNoGravity(false);
        }
    }

    public static void playerGravity(TickEvent.PlayerTickEvent event, double gravityMult, double jumpBoostAmount, double maxJumpVelocity) {
        Player player = event.player;

        if (event.phase == TickEvent.Phase.END) {
            Vec3 movement = player.getDeltaMovement();
            boolean isJumping = isPlayerJumping(player);

            // **Modify Jumping** (apply a controlled boost)
            if (isJumping && movement.y > 0 && !player.getAbilities().flying) {
                player.setNoGravity(true);
                double boostedJump = movement.y + jumpBoostAmount;
                if (boostedJump > maxJumpVelocity) boostedJump = maxJumpVelocity; // Prevent extreme jumps
                player.setDeltaMovement(movement.x, boostedJump, movement.z);
            }

            // **Apply Custom Gravity**
            if (!player.onGround() && !player.getAbilities().flying) {
                player.setNoGravity(true);
                double newYVelocity = movement.y - (0.08 * gravityMult); // Gravity pulls player down
                player.setDeltaMovement(movement.x, newYVelocity, movement.z);
            }

            if (player.isFallFlying()) {
                player.setNoGravity(true);
            }

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
