package io.github.lukegrahamlandry.mountables.init;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.client.models.SheepMountModel;
import io.github.lukegrahamlandry.mountables.mounts.*;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PigModel;
import net.minecraft.client.renderer.entity.model.SheepModel;
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
    }
}
