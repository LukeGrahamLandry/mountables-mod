package io.github.lukegrahamlandry.mountables.mounts;

import io.github.lukegrahamlandry.mountables.items.MountSummonItem;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class WanderGoal extends RandomWalkingGoal {
    private final MountEntity mount;
    private final double speed;

    public WanderGoal(MountEntity mount, double speed) {
        super(mount, speed, 100);
        this.mount = mount;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        if (this.mob.isVehicle() || this.mount.getMovementMode() != 2) {
            return false;
        } else {
            if (this.mob.getRandom().nextInt(this.interval) != 0) {
                return false;
            }

            Vector3d vector3d = this.getPosition();
            if (vector3d == null) {
                return false;
            } else {
                this.wantedX = vector3d.x;
                this.wantedY = vector3d.y;
                this.wantedZ = vector3d.z;
                this.forceTrigger = false;
                return true;
            }
        }
    }


    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.mount.getMovementMode() == 2;
    }

    @Nullable
    protected Vector3d getPosition() {
        if (MountSummonItem.canFlyByDefault(this.mount.getVanillaType())) return waterAvoidingFly();

        return waterAvoidingWalk();
    }

    private Vector3d waterAvoidingWalk() {
        if (this.mob.isInWaterOrBubble()) {
            Vector3d vector3d = RandomPositionGenerator.getLandPos(this.mob, 15, 7);
            return vector3d == null ? super.getPosition() : vector3d;
        } else {
            return this.mob.getRandom().nextFloat() >= 0.001D ? RandomPositionGenerator.getLandPos(this.mob, 10, 7) : super.getPosition();
        }
    }

    protected Vector3d waterAvoidingFly() {
        Vector3d vector3d = null;
        if (this.mob.isInWater()) {
            vector3d = RandomPositionGenerator.getLandPos(this.mob, 15, 15);
        }

        if (this.mob.getRandom().nextFloat() >= 0.001D) {
            vector3d = this.getTreePos();
        }

        return vector3d == null ? super.getPosition() : vector3d;
    }

    @Nullable
    private Vector3d getTreePos() {
        BlockPos blockpos = this.mob.blockPosition();
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        BlockPos.Mutable blockpos$mutable1 = new BlockPos.Mutable();

        for(BlockPos blockpos1 : BlockPos.betweenClosed(MathHelper.floor(this.mob.getX() - 3.0D), MathHelper.floor(this.mob.getY() - 6.0D), MathHelper.floor(this.mob.getZ() - 3.0D), MathHelper.floor(this.mob.getX() + 3.0D), MathHelper.floor(this.mob.getY() + 6.0D), MathHelper.floor(this.mob.getZ() + 3.0D))) {
            if (!blockpos.equals(blockpos1)) {
                Block block = this.mob.level.getBlockState(blockpos$mutable1.setWithOffset(blockpos1, Direction.DOWN)).getBlock();
                boolean flag = block instanceof LeavesBlock || block.is(BlockTags.LOGS);
                if (flag && this.mob.level.isEmptyBlock(blockpos1) && this.mob.level.isEmptyBlock(blockpos$mutable.setWithOffset(blockpos1, Direction.UP))) {
                    return Vector3d.atBottomCenterOf(blockpos1);
                }
            }
        }

        return null;
    }
}
