package io.github.lukegrahamlandry.mountables.mounts;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.World;

public class GoPoof extends MobEntity {
    public GoPoof(EntityType<? extends MobEntity> p_i48576_1_, World p_i48576_2_) {
        super(p_i48576_1_, p_i48576_2_);
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        this.remove();
    }
}
