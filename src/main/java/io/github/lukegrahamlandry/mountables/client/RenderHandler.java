package io.github.lukegrahamlandry.mountables.client;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.client.models.*;
import io.github.lukegrahamlandry.mountables.client.render.GenericMountRenderer;
import io.github.lukegrahamlandry.mountables.init.MountTypes;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.renderer.entity.PillagerRenderer;
import net.minecraft.client.renderer.entity.model.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = MountablesMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderHandler {
    static Map<EntityType, Supplier<? extends EntityModel>> mountModels = new HashMap<>();
    static {
        mountModels.put(EntityType.SHEEP, SheepMountModel::new);
        mountModels.put(EntityType.PIG, PigModel::new);
        mountModels.put(EntityType.COW, CowModel::new);
        mountModels.put(EntityType.SNOW_GOLEM, SnowManModel::new);
        mountModels.put(EntityType.CHICKEN, ChickenModel::new);
        mountModels.put(EntityType.SPIDER, SpiderModel::new);
        mountModels.put(EntityType.CREEPER, CreeperModel::new);
        mountModels.put(EntityType.WOLF, WolfMountModel::new);
        mountModels.put(EntityType.CAT, () -> new OcelotModel<MountEntity>(0.0F));
        mountModels.put(EntityType.LLAMA, LlamaMountModel::new);
        mountModels.put(EntityType.FOX, FoxMountModel::new);
        mountModels.put(EntityType.PANDA, PandaMountModel::new);
        mountModels.put(EntityType.ZOMBIE, ZombieMountModel::new);
        mountModels.put(EntityType.SKELETON, SkeletonMountModel::new);
        mountModels.put(EntityType.PHANTOM, PhantomModel::new);
        mountModels.put(EntityType.BEE, BeeMountModel::new);
        mountModels.put(EntityType.GHAST, GhastModel::new);
        mountModels.put(EntityType.WITHER, WitherMountModel::new);
        mountModels.put(EntityType.HOGLIN, HogMountModel::new);
        mountModels.put(EntityType.RAVAGER, RavagerMountModel::new);
        mountModels.put(EntityType.TURTLE, TurtleMountModel::new);
        mountModels.put(EntityType.SQUID, SquidModel::new);
        mountModels.put(EntityType.GUARDIAN, GuardianMountModel::new);
        mountModels.put(EntityType.SLIME, () -> new SlimeModel<MountEntity>(16));
        mountModels.put(EntityType.MAGMA_CUBE, MagmaMountModel::new);
        mountModels.put(EntityType.SILVERFISH, SilverfishModel::new);
        mountModels.put(EntityType.STRIDER, StriderMountModel::new);
        mountModels.put(EntityType.IRON_GOLEM, IronGolemMountModel::new);
        mountModels.put(EntityType.RABBIT, RabbitMountModel::new);
        mountModels.put(EntityType.BLAZE, BlazeModel::new);
        mountModels.put(EntityType.PIGLIN, () -> new PiglinModel<>(0.0F, 64, 64));
        mountModels.put(EntityType.COD, CodModel::new);
        mountModels.put(EntityType.PILLAGER, IllagerMountModel::new);
        mountModels.put(EntityType.ENDERMAN, () -> new EndermanModel<>(0));
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MountTypes.createExtraMounts(); // client only
        mountModels.put(MountTypes.MUSHROOM.get(), RedMushroomModel::new);
        mountModels.put(MountTypes.WISP.get(), WispModel::new);
        mountModels.put(MountTypes.BLUE_ELEPHANT.get(), BlueElephantModel::new);

        for (EntityType vanillaType : mountModels.keySet()){
            MountTypes.MountTypeData thing = MountTypes.get(vanillaType);
            RenderingRegistry.registerEntityRenderingHandler(thing.getType(), (manager) -> {
                return new GenericMountRenderer<>(manager, vanillaType, thing.textureLocation, mountModels.getOrDefault(vanillaType, null));
            });
        }
    }
}
