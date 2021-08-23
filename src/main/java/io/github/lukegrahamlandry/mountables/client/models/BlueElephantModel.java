package io.github.lukegrahamlandry.mountables.client.models;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;

public class BlueElephantModel<T extends MountEntity> extends EntityModel<T> {
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer cube_r1;
	private final ModelRenderer ear1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer ear2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer trunk1;
	private final ModelRenderer cube_r4;
	private final ModelRenderer trunk2;
	private final ModelRenderer leg1;
	private final ModelRenderer leg2;
	private final ModelRenderer leg3;
	private final ModelRenderer leg4;
	private final ModelRenderer tail;
	private final ModelRenderer cube_r5;

	public BlueElephantModel() {
		texWidth = 80;
		texHeight = 80;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 15.0F, 0.0F);
		body.texOffs(0, 0).addBox(-6.5F, -5.0F, -8.0F, 13.0F, 10.0F, 16.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setPos(0.0F, -3.0F, -8.0F);
		body.addChild(head);
		head.texOffs(0, 26).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, 0.0F, false);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(-6.0F, 3.0F, -3.0F);
		head.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.3927F, 0.0F, 0.0F);
		cube_r1.texOffs(42, 0).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, 0.0F, false);
		cube_r1.texOffs(42, 0).addBox(11.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, 0.0F, true);

		ear1 = new ModelRenderer(this);
		ear1.setPos(6.0F, 0.0F, -1.0F);
		head.addChild(ear1);
		

		cube_r2 = new ModelRenderer(this);
		cube_r2.setPos(0.0F, 0.0F, 0.0F);
		ear1.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, -0.7854F, 0.0F);
		cube_r2.texOffs(48, 31).addBox(0.0F, -5.0F, 0.0F, 8.0F, 10.0F, 1.0F, 0.0F, false);

		ear2 = new ModelRenderer(this);
		ear2.setPos(-6.0F, 0.0F, -1.0F);
		head.addChild(ear2);
		

		cube_r3 = new ModelRenderer(this);
		cube_r3.setPos(0.0F, 0.0F, 0.0F);
		ear2.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, 0.7854F, 0.0F);
		cube_r3.texOffs(48, 31).addBox(-8.0F, -5.0F, 0.0F, 8.0F, 10.0F, 1.0F, 0.0F, true);

		trunk1 = new ModelRenderer(this);
		trunk1.setPos(0.0F, -1.0F, -6.0F);
		head.addChild(trunk1);
		

		cube_r4 = new ModelRenderer(this);
		cube_r4.setPos(0.0F, 0.0F, 0.0F);
		trunk1.addChild(cube_r4);
		setRotationAngle(cube_r4, -0.7854F, 0.0F, 0.0F);
		cube_r4.texOffs(45, 47).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 6.0F, 3.0F, 0.0F, false);

		trunk2 = new ModelRenderer(this);
		trunk2.setPos(0.0F, 4.0F, -4.0F);
		trunk1.addChild(trunk2);
		trunk2.texOffs(0, 26).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 6.0F, 3.0F, 0.0F, false);

		leg1 = new ModelRenderer(this);
		leg1.setPos(4.0F, 5.0F, -6.0F);
		body.addChild(leg1);
		leg1.texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

		leg2 = new ModelRenderer(this);
		leg2.setPos(-4.0F, 5.0F, -6.0F);
		body.addChild(leg2);
		leg2.texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, true);

		leg3 = new ModelRenderer(this);
		leg3.setPos(4.0F, 5.0F, 6.0F);
		body.addChild(leg3);
		leg3.texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

		leg4 = new ModelRenderer(this);
		leg4.setPos(-4.0F, 5.0F, 6.0F);
		body.addChild(leg4);
		leg4.texOffs(0, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, true);

		tail = new ModelRenderer(this);
		tail.setPos(0.0F, -3.0F, 8.0F);
		body.addChild(tail);
		

		cube_r5 = new ModelRenderer(this);
		cube_r5.setPos(0.0F, 0.0F, 0.0F);
		tail.addChild(cube_r5);
		setRotationAngle(cube_r5, -0.7854F, 0.0F, 0.0F);
		cube_r5.texOffs(36, 16).addBox(0.0F, -2.0F, 0.0F, 0.0F, 5.0F, 10.0F, 0.0F, true);
	}

	@Override
	public void setupAnim(T mount, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
		if (!mount.hasMoved()){
			updateRotation(leg1, 0, 0, 0);
			updateRotation(leg2, 0, 0, 0);
			updateRotation(leg3, 0, 0, 0);
			updateRotation(leg4, 0, 0, 0);
			setRotationAngle(cube_r4, -0.7854F, 0.0F, 0.0F);
			setRotationAngle(cube_r5, -0.7854F, 0.0F, 0.0F);
			return;
		}

		double anim_time = (mount.tickCount % 15) / 25D;
		float shift = degSin(anim_time * 480);
		updateRotation(leg1, shift * 30, 0, 0);
		updateRotation(leg2, shift * -30, 0, 0);
		updateRotation(leg3, shift * -30, 0, 0);
		updateRotation(leg4, shift * 30, 0, 0);
		updateRotation(tail, 0, shift * -15, shift *15);
		updateRotation(trunk1, shift * 5, 0, 0);
		updateRotation(trunk2, shift * 5, 0, 0);
		updateRotation(ear1, 0, shift * 15, 0);
		updateRotation(ear2, 0, shift * -15, 0);
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