package io.github.lukegrahamlandry.mountables.events;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.init.ItemInit;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.EmptyLootEntry;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MountablesMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AddToLootChests {
    @SubscribeEvent
    public static void onCraft(LootTableLoadEvent event){
        // TODO: make sure I'm using the correct loot table registry names here
        boolean shouldSpawnCommandChip = event.getName().toString().equals("minecraft:end_city") || event.getName().toString().equals("minecraft:nether_fortress") || event.getName().toString().equals("minecraft:mineshaft") || event.getName().toString().equals("minecraft:bastion");
        if (shouldSpawnCommandChip){
            LootPool.Builder lootBuilder = LootPool.lootPool().add(ItemLootEntry.lootTableItem(ItemInit.MYSTERIOUS_FRAGMENT.get()).setWeight(10)).add(EmptyLootEntry.emptyItem().setWeight(90));
            event.getTable().addPool(lootBuilder.build());
        }
    }
}
