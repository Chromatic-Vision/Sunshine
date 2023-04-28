package nl.chromaticvision.sunshine.impl.gui.clickgui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import nl.chromaticvision.sunshine.impl.gui.clickgui.ClickGUI;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.Button;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.buttons.*;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;

import java.awt.*;
import java.util.ArrayList;

public class SettingPanelComponent {

    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean dragging;


    private final int buttonHeight = 18;

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

        if (currentModule.hasSettings()) {
            for (Setting setting : currentModule.getSettings()) {

                if (!setting.isVisible()) continue;

                if (setting.getValue() instanceof Boolean) {
                    buttons.add(new BooleanButton(setting));
                    continue;
                }

                if (setting.isNumberSetting()) {
                    buttons.add(new NumberButton(setting));
                    continue;
                }

                if (setting.isStringSetting()) {
                    buttons.add(new StringButton(setting));
                    continue;
                }

                if (setting.getName().equalsIgnoreCase("Keybind")) {
                    buttons.add(new BindButton(setting));
                    continue;
                }

                if (setting.isEnumSetting()) {
                    buttons.add(new EnumButton(setting));
                }
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if (dragging) {
            x = mouseX + x2;
            y = mouseY + y2;
        }

        Gui.drawRect(x, y, x + width, y + height, new Color(70, 70, 70).getRGB()); //background

        if (currentModule == null) return;

        Gui.drawRect(x, y, x + width, y + buttonHeight, new Color(229, 211, 21).getRGB());
        mc.fontRenderer.drawString(currentModule.getName(), x + 5, y + 5, -6);

        int sy = y + buttonHeight + 1;

        if (buttons.isEmpty()) return;

        for (Button button : buttons) {

            button.setX(x);
            button.setY(sy);
            button.setWidth(width);
            button.setHeight(buttonHeight);

            sy += buttonHeight + 1;
        }

        buttons.forEach(button -> button.drawScreen(mouseX, mouseY, partialTicks));
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        if (ClickGUI.isHovering(mouseX, mouseY, x, y, x + width, y + buttonHeight) && mouseButton == 0) {

            x2 = x - mouseX;
            y2 = y - mouseY;

            dragging = true;
        }

        buttons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) dragging = false;

        buttons.forEach(button -> button.mouseReleased(mouseX, mouseY, state));
    }

    public void keyTyped(char typedChar, int keyCode) {
        buttons.forEach(button -> button.keyTyped(typedChar, keyCode));
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