package nl.chromaticvision.sunshine.impl.gui.clickgui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.Button;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.buttons.BooleanButton;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;

import java.awt.*;
import java.util.ArrayList;

public class SettingPanelComponent {

    private int x;
    private int y;
    private int width;
    private int height;

    private Module currentModule;
    private final Minecraft mc = Minecraft.getMinecraft();
    public ArrayList<Button> buttons = new ArrayList<>();

    public SettingPanelComponent(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void updateSettings() {

        buttons.clear();

        if (currentModule.getSettings().isEmpty()) return;

        for (Setting setting : currentModule.getSettings()) {

            if (!setting.isVisible()) continue;

            if (setting.getValue() instanceof Boolean) {
                buttons.add(new BooleanButton(setting));
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        Gui.drawRect(x, y, x + width, y + height, new Color(70, 70, 70).getRGB());

        if (currentModule == null) return;

        Gui.drawRect(x, y, x + width, y + 18, new Color(229, 211, 21).getRGB());
        mc.fontRenderer.drawString(currentModule.getName(), x + 5, y + 5, -1);

        int sy = y + 18;

        if (buttons.isEmpty()) return;

        for (Button button : buttons) {

            button.setX(x);
            button.setY(sy);
            button.setWidth(width);
            button.setHeight(18);

            sy += 19;
        }

        buttons.forEach(button -> button.drawScreen(mouseX, mouseY, partialTicks));
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        buttons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
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
