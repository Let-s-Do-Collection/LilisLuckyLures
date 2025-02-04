package net.satisfy.lilis_lucky_lures.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;
import org.joml.Matrix4f;

public class AnglersHatModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new LilisLuckyLuresIdentifier("anglers_hat"), "main");
    private final ModelPart anglersHat;

    public AnglersHatModel(ModelPart root) {
        this.anglersHat = root.getChild("anglersHat");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition anglersHat = partdefinition.addOrReplaceChild("anglersHat", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(-14, 12).addBox(-7.0F, -5.0F, -7.0F, 14.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 23.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        Matrix4f scaleMatrix = new Matrix4f().scaling(1.05F, 1.05F, 1.05F);
        poseStack.mulPoseMatrix(scaleMatrix);
        anglersHat.render(poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {
    }

    public void copyHead(ModelPart model) {
        anglersHat.copyFrom(model);
    }
}
