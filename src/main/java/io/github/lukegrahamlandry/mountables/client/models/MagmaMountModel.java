package io.github.lukegrahamlandry.mountables.client.models;

import com.google.common.collect.ImmutableList;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;

public class MagmaMountModel extends SegmentedModel<MountEntity> {
    private final ModelRenderer[] bodyCubes = new ModelRenderer[8];
    private final ModelRenderer insideCube;
    private final ImmutableList<ModelRenderer> parts;

    public MagmaMountModel() {
        for(int i = 0; i < this.bodyCubes.length; ++i) {
            int j = 0;
            int k = i;
            if (i == 2) {
                j = 24;
                k = 10;
            } else if (i == 3) {
                j = 24;
                k = 19;
            }

            this.bodyCubes[i] = new ModelRenderer(this, j, k);
            this.bodyCubes[i].addBox(-4.0F, (float)(16 + i), -4.0F, 8.0F, 1.0F, 8.0F);
        }

        this.insideCube = new ModelRenderer(this, 0, 16);
        this.insideCube.addBox(-2.0F, 18.0F, -2.0F, 4.0F, 4.0F, 4.0F);
        ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
        builder.add(this.insideCube);
        builder.addAll(Arrays.asList(this.bodyCubes));
        this.parts = builder.build();
    }

    public void setupAnim(MountEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
    }


    public void prepareMobModel(MountEntity mount, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
        float f = MathHelper.lerp(p_212843_4_, mount.oSquish, mount.squish);
        if (f < 0.0F) {
            f = 0.0F;
        }

        for(int i = 0; i < this.bodyCubes.length; ++i) {
            this.bodyCubes[i].y = (float)(-(4 - i)) * f * 1.7F;
        }

    }

    public ImmutableList<ModelRenderer> parts() {
        return this.parts;
    }
}