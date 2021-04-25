package io.github.lukegrahamlandry.mountables.init;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.items.MountSummonItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MountablesMain.MOD_ID);

    public static final RegistryObject<Item> MOUNT_SUMMON = ITEMS.register("mount_summon", () -> new MountSummonItem(new Item.Properties().tab(ItemGroup.TAB_MISC)));

}
