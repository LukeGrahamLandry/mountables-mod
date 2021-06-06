package io.github.lukegrahamlandry.mountables;

import io.github.lukegrahamlandry.mountables.config.MountsConfig;
import io.github.lukegrahamlandry.mountables.init.ItemInit;
import io.github.lukegrahamlandry.mountables.init.MountTypes;
import io.github.lukegrahamlandry.mountables.items.MountSummonItem;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("mountables")
public class MountablesMain {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "mountables";

    public MountablesMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MountTypes.ENTITY_TYPES.register(modEventBus);
        ItemInit.ITEMS.register(modEventBus);
        modEventBus.addListener(MountablesMain::mobAttributes);
        modEventBus.addListener(MountablesMain::generateEggMap);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MountsConfig.server_config);
        MountsConfig.loadConfig(MountsConfig.server_config, FMLPaths.CONFIGDIR.get().resolve(MOD_ID + ".toml").toString());
    }

    public static void mobAttributes(EntityAttributeCreationEvent event){
        for (EntityType type : MountTypes.getMountTypes()){
            event.put(MountTypes.get(type).getType(), MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, MountEntity.maxHealth).add(Attributes.MOVEMENT_SPEED, (double)0.23F).build());
        }
    }

    // this is after all items registered for mod compatibility
    public static void generateEggMap(FMLCommonSetupEvent event) {
        ForgeRegistries.ITEMS.getValues().forEach((item) -> {
            if (item instanceof SpawnEggItem){
                MountSummonItem.eggs.put(((SpawnEggItem)item).getType(null), ((SpawnEggItem)item));
            }
        });
        MountSummonItem.eggs.put(EntityType.WITHER, (SpawnEggItem) Items.WITHER_SKELETON_SPAWN_EGG);
    }
}
