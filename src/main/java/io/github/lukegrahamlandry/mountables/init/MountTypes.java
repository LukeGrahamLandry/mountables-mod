package io.github.lukegrahamlandry.mountables.init;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.mounts.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
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

    private static void create(EntityType<? extends LivingEntity> vanillaType, Item recipe, String texture, int textureCount){
        String name = vanillaType.getRegistryName().getPath() + "_mount";

        RegistryObject<EntityType<MountEntity>> registryObj = ENTITY_TYPES.register(name,
                () -> EntityType.Builder.of(MountEntity::new, EntityClassification.MISC).sized(vanillaType.getWidth(), vanillaType.getHeight())
                        .build(new ResourceLocation(MountablesMain.MOD_ID, name).toString()));

        mountLookup.put(vanillaType, new MountTypeData(registryObj, recipe, texture, textureCount));
        mountRecipeLookup.put(recipe, vanillaType);
    }

    public static class MountTypeData {
        private final RegistryObject<EntityType<MountEntity>> mountType;
        public final Item recipe;
        public final String textureLocation;
        public int textureCount;

        public MountTypeData(RegistryObject<EntityType<MountEntity>> mountType, Item recipe, String textureLocation, int textureCount){
            this.mountType = mountType;
            this.recipe = recipe;
            this.textureLocation = textureLocation;
            this.textureCount = textureCount;
        }

        public EntityType<MountEntity> getType() {
            return mountType.get();
        }
    }

    static {
        create(EntityType.SHEEP, Items.MUTTON, "textures/entity/sheep/sheep.png", 1);
        create(EntityType.PIG, Items.PORKCHOP, "textures/entity/pig/pig.png", 2);
        create(EntityType.COW, Items.BEEF, "textures/entity/cow/cow.png", 3);
        create(EntityType.SNOW_GOLEM, Items.SNOWBALL, "textures/entity/snow_golem.png", 1);
        create(EntityType.CHICKEN, Items.CHICKEN, "textures/entity/chicken.png", 1);
        create(EntityType.SPIDER, Items.STRING, "textures/entity/spider/spider.png", 2);
        create(EntityType.CREEPER, Items.GUNPOWDER, "textures/entity/creeper/creeper.png", 2);
        create(EntityType.WOLF, Items.BONE, "textures/entity/wolf/wolf.png", 2);
        create(EntityType.CAT, Items.COD, "textures/entity/cat/ocelot.png", CatEntity.TEXTURE_BY_TYPE.size());
        create(EntityType.LLAMA, Items.WHEAT, "textures/entity/llama/creamy.png", 4);
        create(EntityType.FOX, Items.SWEET_BERRIES, "textures/entity/fox/fox.png", 2);
        create(EntityType.PANDA, Items.BAMBOO, "textures/entity/panda/panda.png", 7);
        create(EntityType.ZOMBIE, Items.ROTTEN_FLESH, "textures/entity/zombie/zombie.png", 3);
        create(EntityType.SKELETON, Items.ARROW, "textures/entity/skeleton/skeleton.png", 3);
        create(EntityType.PHANTOM, Items.PHANTOM_MEMBRANE, "textures/entity/phantom.png", 1);
        create(EntityType.BEE, Items.HONEYCOMB, "textures/entity/bee/bee.png", 1);
        create(EntityType.GHAST, Items.GHAST_TEAR, "textures/entity/ghast/ghast.png", 1);
        create(EntityType.WITHER, Items.SOUL_SAND, "textures/entity/wither/wither.png", 1);
        create(EntityType.HOGLIN, Items.COOKED_PORKCHOP, "textures/entity/hoglin/hoglin.png", 2);
        create(EntityType.RAVAGER, Items.LEATHER, "textures/entity/illager/ravager.png", 1);
        create(EntityType.TURTLE, Items.SCUTE, "textures/entity/turtle/big_sea_turtle.png", 1);
        create(EntityType.SQUID, Items.INK_SAC, "textures/entity/squid.png", 2);
        create(EntityType.GUARDIAN, Items.PRISMARINE_SHARD, "textures/entity/guardian.png", 2);
        create(EntityType.SLIME, Items.SLIME_BALL,  "textures/entity/slime/slime.png", 2);
        create(EntityType.MAGMA_CUBE, Items.BEDROCK,  "textures/entity/slime/magmacube.png", 0);  // hack to switch model
        create(EntityType.SILVERFISH, Items.STONE,  "textures/entity/silverfish.png", 2);
        create(EntityType.STRIDER, Items.BASALT,  "textures/entity/strider/strider.png", 2);
        create(EntityType.IRON_GOLEM, Items.IRON_BLOCK,  "textures/entity/iron_golem/iron_golem.png", 1);
        create(EntityType.RABBIT, Items.RABBIT,  "textures/entity/rabbit/brown.png", 8);
        create(EntityType.BLAZE, Items.BLAZE_ROD,  "textures/entity/blaze.png", 1);
        create(EntityType.PIGLIN, Items.GOLD_INGOT,  "textures/entity/piglin/piglin.png", 3);
        create(EntityType.COD, Items.COD,  "textures/entity/fish/cod.png", 5);
        create(EntityType.PILLAGER, Items.CROSSBOW, "textures/entity/illager/pillager.png", 5);
        create(EntityType.ENDERMAN, Items.ENDER_PEARL, "textures/entity/enderman/enderman.png", 1);
    }
}
