package net.kb07.kbspace.item;

import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KBSpace.MOD_ID);

    public static final RegistryObject<CreativeModeTab> KB_SPACE_TAB = CREATIVE_MODE_TABS.register("kb_space_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.MOON_IRON_ORE.get()))
                    .title(Component.translatable("creativetab.kb_space_tab"))
                    .displayItems((pParameters, pOutput) -> {

                        pOutput.accept(ModBlocks.MOON_IRON_ORE.get());
                        pOutput.accept(ModBlocks.MOON_PORTAL.get());

                    }).build());

    public static void register(IEventBus eventBus) { CREATIVE_MODE_TABS.register(eventBus); }
}
