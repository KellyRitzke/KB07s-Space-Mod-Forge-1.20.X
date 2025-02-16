package net.kb07.kbspace.worldgen.biome;

import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.worldgen.ModPlacedFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;

public class ModBiomes {

    public static final ResourceKey<Biome> MOON = ResourceKey.create(Registries.BIOME,
            new ResourceLocation(KBSpace.MOD_ID, "the_moon"));

    public static void bootstrap(BootstapContext<Biome> context) {

        context.register(MOON, moon(context));

    }

    private static Biome moon(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0.5f)
                .temperature(0.5f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .fogColor(0x000000)
                        .skyColor(0x000000)
                        .waterColor(0x3f76e4)
                        .waterFogColor(0x050533)
                        .foliageColorOverride(0x71a74d)
                        .grassColorOverride(0x8eb971)
                        .build())
                .build();

    }

}
