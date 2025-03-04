package net.kb07.kbspace.entity.client;// Made with Blockbench 4.12.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.kb07.kbspace.entity.custom.RocketEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class RocketModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "rocket2"), "main");
	private final ModelPart top;
	private final ModelPart walls;
	private final ModelPart north;
	private final ModelPart window;
	private final ModelPart glass;
	private final ModelPart stand;
	private final ModelPart thrusters;
	private final ModelPart bb_main;

	public RocketModel(ModelPart root) {
		this.top = root.getChild("top");
		this.walls = root.getChild("walls");
		this.north = this.walls.getChild("north");
		this.window = this.north.getChild("window");
		this.glass = this.window.getChild("glass");
		this.stand = root.getChild("stand");
		this.thrusters = root.getChild("thrusters");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition top = partdefinition.addOrReplaceChild("top", CubeListBuilder.create().texOffs(108, 40).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-9.0F, 4.0F, -9.0F, 18.0F, 2.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(64, 68).addBox(-7.0F, 2.0F, -7.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(72, 54).addBox(-5.0F, 0.0F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(64, 84).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -21.0F, 0.0F));

		PartDefinition walls = partdefinition.addOrReplaceChild("walls", CubeListBuilder.create().texOffs(72, 0).addBox(0.0F, -32.0F, 7.0F, 16.0F, 32.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 20).addBox(-1.0F, -32.0F, -8.0F, 2.0F, 32.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(36, 20).addBox(15.0F, -32.0F, -8.0F, 2.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 17.0F, 0.0F));

		PartDefinition north = walls.addOrReplaceChild("north", CubeListBuilder.create().texOffs(72, 34).addBox(-8.0F, -18.0F, -1.0F, 16.0F, 18.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(44, 93).addBox(6.0F, -32.0F, -1.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(52, 93).addBox(-8.0F, -32.0F, -1.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(108, 18).addBox(-6.0F, -32.0F, -1.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(108, 26).addBox(5.0F, -32.0F, -1.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(108, 44).addBox(5.0F, -22.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(108, 49).addBox(-6.0F, -22.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(100, 96).addBox(-8.0F, -19.0F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(100, 99).addBox(2.0F, -19.0F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(108, 34).addBox(2.0F, -30.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(108, 37).addBox(-5.0F, -30.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 86).addBox(-5.0F, -32.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(50, 108).addBox(-5.0F, -20.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(108, 84).addBox(-5.0F, -29.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(108, 87).addBox(4.0F, -29.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(108, 102).addBox(4.0F, -20.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -8.0F));

		PartDefinition window = north.addOrReplaceChild("window", CubeListBuilder.create().texOffs(0, 97).addBox(1.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(100, 91).addBox(1.0F, -12.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 102).addBox(8.0F, -8.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(10, 102).addBox(-3.0F, -8.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(88, 104).addBox(5.0F, -2.0F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(20, 106).addBox(7.0F, -4.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(108, 0).addBox(7.0F, -10.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 105).addBox(5.0F, -11.0F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(60, 105).addBox(-1.0F, -11.0F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(108, 6).addBox(-2.0F, -10.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(108, 12).addBox(-2.0F, -4.0F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(72, 105).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -18.0F, 0.0F));

		PartDefinition glass = window.addOrReplaceChild("glass", CubeListBuilder.create().texOffs(32, 93).addBox(-2.0F, -10.0F, -1.0F, 4.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(100, 102).addBox(-4.0F, -9.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(100, 102).addBox(2.0F, -9.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(44, 108).addBox(4.0F, -7.0F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(44, 108).addBox(-5.0F, -7.0F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -1.0F, 0.0F));

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create().texOffs(20, 93).addBox(16.0F, -10.0F, -3.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(88, 91).addBox(16.0F, -10.0F, -20.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(64, 92).addBox(-1.0F, -10.0F, -20.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(76, 92).addBox(-1.0F, -10.0F, -3.0F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 24.0F, 10.0F));

		PartDefinition thrusters = partdefinition.addOrReplaceChild("thrusters", CubeListBuilder.create().texOffs(0, 90).addBox(-11.0F, -2.0F, -4.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(24, 86).addBox(-11.0F, -2.0F, -11.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(44, 86).addBox(-4.0F, -2.0F, -11.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(88, 84).addBox(-4.0F, -2.0F, -4.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 21.0F, 5.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 68).addBox(-8.0F, -7.0F, -8.0F, 16.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		top.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		walls.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		thrusters.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}