package io.github.lukegrahamlandry.mountables.util;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.mounts.SheepMount;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class MountUtil {
    public static void travel(LivingEntity mount, Vector3d travelVec) {
        if (MountUtil.isControlled(mount)){
            walkTravel(mount, travelVec);
        } else {
            ((IMount)mount).baseTravel(travelVec);
        }
    }

    // from AbastractHorseEntity
    private static void walkTravel(LivingEntity mount, Vector3d travelVec) {
        mount.maxUpStep = 1;

        LivingEntity livingentity = (LivingEntity) mount.getPassengers().get(0);
        mount.yRot = livingentity.yRot;
        mount.yRotO = mount.yRot;
        mount.xRot = livingentity.xRot * 0.5F;
        ((IMount)mount).rotate(mount.yRot, mount.xRot);
        mount.yBodyRot = mount.yRot;
        mount.yHeadRot = mount.yBodyRot;
        float f = livingentity.xxa * 0.5F;
        float f1 = livingentity.zza;
        if (f1 <= 0.0F) {
            f1 *= 0.25F;
        }

        //if (mount.isOnGround() && ((IMount)mount).playerJumpPendingScale() == 0.0F && mount.isStanding() && !mount.allowStandSliding)
          //  f = 0.0F;
           // f1 = 0.0F;
        //}

        if (((IMount)mount).playerJumpPendingScale() > 0.0F && !((IMount)mount).isJumping() && mount.isOnGround()) {
            double d0 = getJumpStrength(mount) * (double)((IMount)mount).playerJumpPendingScale(); // * (double)mount.getBlockJumpFactor();
            double d1;
            if (mount.hasEffect(Effects.JUMP)) {
                d1 = d0 + (double)((float)(mount.getEffect(Effects.JUMP).getAmplifier() + 1) * 0.1F);
            } else {
                d1 = d0;
            }

            Vector3d vector3d = mount.getDeltaMovement();
            MountablesMain.LOGGER.debug(vector3d.x + " " + d1 + " " + vector3d.z);
            mount.setDeltaMovement(vector3d.x, d1, vector3d.z);
            mount.setJumping(true);
            mount.hasImpulse = true;
            net.minecraftforge.common.ForgeHooks.onLivingJump(mount);
            //if (f1 > 0.0F) {
            //    float f2 = MathHelper.sin(mount.yRot * ((float)Math.PI / 180F));
             //   float f3 = MathHelper.cos(mount.yRot * ((float)Math.PI / 180F));
              //  mount.setDeltaMovement(mount.getDeltaMovement().add((double)(-0.4F * f2 * ((IMount)mount).playerJumpPendingScale()), 0.0D, (double)(0.4F * f3 * ((IMount)mount).playerJumpPendingScale())));
            //}

            ((IMount)mount).setPlayerJumpPendingScale(0.0F);
        }

        mount.flyingSpeed = mount.getSpeed() * 0.1F;
        if (mount.isControlledByLocalInstance()) {
            // MountablesMain.LOGGER.debug(mount.position() + "   " + livingentity.position());
            mount.setSpeed(getSpeed(mount, false));
            ((IMount)mount).baseTravel(new Vector3d((double)f, travelVec.y, (double)f1));
        } //else if (livingentity instanceof PlayerEntity) {
            //mount.setDeltaMovement(Vector3d.ZERO);
        //}

        //if (mount.isOnGround()) {
          //  ((IMount)mount).setPlayerJumpPendingScale(0.0F);
            //mount.setJumping(false);
        //}

        mount.calculateEntityAnimation(mount, false);


        //
    }

    private static double getJumpStrength(LivingEntity mount) {
        return 1;
    }

    private static void flyTravel(LivingEntity mount, Vector3d travelVec){
        LivingEntity rider = (LivingEntity) mount.getPassengers().get(0);

        mount.setOnGround(!((IMount)mount).isFlying());

        if (((IMount)mount).isFlying()) {

            mount.yRot = rider.yRot;
            mount.yRotO = mount.yRot;

            double yComponent = 0;
            double moveForward = 0;

            if (rider.zza > 0) {
                moveForward = getSpeed(mount, true);
                mount.xRot = -MathHelper.clamp(rider.xRot, -10, 10);
                ((IMount)mount).rotate(mount.yRot, mount.xRot);
                if (rider.xRot < -10 || rider.xRot > 10) {
                    yComponent = -(Math.toRadians(rider.xRot) * getSpeed(mount, true));
                    if (!((IMount)mount).isFlying() && yComponent > 0) ((IMount)mount).setFlying(true);
                    else if (((IMount)mount).isFlying() && yComponent < 0 && !mount.level.getBlockState(mount.blockPosition().below(2)).isAir())  // !isAir should be isSolid
                        ((IMount)mount).setFlying(false);
                }
            }

            ((IMount)mount).baseTravel(new Vector3d(rider.xxa, yComponent, moveForward));
            /*
            if (mount.isControlledByLocalInstance()){
                ((IMount)mount).baseTravel(new Vector3d(rider.xxa, yComponent, moveForward));
            } else if (rider instanceof PlayerEntity) {
                mount.setDeltaMovement(Vector3d.ZERO);
            }

             */

        } else {
            mount.yRot = rider.yRot;
            mount.yRotO = mount.yRot;
            ((IMount)mount).rotate(mount.yRot, mount.xRot);

            // mount.renderYawOffset    = mount.rotationYaw;
            // mount.rotationYawHead    = mount.renderYawOffset;
            // are the cca the same as in the travelVec?
            mount.yya = rider.yya;
            double strafe = rider.xxa * 0.5f;
            double forward = rider.zza * 0.4f;
            double vertical = rider.yya;
            mount.maxUpStep = 2f;
            mount.fallDistance = 0;

            if (forward <= 0.0f) {
                forward *= 0.001f;
            }

            mount.flyingSpeed = 0.2f;

            mount.setSpeed(getSpeed(mount, false));
            ((IMount)mount).baseTravel(new Vector3d(strafe, vertical, forward));

            /*
            if (mount.isControlledByLocalInstance()) {
                mount.setSpeed(getSpeed(mount, false));
                ((IMount)mount).baseTravel(new Vector3d(strafe, vertical, forward));
            } else if (rider instanceof PlayerEntity) {
                mount.setDeltaMovement(0, mount.getDeltaMovement().y, 0);
            }

             */

            if (rider.xRot < -25 && rider.zza > 0) ((IMount)mount).setFlying(true);
        }

        mount.calculateEntityAnimation(mount, false);
        MountablesMain.LOGGER.debug(mount.position() + "   " + rider.position());
    }

    private static float getSpeed(LivingEntity mount, boolean isFlying) {
        return 0.5F;
    }

    public static boolean isControlled(LivingEntity mount) {
        return mount.getPassengers().size() > 0;
    }

    public static ActionResultType handleInteract(LivingEntity mount, PlayerEntity player, Hand hand) {
        if (!mount.level.isClientSide()) {
            player.yRot = mount.yRot;
            player.xRot = mount.xRot;
            player.startRiding(mount);
        }
        
        return ActionResultType.sidedSuccess(mount.level.isClientSide());
    }


            /*   // jump i think
                if (mount.playerJumpPendingScale > 0.0F && !mount.isJumping() && mount.isOnGround()) {
                    double d0 = mount.getCustomJump() * (double)mount.playerJumpPendingScale * (double)mount.getBlockJumpFactor();
                    double d1;
                    if (mount.hasEffect(Effects.JUMP)) {
                        d1 = d0 + (double)((float)(mount.getEffect(Effects.JUMP).getAmplifier() + 1) * 0.1F);
                    } else {
                        d1 = d0;
                    }

                    Vector3d vector3d = mount.getDeltaMovement();
                    mount.setDeltaMovement(vector3d.x, d1, vector3d.z);
                    mount.setIsJumping(true);
                    mount.hasImpulse = true;
                    net.minecraftforge.common.ForgeHooks.onLivingJump(mount);
                    if (f1 > 0.0F) {
                        float f2 = MathHelper.sin(mount.yRot * ((float)Math.PI / 180F));
                        float f3 = MathHelper.cos(mount.yRot * ((float)Math.PI / 180F));
                        mount.setDeltaMovement(mount.getDeltaMovement().add((double)(-0.4F * f2 * mount.playerJumpPendingScale), 0.0D, (double)(0.4F * f3 * mount.playerJumpPendingScale)));
                    }

                    mount.playerJumpPendingScale = 0.0F;
                }

            */
}
