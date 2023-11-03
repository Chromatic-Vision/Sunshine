package nl.chromaticvision.sunshine.impl.module.modules.render;

import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;

public class FullBright extends Module {

    public FullBright() {
        super("FullBright", Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        mc.gameSettings.gammaSetting = 1000f;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
