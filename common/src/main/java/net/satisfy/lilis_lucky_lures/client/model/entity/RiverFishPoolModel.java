package net.satisfy.lilis_lucky_lures.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.satisfy.lilis_lucky_lures.client.model.entity.animation.RiverFishPoolAnimation;
import net.satisfy.lilis_lucky_lures.core.entity.RiverFishPoolEntity;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class RiverFishPoolModel<T extends RiverFishPoolEntity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(LilisLuckyLuresIdentifier.identifier("river_fish_pool"), "main");
	private final ModelPart river_swarm;

	public RiverFishPoolModel(ModelPart root) {
		this.river_swarm = root.getChild("river_swarm");
		ModelPart salmon_1 = this.river_swarm.getChild("salmon_1");
		ModelPart body_back = salmon_1.getChild("body_back");
		ModelPart dorsal_back = body_back.getChild("dorsal_back");
		ModelPart tailfin4 = body_back.getChild("tailfin4");
		ModelPart dorsal_front = salmon_1.getChild("dorsal_front");
		ModelPart head4 = salmon_1.getChild("head4");
		ModelPart leftFin4 = salmon_1.getChild("leftFin4");
		ModelPart rightFin4 = salmon_1.getChild("rightFin4");
		ModelPart salmon_2 = this.river_swarm.getChild("salmon_2");
		ModelPart body_back2 = salmon_2.getChild("body_back2");
		ModelPart dorsal_back2 = body_back2.getChild("dorsal_back2");
		ModelPart tailfin5 = body_back2.getChild("tailfin5");
		ModelPart dorsal_front2 = salmon_2.getChild("dorsal_front2");
		ModelPart head5 = salmon_2.getChild("head5");
		ModelPart leftFin5 = salmon_2.getChild("leftFin5");
		ModelPart rightFin5 = salmon_2.getChild("rightFin5");
		ModelPart salmon_3 = this.river_swarm.getChild("salmon_3");
		ModelPart body_back3 = salmon_3.getChild("body_back3");
		ModelPart dorsal_back3 = body_back3.getChild("dorsal_back3");
		ModelPart tailfin6 = body_back3.getChild("tailfin6");
		ModelPart dorsal_front3 = salmon_3.getChild("dorsal_front3");
		ModelPart head6 = salmon_3.getChild("head6");
		ModelPart leftFin6 = salmon_3.getChild("leftFin6");
		ModelPart rightFin6 = salmon_3.getChild("rightFin6");
		ModelPart cod_3 = this.river_swarm.getChild("cod_3");
		ModelPart head3 = cod_3.getChild("head3");
		ModelPart leftFin3 = cod_3.getChild("leftFin3");
		ModelPart rightFin3 = cod_3.getChild("rightFin3");
		ModelPart tailfin3 = cod_3.getChild("tailfin3");
		ModelPart cod_2 = this.river_swarm.getChild("cod_2");
		ModelPart head2 = cod_2.getChild("head2");
		ModelPart leftFin2 = cod_2.getChild("leftFin2");
		ModelPart rightFin2 = cod_2.getChild("rightFin2");
		ModelPart tailfin2 = cod_2.getChild("tailfin2");
		ModelPart cod_1 = this.river_swarm.getChild("cod_1");
		ModelPart head = cod_1.getChild("head");
		ModelPart leftFin = cod_1.getChild("leftFin");
		ModelPart rightFin = cod_1.getChild("rightFin");
		ModelPart tailfin = cod_1.getChild("tailfin");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition river_swarm = partdefinition.addOrReplaceChild("river_swarm", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition salmon_1 = river_swarm.addOrReplaceChild("salmon_1", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, -2.5F, -4.0F, 3.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 10.0F, 20.0F));

		PartDefinition body_back = salmon_1.addOrReplaceChild("body_back", CubeListBuilder.create().texOffs(0, 24).addBox(-1.5F, -8.5F, 0.0F, 3.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 4.0F));

		PartDefinition dorsal_back = body_back.addOrReplaceChild("dorsal_back", CubeListBuilder.create().texOffs(2, 14).addBox(0.0F, -5.5F, 0.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition tailfin4 = body_back.addOrReplaceChild("tailfin4", CubeListBuilder.create().texOffs(20, 21).addBox(0.0F, -8.5F, 0.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

		PartDefinition dorsal_front = salmon_1.addOrReplaceChild("dorsal_front", CubeListBuilder.create().texOffs(4, 13).addBox(0.0F, -5.5F, 0.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 2.0F));

		PartDefinition head4 = salmon_1.addOrReplaceChild("head4", CubeListBuilder.create().texOffs(22, 11).addBox(-1.0F, -5.5F, -3.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -4.0F));

		PartDefinition leftFin4 = salmon_1.addOrReplaceChild("leftFin4", CubeListBuilder.create().texOffs(2, 11).addBox(-2.0075F, -2.867F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0F, -4.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition rightFin4 = salmon_1.addOrReplaceChild("rightFin4", CubeListBuilder.create().texOffs(-2, 11).addBox(0.0074F, -2.867F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 5.0F, -4.0F, 0.0F, 0.0F, -0.6109F));

		PartDefinition salmon_2 = river_swarm.addOrReplaceChild("salmon_2", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, -2.5F, -4.0F, 3.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, 15.0F, -3.0F));

		PartDefinition body_back2 = salmon_2.addOrReplaceChild("body_back2", CubeListBuilder.create().texOffs(0, 24).addBox(-1.5F, -8.5F, 0.0F, 3.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 4.0F));

		PartDefinition dorsal_back2 = body_back2.addOrReplaceChild("dorsal_back2", CubeListBuilder.create().texOffs(2, 14).addBox(0.0F, -5.5F, 0.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition tailfin5 = body_back2.addOrReplaceChild("tailfin5", CubeListBuilder.create().texOffs(20, 21).addBox(0.0F, -8.5F, 0.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

		PartDefinition dorsal_front2 = salmon_2.addOrReplaceChild("dorsal_front2", CubeListBuilder.create().texOffs(4, 13).addBox(0.0F, -5.5F, 0.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 2.0F));

		PartDefinition head5 = salmon_2.addOrReplaceChild("head5", CubeListBuilder.create().texOffs(22, 11).addBox(-1.0F, -5.5F, -3.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -4.0F));

		PartDefinition leftFin5 = salmon_2.addOrReplaceChild("leftFin5", CubeListBuilder.create().texOffs(2, 11).addBox(-2.0075F, -2.867F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0F, -4.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition rightFin5 = salmon_2.addOrReplaceChild("rightFin5", CubeListBuilder.create().texOffs(-2, 11).addBox(0.0074F, -2.867F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 5.0F, -4.0F, 0.0F, 0.0F, -0.6109F));

		PartDefinition salmon_3 = river_swarm.addOrReplaceChild("salmon_3", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, -2.5F, -4.0F, 3.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-17.0F, 18.0F, 14.0F));

		PartDefinition body_back3 = salmon_3.addOrReplaceChild("body_back3", CubeListBuilder.create().texOffs(0, 24).addBox(-1.5F, -8.5F, 0.0F, 3.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 4.0F));

		PartDefinition dorsal_back3 = body_back3.addOrReplaceChild("dorsal_back3", CubeListBuilder.create().texOffs(2, 14).addBox(0.0F, -5.5F, 0.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition tailfin6 = body_back3.addOrReplaceChild("tailfin6", CubeListBuilder.create().texOffs(20, 21).addBox(0.0F, -8.5F, 0.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

		PartDefinition dorsal_front3 = salmon_3.addOrReplaceChild("dorsal_front3", CubeListBuilder.create().texOffs(4, 13).addBox(0.0F, -5.5F, 0.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 2.0F));

		PartDefinition head6 = salmon_3.addOrReplaceChild("head6", CubeListBuilder.create().texOffs(22, 11).addBox(-1.0F, -5.5F, -3.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -4.0F));

		PartDefinition leftFin6 = salmon_3.addOrReplaceChild("leftFin6", CubeListBuilder.create().texOffs(2, 11).addBox(-2.0075F, -2.867F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 5.0F, -4.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition rightFin6 = salmon_3.addOrReplaceChild("rightFin6", CubeListBuilder.create().texOffs(-2, 11).addBox(0.0074F, -2.867F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 5.0F, -4.0F, 0.0F, 0.0F, -0.6109F));

		PartDefinition cod_3 = river_swarm.addOrReplaceChild("cod_3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -2.8333F, 2.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(20, -6).addBox(0.0F, -3.0F, -3.8333F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(22, -1).addBox(0.0F, 2.0F, -0.8333F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 5.0F, -4.1667F));

		PartDefinition head3 = cod_3.addOrReplaceChild("head3", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9992F, -2.0008F, -3.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(11, 0).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -3.8333F));

		PartDefinition leftFin3 = cod_3.addOrReplaceChild("leftFin3", CubeListBuilder.create().texOffs(24, 4).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, -3.8333F, 0.0F, 0.0F, 0.6109F));

		PartDefinition rightFin3 = cod_3.addOrReplaceChild("rightFin3", CubeListBuilder.create().texOffs(24, 1).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, -3.8333F, 0.0F, 0.0F, -0.6109F));

		PartDefinition tailfin3 = cod_3.addOrReplaceChild("tailfin3", CubeListBuilder.create().texOffs(20, 1).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 4.1667F));

		PartDefinition cod_2 = river_swarm.addOrReplaceChild("cod_2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, 1.0F, 2.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(20, -6).addBox(0.0F, -5.0F, 0.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(22, -1).addBox(0.0F, 0.0F, 3.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 7.0F, 10.0F));

		PartDefinition head2 = cod_2.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9992F, -2.0008F, -3.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(11, 0).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition leftFin2 = cod_2.addOrReplaceChild("leftFin2", CubeListBuilder.create().texOffs(24, 4).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition rightFin2 = cod_2.addOrReplaceChild("rightFin2", CubeListBuilder.create().texOffs(24, 1).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.6109F));

		PartDefinition tailfin2 = cod_2.addOrReplaceChild("tailfin2", CubeListBuilder.create().texOffs(20, 1).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 8.0F));

		PartDefinition cod_1 = river_swarm.addOrReplaceChild("cod_1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -2.8333F, 2.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(20, -6).addBox(0.0F, -3.0F, -3.8333F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(22, -1).addBox(0.0F, 2.0F, -0.8333F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, 8.0F, 3.8333F));

		PartDefinition head = cod_1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9992F, -2.0008F, -3.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(11, 0).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -3.8333F));

		PartDefinition leftFin = cod_1.addOrReplaceChild("leftFin", CubeListBuilder.create().texOffs(24, 4).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, -3.8333F, 0.0F, 0.0F, 0.6109F));

		PartDefinition rightFin = cod_1.addOrReplaceChild("rightFin", CubeListBuilder.create().texOffs(24, 1).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, -3.8333F, 0.0F, 0.0F, -0.6109F));

		PartDefinition tailfin = cod_1.addOrReplaceChild("tailfin", CubeListBuilder.create().texOffs(20, 1).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 4.1667F));

		return LayerDefinition.create(meshdefinition, 48, 48);
	}

	@Override
	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(entity.idleAnimationState, RiverFishPoolAnimation.idle, ageInTicks, 1.0F);
	}

	@Override
	public @NotNull ModelPart root() {
		return river_swarm;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		river_swarm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}
