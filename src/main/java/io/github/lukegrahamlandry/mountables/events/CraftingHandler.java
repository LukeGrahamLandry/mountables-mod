package io.github.lukegrahamlandry.mountables.events;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.init.MountTypes;
import io.github.lukegrahamlandry.mountables.items.MountSummonItem;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MountablesMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CraftingHandler {
    @SubscribeEvent
    public static void onCraft(PlayerEvent.ItemCraftedEvent event){
        if (!(event.getCrafting().getItem() instanceof MountSummonItem)) return;

        Item recipeItem = event.getInventory().getItem(0).getItem();
        MountablesMain.LOGGER.debug(recipeItem);
        EntityType type = MountTypes.getToCraft(recipeItem);
        MountablesMain.LOGGER.debug(type);
        MountSummonItem.writeNBT(event.getCrafting(), type, 0);
    }
}
