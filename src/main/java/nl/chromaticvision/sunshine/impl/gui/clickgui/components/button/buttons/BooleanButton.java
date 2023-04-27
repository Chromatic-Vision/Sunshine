package nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.buttons;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.Button;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;

import java.awt.*;

public class BooleanButton extends Button {

    public Setting parentSetting;

    public BooleanButton(Setting parentSetting) {
        super(parentSetting.getName());

        this.parentSetting = parentSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, (Boolean) parentSetting.getValue() ? new Color(178, 124, 88).getRGB() : new Color(144, 153, 122).getRGB());
        fr.drawString(this.getName(), x + 5, y + 6, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (isHoveringComponent(mouseX, mouseY) && mouseButton == 0) {
            parentSetting.setValue(!((Boolean) parentSetting.getValue()));
        }
    }
}
