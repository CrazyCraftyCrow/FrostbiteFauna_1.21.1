package net.night.frostbitefauna;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.night.frostbitefauna.entity.ModEntities;
import net.night.frostbitefauna.entity.client.DummyModel;
import net.night.frostbitefauna.entity.client.DummyRenderer;
import net.night.frostbitefauna.entity.client.GreatModel;
import net.night.frostbitefauna.entity.client.GreatRenderer;

public class FrostbiteFaunaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        EntityModelLayerRegistry.registerModelLayer(GreatModel.THEGREATWANDERER, GreatModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.THEGREATWANDERER, GreatRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(DummyModel.DECOYDUMMY, DummyModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.DECOYDUMMY, DummyRenderer::new);
    }
}
