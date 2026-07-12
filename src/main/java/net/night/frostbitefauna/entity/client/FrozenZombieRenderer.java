package net.night.frostbitefauna.entity.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.night.frostbitefauna.FrostbiteFauna;
import net.night.frostbitefauna.entity.custom.FrozenZombieEntity;

public class FrozenZombieRenderer extends MobEntityRenderer<FrozenZombieEntity, FrozenZombieModel<FrozenZombieEntity>> {

    public FrozenZombieRenderer(EntityRendererFactory.Context context) {
        super(context, new FrozenZombieModel<>(context.getPart(FrozenZombieModel.FROZEN_ZOMBIE)), 0.5f);
    }

    @Override
    public Identifier getTexture(FrozenZombieEntity entity) {
        // Points directly to your custom texture map sheet
        return Identifier.of(FrostbiteFauna.MOD_ID, "textures/entity/frozen_zombie/frozen_zombie.png");
    }
}
