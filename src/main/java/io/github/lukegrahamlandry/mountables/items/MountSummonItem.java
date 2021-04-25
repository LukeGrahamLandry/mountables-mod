package io.github.lukegrahamlandry.mountables.items;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
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

    // doesnt work; is always defaulttype
    // either nbt is only set on server so client cant read
    // or getColour is called once per stack before nbt is set (or even once per item? surely not) how to trigger recall?
    // or getCrafting isnt getting the right output stack but i dont think so
    static EntityType defaultType = EntityType.CREEPER;
    private static EntityType getType(ItemStack stack){
        if (!stack.hasTag()) return defaultType;
        String typeName = stack.getTag().getString("typeid");
        MountablesMain.LOGGER.debug(typeName);
        return EntityType.byString(typeName).orElse(defaultType);
    }

    public static void writeNBT(ItemStack stack, EntityType type, int textureType){
        CompoundNBT tag = stack.hasTag() ? stack.getTag() : new CompoundNBT();
        MountablesMain.LOGGER.debug(EntityType.getKey(type).toString());
        tag.putString("typeid", EntityType.getKey(type).toString());
        tag.putInt("texturetype", textureType);
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
