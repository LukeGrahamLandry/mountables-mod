package io.github.lukegrahamlandry.mountables.network;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

// it seems that entityData parameters only sync server -> client
// so changing it from a screen will not update on the server
// must send a packet to do it manually

// client -> server
public class UpdateMountSettingsPacket {
    private final int entityId;
    private final int textureType;
    private final int movementType;

    public UpdateMountSettingsPacket(int entityId, int textureType, int movementType) {
        this.entityId = entityId;
        this.textureType = textureType;
        this.movementType = movementType;
    }

    public UpdateMountSettingsPacket(PacketBuffer buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static void toBytes(UpdateMountSettingsPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.textureType);
        buf.writeInt(msg.movementType);
    }

    public static void handle(UpdateMountSettingsPacket msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity player = context.get().getSender();
            assert player != null;
            Entity thing = player.level.getEntity(msg.entityId);
            if (thing instanceof MountEntity){
                MountEntity mount = (MountEntity) thing;
                mount.setTextureType(msg.textureType);
                mount.setMovementMode(msg.movementType);
            }
        });
        context.get().setPacketHandled(true);
    }
}