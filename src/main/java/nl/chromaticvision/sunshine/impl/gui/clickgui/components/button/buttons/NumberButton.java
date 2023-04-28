package nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.buttons;

import net.minecraft.client.gui.Gui;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.Button;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;

import java.awt.*;

public class NumberButton extends Button {

    public Setting parentSetting;

    private final Number min;
    private final Number max;
    private final int difference;
    private boolean draggingSlider;

    public NumberButton(Setting parentSetting) {
        super(parentSetting.getName());

        this.parentSetting = parentSetting;

        min = (Number) parentSetting.getMin();
        max = (Number) parentSetting.getMax();

        difference = max.intValue() - min.intValue();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if (draggingSlider) setSettingFromMouseX(mouseX);

        Gui.drawRect(x, y, x + (int) (((((Number) parentSetting.getValue()).floatValue() - min.floatValue()) / (max.floatValue() - min.floatValue())) * width), y + height, new Color(80, 134, 101).getRGB());
        fr.drawString(this.getName() + " > " + parentSetting.getValue(), x + 5, y + 6, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (this.isHoveringComponent(mouseX, mouseY) && mouseButton == 0) {
            draggingSlider = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) draggingSlider = false;
    }

    public void setSettingFromMouseX(int mouseX) {

        float percent = ((float) mouseX - x) / ((float) width);

        if (parentSetting.getMin() == null || parentSetting.getMax() == null) return;

        if (parentSetting.getValue() instanceof Double) {

            double result = (Double) parentSetting.getMin() + (double) ((float) difference * percent);
            double value = (double) Math.round(10.0 * result) / 10.0;

            if (value > (double) max) {
                value = (double) max;
            }

            if (value < (double) min) {
                value = (double) min;
            }

            parentSetting.setValue(value);
        } else if (parentSetting.getValue() instanceof Float) {

            float result = ((Float) parentSetting.getMin()).floatValue() + (float) this.difference * percent;
            float value = (float) Math.round(10.0f * result) / 10.0f;

            if (value > (float) max) {
                value = (float) max;
            }

            if (value < (float) min) {
                value = (float) min;
            }

            parentSetting.setValue(value);
        } else if (parentSetting.getValue() instanceof Integer) {

            int value = (Integer) parentSetting.getMin() + (int) ((float) this.difference * percent);

            if (value > (int) max) {
                value = (int) max;
            }

            if (value < (int) min) {
                value = (int) min;
            }

            parentSetting.setValue(value);
        } else if (parentSetting.getValue() instanceof Long) {

            long value = (Long) parentSetting.getMin() + (long) ((float) this.difference * percent);

            if (value > (long) max) {
                value = (long) max;
            }

            if (value < (long) min) {
                value = (long) min;
            }

            parentSetting.setValue(value);
        } else if (parentSetting.getValue() instanceof Short) {

            short value = (short) ((Short) parentSetting.getMin() + (short) ((float) this.difference * percent));

            if (value > (short) max) {
                value = (short) max;
            }

            if (value < (short) min) {
                value = (short) min;
            }

            parentSetting.setValue(value);
        } else if (parentSetting.getValue() instanceof Byte) {

            byte value = (byte) ((Byte) parentSetting.getMin() + (byte) ((short) this.difference * percent));

            if (value > (byte) max) {
                value = (byte) max;
            }

            if (value < (byte) min) {
                value = (byte) min;
            }
        }
    }
}
