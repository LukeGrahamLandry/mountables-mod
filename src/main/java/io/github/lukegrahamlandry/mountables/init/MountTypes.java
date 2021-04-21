package io.github.lukegrahamlandry.mountables.init;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.mounts.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class MountTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MountablesMain.MOD_ID);

    private static final Map<EntityType<? extends LivingEntity>, RegistryObject<EntityType<?>>> mountLookup = new HashMap<>();

    public static EntityType<?> get(EntityType<? extends LivingEntity> type) {
        return mountLookup.get(type).get();
    }

    private static void create(EntityType<? extends LivingEntity> vanillaType, EntityType.IFactory factory){
        String name = vanillaType.getRegistryName().getPath() + "_mount";

        RegistryObject<EntityType<?>> registryObj = ENTITY_TYPES.register(name,
                () -> EntityType.Builder.of(factory, EntityClassification.MISC).sized(vanillaType.getWidth(), vanillaType.getHeight())
                        .build(new ResourceLocation(MountablesMain.MOD_ID, name).toString()));

        mountLookup.put(vanillaType, registryObj);
    }

    static {
        create(EntityType.SHEEP, SheepMount::new);
    }
}
