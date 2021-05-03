package io.github.lukegrahamlandry.mountables.client.render;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.client.models.SheepMountModel;
import io.github.lukegrahamlandry.mountables.client.render.layer.MushroomLayer;
import io.github.lukegrahamlandry.mountables.client.render.layer.PumpkinHeadLayer;
import io.github.lukegrahamlandry.mountables.client.render.layer.WoolLayer;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SnowManModel;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public class GenericMountRenderer<M extends EntityModel<MountEntity>> extends MobRenderer<MountEntity, M> {
    private final ResourceLocation TEXTURE_LOCATION;

    public GenericMountRenderer(EntityRendererManager p_i47198_1_, EntityType vanillaType, String location, Supplier<M> modelFactory) {
        super(p_i47198_1_, modelFactory.get(), 0.7F);
        TEXTURE_LOCATION = new ResourceLocation(location);
        if (vanillaType == EntityType.SHEEP) this.addLayer((LayerRenderer<MountEntity, M>) new WoolLayer((IEntityRenderer<MountEntity, SheepMountModel>) this));
        if (vanillaType == EntityType.COW) this.addLayer((LayerRenderer<MountEntity, M>) new MushroomLayer((IEntityRenderer<MountEntity, CowModel<MountEntity>>) this));
        if (vanillaType == EntityType.SNOW_GOLEM) this.addLayer((LayerRenderer<MountEntity, M>) new PumpkinHeadLayer((IEntityRenderer<MountEntity, SnowManModel<MountEntity>>) this));
        // this.addLayer(new SaddleLayer<>(this, new PigModel<>(0.5F), new ResourceLocation("textures/entity/pig/pig_saddle.png")));
    }

    public ResourceLocation getTextureLocation(MountEntity mount) {
        if (mount.getVanillaType() == EntityType.COW){
            if (mount.getTextureType() == 1) return new ResourceLocation("textures/entity/cow/red_mooshroom.png");
            if (mount.getTextureType() == 2) return new ResourceLocation("textures/entity/cow/brown_mooshroom.png");
        } else if (mount.getVanillaType() == EntityType.PIG){
            if (mount.getTextureType() == 1) return new ResourceLocation(MountablesMain.MOD_ID, "textures/entity/nyan_pig.png");
        } else if (mount.getVanillaType() == EntityType.SPIDER){
            if (mount.getTextureType() == 1) return new ResourceLocation("textures/entity/spider/cave_spider.png");
        }

        return TEXTURE_LOCATION;
    }
}