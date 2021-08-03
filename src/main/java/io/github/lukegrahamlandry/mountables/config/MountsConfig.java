package io.github.lukegrahamlandry.mountables.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MountsConfig {
    public static final ForgeConfigSpec server_config;

    private static ForgeConfigSpec.BooleanValue textureSwapConsumesItem;
    private static ForgeConfigSpec.BooleanValue someStartWithFlight;
    private static ForgeConfigSpec.DoubleValue walkingSpeedCoreBoost;
    private static ForgeConfigSpec.DoubleValue flyingSpeedCoreBoost;
    private static ForgeConfigSpec.IntValue maxSpeedCores;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> disallowedCores;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> disallowedMounts;
    private static ForgeConfigSpec.BooleanValue resetReturnsCores;


    static {
        final ForgeConfigSpec.Builder serverBuilder = new ForgeConfigSpec.Builder();

        serverBuilder.comment("mountables server side configuration settings")
                .push("server");

        textureSwapConsumesItem = serverBuilder
                .comment("Whether it should consume the item when you switch a mount's texture: ")
                .define("textureSwapConsumesItem", true);
        someStartWithFlight = serverBuilder
                .comment("Whether ghast/wither/bee/phantom mounts should be able to fly without being upgraded: ")
                .define("someStartWithFlight", true);
        walkingSpeedCoreBoost = serverBuilder
                .comment("How much faster each speed core should make it (while walking): ")
                .defineInRange("walkingSpeedCoreBoost", 0.1D, 0, 100);
        flyingSpeedCoreBoost = serverBuilder
                .comment("How much faster each speed core should make it (while flyinh): ")
                .defineInRange("flyingSpeedCoreBoost", 0.05D, 0, 100);
        maxSpeedCores = serverBuilder
                .comment("How many speed cores are permited at once: ")
                .defineInRange("maxSpeedCores", 10, 0, Integer.MAX_VALUE);
        resetReturnsCores = serverBuilder
                .comment("Whether the cores you used should be returned as items when you use a reset core ")
                .define("resetReturnsCores", false);

        disallowedCores = serverBuilder
                .comment("list of registry names of upgrade cores that are not allowed. options: mountables:flight_core, mountables:fire_core, mountables:water_core, mountables:speed_core, mountables:slow_core, mountables:reset_core")
                .defineList("disallowedCores", Arrays.asList(), i ->((String) i).contains(":"));
        disallowedMounts = serverBuilder
                .comment("list of registry names of mount types that are not allowed (include namespace)")
                .defineList("disallowedMounts", Arrays.asList(), i ->((String) i).contains(":"));


        server_config = serverBuilder.build();
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

    public static int getMaxSpeedCores() {
        return maxSpeedCores.get();
    }

    public static boolean isCoreAllowed(Item core) {
        return !disallowedCores.get().contains(core.getRegistryName().toString());
    }

    public static boolean isMountAllowed(EntityType vanillaType) {
        return !disallowedMounts.get().contains(vanillaType.getRegistryName().toString());
    }

    public static boolean shouldResetReturnCores() {
        return resetReturnsCores.get();
    }

    public static void loadConfig(ForgeConfigSpec config, String path){
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }



}