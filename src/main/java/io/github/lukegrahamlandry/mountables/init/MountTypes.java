package io.github.lukegrahamlandry.mountables.init;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.client.models.*;
import io.github.lukegrahamlandry.mountables.mounts.*;
import net.minecraft.client.renderer.entity.model.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class MountTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MountablesMain.MOD_ID);

    private static final Map<EntityType<? extends LivingEntity>, MountTypeData> mountLookup = new HashMap<>();
    private static final Map<Item, EntityType<? extends LivingEntity>> mountRecipeLookup = new HashMap<>();

    public static MountTypeData get(EntityType<MountEntity> type) {
        return mountLookup.get(type);
    }
    public static EntityType<?> getToCraft(Item item) {
        return mountRecipeLookup.get(item);
    }
    public static Set<EntityType<? extends LivingEntity>> getMountTypes() {
        return mountLookup.keySet();
    }


    private static void create(EntityType<? extends LivingEntity> vanillaType, Item recipe, String texture, Supplier<? extends EntityModel> model){
        String name = vanillaType.getRegistryName().getPath() + "_mount";

        RegistryObject<EntityType<MountEntity>> registryObj = ENTITY_TYPES.register(name,
                () -> EntityType.Builder.of(MountEntity::new, EntityClassification.MISC).sized(vanillaType.getWidth(), vanillaType.getHeight())
                        .build(new ResourceLocation(MountablesMain.MOD_ID, name).toString()));

        mountLookup.put(vanillaType, new MountTypeData(registryObj, recipe, texture, model));
        mountRecipeLookup.put(recipe, vanillaType);
    }

    public static class MountTypeData {
        private final RegistryObject<EntityType<MountEntity>> mountType;
        public final Item recipe;
        public final String textureLocation;
        public final Supplier<? extends EntityModel> modelFactory;

        public MountTypeData(RegistryObject<EntityType<MountEntity>> mountType, Item recipe, String textureLocation, Supplier<? extends EntityModel> modelFactory){
            this.mountType = mountType;
            this.recipe = recipe;
            this.textureLocation = textureLocation;
            this.modelFactory = modelFactory;
        }

        public EntityType<MountEntity> getType() {
            return mountType.get();
        }
    }

    static {
        create(EntityType.SHEEP, Items.MUTTON, "textures/entity/sheep/sheep.png", SheepMountModel::new);
        create(EntityType.PIG, Items.PORKCHOP, "textures/entity/pig/pig.png", PigModel::new);
        create(EntityType.COW, Items.BEEF, "textures/entity/cow/cow.png", CowModel::new);
        create(EntityType.SNOW_GOLEM, Items.SNOWBALL, "textures/entity/snow_golem.png", SnowManModel::new);
        create(EntityType.CHICKEN, Items.CHICKEN, "textures/entity/chicken.png", ChickenModel::new);
        create(EntityType.SPIDER, Items.STRING, "textures/entity/spider/spider.png", SpiderModel::new);
        create(EntityType.CREEPER, Items.GUNPOWDER, "textures/entity/creeper/creeper.png", CreeperModel::new);
        create(EntityType.WOLF, Items.BONE, "textures/entity/wolf/wolf.png", WolfMountModel::new);
        create(EntityType.CAT, Items.COD, "textures/entity/cat/ocelot.png", () -> new OcelotModel<MountEntity>(0.0F));
        create(EntityType.LLAMA, Items.WHEAT, "textures/entity/llama/creamy.png", LlamaMountModel::new);
        create(EntityType.FOX, Items.SWEET_BERRIES, "textures/entity/fox/fox.png", FoxModel::new);
        create(EntityType.PANDA, Items.BAMBOO, "textures/entity/panda/panda.png", PandaMountModel::new);
        create(EntityType.ZOMBIE, Items.ROTTEN_FLESH, "textures/entity/zombie/zombie.png", ZombieMountModel::new);
        create(EntityType.SKELETON, Items.BONE_MEAL, "textures/entity/skeleton/skeleton.png", SkeletonMountModel::new);
        create(EntityType.PHANTOM, Items.PHANTOM_MEMBRANE, "textures/entity/phantom.png", PhantomModel::new);
        create(EntityType.BEE, Items.HONEYCOMB, "textures/entity/bee/bee.png", BeeMountModel::new);
        create(EntityType.GHAST, Items.GHAST_TEAR, "textures/entity/ghast/ghast.png", GhastModel::new);
        create(EntityType.WITHER, Items.SOUL_SAND, "textures/entity/wither/wither.png", WitherMountModel::new);
        create(EntityType.HOGLIN, Items.COOKED_PORKCHOP, "textures/entity/hoglin/hoglin.png", HogMountModel::new);
        create(EntityType.RAVAGER, Items.LEATHER, "textures/entity/illager/ravager.png", RavagerMountModel::new);
        create(EntityType.TURTLE, Items.SCUTE, "textures/entity/turtle/big_sea_turtle.png", TurtleMountModel::new);
        create(EntityType.SQUID, Items.INK_SAC, "textures/entity/squid.png", SquidModel::new);
        create(EntityType.GUARDIAN, Items.PRISMARINE_SHARD, "textures/entity/guardian.png", GuardianMountModel::new);
    }
}
