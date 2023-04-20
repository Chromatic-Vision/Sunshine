package nl.chromaticvision.sunshine.impl.gui.clickgui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;

import java.awt.*;

public class SettingPanelComponent {

    private int x;
    private int y;
    private int width;
    private int height;

    private Module currentModule;

    public SettingPanelComponent(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        Gui.drawRect(x, y, x + width, y + height, new Color(70, 70, 70).getRGB());

        if (currentModule != null && currentModule.getSettings().size() > 0) {

            int sy = y + 10;

            for (Setting setting : currentModule.getSettings()) {
                Minecraft.getMinecraft().fontRenderer.drawString(setting.getName(), x + 5, sy, -1);
                sy += 15;
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Module getCurrentModule() {
        return currentModule;
    }

    public void setCurrentModule(Module module) {
        this.currentModule = module;
    }
}
