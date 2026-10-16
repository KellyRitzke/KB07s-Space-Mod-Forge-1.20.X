package net.kb07.kbspace.datagen;

import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, KBSpace.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        tag(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.STRIPPED_PURPLEHEART_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_PURPLEHEART_LOG.get().asItem())
                .add(ModBlocks.PURPLEHEART_WOOD.get().asItem())
                .add(ModBlocks.PURPLEHEART_LOG.get().asItem());

        tag(ItemTags.PLANKS)
                .add(ModBlocks.PURPLEHEART_PLANKS.get().asItem());

        tag(ItemTags.SAPLINGS)
                .add(ModBlocks.PURPLEHEART_SAPLING.get().asItem());

    }
}
