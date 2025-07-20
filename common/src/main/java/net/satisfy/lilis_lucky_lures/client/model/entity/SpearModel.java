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
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(LilisLuckyLuresIdentifier.identifier("thrown_spear"), "main");
    private final ModelPart spear;

    public SpearModel(ModelPart modelPart) {
        super(RenderType::entitySolid);
        this.spear = modelPart;
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition spear = partdefinition.addOrReplaceChild("spear",
                CubeListBuilder.create()
                        .texOffs(0, 0).mirror()
                        .addBox(-1.0F, -11.0F, -1.0F, 24.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .mirror(false)
                        .texOffs(-5, 2)
                        .addBox(19.0F, -10.5F, -3.0F, 7.0F, 0.0F, 5.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 2)
                        .addBox(19.0F, -13.0F, -0.5F, 7.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-11.0F, 24.5F, 0.5F)
        );

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, int k) {
        this.spear.render(poseStack, vertexConsumer, i, j, k);
    }
}
