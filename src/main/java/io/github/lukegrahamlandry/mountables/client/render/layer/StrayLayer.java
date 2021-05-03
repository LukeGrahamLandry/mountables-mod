package io.github.lukegrahamlandry.mountables.client.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.lukegrahamlandry.mountables.client.models.SkeletonMountModel;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.util.ResourceLocation;

public class StrayLayer extends LayerRenderer<MountEntity, SkeletonMountModel> {
    private static final ResourceLocation STRAY_CLOTHES_LOCATION = new ResourceLocation("textures/entity/skeleton/stray_overlay.png");
    private final SkeletonMountModel layerModel = new SkeletonMountModel(0.25F, true);

    public StrayLayer(IEntityRenderer<MountEntity, SkeletonMountModel> p_i50919_1_) {
        super(p_i50919_1_);
    }

    public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, MountEntity mount, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (mount.getTextureType() == 2) coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, STRAY_CLOTHES_LOCATION, p_225628_1_, p_225628_2_, p_225628_3_, mount, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_, p_225628_7_, 1.0F, 1.0F, 1.0F);
    }
}