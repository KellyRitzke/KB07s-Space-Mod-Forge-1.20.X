package net.kb07.kbspace.worldgen.biome.surface;

import net.kb07.kbspace.worldgen.biome.ModBiomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public class ModSurfaceRules {
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final SurfaceRules.RuleSource RED_SAND = makeStateRule(Blocks.RED_SAND);
    private static final SurfaceRules.RuleSource TERRACOTTA = makeStateRule(Blocks.TERRACOTTA);
    private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);


    public static SurfaceRules.RuleSource makeMoonRules() {

        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.verticalGradient("minecraft:bedrock_floor",
                        VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(5)), BEDROCK)
        );
    }

    public static SurfaceRules.RuleSource makeMarsRules() {
        SurfaceRules.ConditionSource isSurface = SurfaceRules.abovePreliminarySurface();
        SurfaceRules.ConditionSource notSteep = SurfaceRules.not(SurfaceRules.steep());

        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.MARS),
                        SurfaceRules.ifTrue(isSurface,
                                SurfaceRules.sequence(
                                        SurfaceRules.ifTrue(notSteep, RED_SAND), // Top layer of red sand
                                        SurfaceRules.ifTrue(notSteep, TERRACOTTA), // Below red sand
                                        SurfaceRules.ifTrue(notSteep, TERRACOTTA), // Another terracotta layer
                                        TERRACOTTA // Third layer of terracotta
                                )
                        )
                ),

                SurfaceRules.ifTrue(SurfaceRules.verticalGradient("minecraft:bedrock_floor",
                        VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(5)), BEDROCK)
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
