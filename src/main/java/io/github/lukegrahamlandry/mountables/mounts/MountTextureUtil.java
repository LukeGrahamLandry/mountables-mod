package io.github.lukegrahamlandry.mountables.mounts;

import io.github.lukegrahamlandry.mountables.init.MountTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;

import java.util.HashMap;
import java.util.Map;

public class MountTextureUtil {
    private static final Map<Item, DyeColor> woolBlocks = new HashMap<>();
    static {
        woolBlocks.put(Items.WHITE_WOOL, DyeColor.WHITE);
        woolBlocks.put(Items.ORANGE_WOOL, DyeColor.ORANGE);
        woolBlocks.put(Items.MAGENTA_WOOL, DyeColor.MAGENTA);
        woolBlocks.put(Items.LIGHT_BLUE_WOOL, DyeColor.LIGHT_BLUE);
        woolBlocks.put(Items.YELLOW_WOOL, DyeColor.YELLOW);
        woolBlocks.put(Items.LIME_WOOL, DyeColor.LIME);
        woolBlocks.put(Items.PINK_WOOL, DyeColor.PINK);
        woolBlocks.put(Items.GRAY_WOOL, DyeColor.GRAY);
        woolBlocks.put(Items.LIGHT_GRAY_WOOL, DyeColor.LIGHT_GRAY);
        woolBlocks.put(Items.CYAN_WOOL, DyeColor.CYAN);
        woolBlocks.put(Items.PURPLE_WOOL, DyeColor.PURPLE);
        woolBlocks.put(Items.BLUE_WOOL, DyeColor.BLUE);
        woolBlocks.put(Items.BROWN_WOOL, DyeColor.BROWN);
        woolBlocks.put(Items.GREEN_WOOL, DyeColor.GREEN);
        woolBlocks.put(Items.RED_WOOL, DyeColor.RED);
        woolBlocks.put(Items.BLACK_WOOL, DyeColor.BLACK);
    }

    private static int colorOf(Item item){
        if (woolBlocks.containsKey(item)) return woolBlocks.get(item).getId();
        if (item instanceof DyeItem) return ((DyeItem) item).getDyeColor().getId();
        return -1;
    }



    public static boolean tryUpdateTexture(MountEntity mount, ItemStack itemstack) {
        int oldTexture = mount.getTextureType();
        int newTexture = -1;
        EntityType vanillaType = mount.getVanillaType();
        
        if (vanillaType == EntityType.SHEEP) newTexture = updateSheepTexture(itemstack.getItem());
        if (vanillaType == EntityType.COW) newTexture = updateCowTexture(itemstack.getItem());
        if (vanillaType == EntityType.PIG) newTexture = updatePigTexture(itemstack.getItem());
        if (vanillaType == EntityType.SNOW_GOLEM) newTexture = updateSnowmanTexture(itemstack.getItem());
        if (vanillaType == EntityType.SPIDER) newTexture = updateSpiderTexture(itemstack.getItem());
        if (vanillaType == EntityType.CAT && itemstack.getItem() == Items.COD){
            newTexture = (mount.getTextureType() + 1) % (CatEntity.TEXTURE_BY_TYPE.size() + 1);
        }
        if (vanillaType == EntityType.CAT && itemstack.getItem() == Items.WHEAT){
            newTexture = (mount.getTextureType() + 1) % 4;
        }
        if (vanillaType == EntityType.FOX) newTexture = updateFoxTexture(itemstack.getItem());
        if (vanillaType == EntityType.LLAMA && itemstack.getItem() == Items.WHEAT){
            newTexture = ((mount.getTextureType() + 1) % 4);
        }
        if (vanillaType == EntityType.PANDA && itemstack.getItem() == Items.BAMBOO){
            newTexture = ((mount.getTextureType() + 1) % 7);
        }
        if (vanillaType == EntityType.ZOMBIE) newTexture = updateZombieTexture(itemstack.getItem());
        if (vanillaType == EntityType.SKELETON) newTexture = updateSkeletonTexture(itemstack.getItem());
        if (vanillaType == EntityType.HOGLIN) newTexture = updateHogTexture(itemstack.getItem());
        if (vanillaType == EntityType.SQUID) newTexture = updateSquidTexture(itemstack.getItem());
        if (vanillaType == EntityType.GUARDIAN) newTexture = updateGuardianTexture(itemstack.getItem());
        if (vanillaType == EntityType.SLIME) newTexture = updateSlimeTexture(itemstack.getItem());
        if (vanillaType == EntityType.CREEPER) {
            if (itemstack.getItem() == Items.REDSTONE_BLOCK) newTexture = 1;
            else if (itemstack.getItem() == Items.GUNPOWDER) newTexture = 0;
        }
        if (vanillaType == EntityType.STRIDER) {
            if (itemstack.getItem() == Items.ICE) newTexture = 1;
            else if (itemstack.getItem() == Items.LAVA_BUCKET) newTexture = 0;
        }
        if (vanillaType == EntityType.ENDERMITE) {
            if (itemstack.getItem() == Items.ENDER_PEARL) newTexture = 1;
            else if (itemstack.getItem() == Items.STONE) newTexture = 0;
        }
        if (vanillaType == EntityType.RABBIT) {
            if (itemstack.getItem() == Items.CARROT) newTexture = ((mount.getTextureType() + 1) % 6);
            else if (itemstack.getItem() == Items.BREAD) newTexture = 6;
            else if (itemstack.getItem() == Items.IRON_SWORD) newTexture = 7;
        }
        if (vanillaType == EntityType.PIGLIN) {
            if (itemstack.getItem() == Items.GOLD_INGOT) newTexture = 0;
            else if (itemstack.getItem() == Items.ROTTEN_FLESH) newTexture = 1;
            else if (itemstack.getItem() == Items.GOLDEN_AXE) newTexture = 2;
        }
        if (vanillaType == EntityType.PILLAGER) {
            if (itemstack.getItem() == Items.CROSSBOW) newTexture = 0;
            else if (itemstack.getItem() == Items.IRON_AXE) newTexture = 1;
            else if (itemstack.getItem() == Items.BOW) newTexture = 2;
            else if (itemstack.getItem() == Items.EMERALD) newTexture = 3;
            else if (itemstack.getItem() == Items.POTION && PotionUtils.getPotion(itemstack) == Potions.WATER) newTexture = 4;
        }

        if (vanillaType == EntityType.COD) {
            if (itemstack.getItem() == Items.COD) newTexture = 0;
            else if (itemstack.getItem() == Items.SALMON) newTexture = 1;
            else if (itemstack.getItem() == Items.PUFFERFISH) newTexture = 2 + ((oldTexture + 1) % 3);
        }

        if (vanillaType == MountTypes.MUSHROOM.get()) {
            if (itemstack.getItem() == Items.RED_MUSHROOM) newTexture = 0;
            else if (itemstack.getItem() == Items.BROWN_MUSHROOM) newTexture = 1;
            else if (itemstack.getItem() == Items.WARPED_FUNGUS) newTexture = 2;
            else if (itemstack.getItem() == Items.CRIMSON_FUNGUS) newTexture = 3;
        }
        if (vanillaType == MountTypes.BLUE_ELEPHANT.get()){
            if (itemstack.getItem() == Items.LIGHT_BLUE_DYE) newTexture = 0;
            if (itemstack.getItem() == Items.BLUE_DYE) newTexture = 1;
        }

        if (newTexture != -1) mount.setTextureType(newTexture);
        return newTexture != -1 && newTexture != oldTexture;
    }

    public static boolean tryUpdateColor(MountEntity mount, ItemStack itemstack) {
        int newColor = -1;
        int oldColor = mount.getColorType();
        EntityType vanillaType = mount.getVanillaType();

        if (vanillaType == EntityType.LLAMA){
            newColor = colorOf(itemstack.getItem()) + 1;
            if (itemstack.getItem() == Items.SHEARS) newColor = 0;
            if (itemstack.getItem() == Items.EMERALD) newColor = 17;
            if (itemstack.getItem() == Items.NETHER_STAR) newColor = 18;
        }
        if (vanillaType == EntityType.WOLF){
            newColor = colorOf(itemstack.getItem()) + 1;
            if (itemstack.getItem() == Items.SHEARS) newColor = 0;
            if (itemstack.getItem() == Items.NETHER_STAR) newColor = 17;
        }

        if (newColor != -1) mount.setColorType(newColor);
        return newColor != -1 && newColor != oldColor;
    }

    private static int updateSheepTexture(Item item){
        if (item == Items.SHEARS){
            return (17);
        } else if (item == Items.NETHER_STAR){
            return (16);
        }

        return colorOf(item);
    }

    private static int updateCowTexture(Item item){
        if (item == Items.RED_MUSHROOM){
            return (1);
        } else if (item == Items.BROWN_MUSHROOM){
            return (2);
        } else if (item == Items.SHEARS){
            return (0);
        } return -1;
    }

    private static int updatePigTexture(Item item){
        if (item == Items.GLOWSTONE_DUST){
            return (1);
        } else if (item == Items.CARROT){
            return (0);
        } return -1;
    }

    private static int updateSnowmanTexture(Item item){
        if (item == Items.CARVED_PUMPKIN){
            return (0);
        } else if (item == Items.SHEARS){
            return (1);
        } return -1;
    }

    private static int updateSpiderTexture(Item item){
        if (item == Items.STRING){
            return (0);
        } else if (item == Items.SPIDER_EYE){
            return (1);
        } return -1;
    }

    private static int updateFoxTexture(Item item){
        if (item == Items.SPRUCE_LOG){
            return (0);
        } else if (item == Items.SNOWBALL){
            return (1);
        } return -1;
    }

    private static int updateZombieTexture(Item item){
        if (item == Items.ROTTEN_FLESH){
            return (0);
        } else if (item == Items.SAND){
            return (1);
        } else if (item == Items.WATER_BUCKET){
            return (2);
        } return -1;
    }

    private static int updateSkeletonTexture(Item item){
        if (item == Items.BONE){
            return (0);
        } else if (item == Items.COAL){
            return (1);
        } else if (item == Items.SNOWBALL){
            return (2);
        } return -1;
    }

    private static int updateHogTexture(Item item){
        if (item == Items.PORKCHOP){
            return (0);
        } else if (item == Items.ROTTEN_FLESH){
            return (1);
        } return -1;
    }

    private static int updateSquidTexture(Item item){
        if (item == Items.INK_SAC){
            return (0);
        } else if (item == Items.GLOWSTONE){
            return (1);
        } return -1;
    }

    private static int updateGuardianTexture(Item item){
        if (item == Items.PRISMARINE_SHARD){
            return (0);
        } else if (item == Items.COOKED_COD){
            return (1);
        } return -1;
    }

    private static int updateSlimeTexture(Item item){
        if (item == Items.SLIME_BALL){
            return (0);
        } else if (item == Items.MAGMA_CREAM){
            return (1);
        } return -1;
    }
}
