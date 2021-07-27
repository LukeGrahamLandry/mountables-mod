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
    private static ForgeConfigSpec.DoubleValue walkingSpeedCoreBoost;
    private static ForgeConfigSpec.DoubleValue flyingSpeedCoreBoost;

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
        someStartWithFlight = serverBuilder
                .comment("Whether ghast/wither/bee/phantom mounts should be able to fly without being upgraded: ")
                .define("someStartWithFlight", true);
        walkingSpeedCoreBoost = serverBuilder
                .comment("How much faster each speed core should make it (while walking): ")
                .defineInRange("walkingSpeedCoreBoost", 0.1D, 0, 999);
        flyingSpeedCoreBoost = serverBuilder
                .comment("How much faster each speed core should make it (while flyinh): ")
                .defineInRange("flyingSpeedCoreBoost", 0.05D, 0, 999);

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

    public static float getWalkingSpeedPerCore() {
        return walkingSpeedCoreBoost.get().floatValue();
    }

    public static float getFlyingSpeedPerCore() {
        return flyingSpeedCoreBoost.get().floatValue();
    }

    public static void loadConfig(ForgeConfigSpec config, String path){
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }


}