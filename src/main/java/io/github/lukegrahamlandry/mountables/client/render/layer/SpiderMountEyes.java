package io.github.lukegrahamlandry.mountables.client.render.layer;

import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class SpiderMountEyes extends AbstractEyesLayer<MountEntity, EntityModel<MountEntity>> {
    private static final RenderType SPIDER_EYES = RenderType.eyes(new ResourceLocation("textures/entity/spider_eyes.png"));

    public SpiderMountEyes(IEntityRenderer<MountEntity, EntityModel<MountEntity>> p_i50921_1_) {
        super(p_i50921_1_);
    }

    public RenderType renderType() {
        return SPIDER_EYES;
    }
}