package nl.chromaticvision.sunshine.impl.module.modules.misc;

import net.minecraft.item.ItemEnderPearl;
import net.minecraft.util.EnumHand;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.util.minecraft.InventoryUtils;
import nl.chromaticvision.sunshine.impl.util.minecraft.MessageUtils;

public class SilentPearl extends Module {

    public SilentPearl() {
        super("SilentPearl",
                "Allows you to throw automatically an ender pearl from your hotbar",
                Category.MISC
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (!safeToUpdate()) return;

        for (int i = 8; i >= 0; i--) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemEnderPearl) {
                InventoryUtils.update(i, true);
                mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
                InventoryUtils.update(mc.player.inventory.currentItem, false);
                return;
            }
        }

        MessageUtils.sendClientChatMessage("No ender pearl found in your hotbar!");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onTick() {
        disable();
    }
}
