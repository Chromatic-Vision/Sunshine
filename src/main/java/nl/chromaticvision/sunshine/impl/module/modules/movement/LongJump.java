package nl.chromaticvision.sunshine.impl.module.modules.movement;

import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;

public class LongJump extends Module {

    public LongJump() {
        super("LongJump", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        mc.player.jumpMovementFactor = 3f;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.player.jumpMovementFactor = 0.05f;
    }
}
