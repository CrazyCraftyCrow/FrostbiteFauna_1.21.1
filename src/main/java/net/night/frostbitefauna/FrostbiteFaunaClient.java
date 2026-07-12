package net.night.frostbitefauna;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.night.frostbitefauna.block.ModBlocks;
import net.night.frostbitefauna.entity.ModEntities;
import net.night.frostbitefauna.entity.client.*;

public class FrostbiteFaunaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ICE_GRASS, RenderLayer.getCutout());

        EntityModelLayerRegistry.registerModelLayer(GreatModel.THEGREATWANDERER, GreatModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.THEGREATWANDERER, GreatRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(DummyModel.DECOYDUMMY, DummyModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.DECOYDUMMY, DummyRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(FrozenZombieModel.FROZEN_ZOMBIE, FrozenZombieModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.FROZEN_ZOMBIE, FrozenZombieRenderer::new);
    }
}
