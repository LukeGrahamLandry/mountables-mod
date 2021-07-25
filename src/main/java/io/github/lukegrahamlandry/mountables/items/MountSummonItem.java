package io.github.lukegrahamlandry.mountables.items;

import io.github.lukegrahamlandry.mountables.config.MountsConfig;
import io.github.lukegrahamlandry.mountables.init.ItemInit;
import io.github.lukegrahamlandry.mountables.init.MountTypes;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MountSummonItem extends Item {
    public static HashMap<EntityType, SpawnEggItem> eggs = new HashMap<>();
    public MountSummonItem(Item.Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (getType(stack) != null) {  // KeyboardHelper.isHoldingShift() &&
            tooltip.add(getType(stack).getDescription());
        }

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public static EntityType getType(ItemStack stack){
        if (!stack.hasTag()) return null;
        String typeName = stack.getTag().getString("typeid");
        return EntityType.byString(typeName).orElse(null);
    }

    public static void writeNBT(ItemStack stack, EntityType<?> type, int textureType, int health, boolean flight, boolean baby, int color){
        CompoundNBT tag = stack.hasTag() ? stack.getTag() : new CompoundNBT();
        // MountablesMain.LOGGER.debug("writeNBT " + EntityType.getKey(type).toString());
        tag.putString("typeid", EntityType.getKey(type).toString());
        tag.putInt("texturetype", textureType);
        tag.putInt("health", health);
        tag.putBoolean("canfly", flight);
        tag.putBoolean("baby", baby);
        tag.putInt("colortype", color);
        stack.setTag(tag);
    }

    public static int getItemColor(ItemStack stack, int tintIndex){
        if (getType(stack) == null) return 0XFFFFFF;  // tintIndex == 0 ? 0XFFFFFF : 0;
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
            // EntityType#spawn sets the entity's name to the name of the item stack you pass in
            Entity summon = entitytype.spawn((ServerWorld)world, itemstack, context.getPlayer(), blockpos1, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
            if (summon != null) {
                ((MountEntity)summon).setMountType(itemstack);
                ((MountEntity)summon).setOwnerUUID(context.getPlayer().getUUID());
                summon.refreshDimensions();
                itemstack.shrink(1);
            }

            return ActionResultType.CONSUME;
        }
    }

    // if i just do this in the PlayerEvent.ItemCraftedEvent or here, it cant set nbt when its shift clicked out because the event is given a copy of the stack
    // so the mixin is what is actually useful. this is just a last ditch attempt at compatability with mod crafting table things that dont use the right container
    @Override
    public void onCraftedBy(ItemStack stack, World p_77622_2_, PlayerEntity player) {
        // the open container at this point will always be WorkbenchContainer
        // get an item from the crafting grid (not the middle one) and it must the the item for that mount type
        Item recipeItem = player.containerMenu.slots.get(1).getItem().getItem();
        // MountablesMain.LOGGER.debug(recipeItem);

        EntityType type = MountTypes.getToCraft(recipeItem);

        if (type == null) return;

        // MountablesMain.LOGGER.debug(type);

        MountSummonItem.writeNBT(stack, type, 0, MountEntity.maxHealth, canFlyByDefault(type), false, 0);
    }

    public static boolean canFlyByDefault(EntityType type) {
        if (!MountsConfig.doSomeStartWithFlight()) return false;
        return type == EntityType.GHAST || type == EntityType.WITHER || type == EntityType.BEE || type == EntityType.PHANTOM;
    }

    public void fillItemCategory(ItemGroup p_150895_1_, NonNullList<ItemStack> p_150895_2_) {
        if (this.allowdedIn(p_150895_1_)) {
            for (EntityType vanillaType : MountTypes.getMountTypes()){
                if (vanillaType == EntityType.MAGMA_CUBE) continue; // ugly hack because magma cream is acutally a texture of slime but needs a different model

                ItemStack stack = new ItemStack(ItemInit.MOUNT_SUMMON.get());
                MountSummonItem.writeNBT(stack, vanillaType, 0, MountEntity.maxHealth, canFlyByDefault(vanillaType), false, 0);
                p_150895_2_.add(stack);
            }
        }
    }
}
