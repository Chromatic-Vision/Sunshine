package nl.chromaticvision.sunshine;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nl.chromaticvision.sunshine.impl.module.ModuleManager;

@Mod(name = Reference.NAME,
        version = Reference.VERSION,
        modid = Reference.MOD_ID,
        acceptedMinecraftVersions = "[1.12.2]"
)
public class Main {

    @Mod.Instance
    public static Main instance;

    public static ModuleManager moduleManager = new ModuleManager();

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(instance);
        MinecraftForge.EVENT_BUS.register(moduleManager);
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {

    }
}