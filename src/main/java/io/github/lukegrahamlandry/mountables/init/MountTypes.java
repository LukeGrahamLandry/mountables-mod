package io.github.lukegrahamlandry.mountables.init;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.mounts.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MountTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MountablesMain.MOD_ID);

    private static final Map<EntityType<? extends LivingEntity>, MountTypeData> mountLookup = new HashMap<>();
    private static final Map<Item, EntityType<? extends LivingEntity>> mountRecipeLookup = new HashMap<>();

    public static MountTypeData get(EntityType type) {
        return mountLookup.get(type);
    }
    public static EntityType<?> getToCraft(Item item) {
        return mountRecipeLookup.get(item);
    }
    public static Set<EntityType<? extends LivingEntity>> getMountTypes() {
        return mountLookup.keySet();
    }

    private static void create(EntityType<? extends LivingEntity> vanillaType, Item recipe, String texture){
        String name = vanillaType.getRegistryName().getPath() + "_mount";

        RegistryObject<EntityType<MountEntity>> registryObj = ENTITY_TYPES.register(name,
                () -> EntityType.Builder.of(MountEntity::new, EntityClassification.MISC).sized(vanillaType.getWidth(), vanillaType.getHeight())
                        .build(new ResourceLocation(MountablesMain.MOD_ID, name).toString()));

        mountLookup.put(vanillaType, new MountTypeData(registryObj, recipe, texture));
        mountRecipeLookup.put(recipe, vanillaType);
    }

    public static class MountTypeData {
        private final RegistryObject<EntityType<MountEntity>> mountType;
        public final Item recipe;
        public final String textureLocation;

        public MountTypeData(RegistryObject<EntityType<MountEntity>> mountType, Item recipe, String textureLocation){
            this.mountType = mountType;
            this.recipe = recipe;
            this.textureLocation = textureLocation;
        }

        public EntityType<MountEntity> getType() {
            return mountType.get();
        }
    }

    static {
        create(EntityType.SHEEP, Items.MUTTON, "textures/entity/sheep/sheep.png");
        create(EntityType.PIG, Items.PORKCHOP, "textures/entity/pig/pig.png");
        create(EntityType.COW, Items.BEEF, "textures/entity/cow/cow.png");
        create(EntityType.SNOW_GOLEM, Items.SNOWBALL, "textures/entity/snow_golem.png");
        create(EntityType.CHICKEN, Items.CHICKEN, "textures/entity/chicken.png");
        create(EntityType.SPIDER, Items.STRING, "textures/entity/spider/spider.png");
        create(EntityType.CREEPER, Items.GUNPOWDER, "textures/entity/creeper/creeper.png");
        create(EntityType.WOLF, Items.BONE, "textures/entity/wolf/wolf.png");
        create(EntityType.CAT, Items.COD, "textures/entity/cat/ocelot.png");
        create(EntityType.LLAMA, Items.WHEAT, "textures/entity/llama/creamy.png");
        create(EntityType.FOX, Items.SWEET_BERRIES, "textures/entity/fox/fox.png");
        create(EntityType.PANDA, Items.BAMBOO, "textures/entity/panda/panda.png");
        create(EntityType.ZOMBIE, Items.ROTTEN_FLESH, "textures/entity/zombie/zombie.png");
        create(EntityType.SKELETON, Items.ARROW, "textures/entity/skeleton/skeleton.png");
        create(EntityType.PHANTOM, Items.PHANTOM_MEMBRANE, "textures/entity/phantom.png");
        create(EntityType.BEE, Items.HONEYCOMB, "textures/entity/bee/bee.png");
        create(EntityType.GHAST, Items.GHAST_TEAR, "textures/entity/ghast/ghast.png");
        create(EntityType.WITHER, Items.SOUL_SAND, "textures/entity/wither/wither.png");
        create(EntityType.HOGLIN, Items.COOKED_PORKCHOP, "textures/entity/hoglin/hoglin.png");
        create(EntityType.RAVAGER, Items.LEATHER, "textures/entity/illager/ravager.png");
        create(EntityType.TURTLE, Items.SCUTE, "textures/entity/turtle/big_sea_turtle.png");
        create(EntityType.SQUID, Items.INK_SAC, "textures/entity/squid.png");
        create(EntityType.GUARDIAN, Items.PRISMARINE_SHARD, "textures/entity/guardian.png");
        create(EntityType.SLIME, Items.SLIME_BALL,  "textures/entity/slime/slime.png");
        create(EntityType.MAGMA_CUBE, Items.BEDROCK,  "textures/entity/slime/magmacube.png");
        create(EntityType.SILVERFISH, Items.STONE,  "textures/entity/silverfish.png");
        create(EntityType.STRIDER, Items.BASALT,  "textures/entity/strider/strider.png");
        create(EntityType.IRON_GOLEM, Items.IRON_BLOCK,  "textures/entity/iron_golem/iron_golem.png");
        create(EntityType.RABBIT, Items.RABBIT,  "textures/entity/rabbit/brown.png");
        create(EntityType.BLAZE, Items.BLAZE_ROD,  "textures/entity/blaze.png");
        create(EntityType.PIGLIN, Items.GOLD_INGOT,  "textures/entity/piglin/piglin.png");
    }
}
