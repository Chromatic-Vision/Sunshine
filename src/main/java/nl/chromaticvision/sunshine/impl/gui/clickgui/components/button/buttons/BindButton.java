package nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.buttons;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.Button;
import nl.chromaticvision.sunshine.impl.module.settings.Bind;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Locale;

public class BindButton extends Button {

    public Setting parentSetting;
    private boolean listening;
    private Bind bind;

    public BindButton(Setting parentSetting) {
        super(parentSetting.getName());

        this.parentSetting = parentSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        bind = (Bind) parentSetting.getValue();

        Gui.drawRect(x, y, x + width, y + height, listening ? new Color(123, 155, 131).getRGB() : new Color(80, 134, 101).getRGB());
        fr.drawString(listening ? "Press a new keybind..." : (this.getName() + " > " + getKeyDisplayString(bind.getKey())), x + 5, y + 6, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (this.isHoveringComponent(mouseX, mouseY) && mouseButton == 0) {
            listening = !listening;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (listening) {
            switch (keyCode) {
                case 1:
                    listening = false;
                    return;
                case 14:
                    parentSetting.setValue(new Bind(-1));
                    listening = false;
                    return;
            }

            parentSetting.setValue(new Bind(keyCode));
            listening = false;
        }
    }

    public static String getKeyDisplayString(int key) {

        if (key == -1) return "NONE";

        if (key < 0) {
            switch (key) {
                case -100:
                    return I18n.format("key.mouse.left");
                case -99:
                    return I18n.format("key.mouse.right");
                case -98:
                    return I18n.format("key.mouse.middle");
                default:
                    return I18n.format("key.mouseButton", key + 101);
            }
        } else {
            return key < 256 ? Keyboard.getKeyName(key) : String.format(String.valueOf((char)(key - 256)).toUpperCase(Locale.ROOT));
        }
    }
}
