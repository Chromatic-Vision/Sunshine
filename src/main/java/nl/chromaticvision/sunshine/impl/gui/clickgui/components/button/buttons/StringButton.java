package nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.buttons;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.ChatAllowedCharacters;
import nl.chromaticvision.sunshine.impl.gui.clickgui.ClickGUI;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.Button;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;

import java.awt.*;
import java.util.Calendar;

public class StringButton extends Button {

    public Setting parentSetting;
    private String input = "";
    private boolean listening = false;

    public StringButton(Setting parentSetting) {
        super(parentSetting.getName());

        this.parentSetting = parentSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        int currentSeconds = Calendar.getInstance().get(Calendar.SECOND);

        Gui.drawRect(x, y, x + width, y + height, listening ? new Color(123, 155, 131).getRGB() : new Color(80, 134, 101).getRGB());
        fr.drawString(listening ? ("Listening... > " + input + (currentSeconds % 2 == 0 ? "_" : "")) : (this.getName() + " > " + parentSetting.getValueAsString()), x + 5, y + 6, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (isHoveringComponent(mouseX, mouseY) && mouseButton == 0) {
            listening = !listening;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (listening) {
            switch (keyCode) {
                case 1:

                    listening = false;
                    input = "";

                    break;
                case 14:

                    if (input != null && input.length() > 0) {
                        input = input.substring(0, input.length() - 1);
                        break;
                    }

                case 28:

                    listening = false;

                    if (parentSetting.getName().equalsIgnoreCase("DisplayName") && input.equalsIgnoreCase("")) {
                        parentSetting.setValue(ClickGUI.getInstance().settingPanelComponent.getCurrentModule().getName());
                    } else {
                        parentSetting.setValue(input);
                    }

                    input = "";
                    break;
            }

            if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                input += typedChar;
            }
        }
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public boolean isListening() {
        return listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }
}
