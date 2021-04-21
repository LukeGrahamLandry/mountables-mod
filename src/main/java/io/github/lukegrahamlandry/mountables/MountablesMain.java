package io.github.lukegrahamlandry.mountables;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod("mountables")
public class MountablesMain {
    public static final Logger LOGGER = LogManager.getLogger();

    public MountablesMain() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
