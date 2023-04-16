package nl.chromaticvision.sunshine.impl.module.modules.movement;

import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;
import org.lwjgl.input.Keyboard;

public class Fly extends Module {

    public Fly() {
        super("Fly", Category.MOVEMENT);
    }

    public final Setting<Boolean> test1 = register(new Setting<>("Test1", true));
    public final Setting<Boolean> test2 = register(new Setting<>("Test2", false));

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

    @Override
    public void onTick() {

        if (mc.world == null || mc.player == null) return;

        if (test1.getValue()) {
            mc.player.sendChatMessage("1");
        }

        if (test2.getValue()) {
            mc.player.sendChatMessage("2");
        }
    }
}
