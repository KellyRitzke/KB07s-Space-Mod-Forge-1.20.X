package net.kb07.kbspace.util.registries;

import net.kb07.kbspace.worldgen.dimension.ModDimensions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class DimensionRegistry {

    public static ServerLevel getDimension(String name, MinecraftServer server) {
        if ("earth".equals(name)) return server.getLevel(Level.OVERWORLD);
        if ("moon".equals(name)) return server.getLevel(ModDimensions.MOON_LEVEL_KEY);
        if ("mars".equals(name)) return server.getLevel(ModDimensions.MARS_LEVEL_KEY);
        return null;
    }
}
