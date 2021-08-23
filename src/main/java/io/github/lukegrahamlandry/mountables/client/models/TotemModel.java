package io.github.lukegrahamlandry.mountables.client.models;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class TotemModel<T extends MountEntity> extends EntityModel<T> {
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer arms;
	private final ModelRenderer leg0;
	private final ModelRenderer leg1;
	private final ModelRenderer wing1;
	private final ModelRenderer wing2;

	public TotemModel() {
		texWidth = 32;
		texHeight = 32;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 16.0F, 0.0F);
		body.texOffs(0, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setPos(0.0F, 0.0F, 0.0F);
		body.addChild(head);
		head.texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
		head.texOffs(0, 20).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		arms = new ModelRenderer(this);
		arms.setPos(0.0F, 0.0F, 2.0F);
		body.addChild(arms);
		setRotationAngle(arms, -0.7854F, 0.0F, 0.0F);
		arms.texOffs(12, 12).addBox(-2.0F, 4.0F, -2.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);
		arms.texOffs(18, 0).addBox(-4.0F, 3.0F, -2.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
		arms.texOffs(18, 0).addBox(2.0F, 3.0F, -2.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);

		leg0 = new ModelRenderer(this);
		leg0.setPos(-1.0F, 4.0F, 0.0F);
		body.addChild(leg0);
		leg0.texOffs(16, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, -0.01F, true);

		leg1 = new ModelRenderer(this);
		leg1.setPos(1.0F, 4.0F, 0.0F);
		body.addChild(leg1);
		leg1.texOffs(16, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, -0.01F, false);

		wing1 = new ModelRenderer(this);
		wing1.setPos(2.0F, 1.5F, 2.0F);
		body.addChild(wing1);
		setRotationAngle(wing1, 0.0F, -0.3927F, 0.0F);
		wing1.texOffs(0, 0).addBox(0.0F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, true);

		wing2 = new ModelRenderer(this);
		wing2.setPos(-2.0F, 1.5F, 2.0F);
		body.addChild(wing2);
		setRotationAngle(wing2, 0.0F, 0.3927F, 0.0F);
		wing2.texOffs(0, 0).addBox(-3.0F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(T mount, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
		double anim_time = (mount.tickCount % (15)) / 25D;

		if (mount.isFlying()){
			updateRotation(leg0, (float) Math.toRadians(45), 0, 0);
			updateRotation(leg1, (float) Math.toRadians(45), 0, 0);

			updateRotation(wing1, 0, degSin(anim_time*960)*15, 0);
			updateRotation(wing2, 0, degSin(anim_time*960)*15, 0);
		} else if (mount.hasMoved()){
			updateRotation(leg0, degSin(anim_time*480)*-15, 0, 0);
			updateRotation(leg1, degSin(anim_time*480)*15, 0, 0);

			setRotationAngle(wing1, 0.0F, -0.3927F, 0.0F);
			setRotationAngle(wing2, 0.0F, 0.3927F, 0.0F);
		} else {
			setRotationAngle(leg0, 0, 0, 0);
			setRotationAngle(leg1, 0, 0, 0);
			setRotationAngle(wing1, 0.0F, -0.3927F, 0.0F);
			setRotationAngle(wing2, 0.0F, 0.3927F, 0.0F);
		}
	}

	private float degSin(double theta){
		return (float) Math.sin(Math.toRadians(theta));
	}


	private void updateRotation(ModelRenderer part, float x, float y, float z){
		setRotationAngle(part, (float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}