package net.kb07.kbspace.util;

import net.kb07.kbspace.KBSpace;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ModTags {
    public static class Biomes {
        public static final TagKey<Biome> MOON_DIMENSION = tag("moon_dimension");

        private static TagKey<Biome> tag(String name) {
            return TagKey.create(Registries.BIOME, new ResourceLocation(KBSpace.MOD_ID, name));
        }
    }

}
