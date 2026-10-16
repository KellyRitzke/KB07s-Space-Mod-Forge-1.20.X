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

    // Custom Biomes
    public static final ResourceKey<Biome> CYAN_PLAINS = registerKey("cyan_plains");
    public static final ResourceKey<Biome> PURPLEHEART_FOREST = registerKey("purpleheart_forest");
    public static final ResourceKey<Biome> DARK_OCEAN = registerKey("dark_ocean");

    // Normal Planet Biomes
    public static final ResourceKey<Biome> MOON = registerKey("the_moon");
    public static final ResourceKey<Biome> MARS = registerKey("mars");

    public static void bootstrap(BootstapContext<Biome> context) {

        // Custom Biomes
        context.register(CYAN_PLAINS, veilmereBasics(context));
        context.register(PURPLEHEART_FOREST, veilmereBasics(context));
        context.register(DARK_OCEAN, veilmereBasics(context));

        // Normal Planet Biomes
        context.register(MOON, moon(context));
        context.register(MARS, mars(context));

    }

    private static Biome veilmereBasics(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        //BiomeDefaultFeatures.farmAnimals(spawnBuilder);

        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        globalOverworldGeneration(biomeBuilder);
        BiomeDefaultFeatures.addForestFlowers(biomeBuilder);
        BiomeDefaultFeatures.addFerns(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        BiomeDefaultFeatures.addDefaultMushrooms(biomeBuilder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(biomeBuilder);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(0.7f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x070091)
                        .waterFogColor(0x070091)
                        .skyColor(0x8A70FF)
                        .grassColorOverride(0x00DDFF)
                        .foliageColorOverride(0xFFFA77)
                        .fogColor(0x8A70FF)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build())
                .build();

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

    private static Biome mars(BootstapContext<Biome> context) {
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
                        .fogColor(0xb73d00)
                        .skyColor(0x000000)
                        .waterColor(0x3f76e4)
                        .waterFogColor(0x050533)
                        .foliageColorOverride(0x71a74d)
                        .grassColorOverride(0x8eb971)
                        .build())
                .build();

    }

    public static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        //BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        //BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        //BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

    public static ResourceKey<Biome> registerKey(String name) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(KBSpace.MOD_ID, name));
    }


}
