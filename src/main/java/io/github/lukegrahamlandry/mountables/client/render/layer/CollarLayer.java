package io.github.lukegrahamlandry.mountables.client.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.lukegrahamlandry.mountables.client.models.WolfMountModel;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.WolfModel;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;

public class CollarLayer extends LayerRenderer<MountEntity, WolfMountModel> {
    private static final ResourceLocation WOLF_COLLAR_LOCATION = new ResourceLocation("textures/entity/wolf/wolf_collar.png");

    public CollarLayer(IEntityRenderer<MountEntity, WolfMountModel> p_i50914_1_) {
        super(p_i50914_1_);
    }

    public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, MountEntity mount, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (!mount.isInvisible() && mount.getColorType() != 0) {
            DyeColor color;

            if (mount.getColorType() <= 16) {
                color = DyeColor.byId(mount.getColorType() - 1);
            } else {
                int index = ((mount.tickCount + mount.getId()) / 25) % 16;
                color = DyeColor.byId(index);
            }

            float[] afloat = color.getTextureDiffuseColors();
            renderColoredCutoutModel(this.getParentModel(), WOLF_COLLAR_LOCATION, p_225628_1_, p_225628_2_, p_225628_3_, mount, afloat[0], afloat[1], afloat[2]);
        }
    }
}
