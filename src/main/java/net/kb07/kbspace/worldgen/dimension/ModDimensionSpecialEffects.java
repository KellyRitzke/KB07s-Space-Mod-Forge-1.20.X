package net.kb07.kbspace.worldgen.dimension;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.item.ModItems;
import net.minecraft.Util;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

public class ModDimensionSpecialEffects extends DimensionSpecialEffects {

    public static final ResourceKey<Registry<DimensionSpecialEffects>> DIM_SPECIAL_EFFECT = ResourceKey.createRegistryKey(new ResourceLocation("dim_special_effect"));
    protected static final BiFunction<Integer, Integer, Boolean> FOGGY_FALSE = (x, z) -> false;
    protected static final BiFunction<Integer, Integer, Boolean> FOGGY_TRUE = (x, z) -> true;

    public static final DimensionSpecialEffects MOON = new ModDimensionSpecialEffects(Float.NaN, true, SkyType.NONE, true, false, (vec, worldTime) -> {
        return vec;
    }, FOGGY_FALSE);



    protected final BiFunction<Vec3, Float, Vec3> fogColorBrightness;
    protected final BiFunction<Integer, Integer, Boolean> foggyAtPos;

    protected ModDimensionSpecialEffects(float cloudLevel, boolean hasGround, SkyType skyType, boolean brightLightmap, boolean ambientLight, BiFunction<Vec3, Float, Vec3> fogColorBrightness, BiFunction<Integer, Integer, Boolean> foggyAtPos) {
        super(cloudLevel, hasGround, skyType, brightLightmap, ambientLight);
        this.fogColorBrightness = fogColorBrightness;
        this.foggyAtPos = foggyAtPos;
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 vector, float worldTime) {
        return this.fogColorBrightness.apply(vector, worldTime);
    }

    @Override
    public boolean isFoggyAt(int x, int z) {
        return this.foggyAtPos.apply(x, z);
    }
}
