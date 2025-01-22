package net.satisfy.lilis_lucky_lures.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.satisfy.lilis_lucky_lures.core.entity.FloatingDebrisEntity;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;

public class FloatingDebrisModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new LilisLuckyLuresIdentifier("floating_debris"), "main");
    private final ModelPart button;
    private final ModelPart planks;
    private final ModelPart barrel;

    @SuppressWarnings("unused")
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition button = partdefinition.addOrReplaceChild("button", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition button_r1 = button.addOrReplaceChild("button_r1", CubeListBuilder.create().texOffs(24, 7).addBox(-3.0F, -4.0F, -1.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.0F, 3.0F, 6.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition planks = partdefinition.addOrReplaceChild("planks", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition planks_r1 = planks.addOrReplaceChild("planks_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, -2.0F, -1.0F, 7.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.0F, 1.0F, -15.0F, 0.0F, 0.7854F, 0.0F));
        PartDefinition barrel = partdefinition.addOrReplaceChild("barrel", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition barrel_r1 = barrel.addOrReplaceChild("barrel_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 16.0F, 3.0F, 0.7854F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public FloatingDebrisModel(ModelPart root) {
        this.button = root.getChild("button");
        this.planks = root.getChild("planks");
        this.barrel = root.getChild("barrel");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof FloatingDebrisEntity) {
            float buttonOscillation = (float) Math.sin(ageInTicks * 0.1) * 0.3F;
            button.setPos(0.0F, 24.0F + buttonOscillation, 0.0F);
            button.setRotation(0.0F, (float) Math.sin(ageInTicks * 0.05) * 0.1F, 0.0F);

            float planksOscillation = (float) Math.sin(ageInTicks * 0.0125 + Math.sin(ageInTicks * 0.02) * 2.0F) * 0.4F;
            planks.setPos(0.0F, 24.0F + planksOscillation, 0.0F);
            planks.setRotation(
                    (float) Math.sin(ageInTicks * 0.03) * 0.15F,
                    (float) Math.cos(ageInTicks * 0.05) * 0.2F,
                    0.0F
            );

            float barrelOscillation = (float) Math.sin(ageInTicks * 0.05 + 1.0F) * 0.3F;
            barrel.setPos(0.0F, 24.0F + barrelOscillation, 0.0F);
            barrel.setRotation(
                    (float) Math.sin(ageInTicks * 0.05) * 0.08F,
                    (float) Math.sin(ageInTicks * 0.03) * 0.08F,
                    (float) Math.cos(ageInTicks * 0.02) * 0.08F
            );
        }
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        button.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        planks.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        barrel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
