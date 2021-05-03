package io.github.lukegrahamlandry.mountables.client.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.util.math.vector.Vector3f;

public class MushroomLayer extends LayerRenderer<MountEntity, CowModel<MountEntity>> {
    public MushroomLayer(IEntityRenderer<MountEntity, CowModel<MountEntity>> p_i50931_1_) {
        super(p_i50931_1_);
    }

    public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, MountEntity mount, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (!mount.isBaby() && !mount.isInvisible() && mount.getTextureType() > 0) {
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
            BlockState blockstate = mount.getTextureType() == 1 ? Blocks.RED_MUSHROOM.defaultBlockState() : Blocks.BROWN_MUSHROOM.defaultBlockState();
            int i = LivingRenderer.getOverlayCoords(mount, 0.0F);
            p_225628_1_.pushPose();
            p_225628_1_.translate((double)0.2F, (double)-0.35F, 0.5D);
            p_225628_1_.mulPose(Vector3f.YP.rotationDegrees(-48.0F));
            p_225628_1_.scale(-1.0F, -1.0F, 1.0F);
            p_225628_1_.translate(-0.5D, -0.5D, -0.5D);
            blockrendererdispatcher.renderSingleBlock(blockstate, p_225628_1_, p_225628_2_, p_225628_3_, i);
            p_225628_1_.popPose();
            p_225628_1_.pushPose();
            p_225628_1_.translate((double)0.2F, (double)-0.35F, 0.5D);
            p_225628_1_.mulPose(Vector3f.YP.rotationDegrees(42.0F));
            p_225628_1_.translate((double)0.1F, 0.0D, (double)-0.6F);
            p_225628_1_.mulPose(Vector3f.YP.rotationDegrees(-48.0F));
            p_225628_1_.scale(-1.0F, -1.0F, 1.0F);
            p_225628_1_.translate(-0.5D, -0.5D, -0.5D);
            blockrendererdispatcher.renderSingleBlock(blockstate, p_225628_1_, p_225628_2_, p_225628_3_, i);
            p_225628_1_.popPose();
            p_225628_1_.pushPose();
            this.getParentModel().getHead().translateAndRotate(p_225628_1_);
            p_225628_1_.translate(0.0D, (double)-0.7F, (double)-0.2F);
            p_225628_1_.mulPose(Vector3f.YP.rotationDegrees(-78.0F));
            p_225628_1_.scale(-1.0F, -1.0F, 1.0F);
            p_225628_1_.translate(-0.5D, -0.5D, -0.5D);
            blockrendererdispatcher.renderSingleBlock(blockstate, p_225628_1_, p_225628_2_, p_225628_3_, i);
            p_225628_1_.popPose();
        }
    }
}