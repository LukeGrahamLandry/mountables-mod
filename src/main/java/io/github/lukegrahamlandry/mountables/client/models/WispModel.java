package io.github.lukegrahamlandry.mountables.client.models;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class WispModel<T extends Entity> extends EntityModel<T> {
	private final ModelRenderer root;
	private final ModelRenderer arm1;
	private final ModelRenderer arm2;
	private final ModelRenderer tail;
	private final float d;
	private final float c;
	private final float b;
	private final float a;

	public WispModel() {
		texWidth = 48;
		texHeight = 48;

		root = new ModelRenderer(this);
		root.setPos(0.0F, 19.5F, 0.0F);
		root.texOffs(0, 0).addBox(-4.5F, -4.5F, -4.5F, 9.0F, 9.0F, 9.0F, 0.0F, false);

		arm1 = new ModelRenderer(this);
		arm1.setPos(4.5F, 0.0F, 0.0F);
		root.addChild(arm1);
		setRotationAngle(arm1, 0.0F, 0.0F, 0.3927F);
		arm1.texOffs(0, 0).addBox(-0.5F, -0.5F, 0.0F, 4.0F, 1.0F, 0.0F, 0.0F, false);

		arm2 = new ModelRenderer(this);
		arm2.setPos(-4.5F, 0.0F, 0.0F);
		root.addChild(arm2);
		setRotationAngle(arm2, 0.0F, 0.0F, -0.3927F);
		arm2.texOffs(0, 0).addBox(-3.5F, -0.5F, 0.0F, 4.0F, 1.0F, 0.0F, 0.0F, true);

		tail = new ModelRenderer(this);
		tail.setPos(0.0F, 4.5F, 0.0F);
		root.addChild(tail);
		setRotationAngle(tail, 0.3927F, 0.0F, 0.0F);
		tail.texOffs(0, 1).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, false);

		this.a = this.root.xRot;
		this.b = this.arm1.yRot;
		this.c = this.arm2.yRot;
		this.d = this.tail.xRot;
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		root.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	//
	@Override
	public void setupAnim(T mount, float animPosition, float animSpeed, float bob, float rotationSomethingIDK, float xRot) {
		// time should be in seconds. should not be larger than the length of the animation (because you don't want the trig functions to make a circle)
		double time = (mount.tickCount % 40) / 20D;
		this.root.xRot = (float) Math.toRadians( (float) Math.sin(Math.toDegrees(time * 180)) * 2 ) + a;
		this.arm1.yRot = (float) Math.toRadians( (float) Math.sin(Math.toDegrees(time * 180)) * -4 ) + b;
		this.arm2.yRot = (float) Math.toRadians( (float) Math.sin(Math.toDegrees(time * 180)) * 4 ) + c;
		this.tail.xRot = (float) Math.toRadians( (float) Math.sin(Math.toDegrees(-36+time * 180)) * 8 ) + d;
	}

	@Override
	public void prepareMobModel(Entity mount, float p_212843_2_, float p_212843_3_, float p_212843_4_) {

	}
}