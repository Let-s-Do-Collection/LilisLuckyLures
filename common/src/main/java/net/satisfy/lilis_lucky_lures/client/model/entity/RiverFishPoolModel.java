package net.satisfy.lilis_lucky_lures.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;

public class RiverFishPoolModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new LilisLuckyLuresIdentifier("river_fish_pool"), "main");
	private final ModelPart river_swarm;
	private final ModelPart salmon_1;
	private final ModelPart body_back;
	private final ModelPart dorsal_back;
	private final ModelPart tailfin4;
	private final ModelPart dorsal_front;
	private final ModelPart head4;
	private final ModelPart leftFin4;
	private final ModelPart rightFin4;
	private final ModelPart salmon_2;
	private final ModelPart body_back2;
	private final ModelPart dorsal_back2;
	private final ModelPart tailfin5;
	private final ModelPart dorsal_front2;
	private final ModelPart head5;
	private final ModelPart leftFin5;
	private final ModelPart rightFin5;
	private final ModelPart salmon_3;
	private final ModelPart body_back3;
	private final ModelPart dorsal_back3;
	private final ModelPart tailfin6;
	private final ModelPart dorsal_front3;
	private final ModelPart head6;
	private final ModelPart leftFin6;
	private final ModelPart rightFin6;
	private final ModelPart cod_3;
	private final ModelPart head3;
	private final ModelPart leftFin3;
	private final ModelPart rightFin3;
	private final ModelPart tailfin3;
	private final ModelPart cod_2;
	private final ModelPart head2;
	private final ModelPart leftFin2;
	private final ModelPart rightFin2;
	private final ModelPart tailfin2;
	private final ModelPart cod_1;
	private final ModelPart head;
	private final ModelPart leftFin;
	private final ModelPart rightFin;
	private final ModelPart tailfin;

	public RiverFishPoolModel(ModelPart root) {
		this.river_swarm = root.getChild("river_swarm");
		this.salmon_1 = this.river_swarm.getChild("salmon_1");
		this.body_back = this.salmon_1.getChild("body_back");
		this.dorsal_back = this.body_back.getChild("dorsal_back");
		this.tailfin4 = this.body_back.getChild("tailfin4");
		this.dorsal_front = this.salmon_1.getChild("dorsal_front");
		this.head4 = this.salmon_1.getChild("head4");
		this.leftFin4 = this.salmon_1.getChild("leftFin4");
		this.rightFin4 = this.salmon_1.getChild("rightFin4");
		this.salmon_2 = this.river_swarm.getChild("salmon_2");
		this.body_back2 = this.salmon_2.getChild("body_back2");
		this.dorsal_back2 = this.body_back2.getChild("dorsal_back2");
		this.tailfin5 = this.body_back2.getChild("tailfin5");
		this.dorsal_front2 = this.salmon_2.getChild("dorsal_front2");
		this.head5 = this.salmon_2.getChild("head5");
		this.leftFin5 = this.salmon_2.getChild("leftFin5");
		this.rightFin5 = this.salmon_2.getChild("rightFin5");
		this.salmon_3 = this.river_swarm.getChild("salmon_3");
		this.body_back3 = this.salmon_3.getChild("body_back3");
		this.dorsal_back3 = this.body_back3.getChild("dorsal_back3");
		this.tailfin6 = this.body_back3.getChild("tailfin6");
		this.dorsal_front3 = this.salmon_3.getChild("dorsal_front3");
		this.head6 = this.salmon_3.getChild("head6");
		this.leftFin6 = this.salmon_3.getChild("leftFin6");
		this.rightFin6 = this.salmon_3.getChild("rightFin6");
		this.cod_3 = this.river_swarm.getChild("cod_3");
		this.head3 = this.cod_3.getChild("head3");
		this.leftFin3 = this.cod_3.getChild("leftFin3");
		this.rightFin3 = this.cod_3.getChild("rightFin3");
		this.tailfin3 = this.cod_3.getChild("tailfin3");
		this.cod_2 = this.river_swarm.getChild("cod_2");
		this.head2 = this.cod_2.getChild("head2");
		this.leftFin2 = this.cod_2.getChild("leftFin2");
		this.rightFin2 = this.cod_2.getChild("rightFin2");
		this.tailfin2 = this.cod_2.getChild("tailfin2");
		this.cod_1 = this.river_swarm.getChild("cod_1");
		this.head = this.cod_1.getChild("head");
		this.leftFin = this.cod_1.getChild("leftFin");
		this.rightFin = this.cod_1.getChild("rightFin");
		this.tailfin = this.cod_1.getChild("tailfin");
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
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// Entferne oder kommentiere die folgende Zeile, um die Drehung der gesamten Entität zu verhindern
		// river_swarm.yRot = (float)Math.PI;

		salmon_1.yRot = 0;
		salmon_2.yRot = 0;
		salmon_3.yRot = 0;
		cod_1.yRot = 0;
		cod_2.yRot = 0;
		cod_3.yRot = 0;
		animateFishMovement(ageInTicks);
	}


	private void animateFishMovement(float ageInTicks) {
		// Salmon 1
		float frequency1 = 0.15f; // Movement speed
		float radiusX1 = 10.0f;
		float radiusZ1 = 10.0f;
		float phase1 = 0.0f;
		float angle1 = ageInTicks * frequency1 + phase1;
		salmon_1.x = (float)Math.cos(angle1) * radiusX1;
		salmon_1.z = (float)Math.sin(angle1) * radiusZ1;
		salmon_1.y = 4.0f + (float)Math.sin(ageInTicks * 0.05f) * 1.0f;

		// Align head to movement direction
		salmon_1.yRot = angle1 + ((float)Math.PI / 2);

		// Animate tail fin faster
		tailfin4.yRot = 2.0f * (float)Math.sin(angle1 * 4.0f);

		// Salmon 2
		float frequency2 = 0.14f;
		float radiusX2 = 12.0f;
		float radiusZ2 = 6.0f;
		float phase2 = 1.0f;
		float angle2 = ageInTicks * frequency2 + phase2;
		salmon_2.x = (float)Math.cos(angle2) * radiusX2;
		salmon_2.z = (float)Math.sin(angle2) * radiusZ2;
		salmon_2.y = 6.0f + (float)Math.sin(ageInTicks * 0.045f + phase2) * 1.0f;

		// Align head to movement direction
		salmon_2.yRot = angle2 + ((float)Math.PI / 2);

		// Animate tail fin faster
		tailfin5.yRot = 2.0f * (float)Math.sin(angle2 * 4.0f);

		// Salmon 3
		float frequency3 = 0.13f;
		float radiusX3 = 14.0f;
		float radiusZ3 = 3.5f;
		float phase3 = 2.0f;
		float angle3 = ageInTicks * frequency3 + phase3;
		salmon_3.x = (float)Math.cos(angle3) * radiusX3;
		salmon_3.z = (float)Math.sin(angle3) * radiusZ3;
		salmon_3.y = 8.0f + (float)Math.sin(ageInTicks * 0.04f + phase3) * 1.0f;

		// Align head to movement direction
		salmon_3.yRot = angle3 + ((float)Math.PI / 2);

		// Animate tail fin faster
		tailfin6.yRot = 2.0f * (float)Math.sin(angle3 * 4.0f);

		// Cod 1
		float frequencyC1 = 0.15f;
		float radiusXC1 = 16.0f;
		float radiusZC1 = 16.0f;
		float phaseC1 = 3.0f;
		float angleC1 = ageInTicks * frequencyC1 + phaseC1;
		cod_1.x = (float)Math.cos(angleC1) * radiusXC1;
		cod_1.z = (float)Math.sin(angleC1) * radiusZC1;
		cod_1.y = 10.0f + (float)Math.sin(ageInTicks * 0.035f + phaseC1) * 1.0f;

		// Align head to movement direction
		cod_1.yRot = angleC1 + ((float)Math.PI / 2);

		// Animate tail fin faster
		tailfin4.yRot = 2.0f * (float)Math.sin(angleC1 * 4.0f);

		// Cod 2
		float frequencyC2 = 0.14f;
		float radiusXC2 = 18.0f;
		float radiusZC2 = 9.0f;
		float phaseC2 = 4.0f;
		float angleC2 = ageInTicks * frequencyC2 + phaseC2;
		cod_2.x = (float)Math.cos(angleC2) * radiusXC2;
		cod_2.z = (float)Math.sin(angleC2) * radiusZC2;
		cod_2.y = 12.0f + (float)Math.sin(ageInTicks * 0.033f + phaseC2) * 1.0f;

		// Align head to movement direction
		cod_2.yRot = angleC2 + ((float)Math.PI / 2);

		// Animate tail fin faster
		tailfin5.yRot = 2.0f * (float)Math.sin(angleC2 * 4.0f);

		// Cod 3
		float frequencyC3 = 0.13f;
		float radiusXC3 = 20.0f;
		float radiusZC3 = 5.0f;
		float phaseC3 = 5.0f;
		float angleC3 = ageInTicks * frequencyC3 + phaseC3;
		cod_3.x = (float)Math.cos(angleC3) * radiusXC3;
		cod_3.z = (float)Math.sin(angleC3) * radiusZC3;
		cod_3.y = 14.0f + (float)Math.sin(ageInTicks * 0.03f + phaseC3) * 1.0f;

		// Align head to movement direction
		cod_3.yRot = angleC3 + ((float)Math.PI / 2);

		// Animate tail fin faster
		tailfin6.yRot = 2.0f * (float)Math.sin(angleC3 * 4.0f);
	}





	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		river_swarm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
