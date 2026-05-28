package net.night.frostbitefauna.entity.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.night.frostbitefauna.FrostbiteFauna;
import net.night.frostbitefauna.entity.custom.GreatEntity;

public class GreatModel<T extends GreatEntity> extends SinglePartEntityModel<T> {
    // Made with Blockbench 4.12.5
    // Exported for Minecraft version 1.17+ for Yarn
    // Paste this class into your mod and generate all required imports
    public static final EntityModelLayer THEGREATWANDERER = new EntityModelLayer(Identifier.of(FrostbiteFauna.MOD_ID, "thegreatwanderer"), "main");

    private final ModelPart All;
    private final ModelPart Body;
    private final ModelPart Nose;
    private final ModelPart Fur;
    private final ModelPart Tusks;
    private final ModelPart LeftT;
    private final ModelPart RightT;
    public GreatModel(ModelPart root) {
        this.All = root.getChild("All");
        this.Body = this.All.getChild("Body");
        this.Nose = this.Body.getChild("Nose");
        this.Fur = this.Body.getChild("Fur");
        this.Tusks = this.Body.getChild("Tusks");
        this.LeftT = this.Tusks.getChild("LeftT");
        this.RightT = this.Tusks.getChild("RightT");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData All = modelPartData.addChild("All", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData Body = All.addChild("Body", ModelPartBuilder.create().uv(0, 0).cuboid(-15.0F, -30.0F, -20.0F, 30.0F, 26.0F, 40.0F, new Dilation(0.0F))
                .uv(0, 96).cuboid(15.0F, -4.0F, -20.0F, 0.0F, 4.0F, 40.0F, new Dilation(0.0F))
                .uv(80, 96).cuboid(-15.0F, -4.0F, -20.0F, 0.0F, 4.0F, 40.0F, new Dilation(0.0F))
                .uv(140, 61).cuboid(-15.0F, -4.0F, -20.0F, 30.0F, 4.0F, 0.0F, new Dilation(0.0F))
                .uv(160, 94).cuboid(-15.0F, -4.0F, 20.0F, 30.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Nose = Body.addChild("Nose", ModelPartBuilder.create().uv(122, 168).cuboid(-4.0F, -3.0F, -2.0F, 8.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -10.0F, -20.0F));

        ModelPartData Fur = Body.addChild("Fur", ModelPartBuilder.create().uv(60, 140).cuboid(-15.5F, -30.5F, -20.5F, 31.0F, 31.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 66).cuboid(-15.5F, -30.5F, -20.5F, 31.0F, 0.0F, 30.0F, new Dilation(0.0F))
                .uv(0, 140).cuboid(15.5F, -30.5F, -20.5F, 0.0F, 31.0F, 30.0F, new Dilation(0.0F))
                .uv(140, 0).cuboid(-15.5F, -30.5F, -20.5F, 0.0F, 31.0F, 30.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Tusks = Body.addChild("Tusks", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, 0.0F, 0.0F));

        ModelPartData LeftT = Tusks.addChild("LeftT", ModelPartBuilder.create().uv(122, 66).cuboid(-3.0F, -5.0F, -22.0F, 6.0F, 6.0F, 22.0F, new Dilation(0.0F))
                .uv(160, 98).cuboid(-3.0F, -12.0F, -28.0F, 6.0F, 13.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, -5.0F, -20.0F));

        ModelPartData RightT = Tusks.addChild("RightT", ModelPartBuilder.create().uv(122, 140).cuboid(-3.0F, -5.0F, -23.0F, 6.0F, 6.0F, 22.0F, new Dilation(0.0F))
                .uv(160, 117).cuboid(-3.0F, -12.0F, -29.0F, 6.0F, 13.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-10.0F, -5.0F, -19.0F));
        return TexturedModelData.of(modelData, 256, 256);
    }

    @Override
    public void setAngles(GreatEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        this.animateMovement(GreatAnimations.ANIM_GREAT_WALK, limbSwing, limbSwingAmount, 9.0f, 100.0f);
        this.updateAnimation(entity.idleAnimationState, GreatAnimations.ANIM_GREAT_IDLE, ageInTicks, 1f);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        All.render(matrices, vertexConsumer, light, overlay, color);
    }

    public ModelPart getPart() {
        return All;
    }
}
