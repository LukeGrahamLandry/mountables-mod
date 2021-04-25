package io.github.lukegrahamlandry.mountables.mounts;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.util.MountUtil;
import io.github.lukegrahamlandry.mountables.util.IMount;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class SheepMount extends SheepEntity implements IMount, IJumpingMount {
    private float playerJumpPendingScale = 0;

    public SheepMount(EntityType<? extends SheepEntity> p_i50245_1_, World p_i50245_2_) {
        super(p_i50245_1_, p_i50245_2_);
    }

    @Override
    protected void registerGoals() {
        // super.registerGoals();
    }

    @Override
    protected void customServerAiStep() {
        // super.customServerAiStep();
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        return MountUtil.handleInteract(this, player, hand);
    }

    @Override
    public void travel(Vector3d vec) {
        MountUtil.travel(this, vec);
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return !this.level.isClientSide;
        //if (this.getPassengers().size() == 0) return !this.level.isClientSide;
        //LivingEntity rider = (LivingEntity) this.getPassengers().get(0);
        //return rider instanceof PlayerEntity ? ((PlayerEntity)rider).isLocalPlayer() : !this.level.isClientSide;
    }

    @Override  // IMount
    public void baseTravel(Vector3d vec){
        super.travel(vec);
    }

    @Override  // IMount
    public void rotate(float y, float x) {
        this.setRot(y, x);
    }

    boolean flying = false;
    @Override
    public void setFlying(boolean flag) {
        flying = flag;
        this.setNoGravity(flag);
    }

    @Override
    public boolean isFlying() {
        return flying;
    }

    @Override
    public float playerJumpPendingScale() {
        return this.playerJumpPendingScale;
    }

    @Override
    public void setPlayerJumpPendingScale(float v) {
        this.playerJumpPendingScale = v;
    }

    @Override
    public boolean isJumping() {
        return this.jumping;
    }

    @Override
    public void onPlayerJump(int p_110206_1_) {
        if (p_110206_1_ < 0) {
            p_110206_1_ = 0;
        }

        if (p_110206_1_ >= 90) {
            this.playerJumpPendingScale = 1.0F;
        } else {
            this.playerJumpPendingScale = 0.4F + 0.4F * (float)p_110206_1_ / 90.0F;
        }
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public void handleStartJump(int p_184775_1_) {

    }

    @Override
    public void handleStopJump() {

    }

    /*

    @Override
    public void updatePassenger(Entity passenger) {
        float f1 = (float)((this.removed ? (double)0.01F : this.getMountedYOffset()) + passenger.getYOffset());
        float f = -0.6F;
        Vector3d vector3d = (new Vector3d((double)f, 0.0D, 0.0D)).rotateYaw(-this.rotationYaw * ((float)Math.PI / 180F) - ((float)Math.PI / 2F)).scale(50);
        passenger.setPosition(this.getPosX() + vector3d.x, this.getPosY() + (double)f1, this.getPosZ() + vector3d.z);
        super.updatePassenger(passenger);
    }

     */
}
