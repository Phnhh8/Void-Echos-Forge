package com.tenban.voidechoes;

import com.tenban.voidechoes.init.ModEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(VoidEchoesMod.MODID)
public class VoidEchoesMod {
    public static final String MODID = "void_echoes";

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<Item> HEROBRINE_SPAWN_EGG = ITEMS.register("herobrine_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.HEROBRINE, 0x00AAAA, 0xFFFFFF, new Item.Properties()));

    public static final RegistryObject<Item> NULL_SPAWN_EGG = ITEMS.register("null_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.NULL_ENTITY, 0x000000, 0x1A1A1A, new Item.Properties()));

    public static final RegistryObject<Item> ENTITY_404_SPAWN_EGG = ITEMS.register("entity_404_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ENTITY_404, 0xFFFFFF, 0x00FFAA, new Item.Properties()));

    public static final RegistryObject<CreativeModeTab> HORROR_TAB = CREATIVE_MODE_TABS.register("horror_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.literal("Echoes of the Void"))
                    .icon(() -> new ItemStack(Items.WITHER_SKELETON_SKULL))
                    .displayItems((parameters, output) -> {
                        output.accept(HEROBRINE_SPAWN_EGG.get());
                        output.accept(NULL_SPAWN_EGG.get());
                        output.accept(ENTITY_404_SPAWN_EGG.get());
                    })
                    .build());

    public VoidEchoesMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.ENTITIES.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::registerAttributes);
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.HEROBRINE.get(), ModEntities.createHorrorAttributes().build());
        event.put(ModEntities.NULL_ENTITY.get(), ModEntities.createHorrorAttributes().build());
        event.put(ModEntities.ENTITY_404.get(), ModEntities.createHorrorAttributes().build());
    }
}
