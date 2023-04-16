package nl.chromaticvision.sunshine.impl.module.modules.misc;

import nl.chromaticvision.sunshine.impl.gui.clickgui.ClickGUI;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import org.lwjgl.input.Keyboard;

public class ClickGUIModule extends Module {

    public ClickGUIModule() {
        super("ClickGUI", "display's gui.", Category.MISC);
        setKeybind(Keyboard.KEY_C);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        mc.displayGuiScreen(new ClickGUI());

        disable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
