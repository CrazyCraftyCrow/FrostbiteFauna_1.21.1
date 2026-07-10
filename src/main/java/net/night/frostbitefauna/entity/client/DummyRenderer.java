package net.night.frostbitefauna.entity.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.night.frostbitefauna.FrostbiteFauna;
import net.night.frostbitefauna.entity.custom.DecoyDummyEntity;
import net.night.frostbitefauna.entity.custom.GreatEntity;

public class DummyRenderer extends MobEntityRenderer<DecoyDummyEntity, DummyModel<DecoyDummyEntity>> {
    public DummyRenderer(EntityRendererFactory.Context context) {
        super(context, new DummyModel<>(context.getPart(DummyModel.DECOYDUMMY)), 0.75f);
    }

    @Override
    public Identifier getTexture(DecoyDummyEntity entity) {
        return Identifier.of(FrostbiteFauna.MOD_ID, "textures/entity/decoydummy/decoydummy.png");
    }

    @Override
    public void render(DecoyDummyEntity livingEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        if(livingEntity.isBaby()) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }

        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
