package nl.chromaticvision.sunshine.impl.gui.clickgui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import nl.chromaticvision.sunshine.impl.gui.clickgui.ClickGUI;
import nl.chromaticvision.sunshine.impl.gui.clickgui.component.item.SettingItem;
import nl.chromaticvision.sunshine.impl.gui.clickgui.component.item.items.BooleanButton;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;

import java.awt.*;
import java.util.ArrayList;

public class ModuleCompoment {

    private final Module parentModule;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean open = false;

    private static final Minecraft mc = Minecraft.getMinecraft();
    public ArrayList<SettingItem> items = new ArrayList<>();

    public ModuleCompoment(Module parentModule, int x, int y, int width, int height) {
        this.parentModule = parentModule;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        init();
    }

    public void init() {
        if (parentModule.getSettings().size() > 0) {

            int sy = y + 5;

            for (Setting setting : parentModule.getSettings()) {
                if (setting.getValue() instanceof Boolean) {

                    BooleanButton booleanButton = new BooleanButton(setting);
                    booleanButton.setLocation(x + 5, y + sy);

                    items.add(booleanButton);
                }

                sy += 18;
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, new Color(34, 155, 66).getRGB());
        mc.fontRenderer.drawString(parentModule.getName(), x + 2, y + 5, -1);

        if (open) {
            items.stream()
                    .filter(SettingItem::isVisible)
                    .forEach(item -> item.drawScreen(mouseX, mouseY, partialTicks));
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (ClickGUI.isHovering(mouseX, mouseY, x, y, x + width, y + height)) {

            if (mouseButton == 0) {
                parentModule.toggle();
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }

            if (mouseButton == 1) {
                open = !open;
            }
        }

        if (open) {
            items.stream()
                    .filter(SettingItem::isVisible)
                    .forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
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

    public boolean isOpen() {
        return open;
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

    public void setOpen(boolean open) {
        this.open = open;
    }

}
