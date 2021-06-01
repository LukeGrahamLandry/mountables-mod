package io.github.lukegrahamlandry.mountables.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;

public class MountsConfig {
    public static final ForgeConfigSpec server_config;

    private static ForgeConfigSpec.BooleanValue allowMountFlight;
    private static ForgeConfigSpec.BooleanValue textureSwapConsumesItem;
    private static ForgeConfigSpec.BooleanValue someStartWithFlight;
    private static ForgeConfigSpec.ConfigValue<String> flightUpgradeItem;

    static {
        final ForgeConfigSpec.Builder serverBuilder = new ForgeConfigSpec.Builder();

        serverBuilder.comment("mountables server side configuration settings")
                .push("server");

        textureSwapConsumesItem = serverBuilder
                .comment("Whether it should consume the item when you switch a mount's texture: ")
                .define("textureSwapConsumesItem", true);
        allowMountFlight = serverBuilder
                .comment("Whether mounts should ever be able to fly: ")
                .define("allowMountFlight", true);
        flightUpgradeItem = serverBuilder
                .comment("Right click a mount with this item to grant flight: ")
                .define("flightUpgradeItem", "minecraft:feather");
        someStartWithFlight = serverBuilder
                .comment("Whether ghast/wither/bee/phantom mounts should be able to fly without being upgraded: ")
                .define("someStartWithFlight", true);

        server_config = serverBuilder.build();
    }

    public static Boolean isFlightAllowed(){
        return allowMountFlight.get();
    }

    public static Boolean doSomeStartWithFlight(){
        return someStartWithFlight.get();
    }

    public static Boolean doesTextureSwapConsume(){
        return textureSwapConsumesItem.get();
    }

    public static Item getFlightItem(){
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(flightUpgradeItem.get()));
    }

    public static void loadConfig(ForgeConfigSpec config, String path){
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }
}