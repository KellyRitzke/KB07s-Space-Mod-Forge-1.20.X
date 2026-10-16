package net.kb07.kbspace.datagen;

import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, KBSpace.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.STRIPPED_PURPLEHEART_WOOD.get())
                .add(ModBlocks.STRIPPED_PURPLEHEART_LOG.get())
                .add(ModBlocks.PURPLEHEART_WOOD.get())
                .add(ModBlocks.PURPLEHEART_LOG.get());

        tag(BlockTags.LOGS)
                .add(ModBlocks.STRIPPED_PURPLEHEART_WOOD.get())
                .add(ModBlocks.STRIPPED_PURPLEHEART_LOG.get())
                .add(ModBlocks.PURPLEHEART_WOOD.get())
                .add(ModBlocks.PURPLEHEART_LOG.get());

        tag(BlockTags.PLANKS)
                .add(ModBlocks.PURPLEHEART_PLANKS.get());

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.STRIPPED_PURPLEHEART_WOOD.get())
                .add(ModBlocks.STRIPPED_PURPLEHEART_LOG.get())
                .add(ModBlocks.PURPLEHEART_WOOD.get())
                .add(ModBlocks.PURPLEHEART_LOG.get())
                .add(ModBlocks.PURPLEHEART_PLANKS.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.MOON_IRON_ORE.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.MOON_IRON_ORE.get());

    }
}
