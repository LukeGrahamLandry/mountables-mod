package io.github.lukegrahamlandry.mountables.mounts;

import io.github.lukegrahamlandry.mountables.util.MountUtil;
import io.github.lukegrahamlandry.mountables.util.IMount;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.IRideable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class SheepMount extends SheepEntity implements IMount {  // IJumpingMount
    public SheepMount(EntityType<? extends SheepEntity> p_i50245_1_, World p_i50245_2_) {
        super(p_i50245_1_, p_i50245_2_);
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        return MountUtil.handleInteract(this, player, hand);
    }

    @Override
    public void travel(Vector3d vec) {
        if (MountUtil.isControlled(this)){
            MountUtil.travel(this, vec);
        } else {
            super.travel(vec);
        }
    }

    @Override
    public boolean isControlledByLocalInstance() {
        if (this.getPassengers().size() == 0) return !this.level.isClientSide;
        LivingEntity rider = (LivingEntity) this.getPassengers().get(0);
        return rider instanceof PlayerEntity ? ((PlayerEntity)rider).isLocalPlayer() : !this.level.isClientSide;
    }

    @Override  // IMount
    public void baseTravel(Vector3d vec){
        super.travel(vec);
    }

    @Override  // IMount
    public void rotate(float y, float x) {
        this.setRot(y, x);
    }
}
