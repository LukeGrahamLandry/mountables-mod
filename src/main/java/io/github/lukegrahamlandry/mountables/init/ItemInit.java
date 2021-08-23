package io.github.lukegrahamlandry.mountables.init;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.items.MountSummonItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MountablesMain.MOD_ID);

    public static final RegistryObject<Item> MOUNT_SUMMON = ITEMS.register("mount_summon", () -> new MountSummonItem(new Item.Properties().stacksTo(1).tab(ModTab.instance)));
    public static final RegistryObject<Item> MOUNT_CORE = ITEMS.register("mount_core", () -> new Item(new Item.Properties().tab(ModTab.instance)));
    public static final RegistryObject<Item> COMMAND_CHIP = ITEMS.register("command_chip", () -> new Item(new Item.Properties().tab(ModTab.instance)));
    public static final RegistryObject<Item> MYSTERIOUS_FRAGMENT = ITEMS.register("mysterious_fragment", () -> new Item(new Item.Properties().tab(ModTab.instance)));

    public static final RegistryObject<Item> FLIGHT_CORE = ITEMS.register("flight_core", () -> new Item(new Item.Properties().tab(ModTab.instance)));
    public static final RegistryObject<Item> FIRE_CORE = ITEMS.register("fire_core", () -> new Item(new Item.Properties().tab(ModTab.instance)));
    public static final RegistryObject<Item> WATER_CORE = ITEMS.register("water_core", () -> new Item(new Item.Properties().tab(ModTab.instance)));
    public static final RegistryObject<Item> SPEED_CORE = ITEMS.register("speed_core", () -> new Item(new Item.Properties().tab(ModTab.instance)));
    public static final RegistryObject<Item> SLOW_CORE = ITEMS.register("slow_core", () -> new Item(new Item.Properties().tab(ModTab.instance)));
    public static final RegistryObject<Item> RESET_CORE = ITEMS.register("reset_core", () -> new Item(new Item.Properties().tab(ModTab.instance)));

    public static class ModTab extends ItemGroup{
        public static final ModTab instance = new ModTab(ItemGroup.TABS.length, "mountables");
        private ModTab(int index, String label) {
            super(index, label);
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(MOUNT_CORE.get());
        }
    }
}
