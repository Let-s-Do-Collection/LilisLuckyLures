package net.satisfy.lilis_lucky_lures.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;

public class SpearModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new LilisLuckyLuresIdentifier("thrown_spear"), "main");
    private final ModelPart spear;

    public SpearModel(ModelPart modelPart) {
        super(RenderType::entitySolid);
        this.spear = modelPart;
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition spear = partdefinition.addOrReplaceChild("spear", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -6.0F, -1.5F, 24.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition cube_r1 = spear.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-9, 2).addBox(-8.0F, -0.5F, -1.0F, 9.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -5.0F, -6.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r2 = spear.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 2).addBox(-8.0F, -9.0F, -1.0F, 9.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, float f, float g, float h, float k) {
        this.spear.render(poseStack, vertexConsumer, i, j, f, g, h, k);
    }
}