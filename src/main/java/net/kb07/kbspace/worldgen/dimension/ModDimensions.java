package net.kb07.kbspace.worldgen.dimension;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.worldgen.biome.ModBiomes;
import net.kb07.kbspace.worldgen.noise.ModNoiseSettingsGenerator;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.NoiseData;
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
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;

import java.util.HashMap;
import java.util.List;
import java.util.OptionalLong;

public class ModDimensions {
    public static final ResourceKey<LevelStem> MOON_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            new ResourceLocation(KBSpace.MOD_ID, "the_moon"));
    public static final ResourceKey<Level> MOON_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(KBSpace.MOD_ID, "the_moon"));
    public static final ResourceKey<DimensionType> MOON_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(KBSpace.MOD_ID, "the_moon"));

    public static void bootstrapType(BootstapContext<DimensionType> context) {

        context.register(MOON_TYPE, new DimensionType(
                OptionalLong.of(6000),
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
                BuiltinDimensionTypes.END_EFFECTS,
                1.0f,
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));

    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator wrappedChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(ModBiomes.MOON)),
                noiseGenSettings.getOrThrow(ModNoiseSettingsGenerator.MOON));

        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(Pair.of(
                                    Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.MOON)),
                             Pair.of(
                                     Climate.parameters(0.1F, 0.2F, 0.0F, 0.2F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.BIRCH_FOREST)),
                             Pair.of(
                                     Climate.parameters(0.3F, 0.6F, 0.1F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.OCEAN)),
                             Pair.of(
                                     Climate.parameters(0.4F, 0.3F, 0.2F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.DARK_FOREST))
                        ))),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.MOON_TYPE), wrappedChunkGenerator);

        context.register(MOON_KEY, stem);
    }

}
