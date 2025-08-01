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
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(LilisLuckyLuresIdentifier.identifier("floating_debris"), "main");
    private final ModelPart button;
    private final ModelPart planks;
    private final ModelPart barrel;

    private final float baseYButton;
    private final float baseYPlanks;
    private final float baseYBarrel;

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition button = partdefinition.addOrReplaceChild("button", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        button.addOrReplaceChild("button_r1", CubeListBuilder.create().texOffs(24, 7).addBox(-3.0F, 1.0F, -1.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.0F, 3.0F, 6.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition planks = partdefinition.addOrReplaceChild("planks", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        planks.addOrReplaceChild("planks_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, 3.0F, -1.0F, 7.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.0F, 1.0F, -15.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition barrel = partdefinition.addOrReplaceChild("barrel", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        barrel.addOrReplaceChild("barrel_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-15.0F, -11.0F, -1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 11.0F, 3.0F, 0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public FloatingDebrisModel(ModelPart root) {
        this.button = root.getChild("button");
        this.planks = root.getChild("planks");
        this.barrel = root.getChild("barrel");

        this.baseYButton = this.button.y;
        this.baseYPlanks = this.planks.y;
        this.baseYBarrel = this.barrel.y;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof FloatingDebrisEntity debrisEntity) {

            float timeFactor = ageInTicks * 0.1F;

            float buttonOscillation = (float) Math.sin(timeFactor + 0.5F) * 0.25F;
            float planksOscillation = (float) Math.sin(timeFactor + 0.3F) * 0.35F;
            float barrelOscillation = (float) Math.sin(timeFactor + 0.7F) * 0.3F;

            float buttonRotation = (float) Math.cos(timeFactor * 0.8F) * 0.05F;
            float planksRotation = (float) Math.sin(timeFactor * 0.7F) * 0.07F;
            float barrelRotation = (float) Math.cos(timeFactor * 0.6F) * 0.06F;

            float sideDriftX = (float) Math.sin(timeFactor * 0.4F) * 0.2F;
            float sideDriftZ = (float) Math.cos(timeFactor * 0.4F) * 0.2F;

            button.setPos(sideDriftX, baseYButton + buttonOscillation, sideDriftZ);
            planks.setPos(-sideDriftX, baseYPlanks + planksOscillation, -sideDriftZ);
            barrel.setPos(sideDriftZ, baseYBarrel + barrelOscillation, -sideDriftX);

            button.xRot = buttonRotation;
            planks.zRot = planksRotation;
            barrel.xRot = barrelRotation;

            if (debrisEntity.getHurtTime() > 0) {
                int hurtTime = debrisEntity.getHurtTime();
                int maxHurtTime = 10;
                float hurtProgress = 1.0F - (hurtTime / (float) maxHurtTime);
                float offset = (float) Math.sin(hurtProgress * 2.0F * Math.PI) * 0.5F;

                float explosionOffsetX = (float) Math.sin(hurtProgress * Math.PI * 2.0F) * 0.5F;
                float explosionOffsetZ = (float) Math.cos(hurtProgress * Math.PI * 2.0F) * 0.5F;

                button.setPos(explosionOffsetX, baseYButton + offset, explosionOffsetZ);
                planks.setPos(-explosionOffsetX, baseYPlanks + offset, -explosionOffsetZ);
                barrel.setPos(explosionOffsetZ, baseYBarrel + offset, explosionOffsetX);
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int red) {
        button.render(poseStack, vertexConsumer, packedLight, packedOverlay, red);
        planks.render(poseStack, vertexConsumer, packedLight, packedOverlay, red);
        barrel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red);
    }
}
