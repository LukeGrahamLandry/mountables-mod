package io.github.lukegrahamlandry.mountables.client.render.layer;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.PhantomModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class PhantomMountEyesLayer<T extends Entity> extends AbstractEyesLayer<T, PhantomModel<T>> {
    private static final RenderType PHANTOM_EYES = RenderType.eyes(new ResourceLocation("textures/entity/phantom_eyes.png"));
    private static final RenderType PIRATE_PHANTOM_EYES = RenderType.eyes(new ResourceLocation(MountablesMain.MOD_ID, "textures/entity/jolly_roger_eyes.png"));
    public int texture;

    public PhantomMountEyesLayer(IEntityRenderer<T, PhantomModel<T>> model) {
        super(model);
    }

    public RenderType renderType() {
        return texture == 0 ? PHANTOM_EYES : PIRATE_PHANTOM_EYES;
    }
}