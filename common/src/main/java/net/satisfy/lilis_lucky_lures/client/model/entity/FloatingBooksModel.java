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

public class FloatingBooksModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new LilisLuckyLuresIdentifier("floating_books"), "main");
    private final ModelPart book_1;
    private final ModelPart book_2;
    private final ModelPart lectern;

    private final float baseYBook_1;
    private final float baseYBook_2;
    private final float baseYLectern;

    @SuppressWarnings("unused")
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition book_1 = partdefinition.addOrReplaceChild("book_1", CubeListBuilder.create(), PartPose.offset(0.0F, 30.0F, 0.0F));

        PartDefinition book_2_r1 = book_1.addOrReplaceChild("book_2_r1", CubeListBuilder.create().texOffs(0, 20).addBox(-8.0F, -3.0F, -1.0F, 9.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 1.0F, -19.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition book_2 = partdefinition.addOrReplaceChild("book_2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 30.0F, 15.0F, 0.0F, 0.6109F, 0.0F));

        PartDefinition book_2_r2 = book_2.addOrReplaceChild("book_2_r2", CubeListBuilder.create().texOffs(0, 33).addBox(-8.0F, -3.0F, -1.0F, 9.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 1.0F, -3.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition lectern = partdefinition.addOrReplaceChild("lectern", CubeListBuilder.create(), PartPose.offset(0.0F, 30.0F, 0.0F));

        PartDefinition lectern_r1 = lectern.addOrReplaceChild("lectern_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-15.0F, -4.0F, -1.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 1.0F, -1.0F, 0.0F, -0.3927F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public FloatingBooksModel(ModelPart root) {
        this.book_1 = root.getChild("book_1");
        this.book_2 = root.getChild("book_2");
        this.lectern = root.getChild("lectern");

        this.baseYBook_1 = this.book_1.y;
        this.baseYBook_2 = this.book_2.y;
        this.baseYLectern = this.lectern.y;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity instanceof FloatingDebrisEntity debrisEntity) {

            float timeFactor = ageInTicks * 0.1F;

            float book_1Oscillation = (float) Math.sin(timeFactor + 0.5F) * 0.25F;
            float book_2Oscillation = (float) Math.sin(timeFactor + 0.3F) * 0.35F;
            float lecternOscillation = (float) Math.sin(timeFactor + 0.7F) * 0.3F;

            float book_1Rotation = (float) Math.cos(timeFactor * 0.8F) * 0.05F;
            float book_2Rotation = (float) Math.sin(timeFactor * 0.7F) * 0.07F;
            float lecternRotation = (float) Math.cos(timeFactor * 0.6F) * 0.06F;

            float sideDriftX = (float) Math.sin(timeFactor * 0.4F) * 0.2F;
            float sideDriftZ = (float) Math.cos(timeFactor * 0.4F) * 0.2F;

            book_1.setPos(sideDriftX, baseYBook_1 + book_1Oscillation, sideDriftZ);
            book_2.setPos(-sideDriftX, baseYBook_2 + book_2Oscillation, -sideDriftZ);
            lectern.setPos(sideDriftZ, baseYLectern + lecternOscillation, -sideDriftX);

            book_1.xRot = book_1Rotation;
            book_2.zRot = book_2Rotation;
            lectern.xRot = lecternRotation;

            if (debrisEntity.getHurtTime() > 0) {
                int hurtTime = debrisEntity.getHurtTime();
                int maxHurtTime = 10;
                float hurtProgress = 1.0F - (hurtTime / (float) maxHurtTime);
                float offset = (float) Math.sin(hurtProgress * 2.0F * Math.PI) * 0.5F;

                float explosionOffsetX = (float) Math.sin(hurtProgress * Math.PI * 2.0F) * 0.5F;
                float explosionOffsetZ = (float) Math.cos(hurtProgress * Math.PI * 2.0F) * 0.5F;

                book_1.setPos(explosionOffsetX, baseYBook_1 + offset, explosionOffsetZ);
                book_2.setPos(-explosionOffsetX, baseYBook_2 + offset, -explosionOffsetZ);
                lectern.setPos(explosionOffsetZ, baseYLectern + offset, explosionOffsetX);
            }
        }
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        book_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        book_2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        lectern.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
