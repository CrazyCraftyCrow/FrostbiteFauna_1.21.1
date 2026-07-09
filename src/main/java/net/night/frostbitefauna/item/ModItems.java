package net.night.frostbitefauna.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.night.frostbitefauna.FrostbiteFauna;
import net.night.frostbitefauna.block.ModBlocks;
import net.night.frostbitefauna.entity.ModEntities;
import net.night.frostbitefauna.item.custom.GreatItem;

public class ModItems {
    public static final Item GREAT_TUSK_FRAGMENT = registerItem("great_tusk_fragment", new Item(new Item.Settings()));
    public static final Item GREAT_FUR = registerItem("great_fur", new Item(new Item.Settings()));
    public static final Item GREAT_TOOL = registerItem("great_tool",
            new GreatItem(ToolMaterials.IRON, new Item.Settings()
                    .attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.IRON, 2f, -2.8f))));

    public static final Item GREAT_SPAWN_EGG = registerItem("great_spawn_egg",
            new SpawnEggItem(ModEntities.THEGREATWANDERER, 0x9dc783,0xbfaf5f, new Item.Settings()));


    private  static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(FrostbiteFauna.MOD_ID, name), item);

    }

    public static void registerModItems() {
        FrostbiteFauna.LOGGER.info("Registering Mod Items for " + FrostbiteFauna.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(GREAT_TUSK_FRAGMENT);
            entries.add(GREAT_FUR);
            entries.add(GREAT_TOOL);
            entries.add(GREAT_SPAWN_EGG);
        });
    }
}
