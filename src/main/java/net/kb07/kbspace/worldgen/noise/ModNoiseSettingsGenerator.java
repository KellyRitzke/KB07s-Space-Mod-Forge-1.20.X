package net.kb07.kbspace.worldgen.noise;

import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.worldgen.biome.surface.ModSurfaceRules;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;
import java.util.stream.Stream;

public class ModNoiseSettingsGenerator {
    public static final ResourceKey<NoiseGeneratorSettings> MOON = ResourceKey.create(Registries.NOISE_SETTINGS,
            new ResourceLocation(KBSpace.MOD_ID, "moon"));


    private static final ResourceKey<DensityFunction> ZERO = createKey("zero");
    private static final ResourceKey<DensityFunction> Y = createKey("y");
    private static final ResourceKey<DensityFunction> SHIFT_X = createKey("shift_x");
    private static final ResourceKey<DensityFunction> SHIFT_Z = createKey("shift_z");
    private static final ResourceKey<DensityFunction> BASE_3D_NOISE_OVERWORLD = createKey("overworld/base_3d_noise");
    private static final ResourceKey<DensityFunction> BASE_3D_NOISE_NETHER = createKey("nether/base_3d_noise");
    private static final ResourceKey<DensityFunction> BASE_3D_NOISE_END = createKey("end/base_3d_noise");
    public static final ResourceKey<DensityFunction> CONTINENTS = createKey("overworld/continents");
    public static final ResourceKey<DensityFunction> EROSION = createKey("overworld/erosion");
    public static final ResourceKey<DensityFunction> RIDGES = createKey("overworld/ridges");
    public static final ResourceKey<DensityFunction> RIDGES_FOLDED = createKey("overworld/ridges_folded");
    public static final ResourceKey<DensityFunction> OFFSET = createKey("overworld/offset");
    public static final ResourceKey<DensityFunction> FACTOR = createKey("overworld/factor");
    public static final ResourceKey<DensityFunction> JAGGEDNESS = createKey("overworld/jaggedness");
    public static final ResourceKey<DensityFunction> DEPTH = createKey("overworld/depth");
    private static final ResourceKey<DensityFunction> SLOPED_CHEESE = createKey("overworld/sloped_cheese");
    public static final ResourceKey<DensityFunction> CONTINENTS_LARGE = createKey("overworld_large_biomes/continents");
    public static final ResourceKey<DensityFunction> EROSION_LARGE = createKey("overworld_large_biomes/erosion");
    private static final ResourceKey<DensityFunction> OFFSET_LARGE = createKey("overworld_large_biomes/offset");
    private static final ResourceKey<DensityFunction> FACTOR_LARGE = createKey("overworld_large_biomes/factor");
    private static final ResourceKey<DensityFunction> JAGGEDNESS_LARGE = createKey("overworld_large_biomes/jaggedness");
    private static final ResourceKey<DensityFunction> DEPTH_LARGE = createKey("overworld_large_biomes/depth");
    private static final ResourceKey<DensityFunction> SLOPED_CHEESE_LARGE = createKey("overworld_large_biomes/sloped_cheese");
    private static final ResourceKey<DensityFunction> OFFSET_AMPLIFIED = createKey("overworld_amplified/offset");
    private static final ResourceKey<DensityFunction> FACTOR_AMPLIFIED = createKey("overworld_amplified/factor");
    private static final ResourceKey<DensityFunction> JAGGEDNESS_AMPLIFIED = createKey("overworld_amplified/jaggedness");
    private static final ResourceKey<DensityFunction> DEPTH_AMPLIFIED = createKey("overworld_amplified/depth");
    private static final ResourceKey<DensityFunction> SLOPED_CHEESE_AMPLIFIED = createKey("overworld_amplified/sloped_cheese");
    private static final ResourceKey<DensityFunction> SLOPED_CHEESE_END = createKey("end/sloped_cheese");
    private static final ResourceKey<DensityFunction> SPAGHETTI_ROUGHNESS_FUNCTION = createKey("overworld/caves/spaghetti_roughness_function");
    private static final ResourceKey<DensityFunction> ENTRANCES = createKey("overworld/caves/entrances");
    private static final ResourceKey<DensityFunction> NOODLE = createKey("overworld/caves/noodle");
    private static final ResourceKey<DensityFunction> PILLARS = createKey("overworld/caves/pillars");
    private static final ResourceKey<DensityFunction> SPAGHETTI_2D_THICKNESS_MODULATOR = createKey("overworld/caves/spaghetti_2d_thickness_modulator");
    private static final ResourceKey<DensityFunction> SPAGHETTI_2D = createKey("overworld/caves/spaghetti_2d");

    public static void bootstrap(BootstapContext<NoiseGeneratorSettings> pContext) {
        pContext.register(MOON, moon(pContext, false, false));
    }

    public static NoiseGeneratorSettings moon(BootstapContext<?> pContext, boolean pAmplified, boolean pLarge) {
        return new NoiseGeneratorSettings(NoiseSettings.create(-64, 384, 1, 2),
                Blocks.END_STONE.defaultBlockState(), Blocks.AIR.defaultBlockState(),
                the_moon(pContext.lookup(Registries.DENSITY_FUNCTION), pContext.lookup(Registries.NOISE), pLarge, pAmplified),
                ModSurfaceRules.makeRules(), List.of(), 63, true, false, false, false);
    }

    protected static NoiseRouter the_moon(HolderGetter<DensityFunction> pDensityFunctions, HolderGetter<NormalNoise.NoiseParameters> pNoiseParameters, boolean pLarge, boolean pAmplified) {
        DensityFunction densityfunction4 = getFunction(pDensityFunctions, SHIFT_X);
        DensityFunction densityfunction5 = getFunction(pDensityFunctions, SHIFT_Z);
        DensityFunction densityfunction6 = DensityFunctions.shiftedNoise2d(densityfunction4, densityfunction5, 0.25D, pNoiseParameters.getOrThrow(pLarge ? Noises.TEMPERATURE_LARGE : Noises.TEMPERATURE));
        DensityFunction densityfunction8 = getFunction(pDensityFunctions, pLarge ? FACTOR_LARGE : (pAmplified ? FACTOR_AMPLIFIED : FACTOR));
        DensityFunction densityfunction9 = getFunction(pDensityFunctions, pLarge ? DEPTH_LARGE : (pAmplified ? DEPTH_AMPLIFIED : DEPTH));
        DensityFunction densityfunction10 = noiseGradientDensity(DensityFunctions.cache2d(densityfunction8), densityfunction9);
        DensityFunction densityfunction11 = getFunction(pDensityFunctions, pLarge ? SLOPED_CHEESE_LARGE : (pAmplified ? SLOPED_CHEESE_AMPLIFIED : SLOPED_CHEESE));
        DensityFunction densityfunction12 = DensityFunctions.min(densityfunction11, DensityFunctions.mul(DensityFunctions.constant(5.0D), getFunction(pDensityFunctions, ENTRANCES)));
        DensityFunction densityfunction13 = DensityFunctions.rangeChoice(densityfunction11, -1000000.0D, 1.5625D, densityfunction12, underground(pDensityFunctions, pNoiseParameters, densityfunction11));
        DensityFunction densityfunction14 = DensityFunctions.min(postProcess(slideOverworld(pAmplified, densityfunction13)), getFunction(pDensityFunctions, NOODLE));
        return new NoiseRouter(DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                densityfunction6,
                DensityFunctions.zero(),
                getFunction(pDensityFunctions, pLarge ? CONTINENTS_LARGE : CONTINENTS),
                getFunction(pDensityFunctions, pLarge ? EROSION_LARGE : EROSION),
                densityfunction9,
                getFunction(pDensityFunctions, RIDGES),
                slideOverworld(pAmplified, DensityFunctions.add(densityfunction10, DensityFunctions.constant(-0.703125D)).clamp(-64.0D, 64.0D)),
                densityfunction14,
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero());
    }

    private static ResourceKey<DensityFunction> createKey(String pLocation) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation(pLocation));
    }

    private static DensityFunction registerAndWrap(BootstapContext<DensityFunction> pContext, ResourceKey<DensityFunction> pKey, DensityFunction pDensityFunction) {
        return new DensityFunctions.HolderHolder(pContext.register(pKey, pDensityFunction));
    }

    private static DensityFunction getFunction(HolderGetter<DensityFunction> pDensityFunctions, ResourceKey<DensityFunction> pKey) {
        return new DensityFunctions.HolderHolder(pDensityFunctions.getOrThrow(pKey));
    }

    private static DensityFunction peaksAndValleys(DensityFunction pDensityFunction) {
        return DensityFunctions.mul(DensityFunctions.add(DensityFunctions.add(pDensityFunction.abs(), DensityFunctions.constant(-0.6666666666666666D)).abs(), DensityFunctions.constant(-0.3333333333333333D)), DensityFunctions.constant(-3.0D));
    }

    public static float peaksAndValleys(float p_224436_) {
        return -(Math.abs(Math.abs(p_224436_) - 0.6666667F) - 0.33333334F) * 3.0F;
    }

    private static DensityFunction spaghettiRoughnessFunction(HolderGetter<NormalNoise.NoiseParameters> pNoiseParameters) {
        DensityFunction densityfunction = DensityFunctions.noise(pNoiseParameters.getOrThrow(Noises.SPAGHETTI_ROUGHNESS));
        DensityFunction densityfunction1 = DensityFunctions.mappedNoise(pNoiseParameters.getOrThrow(Noises.SPAGHETTI_ROUGHNESS_MODULATOR), 0.0D, -0.1D);
        return DensityFunctions.cacheOnce(DensityFunctions.mul(densityfunction1, DensityFunctions.add(densityfunction.abs(), DensityFunctions.constant(-0.4D))));
    }

    private static DensityFunction noodle(HolderGetter<DensityFunction> pDensityFunctions, HolderGetter<NormalNoise.NoiseParameters> pNoiseParameters) {
        DensityFunction densityfunction = getFunction(pDensityFunctions, Y);
        int i = -64;
        int j = -60;
        int k = 320;
        DensityFunction densityfunction1 = yLimitedInterpolatable(densityfunction, DensityFunctions.noise(pNoiseParameters.getOrThrow(Noises.NOODLE), 1.0D, 1.0D), -60, 320, -1);
        DensityFunction densityfunction2 = yLimitedInterpolatable(densityfunction, DensityFunctions.mappedNoise(pNoiseParameters.getOrThrow(Noises.NOODLE_THICKNESS), 1.0D, 1.0D, -0.05D, -0.1D), -60, 320, 0);
        double d0 = 2.6666666666666665D;
        DensityFunction densityfunction3 = yLimitedInterpolatable(densityfunction, DensityFunctions.noise(pNoiseParameters.getOrThrow(Noises.NOODLE_RIDGE_A), 2.6666666666666665D, 2.6666666666666665D), -60, 320, 0);
        DensityFunction densityfunction4 = yLimitedInterpolatable(densityfunction, DensityFunctions.noise(pNoiseParameters.getOrThrow(Noises.NOODLE_RIDGE_B), 2.6666666666666665D, 2.6666666666666665D), -60, 320, 0);
        DensityFunction densityfunction5 = DensityFunctions.mul(DensityFunctions.constant(1.5D), DensityFunctions.max(densityfunction3.abs(), densityfunction4.abs()));
        return DensityFunctions.rangeChoice(densityfunction1, -1000000.0D, 0.0D, DensityFunctions.constant(64.0D), DensityFunctions.add(densityfunction2, densityfunction5));
    }

    private static DensityFunction pillars(HolderGetter<NormalNoise.NoiseParameters> pNoiseParameters) {
        double d0 = 25.0D;
        double d1 = 0.3D;
        DensityFunction densityfunction = DensityFunctions.noise(pNoiseParameters.getOrThrow(Noises.PILLAR), 25.0D, 0.3D);
        DensityFunction densityfunction1 = DensityFunctions.mappedNoise(pNoiseParameters.getOrThrow(Noises.PILLAR_RARENESS), 0.0D, -2.0D);
        DensityFunction densityfunction2 = DensityFunctions.mappedNoise(pNoiseParameters.getOrThrow(Noises.PILLAR_THICKNESS), 0.0D, 1.1D);
        DensityFunction densityfunction3 = DensityFunctions.add(DensityFunctions.mul(densityfunction, DensityFunctions.constant(2.0D)), densityfunction1);
        return DensityFunctions.cacheOnce(DensityFunctions.mul(densityfunction3, densityfunction2.cube()));
    }

    private static DensityFunction underground(HolderGetter<DensityFunction> pDensityFunctions, HolderGetter<NormalNoise.NoiseParameters> pNoiseParameters, DensityFunction p_256658_) {
        DensityFunction densityfunction = getFunction(pDensityFunctions, SPAGHETTI_2D);
        DensityFunction densityfunction1 = getFunction(pDensityFunctions, SPAGHETTI_ROUGHNESS_FUNCTION);
        DensityFunction densityfunction2 = DensityFunctions.noise(pNoiseParameters.getOrThrow(Noises.CAVE_LAYER), 8.0D);
        DensityFunction densityfunction3 = DensityFunctions.mul(DensityFunctions.constant(4.0D), densityfunction2.square());
        DensityFunction densityfunction4 = DensityFunctions.noise(pNoiseParameters.getOrThrow(Noises.CAVE_CHEESE), 0.6666666666666666D);
        DensityFunction densityfunction5 = DensityFunctions.add(DensityFunctions.add(DensityFunctions.constant(0.27D), densityfunction4).clamp(-1.0D, 1.0D), DensityFunctions.add(DensityFunctions.constant(1.5D), DensityFunctions.mul(DensityFunctions.constant(-0.64D), p_256658_)).clamp(0.0D, 0.5D));
        DensityFunction densityfunction6 = DensityFunctions.add(densityfunction3, densityfunction5);
        DensityFunction densityfunction7 = DensityFunctions.min(DensityFunctions.min(densityfunction6, getFunction(pDensityFunctions, ENTRANCES)), DensityFunctions.add(densityfunction, densityfunction1));
        DensityFunction densityfunction8 = getFunction(pDensityFunctions, PILLARS);
        DensityFunction densityfunction9 = DensityFunctions.rangeChoice(densityfunction8, -1000000.0D, 0.03D, DensityFunctions.constant(-1000000.0D), densityfunction8);
        return DensityFunctions.max(densityfunction7, densityfunction9);
    }

    private static DensityFunction postProcess(DensityFunction pDensityFunction) {
        DensityFunction densityfunction = DensityFunctions.blendDensity(pDensityFunction);
        return DensityFunctions.mul(DensityFunctions.interpolated(densityfunction), DensityFunctions.constant(0.64D)).squeeze();
    }

    private static NoiseRouter noNewCaves(HolderGetter<DensityFunction> pDensityFunctions, HolderGetter<NormalNoise.NoiseParameters> pNoiseParameters, DensityFunction p_256378_) {
        DensityFunction densityfunction = getFunction(pDensityFunctions, SHIFT_X);
        DensityFunction densityfunction1 = getFunction(pDensityFunctions, SHIFT_Z);
        DensityFunction densityfunction2 = DensityFunctions.shiftedNoise2d(densityfunction, densityfunction1, 0.25D, pNoiseParameters.getOrThrow(Noises.TEMPERATURE));
        DensityFunction densityfunction3 = DensityFunctions.shiftedNoise2d(densityfunction, densityfunction1, 0.25D, pNoiseParameters.getOrThrow(Noises.VEGETATION));
        DensityFunction densityfunction4 = postProcess(p_256378_);
        return new NoiseRouter(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), densityfunction2, densityfunction3, DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), densityfunction4, DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());
    }

    private static DensityFunction slideOverworld(boolean pAmplified, DensityFunction pDensityFunction) {
        return slide(pDensityFunction, -64, 384, pAmplified ? 16 : 80, pAmplified ? 0 : 64, -0.078125D, 0, 24, pAmplified ? 0.4D : 0.1171875D);
    }

    private static DensityFunction slideNetherLike(HolderGetter<DensityFunction> pDensityFunctions, int pMinY, int pMaxY) {
        return slide(getFunction(pDensityFunctions, BASE_3D_NOISE_NETHER), pMinY, pMaxY, 24, 0, 0.9375D, -8, 24, 2.5D);
    }

    private static DensityFunction slideEndLike(DensityFunction pDensityFunction, int pMinY, int pMaxY) {
        return slide(pDensityFunction, pMinY, pMaxY, 72, -184, -23.4375D, 4, 32, -0.234375D);
    }

    protected static NoiseRouter nether(HolderGetter<DensityFunction> pDensityFunctions, HolderGetter<NormalNoise.NoiseParameters> pNoiseParameters) {
        return noNewCaves(pDensityFunctions, pNoiseParameters, slideNetherLike(pDensityFunctions, 0, 128));
    }

    protected static NoiseRouter caves(HolderGetter<DensityFunction> pDensityFunctions, HolderGetter<NormalNoise.NoiseParameters> pNoiseParameters) {
        return noNewCaves(pDensityFunctions, pNoiseParameters, slideNetherLike(pDensityFunctions, -64, 192));
    }

    protected static NoiseRouter floatingIslands(HolderGetter<DensityFunction> pDensityFunction, HolderGetter<NormalNoise.NoiseParameters> pNoiseParameters) {
        return noNewCaves(pDensityFunction, pNoiseParameters, slideEndLike(getFunction(pDensityFunction, BASE_3D_NOISE_END), 0, 256));
    }

    private static DensityFunction slideEnd(DensityFunction pDensityFunction) {
        return slideEndLike(pDensityFunction, 0, 128);
    }

    protected static NoiseRouter end(HolderGetter<DensityFunction> pDensityFunctions) {
        DensityFunction densityfunction = DensityFunctions.cache2d(DensityFunctions.endIslands(0L));
        DensityFunction densityfunction1 = postProcess(slideEnd(getFunction(pDensityFunctions, SLOPED_CHEESE_END)));
        return new NoiseRouter(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), densityfunction, DensityFunctions.zero(), DensityFunctions.zero(), slideEnd(DensityFunctions.add(densityfunction, DensityFunctions.constant(-0.703125D))), densityfunction1, DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());
    }

    protected static NoiseRouter none() {
        return new NoiseRouter(DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());
    }

    private static DensityFunction splineWithBlending(DensityFunction pMinFunction, DensityFunction pMaxFunction) {
        DensityFunction densityfunction = DensityFunctions.lerp(DensityFunctions.blendAlpha(), pMaxFunction, pMinFunction);
        return DensityFunctions.flatCache(DensityFunctions.cache2d(densityfunction));
    }

    private static DensityFunction noiseGradientDensity(DensityFunction pMinFunction, DensityFunction pMaxFunction) {
        DensityFunction densityfunction = DensityFunctions.mul(pMaxFunction, pMinFunction);
        return DensityFunctions.mul(DensityFunctions.constant(4.0D), densityfunction.quarterNegative());
    }

    private static DensityFunction yLimitedInterpolatable(DensityFunction p_209472_, DensityFunction p_209473_, int p_209474_, int p_209475_, int p_209476_) {
        return DensityFunctions.interpolated(DensityFunctions.rangeChoice(p_209472_, (double)p_209474_, (double)(p_209475_ + 1), p_209473_, DensityFunctions.constant((double)p_209476_)));
    }

    private static DensityFunction slide(DensityFunction pDensityFunction, int pMinY, int pMaxY, int p_224447_, int p_224448_, double p_224449_, int p_224450_, int p_224451_, double p_224452_) {
        DensityFunction densityfunction1 = DensityFunctions.yClampedGradient(pMinY + pMaxY - p_224447_, pMinY + pMaxY - p_224448_, 1.0D, 0.0D);
        DensityFunction $$9 = DensityFunctions.lerp(densityfunction1, p_224449_, pDensityFunction);
        DensityFunction densityfunction2 = DensityFunctions.yClampedGradient(pMinY + p_224450_, pMinY + p_224451_, 0.0D, 1.0D);
        return DensityFunctions.lerp(densityfunction2, p_224452_, $$9);
    }

    protected static final class QuantizedSpaghettiRarity {
        protected static double getSphaghettiRarity2D(double pValue) {
            if (pValue < -0.75D) {
                return 0.5D;
            } else if (pValue < -0.5D) {
                return 0.75D;
            } else if (pValue < 0.5D) {
                return 1.0D;
            } else {
                return pValue < 0.75D ? 2.0D : 3.0D;
            }
        }

        protected static double getSpaghettiRarity3D(double pValue) {
            if (pValue < -0.5D) {
                return 0.75D;
            } else if (pValue < 0.0D) {
                return 1.0D;
            } else {
                return pValue < 0.5D ? 1.5D : 2.0D;
            }
        }
    }

}
