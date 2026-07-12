package net.night.frostbitefauna.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.night.frostbitefauna.FrostbiteFauna;
import net.night.frostbitefauna.block.custom.IceGrassBlock;

public class ModBlocks {

    public static final Block GREAT_WOOL = registerBlock("great_wool",
            new Block(AbstractBlock.Settings.create().strength(1f).burnable().sounds(BlockSoundGroup.WOOL)));
    public static final Block GREAT_WOOL_SLAB = registerBlock("great_wool_slab",
            new SlabBlock(AbstractBlock.Settings.create().strength(1f).burnable().sounds(BlockSoundGroup.WOOL)));
    public static final Block ICE_GRASS = registerBlock("ice_grass",
            new IceGrassBlock(AbstractBlock.Settings.copy(Blocks.TALL_GRASS)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GLASS)
            ));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(FrostbiteFauna.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(FrostbiteFauna.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        FrostbiteFauna.LOGGER.info("Registering Mod Blocks for " + FrostbiteFauna.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(GREAT_WOOL);
            entries.add(GREAT_WOOL_SLAB);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(ICE_GRASS);
        });
    }
}