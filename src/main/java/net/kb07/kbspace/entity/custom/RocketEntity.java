package net.kb07.kbspace.entity.custom;

import net.kb07.kbspace.entity.ModEntities;
import net.kb07.kbspace.worldgen.dimension.ModDimensions;
import net.kb07.kbspace.worldgen.dimension.util.PlayerGravityHandler;
import net.kb07.kbspace.worldgen.portal.ModTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class RocketEntity extends Entity implements PlayerRideable {

    private boolean launching;
    private boolean hasTeleported = false;

    public int YLevel = 200;

    public RocketEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public @NotNull InteractionResult interact(Player player, InteractionHand hand) {
        if (!this.level().isClientSide()) {
            if (player.isCrouching()) {
                //Open Inventory
                return InteractionResult.SUCCESS;
            } else {
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            }
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void tick() {
        super.tick();

        System.out.println("Current Y Position: " + this.getY() + " | Launching: " + this.launching);

        // Check if the rocket is currently being launched
        if (this.launching && this.getY() < YLevel) {
            System.out.println("Should be moving " + this.launching);
            this.setDeltaMovement(this.getDeltaMovement().x, 0.5, this.getDeltaMovement().z); // Move up
        } else if (this.launching && this.getY() >= YLevel) {
            this.setDeltaMovement(0, 0, 0); // Stop movement once at the desired height
            this.onLaunchComplete();
            System.out.println("Launch Completed");
        }

        // Ensure that launching is only set to false after the rocket has reached YLevel
        if (this.getY() >= YLevel) {
            if (this.launching) {
                this.launching = false;  // Set launching to false only once the rocket is at the correct height
                System.out.println("Launch Complete!");  // Debugging
            }
        } else {
            this.launching = true;

        }

        System.out.println("launching: " + this.launching);



        if (!this.launching && checkNearGround(0.5)) {
            System.out.println("Check near ground: " + !this.launching);
            this.setDeltaMovement(0, 0, 0);
        }

        // Apply gravity only if not launching and the rocket is not on the ground
        if (!this.launching) {
            System.out.println("Gravity: " + !this.launching);
            applyCustomGravity();
        } else {
            this.setNoGravity(true);
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    private void applyCustomGravity() {
        if (!this.onGround()) {
            this.setNoGravity(true);
            double gravity = 0;

            if (this.level().dimension().equals(Level.OVERWORLD)) {
                gravity = 1;
            } else if (this.level().dimension().equals(ModDimensions.MOON_LEVEL_KEY)) {
                gravity = PlayerGravityHandler.moonGravityMultiplier;
            }

            Vec3 movement = this.getDeltaMovement();

            if (checkNearGround(0.5)) {
                this.setDeltaMovement(0, 0, 0);
            } else {
                double newYVelocity = movement.y - (0.08 * gravity);
                this.setDeltaMovement(movement.x, newYVelocity, movement.z);
            }
        }
    }

    private boolean checkNearGround(double offset) {
        BlockPos posBelow = new BlockPos(
                Mth.floor(this.getX()),
                Mth.floor(this.getY() - offset),
                Mth.floor(this.getZ())
        );
        return !this.level().getBlockState(posBelow).isAir();
    }

    @Override
    public @Nullable LivingEntity getFirstPassenger() {
        if (!this.getPassengers().isEmpty()) {
            Entity passenger = this.getPassengers().get(0);
            if (passenger instanceof LivingEntity livingPassenger) {
                return livingPassenger;
            }
        }
        return null;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public boolean isVehicle() {
        return true;
    }

    @Override
    public boolean canAddPassenger(@NotNull Entity passenger) {
        return this.getPassengers().isEmpty() && passenger instanceof Player;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.0; // Adjust if necessary
    }

    @Override
    public void positionRider(Entity passenger, MoveFunction moveFunction) {
        if (passenger instanceof Player player) {
            Vec3 seatPos = this.position().add(0, -0.1, 0); // Adjust height
            moveFunction.accept(player, seatPos.x, seatPos.y, seatPos.z);
        } else {
            super.positionRider(passenger, moveFunction);
        }
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false; // Prevents the player from being pushed off
    }

    public void startLaunch() {
        if (!this.launching) {
            System.out.println("Starting launch..."); // Debugging
            this.launching = true;
            this.hasTeleported = false;
        }
    }

    public void onLaunchComplete() {
        this.launching = false;
        if (this.hasTeleported) return; // Prevents repeated teleportation

        this.hasTeleported = true; // Mark teleportation as completed

        if (!this.level().isClientSide) {
            ServerLevel targetLevel = null;

            if (this.level().dimension().equals(Level.OVERWORLD)) {
                targetLevel = Objects.requireNonNull(this.getServer()).getLevel(ModDimensions.MOON_LEVEL_KEY);
            } else if (this.level().dimension().equals(ModDimensions.MOON_LEVEL_KEY)) {
                targetLevel = Objects.requireNonNull(this.getServer()).getLevel(Level.OVERWORLD);
            }

            if (targetLevel != null) {
                teleportRocketAndRider(targetLevel);
            } else {
                System.out.println("Target dimension not found!");
            }
        }
    }

    private void teleportRocketAndRider(ServerLevel targetLevel) {

        if (!(this.level() instanceof ServerLevel currentLevel)) return; // Ensure we are on the server

        Vec3 currentPos = this.position();
        Entity passenger = this.getFirstPassenger();

        if (passenger instanceof ServerPlayer player) {

            // Remove the rocket from the current dimension before spawning a new one
            if (!this.isRemoved()) {
                this.ejectPassengers();
                this.remove(RemovalReason.CHANGED_DIMENSION);
            }

            // Create new rocket in target dimension
            RocketEntity newRocket = ModEntities.ROCKET.get().create(targetLevel);
            if (newRocket != null) {
                newRocket.setUUID(UUID.randomUUID()); // Prevents entity duplication
                newRocket.moveTo(currentPos.x, currentPos.y, currentPos.z, this.getYRot(), this.getXRot());// Copy teleport flag to the new rocket
                newRocket.hasTeleported = this.hasTeleported;
                targetLevel.addFreshEntity(newRocket);

            }

            // Teleport player
            if (player.level() instanceof ServerLevel serverLevel) {
                MinecraftServer minecraftServer = serverLevel.getServer();
                ResourceKey<Level> resourceKey = player.level().dimension() == ModDimensions.MOON_LEVEL_KEY ?
                        Level.OVERWORLD : ModDimensions.MOON_LEVEL_KEY;

                ServerLevel portalDimension = minecraftServer.getLevel(resourceKey);
                if (portalDimension != null && !player.isPassenger()) {
                    BlockPos pos = new BlockPos(player.getBlockX(), player.getBlockY(), player.getBlockZ());
                    player.changeDimension(portalDimension, new ModTeleporter(pos, false));
                }

                assert newRocket != null;
                player.startRiding(newRocket, true);

            }

        }
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }
}
