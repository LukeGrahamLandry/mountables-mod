package io.github.lukegrahamlandry.mountables.client.render;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.client.models.SheepMountModel;
import io.github.lukegrahamlandry.mountables.client.models.ZombieMountModel;
import io.github.lukegrahamlandry.mountables.client.render.layer.DrownedLayer;
import io.github.lukegrahamlandry.mountables.client.render.layer.MushroomLayer;
import io.github.lukegrahamlandry.mountables.client.render.layer.PumpkinHeadLayer;
import io.github.lukegrahamlandry.mountables.client.render.layer.WoolLayer;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SnowManModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.PandaEntity;
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
        if (vanillaType == EntityType.ZOMBIE) this.addLayer((LayerRenderer<MountEntity, M>) new DrownedLayer((IEntityRenderer<MountEntity, ZombieMountModel>) this));
        // this.addLayer(new SaddleLayer<>(this, new PigModel<>(0.5F), new ResourceLocation("textures/entity/pig/pig_saddle.png")));
    }

    public ResourceLocation getTextureLocation(MountEntity mount) {
        if (mount.getVanillaType() == EntityType.COW){
            if (mount.getTextureType() == 1) return new ResourceLocation("textures/entity/cow/red_mooshroom.png");
            if (mount.getTextureType() == 2) return new ResourceLocation("textures/entity/cow/brown_mooshroom.png");
        } else if (mount.getVanillaType() == EntityType.PIG && mount.getTextureType() == 1){
            return new ResourceLocation(MountablesMain.MOD_ID, "textures/entity/nyan_pig.png");
        } else if (mount.getVanillaType() == EntityType.SPIDER && mount.getTextureType() == 1){
            return new ResourceLocation("textures/entity/spider/cave_spider.png");
        } else if (mount.getVanillaType() == EntityType.CAT && mount.getTextureType() >= 1){
            return CatEntity.TEXTURE_BY_TYPE.get(mount.getTextureType() - 1);
        } else if (mount.getVanillaType() == EntityType.LLAMA){
            if (mount.getTextureType() == 1) return new ResourceLocation("textures/entity/llama/white.png");
            if (mount.getTextureType() == 2) return new ResourceLocation("textures/entity/llama/brown.png");
            if (mount.getTextureType() == 3) return new ResourceLocation("textures/entity/llama/gray.png");
        } else if (mount.getVanillaType() == EntityType.FOX && mount.getTextureType() == 1) {
            return new ResourceLocation(MountablesMain.MOD_ID, "textures/entity/fox/snow_fox.png");
        } else if (mount.getVanillaType() == EntityType.PANDA) {
            if (mount.getTextureType() == 1) return new ResourceLocation("textures/entity/panda/lazy_panda.png");
            if (mount.getTextureType() == 2) return new ResourceLocation("textures/entity/panda/worried_panda.png");
            if (mount.getTextureType() == 3) return new ResourceLocation("textures/entity/panda/playful_panda.png");
            if (mount.getTextureType() == 4) return new ResourceLocation("textures/entity/panda/brown_panda.png");
            if (mount.getTextureType() == 5) return new ResourceLocation("textures/entity/panda/weak_panda.png");
            if (mount.getTextureType() == 6) return new ResourceLocation("textures/entity/panda/aggressive_panda.png");
        } else if (mount.getVanillaType() == EntityType.ZOMBIE){
            if (mount.getTextureType() == 1) return new ResourceLocation("textures/entity/zombie/husk.png");
            if (mount.getTextureType() == 2) return new ResourceLocation("textures/entity/zombie/drowned.png");
        }

        return TEXTURE_LOCATION;
    }
}