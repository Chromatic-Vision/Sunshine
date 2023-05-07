package nl.chromaticvision.sunshine.impl.gui.clickgui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import nl.chromaticvision.sunshine.Main;
import nl.chromaticvision.sunshine.impl.gui.clickgui.ClickGUI;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.Button;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.buttons.*;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.ModuleManager;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingPanelComponent {

    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean dragging;


    private final int buttonHeight = 18;
    public int hoveringDescriptionTimer = 0;
    private int buttonOffset = 0;
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

    public String insertNewLine(String str, int limit) {
        if (str.length() > limit) {
            StringBuilder stringBuilder = new StringBuilder(str);
            int index = limit;
            while (index >= 0 && index < stringBuilder.length()) {
                if (Character.isWhitespace(stringBuilder.charAt(index))) {
                    stringBuilder.replace(index, index + 1, "\n");
                    index += limit;
                } else {
                    index--;
                }
            }
            str = stringBuilder.toString();
        }
        return str;
    }

    public List<String> getListOfSplittedStringByWhiteSpace(String string, int limit) {

        if (string.length() > limit) {
            string = insertNewLine(string, limit);
        }

        String[] splitStrings = string.split("\n");

        return Arrays.asList(splitStrings);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if (dragging) {
            x = mouseX + x2;
            y = mouseY + y2;
        }

        Gui.drawRect(x, y, x + width, y + height, new Color(70, 70, 70).getRGB()); //background

        if (currentModule == null) return;

        Gui.drawRect(x, y, x + width, y + buttonHeight, new Color(222, 182, 14).getRGB());
        mc.fontRenderer.drawString(currentModule.getName(), x + 5, y + 6, -6);

        if (currentModule.getDescription() != null) { // draw description

            int bx = x + width - 16;
            int by = y + 3;
            int bwidth = 12;
            int bheight = 12;

            Gui.drawRect(bx, by, bx + bwidth, by + bheight, new Color(90, 117, 173).getRGB());
            mc.fontRenderer.drawString("?", bx + 3, by + 2, -1, true);

            if (ClickGUI.isHovering(mouseX, mouseY, bx, by, bx + bwidth, by + bheight)) {
                hoveringDescriptionTimer++;
            } else {
                hoveringDescriptionTimer = 0;
            }

            if (hoveringDescriptionTimer >= 30) {

                List<String> descriptions = new ArrayList<>();

                if (mc.fontRenderer.getStringWidth(currentModule.getDescription()) - 5 > width) {
                    descriptions = getListOfSplittedStringByWhiteSpace(currentModule.getDescription(), 47);
                } else {
                    descriptions.add(currentModule.getDescription());
                }
                
                int sy = y + 25;

                for (String description : descriptions) {
                    mc.fontRenderer.drawString(description, x + 5, sy, -1);
                    sy += mc.fontRenderer.FONT_HEIGHT + 1;
                }

                if (buttonOffset < descriptions.size() * (mc.fontRenderer.FONT_HEIGHT + 1) + 10) buttonOffset += 1;

            } else {
                if (buttonOffset > 0) buttonOffset -= 1;
            }
        }

        int sy = y + buttonHeight + buttonOffset + 1;

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