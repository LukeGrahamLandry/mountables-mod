package io.github.lukegrahamlandry.mountables.client.models;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class WarpedMushroomModel<T extends Entity> extends EntityModel<T> {
	private final ModelRenderer main;
	private final ModelRenderer head;
	private final ModelRenderer stem;

	public WarpedMushroomModel() {
		texWidth = 40;
		texHeight = 16;

		main = new ModelRenderer(this);
		main.setPos(0.0F, 19.0F, 0.0F);
		

		head = new ModelRenderer(this);
		head.setPos(0.0F, 0.0F, 0.0F);
		main.addChild(head);
		head.texOffs(0, 0).addBox(-5.0F, -4.0F, -5.0F, 10.0F, 4.0F, 10.0F, 0.0F, false);

		stem = new ModelRenderer(this);
		stem.setPos(0.0F, 0.0F, 0.0F);
		main.addChild(stem);
		stem.texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		main.render(matrixStack, buffer, packedLight, packedOverlay);
	}
}