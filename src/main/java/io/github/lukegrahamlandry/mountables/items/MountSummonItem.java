package io.github.lukegrahamlandry.mountables.items;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.init.MountTypes;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Objects;

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

    static EntityType<CreeperEntity> defaultType = EntityType.CREEPER;
    public static EntityType getType(ItemStack stack){
        if (!stack.hasTag()) return defaultType;
        String typeName = stack.getTag().getString("typeid");
        return EntityType.byString(typeName).orElse(defaultType);
    }

    public static void writeNBT(ItemStack stack, EntityType<?> type, int textureType, int health){
        CompoundNBT tag = stack.hasTag() ? stack.getTag() : new CompoundNBT();
        MountablesMain.LOGGER.debug("writeNBT " + EntityType.getKey(type).toString());
        tag.putString("typeid", EntityType.getKey(type).toString());
        tag.putInt("texturetype", textureType);
        tag.putInt("health", health);
        stack.setTag(tag);
    }

    public static int getItemColor(ItemStack stack, int tintIndex){
        SpawnEggItem egg = eggs.get(getType(stack));
        if (tintIndex == 0 || egg == null){
            return 0XFFFFFF;
        } else {
            return egg.getColor(tintIndex - 1);
        }
    }

    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (!(world instanceof ServerWorld)) {
            return ActionResultType.SUCCESS;
        } else {
            context.getItemInHand().getItem();
            ItemStack itemstack = context.getItemInHand();
            BlockPos blockpos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockstate = world.getBlockState(blockpos);

            BlockPos blockpos1;
            if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.relative(direction);
            }

            EntityType<?> entitytype = MountTypes.get(getType(itemstack)).getType();
            Entity summon = entitytype.spawn((ServerWorld)world, itemstack, context.getPlayer(), blockpos1, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
            if (summon != null) {
                ((MountEntity)summon).setMountType(itemstack);
                itemstack.shrink(1);
            }

            return ActionResultType.CONSUME;
        }
    }
}
