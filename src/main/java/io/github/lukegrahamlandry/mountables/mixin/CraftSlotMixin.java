package io.github.lukegrahamlandry.mountables.mixin;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.init.ItemInit;
import io.github.lukegrahamlandry.mountables.init.MountTypes;
import io.github.lukegrahamlandry.mountables.items.MountSummonItem;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



// this feels hacky but idk what else to do
// both PlayerEvent.ItemCraftedEvent and Item#onCrafted are called on a copy of the crafted stack when the item is shift clicked out

@Mixin(WorkbenchContainer.class)
public abstract class CraftSlotMixin {
    @Inject(at = @At("RETURN"), method = "slotChangedCraftingGrid(ILnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/inventory/CraftingInventory;Lnet/minecraft/inventory/CraftResultInventory;)V")
    private static void slotChangedCraftingGrid(int p_217066_0_, World p_217066_1_, PlayerEntity p_217066_2_, CraftingInventory craftInv, CraftResultInventory resultInv, CallbackInfo ci) {
        if (resultInv.getItem(0).getItem() == ItemInit.MOUNT_SUMMON.get()){
            EntityType type = MountTypes.getToCraft(craftInv.getItem(0).getItem());
            if (type == null) return;
            MountSummonItem.writeNBT(resultInv.getItem(0), type, 0, MountEntity.maxHealth, MountSummonItem.canFlyByDefault(type), false, 0);
        }
    }

    /*
    @Shadow @Final private CraftResultInventory resultSlots;

    @Shadow @Final private CraftingInventory craftSlots;

    @Inject(at = @At("RETURN"), method = "fillCraftSlotsStackedContents(Lnet/minecraft/item/crafting/RecipeItemHelper;)V")
    private void fillCraftSlotsStackedContents(RecipeItemHelper p_201771_1_, CallbackInfo ci) {
        if (this.resultSlots.getItem(0).getItem() == ItemInit.MOUNT_SUMMON.get()){
            EntityType type = MountTypes.getToCraft(this.craftSlots.getItem(0).getItem());
            MountSummonItem.writeNBT(this.resultSlots.getItem(0), type, 0, MountEntity.maxHealth, canFlyByDefault(type), false);
        }
    }
     */
}
