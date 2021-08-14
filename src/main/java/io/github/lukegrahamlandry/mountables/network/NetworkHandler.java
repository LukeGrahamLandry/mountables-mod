package io.github.lukegrahamlandry.mountables.network;

import io.github.lukegrahamlandry.mountables.MountablesMain;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerPackets(){
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MountablesMain.MOD_ID, "packets"), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(nextID(), OpenMountScreenPacket.class, OpenMountScreenPacket::toBytes, OpenMountScreenPacket::new, OpenMountScreenPacket::handle);
        INSTANCE.registerMessage(nextID(), UpdateMountSettingsPacket.class, UpdateMountSettingsPacket::toBytes, UpdateMountSettingsPacket::new, UpdateMountSettingsPacket::handle);
    }
}