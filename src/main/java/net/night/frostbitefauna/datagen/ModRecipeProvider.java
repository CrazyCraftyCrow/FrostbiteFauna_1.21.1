package net.night.frostbitefauna.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.night.frostbitefauna.block.ModBlocks;
import net.night.frostbitefauna.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.GREAT_TOOL)
                .pattern(" RR")
                .pattern(" SR")
                .pattern("S  ")
                .input('R',ModItems.GREAT_TUSK_FRAGMENT)
                .input('S',Items.STICK)
                .criterion(hasItem(ModItems.GREAT_TUSK_FRAGMENT), conditionsFromItem(ModItems.GREAT_TUSK_FRAGMENT))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.DECOY_DUMMY_SPAWN_ITEM)
                .pattern(" R ")
                .pattern("ROR")
                .pattern(" S ")
                .input('R',ModItems.GREAT_FUR)
                .input('O',Items.OAK_PLANKS)
                .input('S',Items.STICK)
                .criterion(hasItem(ModItems.GREAT_FUR), conditionsFromItem(ModItems.GREAT_FUR))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.GREAT_WOOL)
                .pattern(" RR")
                .pattern(" RR")
                .pattern("   ")
                .input('R',ModItems.GREAT_FUR)
                .criterion(hasItem(ModItems.GREAT_FUR), conditionsFromItem(ModItems.GREAT_FUR))
                .offerTo(exporter);
    }

}