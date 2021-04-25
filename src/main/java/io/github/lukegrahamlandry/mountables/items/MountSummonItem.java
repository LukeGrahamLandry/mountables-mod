package io.github.lukegrahamlandry.mountables.items;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

public class MountSummonItem extends Item {
    static HashMap<EntityType, SpawnEggItem> eggs = new HashMap<>();
    public MountSummonItem(Item.Properties props) {
        super(props);

        // do this in common setup event? need to wait until all items registered if i want mod compatibility
        ForgeRegistries.ITEMS.getValues().forEach((item) -> {
            if (item instanceof SpawnEggItem){
                eggs.put(((SpawnEggItem)item).getType(null), ((SpawnEggItem)item));
            }
        });
    }

    private static EntityType<? extends LivingEntity> getType(ItemStack stack){
        return EntityType.CREEPER;  // read from nbt of stack
    }

    public static int getItemColor(ItemStack stack, int tintIndex){
        SpawnEggItem egg = eggs.get(getType(stack));
        if (tintIndex == 0 || egg == null){
            return 0XFFFFFF;
        } else {
            return egg.getColor(tintIndex - 1);
        }
    }
}
