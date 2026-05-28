package net.night.frostbitefauna.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
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
    }
}