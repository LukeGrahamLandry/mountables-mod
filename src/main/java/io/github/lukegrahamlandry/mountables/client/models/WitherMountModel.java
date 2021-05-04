package io.github.lukegrahamlandry.mountables.client.models;

import com.google.common.collect.ImmutableList;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;

public class WitherMountModel extends SegmentedModel<MountEntity> {
    private final ModelRenderer[] upperBodyParts;
    private final ModelRenderer[] heads;
    private final ImmutableList<ModelRenderer> parts;

    public WitherMountModel() {
        float p_i46302_1_ = 0;
        this.texWidth = 64;
        this.texHeight = 64;
        this.upperBodyParts = new ModelRenderer[3];
        this.upperBodyParts[0] = new ModelRenderer(this, 0, 16);
        this.upperBodyParts[0].addBox(-10.0F, 3.9F, -0.5F, 20.0F, 3.0F, 3.0F, p_i46302_1_);
        this.upperBodyParts[1] = (new ModelRenderer(this)).setTexSize(this.texWidth, this.texHeight);
        this.upperBodyParts[1].setPos(-2.0F, 6.9F, -0.5F);
        this.upperBodyParts[1].texOffs(0, 22).addBox(0.0F, 0.0F, 0.0F, 3.0F, 10.0F, 3.0F, p_i46302_1_);
        this.upperBodyParts[1].texOffs(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11.0F, 2.0F, 2.0F, p_i46302_1_);
        this.upperBodyParts[1].texOffs(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11.0F, 2.0F, 2.0F, p_i46302_1_);
        this.upperBodyParts[1].texOffs(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11.0F, 2.0F, 2.0F, p_i46302_1_);
        this.upperBodyParts[2] = new ModelRenderer(this, 12, 22);
        this.upperBodyParts[2].addBox(0.0F, 0.0F, 0.0F, 3.0F, 6.0F, 3.0F, p_i46302_1_);
        this.heads = new ModelRenderer[3];
        this.heads[0] = new ModelRenderer(this, 0, 0);
        this.heads[0].addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, p_i46302_1_);
        this.heads[1] = new ModelRenderer(this, 32, 0);
        this.heads[1].addBox(-4.0F, -4.0F, -4.0F, 6.0F, 6.0F, 6.0F, p_i46302_1_);
        this.heads[1].x = -8.0F;
        this.heads[1].y = 4.0F;
        this.heads[2] = new ModelRenderer(this, 32, 0);
        this.heads[2].addBox(-4.0F, -4.0F, -4.0F, 6.0F, 6.0F, 6.0F, p_i46302_1_);
        this.heads[2].x = 10.0F;
        this.heads[2].y = 4.0F;
        ImmutableList.Builder<ModelRenderer> builder = ImmutableList.builder();
        builder.addAll(Arrays.asList(this.heads));
        builder.addAll(Arrays.asList(this.upperBodyParts));
        this.parts = builder.build();
    }

    public ImmutableList<ModelRenderer> parts() {
        return this.parts;
    }

    public void setupAnim(MountEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
        float f = MathHelper.cos(p_225597_4_ * 0.1F);
        this.upperBodyParts[1].xRot = (0.065F + 0.05F * f) * (float)Math.PI;
        this.upperBodyParts[2].setPos(-2.0F, 6.9F + MathHelper.cos(this.upperBodyParts[1].xRot) * 10.0F, -0.5F + MathHelper.sin(this.upperBodyParts[1].xRot) * 10.0F);
        this.upperBodyParts[2].xRot = (0.265F + 0.1F * f) * (float)Math.PI;
        this.heads[0].yRot = p_225597_5_ * ((float)Math.PI / 180F);
        this.heads[0].xRot = p_225597_6_ * ((float)Math.PI / 180F);
    }

    public void prepareMobModel(MountEntity p_212843_1_, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
        for(int i = 1; i < 3; ++i) {
            // p_212843_1_.getHeadYRot(i - 1)
            this.heads[i].yRot = (p_212843_1_.getYHeadRot() - p_212843_1_.yBodyRot) * ((float)Math.PI / 180F);
            this.heads[i].xRot = 0; // p_212843_1_.getHeadXRot(i - 1) * ((float)Math.PI / 180F);
        }

    }
}