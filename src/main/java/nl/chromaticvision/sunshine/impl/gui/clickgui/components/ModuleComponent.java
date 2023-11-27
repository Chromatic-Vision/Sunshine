package nl.chromaticvision.sunshine.impl.gui.clickgui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import nl.chromaticvision.sunshine.impl.gui.clickgui.ClickGUI;
import nl.chromaticvision.sunshine.impl.module.Module;

import java.awt.*;
import java.util.Objects;

public class ModuleComponent {

    public final Module parentModule;
    private int x;
    private int y;
    private int width;
    private int height;

    private static final Minecraft mc = Minecraft.getMinecraft();

    public ModuleComponent(Module parentModule, int x, int y, int width, int height) {
        this.parentModule = parentModule;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, parentModule.isEnabled() ? new Color(34, 155, 66).getRGB() : new Color(60, 30, 30).getRGB());
        mc.fontRenderer.drawString(parentModule.getDisplayName(), x + 2, y + 5, -1);

        if (parentModule.getSettings().size() > 2) mc.fontRenderer.drawString("+", x + width - 10, y + 5, -1);

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (ClickGUI.isHovering(mouseX, mouseY, x, y, x + width, y + height)) {

            if (mouseButton == 0) {
                parentModule.toggle();
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }

            if (mouseButton == 1) {
                ClickGUI.getInstance().settingPanelComponent.setCurrentModule(parentModule);
                ClickGUI.getInstance().settingPanelComponent.updateSettings();
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}