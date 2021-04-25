package io.github.lukegrahamlandry.mountables.util;

import net.minecraft.util.math.vector.Vector3d;

public interface IMount {
    void baseTravel(Vector3d vec);

    void rotate(float y, float x);

    void setFlying(boolean flag);
    boolean isFlying();

    float playerJumpPendingScale();

    void setPlayerJumpPendingScale(float v);

    boolean isJumping();
}
