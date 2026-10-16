package net.kb07.kbspace.worldgen;

import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.util.ModTags;
import net.kb07.kbspace.worldgen.biome.ModBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_MOON_IRON_ORE = registerKey("add_moon_iron_ore");

    public static final ResourceKey<BiomeModifier> ADD_PURPLEHEART_TREES = registerKey("add_purpleheart_trees");

    public static final ResourceKey<BiomeModifier> SPAWN_ALLAY = registerKey("spawn_allay");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {

        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_MOON_IRON_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.MOON_DIMENSION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.MOON_IRON_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(SPAWN_ALLAY, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(ModBiomes.CYAN_PLAINS)),
                List.of(new MobSpawnSettings.SpawnerData(EntityType.ALLAY, 8, 1, 3))
        ));

        context.register(ADD_PURPLEHEART_TREES, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(ModBiomes.PURPLEHEART_FOREST)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.PURPLEHEART_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(KBSpace.MOD_ID, name));
    }
}
