package nl.chromaticvision.sunshine.impl.module.modules.movement;

import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;

public class AutoJump extends Module {

    public AutoJump() {
        super("AutoJump", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onTick() {

        if (mc.player == null || mc.world == null) return;

        if (mc.player.onGround) mc.player.jump();
    }
}
