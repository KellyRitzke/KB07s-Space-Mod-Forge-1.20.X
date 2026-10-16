package net.kb07.kbspace.datagen;

import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, KBSpace.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        blockWithCubeItem(ModBlocks.MOON_IRON_ORE);

        woodBlockWithItem((RotatedPillarBlock) ModBlocks.PURPLEHEART_WOOD.get());
        logBlockWithItem((RotatedPillarBlock) ModBlocks.PURPLEHEART_LOG.get());
        woodBlockWithItem((RotatedPillarBlock) ModBlocks.STRIPPED_PURPLEHEART_WOOD.get());
        logBlockWithItem((RotatedPillarBlock) ModBlocks.STRIPPED_PURPLEHEART_LOG.get());

        blockWithItem(ModBlocks.PURPLEHEART_PLANKS);

        leavesBlock(ModBlocks.PURPLEHEART_LEAVES);
        saplingBlock(ModBlocks.PURPLEHEART_SAPLING);

    }

    private void blockWithCubeItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void saplingBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(), models().cross(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void leavesBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), models().singleTexture(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(), new ResourceLocation("minecraft:block/leaves"),
                "all", blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void blockWithItem(RegistryObject<Block> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void woodBlockWithItem(RotatedPillarBlock block) {
        ResourceLocation rl = replace(blockTexture(block), "wood", "log");
        axisBlock(block, rl, rl);
        simpleBlockItem(block, models().cubeColumn(block.getName().getString(), rl, rl));
    }

    private void logBlockWithItem(RotatedPillarBlock block) {
        logBlock(block);
        simpleBlockItem(block, models().cubeColumn(block.getName().getString(), blockTexture(block), extend(blockTexture(block), "_top")));
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }

    private ResourceLocation replace(ResourceLocation rl, String replacing, String replacement) {
        String nrl = rl.getPath().replaceAll(replacing, replacement);
        return new ResourceLocation(rl.getNamespace(), nrl);
    }

}
