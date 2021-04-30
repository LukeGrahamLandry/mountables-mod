package io.github.lukegrahamlandry.mountables.client;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.init.MountTypes;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MountablesMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderHandler {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // TODO: generate this from the map. have create take the render factory
        RenderingRegistry.registerEntityRenderingHandler((EntityType<SheepEntity>) MountTypes.get(EntityType.SHEEP), SheepRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler((EntityType<PigEntity>) MountTypes.get(EntityType.PIG), PigRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler((EntityType<CowEntity>) MountTypes.get(EntityType.COW), CowRenderer::new);
    }
}
