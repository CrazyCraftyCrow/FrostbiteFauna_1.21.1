package net.night.frostbitefauna.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BedItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.night.frostbitefauna.FrostbiteFauna;
import net.night.frostbitefauna.block.custom.GreatBedBlock;

public class ModBlocks {

    public static final Block GREAT_WOOL = registerBlock("great_wool",
            new Block(AbstractBlock.Settings.create().strength(1f).burnable().sounds(BlockSoundGroup.WOOL)));
    public static final Block GREAT_WOOL_SLAB = registerBlock("great_wool_slab",
            new SlabBlock(AbstractBlock.Settings.create().strength(1f).burnable().sounds(BlockSoundGroup.WOOL)));

    // 1. Define your unique Great Bed Block here
    public static final Block GREAT_BED = registerBed("great_bed",
            new GreatBedBlock(DyeColor.BLUE, AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLUE)
                    .strength(0.2F)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(FrostbiteFauna.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(FrostbiteFauna.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    // 2. Specialized helper to register the Bed block AND the custom BedItem in one go
    private static Block registerBed(String name, Block bedBlock) {
        // Safe cast check to confirm it's using your unique biome logic class
        if (bedBlock instanceof GreatBedBlock) {
            Registry.register(Registries.ITEM, Identifier.of(FrostbiteFauna.MOD_ID, name),
                    new BedItem(bedBlock, new Item.Settings()));
        }
        return Registry.register(Registries.BLOCK, Identifier.of(FrostbiteFauna.MOD_ID, name), bedBlock);
    }

    public static void registerModBlocks() {
        FrostbiteFauna.LOGGER.info("Registering Mod Blocks for " + FrostbiteFauna.MOD_ID);

        // ZERO MIXINS REQUIRED: Java Reflection bypasses the hidden field access limits
        try {
            // 1. Get the private "blocks" field inside the BlockEntityType class
            java.lang.reflect.Field blocksField = net.minecraft.block.entity.BlockEntityType.class.getDeclaredField("blocks");

            // 2. Break Java's restriction gate so we can read/write to it
            blocksField.setAccessible(true);

            // 3. Grab the existing unmodifiable Set from the BED entity type
            java.util.Set<Block> vanillaBlocks = (java.util.Set<Block>) blocksField.get(net.minecraft.block.entity.BlockEntityType.BED);

            // 4. Copy it into a editable HashSet and insert your Great Bed
            java.util.Set<Block> mutableBlocks = new java.util.HashSet<>(vanillaBlocks);
            mutableBlocks.add(GREAT_BED);

            // 5. Overwrite the vanilla private set variable with your updated one
            blocksField.set(net.minecraft.block.entity.BlockEntityType.BED, mutableBlocks);


        } catch (Exception e) {
            FrostbiteFauna.LOGGER.error("Critical Error: Reflection failed to update Bed BlockEntity mappings!", e);
        }



        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(GREAT_WOOL);
            entries.add(GREAT_WOOL_SLAB);
            entries.add(GREAT_BED);
        });
    }
}