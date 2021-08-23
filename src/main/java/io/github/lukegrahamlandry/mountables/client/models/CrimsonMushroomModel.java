package io.github.lukegrahamlandry.mountables.client.models;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class CrimsonMushroomModel<T extends MountEntity> extends EntityModel<T> {
	private final ModelRenderer main;
	private final ModelRenderer head;
	private final ModelRenderer stem;

	public CrimsonMushroomModel() {
		texWidth = 48;
		texHeight = 48;

		main = new ModelRenderer(this);
		main.setPos(0.0F, 18.0F, 0.0F);
		

		head = new ModelRenderer(this);
		head.setPos(0.0F, 1.0F, 0.0F);
		main.addChild(head);
		head.texOffs(0, 0).addBox(-4.5F, -3.0F, -4.5F, 9.0F, 3.0F, 9.0F, 0.0F, false);
		head.texOffs(0, 12).addBox(-3.0F, -7.0F, -3.0F, 6.0F, 4.0F, 6.0F, 0.0F, false);

		stem = new ModelRenderer(this);
		stem.setPos(0.0F, 0.0F, 0.0F);
		main.addChild(stem);
		stem.texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(MountEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {

	}


	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		main.render(matrixStack, buffer, packedLight, packedOverlay);
	}
}