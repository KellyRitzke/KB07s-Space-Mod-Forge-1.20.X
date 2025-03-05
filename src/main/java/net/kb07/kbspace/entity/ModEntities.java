package net.kb07.kbspace.entity;

import net.kb07.kbspace.KBSpace;
import net.kb07.kbspace.entity.custom.RocketEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, KBSpace.MOD_ID);

    public static RegistryObject<EntityType<RocketEntity>> ROCKET =
            ENTITY_TYPES.register("rocket", () -> EntityType.Builder.of(RocketEntity::new, MobCategory.MISC)
                    .sized(1.0f, 2.5f)
                    .build(new ResourceLocation(KBSpace.MOD_ID, "rocket").getPath()));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
