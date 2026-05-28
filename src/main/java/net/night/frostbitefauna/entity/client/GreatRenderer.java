package net.night.frostbitefauna.entity.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.night.frostbitefauna.FrostbiteFauna;
import net.night.frostbitefauna.entity.custom.GreatEntity;

public class GreatRenderer extends MobEntityRenderer<GreatEntity, GreatModel<GreatEntity>> {

    public GreatRenderer(EntityRendererFactory.Context context) {
        super(context, new GreatModel<>(context.getPart(GreatModel.THEGREATWANDERER)), 0.75f);
    }

    /*@Override
    public Identifier getTexture(GreatEntity entity) {
        return Identifier.of(FrostbiteFauna.MOD_ID, "textures/entity/thegreatwanderer/thegreatwanderer.png");
    }*/

    @Override
    public Identifier getTexture(GreatEntity entity) {
        if (entity.isSheared()) {
            return Identifier.of(FrostbiteFauna.MOD_ID, "textures/entity/thegreatwanderer/thegreatwanderer_sheared.png");
        }
        return Identifier.of(FrostbiteFauna.MOD_ID, "textures/entity/thegreatwanderer/thegreatwanderer.png");
    }

    @Override
    public void render(GreatEntity livingEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        if(livingEntity.isBaby()) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }

        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
