package net.kb07.kbspace.entity.custom;

import net.kb07.kbspace.entity.ModEntities;
import net.kb07.kbspace.util.packets.OpenSpaceDestinationPacket;
import net.kb07.kbspace.util.packets.PacketHandler;
import net.kb07.kbspace.util.screens.SpaceSelectionScreen;
import net.kb07.kbspace.worldgen.dimension.ModDimensions;
import net.kb07.kbspace.worldgen.dimension.util.PlayerGravityHandler;
import net.kb07.kbspace.worldgen.portal.ModTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class RocketEntity extends Entity implements PlayerRideable {

    private static final EntityDataAccessor<Boolean> LAUNCHING = SynchedEntityData.defineId(RocketEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CANLAUNCH = SynchedEntityData.defineId(RocketEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SLOWDOWN = SynchedEntityData.defineId(RocketEntity.class, EntityDataSerializers.BOOLEAN);


    private boolean launching;
    private boolean canLaunch;
    private boolean hasTeleported = false;
    private boolean wasFallingFast = false;
    private boolean wasFallingSlow;
    private boolean hasLanded = false;

    private boolean slowdown;
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
        this.setNoGravity(true);

        if (!this.level().isClientSide) { // Only update on server
            this.entityData.set(LAUNCHING, this.launching); // Sync with client
            this.entityData.set(CANLAUNCH, this.canLaunch);
            this.entityData.set(SLOWDOWN, this.slowdown);
        } else {
            this.launching = this.entityData.get(LAUNCHING); // Client reads from server
            this.canLaunch = this.entityData.get(CANLAUNCH);
            this.slowdown = this.entityData.get(SLOWDOWN);
        }


        // Check if the rocket was falling in the previous tick
        if (this.getDeltaMovement().y < -1) {
            this.wasFallingFast = true; // Mark that the rocket was falling
            this.wasFallingSlow = false;
        } else if (this.wasFallingFast && this.getDeltaMovement().y > -0.75 && !(this.getDeltaMovement().y == 0)) {
            this.wasFallingFast = false;
            this.wasFallingSlow = true;
        }

        // If the rocket was falling and now it's on the ground, trigger explosion
        if (this.wasFallingFast && this.onGround()) {
            this.level().explode(null, this.getX(), this.getY(), this.getZ(), 4.0F, Level.ExplosionInteraction.BLOCK);

            // Remove the rocket entity after the explosion
            this.discard();
        } else if (this.wasFallingSlow && this.onGround()) {
            this.hasLanded = true;
        }



        if (this.onGround()) {
            canLaunch = true;
        }

        // Check if the rocket is currently being launched
        if (this.launching && this.getY() < YLevel) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.5, this.getDeltaMovement().z); // Move up
        } else if (this.launching && this.getY() >= YLevel) {
            this.setDeltaMovement(0, 0, 0); // Stop movement once at the desired height
            this.onLaunchComplete();
        }

        // Apply gravity only if not launching and the rocket is not on the ground
        if (!this.launching && !this.slowdown) {
            applyCustomGravity();
        } else {
            this.slowdown = false;
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    private void applyCustomGravity() {
        if (!this.onGround()) {
            this.setNoGravity(true);
            double gravity = 0;

            Vec3 movement = this.getDeltaMovement();

            if (this.level().dimension().equals(Level.OVERWORLD)) {
                // drag aka air resistance
                double newYVelocity = movement.y - 0.08;
                newYVelocity *= 0.96;
                this.setDeltaMovement(movement.x, newYVelocity, movement.z);
            } else if (this.level().dimension().equals(ModDimensions.MOON_LEVEL_KEY)) {
                gravity = PlayerGravityHandler.moonGravityMultiplier;
            }

            if (!this.level().dimension().equals(Level.OVERWORLD)) {
                // gravity
                double newYVelocity = movement.y - (0.08 * gravity);
                this.setDeltaMovement(movement.x, newYVelocity, movement.z);
            }

        }
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
            Vec3 seatPos = this.position().add(0, 0.4, 0); // Adjust height
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

    @Override
    public void ejectPassengers() {
        Entity passenger = this.getFirstPassenger();
        super.ejectPassengers();

        if (passenger instanceof Player) {
            this.wasFallingSlow = false;
            this.hasLanded = false; // Reset when the player exits
        }
    }

    @Override
    public void removePassenger(Entity passenger) {
        super.removePassenger(passenger);

        if (passenger instanceof Player) {
            this.wasFallingSlow = false;
            this.hasLanded = false; // Reset when the player exits
        }
    }

    public void slowDown() {
        double slowdownRate = 0.025;
        if (!this.launching && !this.canLaunch) {
            double newYVelocity = this.getDeltaMovement().y + slowdownRate;
            if (newYVelocity > 0) {
                newYVelocity = 0;
            }
            this.setDeltaMovement(this.getDeltaMovement().x, newYVelocity, this.getDeltaMovement().z);
            this.slowdown = true;
        }
    }

    public void startLaunch() {
        if (!this.launching && canLaunch && !hasLanded) {
            this.launching = true;
            this.canLaunch = false;
            this.entityData.set(LAUNCHING, true); // Sync with client
            this.entityData.set(CANLAUNCH, false); // Sync with client
            this.entityData.set(SLOWDOWN, false); // Sync with client
            this.hasTeleported = false;
        }
    }

    public void onLaunchComplete() {
        this.launching = false;
        this.canLaunch = false;
        if (this.hasTeleported) return; // Prevents repeated teleportation

        this.hasTeleported = true; // Mark teleportation as completed

        if (!this.level().isClientSide) {

            if (this.getFirstPassenger() instanceof ServerPlayer player) {
                PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new OpenSpaceDestinationPacket());
            }


        }
    }

    public void teleportRocketAndRider(ServerLevel targetLevel) {

        if (!(this.level() instanceof ServerLevel)) return; // Ensure we are on the server

        if (this.level().equals(targetLevel)) return; // Ensure we aren't teleporting to the same dimension

        Vec3 currentPos = this.position();
        Entity passenger = this.getFirstPassenger();

        if (passenger instanceof ServerPlayer player) {

            // Remove the rocket from the current dimension before spawning a new one
            if (!this.isRemoved()) {
                this.ejectPassengers();
                System.out.println("hasTeleported: " + hasTeleported);
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
                    player.changeDimension(portalDimension, new ModTeleporter(pos));
                }

                assert newRocket != null;
                player.startRiding(newRocket, true);

            }

        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(LAUNCHING, false); // Default value
        this.entityData.define(CANLAUNCH, false); // Default value
        this.entityData.define(SLOWDOWN, false); // Default value
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    }
}
