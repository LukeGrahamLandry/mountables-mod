package io.github.lukegrahamlandry.mountables.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.client.models.SheepMountModel;
import io.github.lukegrahamlandry.mountables.client.models.SkeletonMountModel;
import io.github.lukegrahamlandry.mountables.client.models.ZombieMountModel;
import io.github.lukegrahamlandry.mountables.client.render.layer.*;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.PhantomEyesLayer;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PhantomModel;
import net.minecraft.client.renderer.entity.model.SnowManModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

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
        if (vanillaType == EntityType.SKELETON) this.addLayer((LayerRenderer<MountEntity, M>) new StrayLayer((IEntityRenderer<MountEntity, SkeletonMountModel>) this));
        if (vanillaType == EntityType.PHANTOM) this.addLayer((LayerRenderer<MountEntity, M>) new PhantomEyesLayer<>((IEntityRenderer<MountEntity, PhantomModel<MountEntity>>) this));
    }

    @Override
    public void render(MountEntity mount, float p_225623_2_, float p_225623_3_, MatrixStack matrixStack, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        if (mount.getVanillaType() == EntityType.PHANTOM) matrixStack.translate(0, -1, 0);
        if (mount.getVanillaType() == EntityType.SQUID) matrixStack.translate(0, -0.5, 0);
        super.render(mount, p_225623_2_, p_225623_3_, matrixStack, p_225623_5_, p_225623_6_);
    }

    @Override
    protected void scale(MountEntity mount, MatrixStack p_225620_2_, float p_225620_3_) {
        if (mount.getVanillaType() == EntityType.GHAST && !mount.isBaby()) p_225620_2_.scale(4.5F, 4.5F, 4.5F);
        if (mount.getVanillaType() == EntityType.WITHER && !mount.isBaby()) p_225620_2_.scale(2,2,2);
    }

    @Override
    protected float getBob(MountEntity mount, float ticks) {
        return mount.getVanillaType() == EntityType.SQUID ? (float) Math.PI / 2: super.getBob(mount, ticks);
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
        } else if (mount.getVanillaType() == EntityType.SKELETON){
            if (mount.getTextureType() == 1) return new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
            if (mount.getTextureType() == 2) return new ResourceLocation("textures/entity/skeleton/stray.png");
        } else if (mount.getVanillaType() == EntityType.HOGLIN && mount.getTextureType() == 1){
            return new ResourceLocation("textures/entity/hoglin/zoglin.png");
        } else if (mount.getVanillaType() == EntityType.SQUID && mount.getTextureType() == 1){
            return new ResourceLocation(MountablesMain.MOD_ID, "textures/entity/glow_squid.png");
        } else if (mount.getVanillaType() == EntityType.GUARDIAN && mount.getTextureType() == 1){
            return new ResourceLocation("textures/entity/guardian_elder.png");
        }



        return TEXTURE_LOCATION;
    }
}