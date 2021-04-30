package io.github.lukegrahamlandry.mountables.init;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.mounts.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
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

    private static final Map<EntityType<? extends LivingEntity>, RegistryObject<EntityType<?>>> mountLookup = new HashMap<>();
    private static final Map<Item, EntityType<? extends LivingEntity>> mountRecipeLookup = new HashMap<>();

    public static EntityType<?> get(EntityType<? extends LivingEntity> type) {
        return mountLookup.get(type).get();
    }
    public static EntityType<?> getToCraft(Item item) {
        return mountRecipeLookup.get(item);
    }
    public static Set<EntityType<? extends LivingEntity>> getMountTypes() {
        return mountLookup.keySet();
    }

    private static void create(EntityType<? extends LivingEntity> vanillaType, EntityType.IFactory factory, Item recipe){
        String name = vanillaType.getRegistryName().getPath() + "_mount";

        RegistryObject<EntityType<?>> registryObj = ENTITY_TYPES.register(name,
                () -> EntityType.Builder.of(factory, EntityClassification.MISC).sized(vanillaType.getWidth(), vanillaType.getHeight())
                        .build(new ResourceLocation(MountablesMain.MOD_ID, name).toString()));

        mountLookup.put(vanillaType, registryObj);
        mountRecipeLookup.put(recipe, vanillaType);
    }

    static {
        create(EntityType.SHEEP, SheepMount::new, Items.MUTTON);
        create(EntityType.PIG, PigEntity::new, Items.PORKCHOP);
        create(EntityType.COW, CowMount::new, Items.BEEF);
    }
}
