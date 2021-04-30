package io.github.lukegrahamlandry.mountables;

import io.github.lukegrahamlandry.mountables.init.ItemInit;
import io.github.lukegrahamlandry.mountables.init.MountTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
    }

    public static void mobAttributes(EntityAttributeCreationEvent event){
        for (EntityType type : MountTypes.getMountTypes()){
            event.put(MountTypes.get(type), MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, (double)0.23F).build());
        }
    }
}
