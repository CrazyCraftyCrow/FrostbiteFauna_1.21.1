package net.night.frostbitefauna.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.util.Identifier;
import net.night.frostbitefauna.block.ModBlocks;
import net.night.frostbitefauna.item.ModItems;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateModelGenerator.BlockTexturePool greatWoolPool = blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.GREAT_WOOL);

        greatWoolPool.slab(ModBlocks.GREAT_WOOL_SLAB);

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.GREAT_TUSK_FRAGMENT, Models.GENERATED);
        itemModelGenerator.register(ModItems.GREAT_FUR, Models.GENERATED);
        itemModelGenerator.register(ModItems.GREAT_TOOL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.GREAT_SPAWN_EGG,
                new Model(Optional.of(Identifier.of("item/template_spawn_egg")), Optional.empty()));
    }
}
