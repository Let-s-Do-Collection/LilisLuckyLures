package net.satisfy.lilis_lucky_lures.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.satisfy.lilis_lucky_lures.client.model.entity.animation.RiverFishPoolAnimation;
import net.satisfy.lilis_lucky_lures.core.entity.OceanFishPoolEntity;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class OceanFishPoolModel<T extends OceanFishPoolEntity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new LilisLuckyLuresIdentifier("ocean_fish_pool"), "main");
	private final ModelPart ocean_swarm;

	public OceanFishPoolModel(ModelPart root) {
		this.ocean_swarm = root.getChild("ocean_swarm");
		ModelPart salmon_1 = this.ocean_swarm.getChild("salmon_1");
		ModelPart body_large = salmon_1.getChild("body_large");
		ModelPart leftFin4 = body_large.getChild("leftFin4");
		ModelPart rightFin4 = body_large.getChild("rightFin4");
		ModelPart spines_top_front2 = body_large.getChild("spines_top_front2");
		ModelPart spines_top_mid = body_large.getChild("spines_top_mid");
		ModelPart spines_top_back2 = body_large.getChild("spines_top_back2");
		ModelPart spines_bottom_front2 = body_large.getChild("spines_bottom_front2");
		ModelPart spines_bottom_mid = body_large.getChild("spines_bottom_mid");
		ModelPart spines_bottom_back2 = body_large.getChild("spines_bottom_back2");
		ModelPart spines_left_front2 = body_large.getChild("spines_left_front2");
		ModelPart spines_left_mid = body_large.getChild("spines_left_mid");
		ModelPart spines_left_back2 = body_large.getChild("spines_left_back2");
		ModelPart spines_right_front2 = body_large.getChild("spines_right_front2");
		ModelPart spines_right_mid = body_large.getChild("spines_right_mid");
		ModelPart spines_right_back2 = body_large.getChild("spines_right_back2");
		ModelPart salmon_2 = this.ocean_swarm.getChild("salmon_2");
		ModelPart body_mid = salmon_2.getChild("body_mid");
		ModelPart leftFin5 = body_mid.getChild("leftFin5");
		ModelPart rightFin5 = body_mid.getChild("rightFin5");
		ModelPart spines_top_front = body_mid.getChild("spines_top_front");
		ModelPart spines_top_back = body_mid.getChild("spines_top_back");
		ModelPart spines_bottom_front = body_mid.getChild("spines_bottom_front");
		ModelPart spines_bottom_back = body_mid.getChild("spines_bottom_back");
		ModelPart spines_left_front = body_mid.getChild("spines_left_front");
		ModelPart spines_left_back = body_mid.getChild("spines_left_back");
		ModelPart spines_right_front = body_mid.getChild("spines_right_front");
		ModelPart spines_right_back = body_mid.getChild("spines_right_back");
		ModelPart salmon_3 = this.ocean_swarm.getChild("salmon_3");
		ModelPart body_mid2 = salmon_3.getChild("body_mid2");
		ModelPart leftFin6 = body_mid2.getChild("leftFin6");
		ModelPart rightFin6 = body_mid2.getChild("rightFin6");
		ModelPart spines_top_front3 = body_mid2.getChild("spines_top_front3");
		ModelPart spines_top_back3 = body_mid2.getChild("spines_top_back3");
		ModelPart spines_bottom_front3 = body_mid2.getChild("spines_bottom_front3");
		ModelPart spines_bottom_back3 = body_mid2.getChild("spines_bottom_back3");
		ModelPart spines_left_front3 = body_mid2.getChild("spines_left_front3");
		ModelPart spines_left_back3 = body_mid2.getChild("spines_left_back3");
		ModelPart spines_right_front3 = body_mid2.getChild("spines_right_front3");
		ModelPart spines_right_back3 = body_mid2.getChild("spines_right_back3");
		ModelPart cod_3 = this.ocean_swarm.getChild("cod_3");
		ModelPart body = cod_3.getChild("body");
		ModelPart tailfin3 = body.getChild("tailfin3");
		ModelPart leftFin3 = body.getChild("leftFin3");
		ModelPart rightFin3 = body.getChild("rightFin3");
		ModelPart cod_2 = this.ocean_swarm.getChild("cod_2");
		ModelPart body2 = cod_2.getChild("body2");
		ModelPart tailfin2 = body2.getChild("tailfin2");
		ModelPart leftFin2 = body2.getChild("leftFin2");
		ModelPart rightFin2 = body2.getChild("rightFin2");
		ModelPart cod_1 = this.ocean_swarm.getChild("cod_1");
		ModelPart body3 = cod_1.getChild("body3");
		ModelPart tailfin4 = body3.getChild("tailfin4");
		ModelPart leftFin7 = body3.getChild("leftFin7");
		ModelPart rightFin7 = body3.getChild("rightFin7");
	}


	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition ocean_swarm = partdefinition.addOrReplaceChild("ocean_swarm", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));

		PartDefinition salmon_1 = ocean_swarm.addOrReplaceChild("salmon_1", CubeListBuilder.create(), PartPose.offset(-7.0F, 15.0F, 20.0F));

		PartDefinition body_large = salmon_1.addOrReplaceChild("body_large", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, -1.0F));

		PartDefinition leftFin4 = body_large.addOrReplaceChild("leftFin4", CubeListBuilder.create().texOffs(24, 3).addBox(0.0F, 0.0F, -5.9904F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -7.0F, 3.0F));

		PartDefinition rightFin4 = body_large.addOrReplaceChild("rightFin4", CubeListBuilder.create().texOffs(24, 0).addBox(-1.9968F, 0.0F, -3.992F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -7.0F, 1.0F));

		PartDefinition spines_top_front2 = body_large.addOrReplaceChild("spines_top_front2", CubeListBuilder.create().texOffs(14, 16).addBox(0.0F, -1.0F, 0.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -8.0F, -4.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition spines_top_mid = body_large.addOrReplaceChild("spines_top_mid", CubeListBuilder.create().texOffs(14, 16).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition spines_top_back2 = body_large.addOrReplaceChild("spines_top_back2", CubeListBuilder.create().texOffs(14, 16).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 4.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition spines_bottom_front2 = body_large.addOrReplaceChild("spines_bottom_front2", CubeListBuilder.create().texOffs(14, 19).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition spines_bottom_mid = body_large.addOrReplaceChild("spines_bottom_mid", CubeListBuilder.create().texOffs(14, 19).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition spines_bottom_back2 = body_large.addOrReplaceChild("spines_bottom_back2", CubeListBuilder.create().texOffs(14, 19).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition spines_left_front2 = body_large.addOrReplaceChild("spines_left_front2", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -8.0F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, -4.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition spines_left_mid = body_large.addOrReplaceChild("spines_left_mid", CubeListBuilder.create().texOffs(4, 16).mirror().addBox(0.0F, -8.0F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, 0.0F, 0.0F));

		PartDefinition spines_left_back2 = body_large.addOrReplaceChild("spines_left_back2", CubeListBuilder.create().texOffs(8, 16).mirror().addBox(0.0F, -8.0F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 0.0F, 4.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition spines_right_front2 = body_large.addOrReplaceChild("spines_right_front2", CubeListBuilder.create().texOffs(4, 16).addBox(-1.0F, -8.0F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, -4.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition spines_right_mid = body_large.addOrReplaceChild("spines_right_mid", CubeListBuilder.create().texOffs(8, 16).addBox(-1.0F, -8.0F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 0.0F));

		PartDefinition spines_right_back2 = body_large.addOrReplaceChild("spines_right_back2", CubeListBuilder.create().texOffs(8, 16).addBox(-1.0F, -8.0F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, 4.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition salmon_2 = ocean_swarm.addOrReplaceChild("salmon_2", CubeListBuilder.create(), PartPose.offset(12.0F, 17.0F, -3.0F));

		PartDefinition body_mid = salmon_2.addOrReplaceChild("body_mid", CubeListBuilder.create().texOffs(12, 22).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

		PartDefinition leftFin5 = body_mid.addOrReplaceChild("leftFin5", CubeListBuilder.create().texOffs(24, 3).addBox(0.0F, 0.0F, -2.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -5.0F, 0.5F));

		PartDefinition rightFin5 = body_mid.addOrReplaceChild("rightFin5", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, 0.0F, -2.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -5.0F, 0.5F));

		PartDefinition spines_top_front = body_mid.addOrReplaceChild("spines_top_front", CubeListBuilder.create().texOffs(19, 17).addBox(-2.5F, -1.0F, 0.0F, 5.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -2.5F));

		PartDefinition spines_top_back = body_mid.addOrReplaceChild("spines_top_back", CubeListBuilder.create().texOffs(11, 17).addBox(-2.5F, -1.0F, 0.0F, 5.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 2.5F));

		PartDefinition spines_bottom_front = body_mid.addOrReplaceChild("spines_bottom_front", CubeListBuilder.create().texOffs(18, 20).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -2.5F));

		PartDefinition spines_bottom_back = body_mid.addOrReplaceChild("spines_bottom_back", CubeListBuilder.create().texOffs(18, 20).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 2.5F, 0.7854F, 0.0F, 0.0F));

		PartDefinition spines_left_front = body_mid.addOrReplaceChild("spines_left_front", CubeListBuilder.create().texOffs(1, 17).addBox(0.0F, -6.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.0F, -2.5F, 0.0F, 0.7854F, 0.0F));

		PartDefinition spines_left_back = body_mid.addOrReplaceChild("spines_left_back", CubeListBuilder.create().texOffs(1, 17).addBox(0.0F, -6.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.0F, 2.5F, 0.0F, -0.7854F, 0.0F));

		PartDefinition spines_right_front = body_mid.addOrReplaceChild("spines_right_front", CubeListBuilder.create().texOffs(5, 17).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.0F, -2.5F, 0.0F, -0.7854F, 0.0F));

		PartDefinition spines_right_back = body_mid.addOrReplaceChild("spines_right_back", CubeListBuilder.create().texOffs(9, 17).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.0F, 2.5F, 0.0F, 0.7854F, 0.0F));

		PartDefinition salmon_3 = ocean_swarm.addOrReplaceChild("salmon_3", CubeListBuilder.create(), PartPose.offset(-17.0F, 24.0F, 14.0F));

		PartDefinition body_mid2 = salmon_3.addOrReplaceChild("body_mid2", CubeListBuilder.create().texOffs(12, 22).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

		PartDefinition leftFin6 = body_mid2.addOrReplaceChild("leftFin6", CubeListBuilder.create().texOffs(24, 3).addBox(0.0F, 0.0F, -2.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -5.0F, 0.5F));

		PartDefinition rightFin6 = body_mid2.addOrReplaceChild("rightFin6", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, 0.0F, -2.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -5.0F, 0.5F));

		PartDefinition spines_top_front3 = body_mid2.addOrReplaceChild("spines_top_front3", CubeListBuilder.create().texOffs(19, 17).addBox(-2.5F, -1.0F, 0.0F, 5.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -2.5F));

		PartDefinition spines_top_back3 = body_mid2.addOrReplaceChild("spines_top_back3", CubeListBuilder.create().texOffs(11, 17).addBox(-2.5F, -1.0F, 0.0F, 5.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 2.5F));

		PartDefinition spines_bottom_front3 = body_mid2.addOrReplaceChild("spines_bottom_front3", CubeListBuilder.create().texOffs(18, 20).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -2.5F));

		PartDefinition spines_bottom_back3 = body_mid2.addOrReplaceChild("spines_bottom_back3", CubeListBuilder.create().texOffs(18, 20).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 2.5F, 0.7854F, 0.0F, 0.0F));

		PartDefinition spines_left_front3 = body_mid2.addOrReplaceChild("spines_left_front3", CubeListBuilder.create().texOffs(1, 17).addBox(0.0F, -6.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.0F, -2.5F, 0.0F, 0.7854F, 0.0F));

		PartDefinition spines_left_back3 = body_mid2.addOrReplaceChild("spines_left_back3", CubeListBuilder.create().texOffs(1, 17).addBox(0.0F, -6.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 0.0F, 2.5F, 0.0F, -0.7854F, 0.0F));

		PartDefinition spines_right_front3 = body_mid2.addOrReplaceChild("spines_right_front3", CubeListBuilder.create().texOffs(5, 17).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.0F, -2.5F, 0.0F, -0.7854F, 0.0F));

		PartDefinition spines_right_back3 = body_mid2.addOrReplaceChild("spines_right_back3", CubeListBuilder.create().texOffs(9, 17).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.0F, 2.5F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cod_3 = ocean_swarm.addOrReplaceChild("cod_3", CubeListBuilder.create(), PartPose.offset(5.0F, 5.0F, -4.1667F));

		PartDefinition body = cod_3.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 0).addBox(-0.5F, -3.0F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(36, 9).addBox(0.5F, -7.0F, -2.9992F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 2.0F, -0.8333F));

		PartDefinition tailfin3 = body.addOrReplaceChild("tailfin3", CubeListBuilder.create().texOffs(40, 5).mirror().addBox(0.0F, -3.0F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 0.0F, 3.0F));

		PartDefinition leftFin3 = body.addOrReplaceChild("leftFin3", CubeListBuilder.create().texOffs(32, 15).addBox(-0.164F, -2.0F, -1.1059F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.0F, -0.6109F, 0.0F));

		PartDefinition rightFin3 = body.addOrReplaceChild("rightFin3", CubeListBuilder.create().texOffs(32, 19).addBox(-1.836F, -2.0F, -1.1059F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.6109F, 0.0F));

		PartDefinition cod_2 = ocean_swarm.addOrReplaceChild("cod_2", CubeListBuilder.create(), PartPose.offset(9.0F, 10.0F, 10.0F));

		PartDefinition body2 = cod_2.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(32, 0).addBox(-0.5F, -3.0F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(36, 9).addBox(0.5F, -7.0F, -2.9992F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, 4.0F));

		PartDefinition tailfin2 = body2.addOrReplaceChild("tailfin2", CubeListBuilder.create().texOffs(40, 5).mirror().addBox(0.0F, -3.0F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 0.0F, 3.0F));

		PartDefinition leftFin2 = body2.addOrReplaceChild("leftFin2", CubeListBuilder.create().texOffs(32, 15).addBox(-0.164F, -2.0F, -1.1059F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.0F, -0.6109F, 0.0F));

		PartDefinition rightFin2 = body2.addOrReplaceChild("rightFin2", CubeListBuilder.create().texOffs(32, 19).addBox(-1.836F, -2.0F, -1.1059F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.6109F, 0.0F));

		PartDefinition cod_1 = ocean_swarm.addOrReplaceChild("cod_1", CubeListBuilder.create(), PartPose.offset(-10.0F, 6.0F, 3.8333F));

		PartDefinition body3 = cod_1.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(32, 0).addBox(-0.5F, -3.0F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(36, 9).addBox(0.5F, -7.0F, -2.9992F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 2.0F, 0.1667F));

		PartDefinition tailfin4 = body3.addOrReplaceChild("tailfin4", CubeListBuilder.create().texOffs(40, 5).mirror().addBox(0.0F, -3.0F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 0.0F, 3.0F));

		PartDefinition leftFin7 = body3.addOrReplaceChild("leftFin7", CubeListBuilder.create().texOffs(32, 15).addBox(-0.164F, -2.0F, -1.1059F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.0F, -0.6109F, 0.0F));

		PartDefinition rightFin7 = body3.addOrReplaceChild("rightFin7", CubeListBuilder.create().texOffs(32, 19).addBox(-1.836F, -2.0F, -1.1059F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.6109F, 0.0F));

		return LayerDefinition.create(meshdefinition, 48, 48);
	}


	@Override
	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(entity.idleAnimationState, RiverFishPoolAnimation.idle, ageInTicks, 1.0F);
	}

	@Override
	public @NotNull ModelPart root() {
		return ocean_swarm;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		ocean_swarm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
