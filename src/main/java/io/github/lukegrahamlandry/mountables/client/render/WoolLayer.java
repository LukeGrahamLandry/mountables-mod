package io.github.lukegrahamlandry.mountables.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.client.models.SheepMountModel;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;

public class WoolLayer extends LayerRenderer<MountEntity, SheepMountModel> {
    private static final ResourceLocation SHEEP_FUR_LOCATION = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    private final SheepMountModel.WoolModel model = new SheepMountModel.WoolModel();

    public WoolLayer(IEntityRenderer<MountEntity, SheepMountModel> p_i50925_1_) {
        super(p_i50925_1_);
    }

    public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, MountEntity mount, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        // MountablesMain.LOGGER.debug(mount.getTextureType());
        if (mount.getTextureType() < 17 && !mount.isInvisible()) {
            float f;
            float f1;
            float f2;
            if (mount.getTextureType() == 16) {
                int i1 = 25;
                int i = mount.tickCount / 25 + mount.getId();
                int j = DyeColor.values().length;
                int k = i % j;
                int l = (i + 1) % j;
                float f3 = ((float)(mount.tickCount % 25) + p_225628_7_) / 25.0F;
                float[] afloat1 = SheepEntity.getColorArray(DyeColor.byId(k));
                float[] afloat2 = SheepEntity.getColorArray(DyeColor.byId(l));
                f = afloat1[0] * (1.0F - f3) + afloat2[0] * f3;
                f1 = afloat1[1] * (1.0F - f3) + afloat2[1] * f3;
                f2 = afloat1[2] * (1.0F - f3) + afloat2[2] * f3;
            } else {
                float[] afloat = SheepEntity.getColorArray(DyeColor.byId(mount.getTextureType()));
                f = afloat[0];
                f1 = afloat[1];
                f2 = afloat[2];
            }

            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, SHEEP_FUR_LOCATION, p_225628_1_, p_225628_2_, p_225628_3_, mount, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_, p_225628_7_, f, f1, f2);
        }
    }
}