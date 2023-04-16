package nl.chromaticvision.sunshine.impl.gui.clickgui.component.item.items;

import net.minecraft.client.gui.Gui;
import nl.chromaticvision.sunshine.impl.gui.clickgui.component.item.SettingItem;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;

import java.awt.*;

public class BooleanButton extends SettingItem {

    private Setting setting;

    public BooleanButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.setWidth(96);
        this.setHeight(16);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), this.getState() ? new Color(65, 233, 113).getRGB() : new Color(155, 23, 77).getRGB());
        mc.fontRenderer.drawString(this.getName(), this.getX() + 2, this.getY() + 5, -1);
    }

    @Override
    public void toggle() {
        this.setting.setValue(!((Boolean) this.setting.getValue()));
    }

    @Override
    public boolean getState() {
        return (Boolean) this.setting.getValue();
    }
}
