package nl.chromaticvision.sunshine.impl.module.modules.movement;

import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import org.lwjgl.input.Keyboard;

public class Fly extends Module {

    public Fly() {
        super("Fly", Category.MOVEMENT);
        setKeybind(Keyboard.KEY_V);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        mc.player.capabilities.allowFlying = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.player.capabilities.allowFlying = false;
    }
}
