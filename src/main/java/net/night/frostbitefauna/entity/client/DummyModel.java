package net.night.frostbitefauna.entity.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.night.frostbitefauna.FrostbiteFauna;
import net.night.frostbitefauna.entity.custom.DecoyDummyEntity;

public class DummyModel<T extends DecoyDummyEntity> extends SinglePartEntityModel<T> {
    public static final EntityModelLayer DECOYDUMMY = new EntityModelLayer(Identifier.of(FrostbiteFauna.MOD_ID, "decoydummy"), "main");

    private final ModelPart root;
    private final ModelPart Head;
    private final ModelPart Forehead;
    private final ModelPart Chin;
    private final ModelPart Beard;
    private final ModelPart Body;
    private final ModelPart Neck;
    private final ModelPart Arms;
    private final ModelPart R_Arm;
    private final ModelPart L_Arm;
    private final ModelPart Stand;

    public DummyModel(ModelPart root) {
        this.root = root.getChild("root");
        this.Head = this.root.getChild("Head");
        this.Forehead = this.Head.getChild("Forehead");
        this.Chin = this.Head.getChild("Chin");
        this.Beard = this.Head.getChild("Beard");
        this.Body = this.root.getChild("Body");
        this.Neck = this.Body.getChild("Neck");
        this.Arms = this.Body.getChild("Arms");
        this.R_Arm = this.Arms.getChild("R_Arm");
        this.L_Arm = this.Arms.getChild("L_Arm");
        this.Stand = this.root.getChild("Stand");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 25.0F, 0.0F));

        ModelPartData Head = root.addChild("Head", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, -24.0F, 1.0F));

        ModelPartData Forehead = Head.addChild("Forehead", ModelPartBuilder.create().uv(42, 0).cuboid(-6.0F, -2.0F, 15.0F, 6.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, -3.0F, -18.0F));

        ModelPartData Chin = Head.addChild("Chin", ModelPartBuilder.create().uv(28, 28).cuboid(-6.5F, -3.5F, 13.5F, 7.0F, 3.0F, 7.0F, new Dilation(0.0F))
                .uv(22, 39).cuboid(-5.0F, -4.0F, 14.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
                .uv(24, 39).cuboid(-2.0F, -4.0F, 14.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, 2.0F, -18.0F));

        ModelPartData Beard = Head.addChild("Beard", ModelPartBuilder.create().uv(28, 38).cuboid(-6.5F, 1.5F, 13.5F, 7.0F, 20.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 39).cuboid(-6.5F, 1.5F, 20.5F, 7.0F, 20.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 12).cuboid(-6.5F, 1.5F, 13.5F, 0.0F, 20.0F, 7.0F, new Dilation(0.0F))
                .uv(14, 12).cuboid(0.5F, 1.5F, 13.5F, 0.0F, 20.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, 0.0F, -18.0F));

        ModelPartData Body = root.addChild("Body", ModelPartBuilder.create().uv(28, 12).cuboid(-7.0F, 3.0F, 5.0F, 6.0F, 10.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, -24.0F, -8.0F));

        ModelPartData Neck = Body.addChild("Neck", ModelPartBuilder.create().uv(14, 50).cuboid(-5.0F, 1.0F, 7.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Arms = Body.addChild("Arms", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData R_Arm = Arms.addChild("R_Arm", ModelPartBuilder.create().uv(42, 38).cuboid(-9.0F, 2.0F, 7.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData L_Arm = Arms.addChild("L_Arm", ModelPartBuilder.create().uv(42, 49).cuboid(-1.0F, 2.0F, 7.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Stand = root.addChild("Stand", ModelPartBuilder.create().uv(14, 39).cuboid(-5.0F, 13.0F, 7.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-9.0F, 22.0F, 3.0F, 10.0F, 1.0F, 11.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, -24.0F, -8.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
    @Override
    public void setAngles(DecoyDummyEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        root.render(matrices, vertexConsumer, light, overlay, color);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }
}
