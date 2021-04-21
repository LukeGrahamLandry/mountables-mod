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
        if (mount.isAlive()) {
            LivingEntity rider = (LivingEntity) mount.getPassengers().get(0);
            mount.yRot = rider.yRot;
            mount.yRotO = mount.yRot;
            mount.xRot = rider.xRot * 0.5F;
            ((IMount)mount).rotate(mount.yRot, mount.xRot);
            mount.yBodyRot = mount.yRot;
            mount.yHeadRot = mount.yBodyRot;
            float f = rider.xxa * 0.5F;
            float f1 = rider.zza;
            if (f1 <= 0.0F) f1 *= 0.25F;

            if (mount.isControlledByLocalInstance()){
                mount.setSpeed(getSpeed(mount)); // (float)mount.getAttributeValue(Attributes.MOVEMENT_SPEED));
                MountablesMain.LOGGER.debug(new Vector3d(f, travelVec.y, f1));
                ((IMount)mount).baseTravel(new Vector3d(f, travelVec.y, f1));
            } else if (rider instanceof PlayerEntity) {
                 mount.setDeltaMovement(Vector3d.ZERO);
            }

            mount.calculateEntityAnimation(mount, false);
        }
    }

    private static float getSpeed(LivingEntity mount) {
        return 1;
    }

    public static boolean isControlled(LivingEntity mount) {
        return mount.getPassengers().size() > 0;
    }

    public static ActionResultType handleInteract(LivingEntity mount, PlayerEntity player, Hand hand) {
        player.yRot = mount.yRot;
        player.xRot = mount.xRot;
        player.startRiding(mount);

        return ActionResultType.SUCCESS;
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
