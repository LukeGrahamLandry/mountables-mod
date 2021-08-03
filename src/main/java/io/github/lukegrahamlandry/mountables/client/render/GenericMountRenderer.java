package io.github.lukegrahamlandry.mountables.client.render;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.client.models.*;
import io.github.lukegrahamlandry.mountables.client.render.layer.*;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.model.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.fish.PufferfishEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.Map;
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
        if (vanillaType == EntityType.SLIME) this.addLayer((LayerRenderer<MountEntity, M>) new SlimeGelLayer(this));
        if (vanillaType == EntityType.LLAMA) this.addLayer((LayerRenderer<MountEntity, M>) new LlamaCarpetLayer((IEntityRenderer<MountEntity, LlamaMountModel>) this));
        if (vanillaType == EntityType.CREEPER) this.addLayer((LayerRenderer<MountEntity, M>) new ChargedCreeperLayer((IEntityRenderer<MountEntity, CreeperModel<MountEntity>>) this));;
        if (vanillaType == EntityType.WOLF) this.addLayer((LayerRenderer<MountEntity, M>) new CollarLayer((IEntityRenderer<MountEntity, WolfMountModel>) this));
    }


    private final CodModel<MountEntity> cod = new CodModel<>();
    private final SalmonModel<MountEntity> salmon = new SalmonModel<>();
    private final PufferFishSmallModel<MountEntity> smallPuff = new PufferFishSmallModel<>();
    private final PufferFishMediumModel<MountEntity> midPuff = new PufferFishMediumModel<>();
    private final PufferFishBigModel<MountEntity> bigPuff = new PufferFishBigModel<>();

    @Override
    public void render(MountEntity mount, float p_225623_2_, float p_225623_3_, MatrixStack matrixStack, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        if (mount.getVanillaType() == EntityType.PHANTOM) matrixStack.translate(0, -1, 0);
        if (mount.getVanillaType() == EntityType.SQUID) matrixStack.translate(0, -0.5, 0);
        if (mount.getVanillaType() == EntityType.COD){
            if (mount.getTextureType() == 0) this.model = (M) cod;
            if (mount.getTextureType() == 1) this.model = (M) salmon;
            if (mount.getTextureType() == 2) this.model = (M) smallPuff;
            if (mount.getTextureType() == 3) this.model = (M) midPuff;
            if (mount.getTextureType() == 4) this.model = (M) bigPuff;
        }
        super.render(mount, p_225623_2_, p_225623_3_, matrixStack, p_225623_5_, p_225623_6_);
    }

    @Override
    protected void scale(MountEntity mount, MatrixStack p_225620_2_, float p_225620_3_) {
        if (mount.getVanillaType() == EntityType.GHAST && !mount.isBaby()) p_225620_2_.scale(4.5F, 4.5F, 4.5F);
        if (mount.getVanillaType() == EntityType.WITHER && !mount.isBaby()) p_225620_2_.scale(2,2,2);
        if (mount.getVanillaType() == EntityType.SLIME){
            float size = mount.isBaby() ? 1 : 4;
            p_225620_2_.scale(0.999F, 0.999F, 0.999F);
            p_225620_2_.translate(0.0D, (double)0.001F, 0.0D);
            float f2 = MathHelper.lerp(p_225620_3_, mount.oSquish, mount.squish) / (size * 0.5F + 1.0F);
            float f3 = 1.0F / (f2 + 1.0F);
            p_225620_2_.scale(f3 * size, 1.0F / f3 * size, f3 * size);
        }
        if (mount.getVanillaType() == EntityType.MAGMA_CUBE){
            float size = mount.isBaby() ? 1 : 4;
            float f = MathHelper.lerp(p_225620_3_, mount.oSquish, mount.squish) / (size * 0.5F + 1.0F);
            float f1 = 1.0F / (f + 1.0F);
            p_225620_2_.scale(f1 * size, 1.0F / f1 * size, f1 * size);
        }
    }

    @Override
    protected float getBob(MountEntity mount, float ticks) {
        if (mount.getVanillaType() == EntityType.CHICKEN) return 0;
        return mount.getVanillaType() == EntityType.SQUID ? (float) Math.PI / 2: super.getBob(mount, ticks);
    }


    private static final ResourceLocation RABBIT_BROWN_LOCATION = new ResourceLocation("textures/entity/rabbit/brown.png");
    private static final ResourceLocation RABBIT_WHITE_LOCATION = new ResourceLocation("textures/entity/rabbit/white.png");
    private static final ResourceLocation RABBIT_BLACK_LOCATION = new ResourceLocation("textures/entity/rabbit/black.png");
    private static final ResourceLocation RABBIT_GOLD_LOCATION = new ResourceLocation("textures/entity/rabbit/gold.png");
    private static final ResourceLocation RABBIT_SALT_LOCATION = new ResourceLocation("textures/entity/rabbit/salt.png");
    private static final ResourceLocation RABBIT_WHITE_SPLOTCHED_LOCATION = new ResourceLocation("textures/entity/rabbit/white_splotched.png");
    private static final ResourceLocation RABBIT_TOAST_LOCATION = new ResourceLocation("textures/entity/rabbit/toast.png");
    private static final ResourceLocation RABBIT_EVIL_LOCATION = new ResourceLocation("textures/entity/rabbit/caerbannog.png");

    static final Map<EntityType<?>, ResourceLocation> piglinTextures = ImmutableMap.of(EntityType.PIGLIN, new ResourceLocation("textures/entity/piglin/piglin.png"), EntityType.ZOMBIFIED_PIGLIN, new ResourceLocation("textures/entity/piglin/zombified_piglin.png"), EntityType.PIGLIN_BRUTE, new ResourceLocation("textures/entity/piglin/piglin_brute.png"));

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
        } else if (mount.getVanillaType() == EntityType.STRIDER && mount.getTextureType() == 1){
            return new ResourceLocation("textures/entity/strider/strider_cold.png");
        }else if (mount.getVanillaType() == EntityType.SILVERFISH && mount.getTextureType() == 1){
            return new ResourceLocation("textures/entity/endermite.png");
        } else if (mount.getVanillaType() == EntityType.RABBIT) {
            if (mount.getTextureType() == 1) return RABBIT_WHITE_LOCATION;
            if (mount.getTextureType() == 2) return RABBIT_BLACK_LOCATION;
            if (mount.getTextureType() == 3) return RABBIT_GOLD_LOCATION;
            if (mount.getTextureType() == 4) return RABBIT_SALT_LOCATION;
            if (mount.getTextureType() == 5) return RABBIT_WHITE_SPLOTCHED_LOCATION;
            if (mount.getTextureType() == 6) return RABBIT_TOAST_LOCATION;
            if (mount.getTextureType() == 7) return RABBIT_EVIL_LOCATION;
        } else if (mount.getVanillaType() == EntityType.PIGLIN) {
            if (mount.getTextureType() == 1) return piglinTextures.get(EntityType.ZOMBIFIED_PIGLIN);
            if (mount.getTextureType() == 2) return piglinTextures.get(EntityType.PIGLIN_BRUTE);
        } else if (mount.getVanillaType() == EntityType.PILLAGER) {
            if (mount.getTextureType() == 1) return new ResourceLocation("textures/entity/illager/vindicator.png");
            if (mount.getTextureType() == 2) return new ResourceLocation("textures/entity/illager/illusioner.png");
            if (mount.getTextureType() == 3) return new ResourceLocation("textures/entity/illager/evoker.png");
            if (mount.getTextureType() == 4) return new ResourceLocation("textures/entity/witch.png");
        } else if (mount.getVanillaType() == EntityType.COD) {
            if (mount.getTextureType() == 1) return new ResourceLocation("textures/entity/fish/salmon.png");
            if (mount.getTextureType() > 1) return new ResourceLocation("textures/entity/fish/pufferfish.png");
        }

        return TEXTURE_LOCATION;
    }
}