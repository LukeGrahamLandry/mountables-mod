package io.github.lukegrahamlandry.mountables.network;

import io.github.lukegrahamlandry.mountables.client.gui.CommandChipScreen;
import io.github.lukegrahamlandry.mountables.mounts.MountEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

// server -> client
public class OpenMountScreenPacket {
    private final int entityId;

    public OpenMountScreenPacket(int entityId) {
        this.entityId = entityId;
    }

    public OpenMountScreenPacket(PacketBuffer buf) {
        this(buf.readInt());
    }

    public static void toBytes(OpenMountScreenPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.entityId);
    }

    public static void handle(OpenMountScreenPacket msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> OpenMountScreenPacket.openGUI(msg));
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void openGUI(OpenMountScreenPacket packet) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            Entity entity = player.level.getEntity(packet.entityId);
            if (entity instanceof MountEntity) {
                Minecraft.getInstance().setScreen(new CommandChipScreen((MountEntity) entity));
            } else {
                System.out.println("ERROR: entity " + packet.entityId + " is not a MountEntity");
            }
        }
    }
}