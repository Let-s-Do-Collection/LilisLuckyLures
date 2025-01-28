package net.satisfy.lilis_lucky_lures.client.model.entity;




public class FloatingBooksModel<T extends Entity> extends EntityModel<T> {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "floating_books"), "main");
	private final ModelPart button;
	private final ModelPart book_1;
	private final ModelPart barrel;
	private final ModelPart book_2;

	public FloatingBooksModel(ModelPart root) {
		this.button = root.getChild("button");
		this.book_1 = root.getChild("book_1");
		this.barrel = root.getChild("barrel");
		this.book_2 = root.getChild("book_2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition button = partdefinition.addOrReplaceChild("button", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition book_1 = partdefinition.addOrReplaceChild("book_1", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition planks_r1 = book_1.addOrReplaceChild("planks_r1", CubeListBuilder.create().texOffs(0, 20).addBox(-8.0F, -3.0F, -1.0F, 9.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 1.0F, -19.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition barrel = partdefinition.addOrReplaceChild("barrel", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition lectern_r1 = barrel.addOrReplaceChild("lectern_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-15.0F, -4.0F, -1.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 1.0F, -1.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition book_2 = partdefinition.addOrReplaceChild("book_2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 15.0F, 0.0F, 0.6109F, 0.0F));

		PartDefinition planks_r2 = book_2.addOrReplaceChild("planks_r2", CubeListBuilder.create().texOffs(0, 33).addBox(-8.0F, -3.0F, -1.0F, 9.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 1.0F, -3.0F, 0.0F, 0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		button.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		book_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		barrel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		book_2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}