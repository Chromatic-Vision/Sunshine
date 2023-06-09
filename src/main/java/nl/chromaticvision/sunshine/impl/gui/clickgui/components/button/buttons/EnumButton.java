package nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.buttons;

import net.minecraft.client.gui.Gui;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.Button;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;

import java.awt.*;

public class EnumButton extends Button {

    private Setting parentSetting;

    public EnumButton(Setting parentSetting) {
        super(parentSetting.getName());

        this.parentSetting = parentSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        float current = ((Number) parentSetting.getEnum(parentSetting.currentEnumName())).floatValue();
        int length = ((Enum) parentSetting.getValue()).getClass().getEnumConstants().length - 1;

        if (length <= 0) {
            System.err.println("Who tf uses only one enum?????");
            return;
        }

        Gui.drawRect(x, y, x + ((int) ((current / length) * width)), y + height, new Color(80, 134, 101).getRGB());
        fr.drawString(parentSetting.getName() + " > " + parentSetting.currentEnumName(), x + 5, y + 6, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (this.isHoveringComponent(mouseX, mouseY) && mouseButton == 0) {
            parentSetting.increaseEnumNoEvent();
        }
    }
}