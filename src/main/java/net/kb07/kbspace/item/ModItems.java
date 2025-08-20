package net.kb07.kbspace.item;

import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.item.custom.RocketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, KBSpace.MOD_ID);

    public static final RegistryObject<Item> ROCKET = ITEMS.register("rocket",
            () -> new RocketItem(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
