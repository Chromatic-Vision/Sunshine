package nl.chromaticvision.sunshine.impl.module.modules.misc;

import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;

public class ChatExtension extends Module {

    private static ChatExtension instance;

    public ChatExtension() {
        super("ChatExtension", Category.MISC);

        instance = this;
    }

    public static ChatExtension getInstance() {
        return instance;
    }

    public final Setting<Boolean> clean = register(new Setting<>("Clean", true));
    public final Setting<Boolean> timeStamp = register(new Setting<>("Timestamp", false));

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
