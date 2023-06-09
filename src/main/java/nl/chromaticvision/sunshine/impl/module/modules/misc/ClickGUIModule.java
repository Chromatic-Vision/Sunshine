package nl.chromaticvision.sunshine.impl.module.modules.misc;

import nl.chromaticvision.sunshine.impl.gui.clickgui.ClickGUI;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import org.lwjgl.input.Keyboard;

public class ClickGUIModule extends Module {

    public ClickGUIModule() {
        super("ClickGUI", "Displays setting gui of this client.", Category.MISC);
        setKeybind(Keyboard.KEY_0);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        mc.displayGuiScreen(new ClickGUI());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onTick() {
        setEnabled(false, false);
    }
}
