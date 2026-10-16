package net.kb07.kbspace.worldgen.dimension;

import com.mojang.datafixers.util.Pair;
import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.worldgen.biome.ModBiomes;
import net.kb07.kbspace.worldgen.noise.ModNoiseSettingsGenerator;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.util.List;
import java.util.OptionalLong;

public class ModDimensions {

    // Custom Dimensions
    public static ResourceKey<LevelStem> VEILMERE_KEY = registerLevelStemKey("veilmere");
    public static ResourceKey<Level> VEILMERE_LEVEL_KEY = registerLevelKey("veilmere");
    public static ResourceKey<DimensionType> VEILMERE_DIM_TYPE = registerDimensionTypeKey("veilmere");

    // Normal Planet Dimensions
    public static ResourceKey<LevelStem> MOON_KEY = registerLevelStemKey("the_moon");
    public static ResourceKey<Level> MOON_LEVEL_KEY = registerLevelKey("the_moon");
    public static ResourceKey<DimensionType> MOON_TYPE = registerDimensionTypeKey("the_moon");

    public static ResourceKey<LevelStem> MARS_KEY = registerLevelStemKey("mars");
    public static ResourceKey<Level> MARS_LEVEL_KEY = registerLevelKey("mars");
    public static ResourceKey<DimensionType> MARS_TYPE = registerDimensionTypeKey("mars");

    public static void bootstrapType(BootstapContext<DimensionType> context) {
        // Custom Dimensions
        context.register(VEILMERE_DIM_TYPE, new DimensionType(
                OptionalLong.empty(),
                true,
                false,
                false,
                true,
                1.0,
                true,
                false,
                -64,
                384,
                320,
                BlockTags.INFINIBURN_OVERWORLD,
                BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                1.0f,
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)
        ));

        // Normal Planet Dimensions
        context.register(MOON_TYPE, new DimensionType(OptionalLong.empty(),
                false,
                false,
                false,
                false,
                1.0,
                true,
                false,
                -64,
                384,
                384,
                BlockTags.INFINIBURN_OVERWORLD,
                new ResourceLocation(KBSpace.MOD_ID, "moon_effect"),
                1.0f,
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));

        context.register(MARS_TYPE, new DimensionType(OptionalLong.empty(),
                false,
                false,
                false,
                false,
                1.0,
                true,
                false,
                -64,
                384,
                384,
                BlockTags.INFINIBURN_OVERWORLD,
                new ResourceLocation(KBSpace.MOD_ID, "mars_effect"),
                1.0f,
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));

    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator moonWrappedChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(ModBiomes.MOON)),
                noiseGenSettings.getOrThrow(ModNoiseSettingsGenerator.MOON));

        NoiseBasedChunkGenerator marsWrappedChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(ModBiomes.MARS)),
                noiseGenSettings.getOrThrow(ModNoiseSettingsGenerator.MARS));

        NoiseBasedChunkGenerator veilmereNoiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(
                                Pair.of(
                                        Climate.parameters(0.3F, 0.0F, 0.1F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.CYAN_PLAINS)),
                                Pair.of(
                                        Climate.parameters(0.3F, 0.0F, 0.0F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.PURPLEHEART_FOREST)),
                                Pair.of(
                                        Climate.parameters(0.3F, 0.5F, -0.5F, 0.1F, -0.1F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.DARK_OCEAN))
                        ))),
                noiseGenSettings.getOrThrow(ModNoiseSettingsGenerator.VEILMERE));

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.VEILMERE_DIM_TYPE), veilmereNoiseBasedChunkGenerator);

        LevelStem moonStem = new LevelStem(dimTypes.getOrThrow(ModDimensions.MOON_TYPE), moonWrappedChunkGenerator);
        LevelStem marsStem = new LevelStem(dimTypes.getOrThrow(ModDimensions.MARS_TYPE), marsWrappedChunkGenerator);

        context.register(MOON_KEY, moonStem);
        context.register(MARS_KEY, marsStem);
        context.register(VEILMERE_KEY, stem);
    }

    public static ResourceKey<LevelStem> registerLevelStemKey(String name) {
        return ResourceKey.create(Registries.LEVEL_STEM, new ResourceLocation(KBSpace.MOD_ID, name));
    }
    public static ResourceKey<Level> registerLevelKey(String name) {
        return ResourceKey.create(Registries.DIMENSION, new ResourceLocation(KBSpace.MOD_ID, name));
    }
    public static ResourceKey<DimensionType> registerDimensionTypeKey(String name) {
        return ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(KBSpace.MOD_ID, name));
    }

}
