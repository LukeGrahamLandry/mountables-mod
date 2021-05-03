package io.github.lukegrahamlandry.mountables.client.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SnowManModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class PumpkinHeadLayer extends LayerRenderer<MountEntity, SnowManModel<MountEntity>> {
    public PumpkinHeadLayer(IEntityRenderer<MountEntity, SnowManModel<MountEntity>> p_i50922_1_) {
        super(p_i50922_1_);
    }

    public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, MountEntity p_225628_4_, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (!p_225628_4_.isInvisible() && p_225628_4_.getTextureType() == 0) {
            p_225628_1_.pushPose();
            this.getParentModel().getHead().translateAndRotate(p_225628_1_);
            float f = 0.625F;
            p_225628_1_.translate(0.0D, -0.34375D, 0.0D);
            p_225628_1_.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            p_225628_1_.scale(0.625F, -0.625F, -0.625F);
            ItemStack itemstack = new ItemStack(Blocks.CARVED_PUMPKIN);
            Minecraft.getInstance().getItemRenderer().renderStatic(p_225628_4_, itemstack, ItemCameraTransforms.TransformType.HEAD, false, p_225628_1_, p_225628_2_, p_225628_4_.level, p_225628_3_, LivingRenderer.getOverlayCoords(p_225628_4_, 0.0F));
            p_225628_1_.popPose();
        }
    }
}