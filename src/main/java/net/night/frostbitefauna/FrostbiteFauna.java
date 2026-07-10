package net.night.frostbitefauna;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.night.frostbitefauna.block.ModBlocks;
import net.night.frostbitefauna.entity.ModEntities;
import net.night.frostbitefauna.entity.custom.DecoyDummyEntity;
import net.night.frostbitefauna.entity.custom.GreatEntity;
import net.night.frostbitefauna.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrostbiteFauna implements ModInitializer {
	public static final String MOD_ID = "frostbitefauna";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ModEntities.registerModEntities();
		FabricDefaultAttributeRegistry.register(ModEntities.THEGREATWANDERER, GreatEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.DECOYDUMMY, DecoyDummyEntity.createAttributes());
	}
}