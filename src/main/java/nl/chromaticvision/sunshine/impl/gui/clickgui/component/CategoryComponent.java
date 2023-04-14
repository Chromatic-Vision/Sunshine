package nl.chromaticvision.sunshine.impl.gui.clickgui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import nl.chromaticvision.sunshine.impl.gui.clickgui.ClickGUI;

import java.awt.*;
import java.util.ArrayList;

public class CategoryComponent {

    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean open = true;
    private String name;

    private static final Minecraft mc = Minecraft.getMinecraft();
    public ArrayList<ModuleCompoment> moduleCompoments = new ArrayList<>();

    public CategoryComponent(int x, int y, int width, int height, String name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        Gui.drawRect(x, y, x + width, y + height, new Color(244, 166, 34).getRGB());
        mc.fontRenderer.drawString(this.getName(), x + 2, y + 5, -1);

        if (open) {
            moduleCompoments.forEach(moduleCompoment -> moduleCompoment.drawScreen(mouseX, mouseY, partialTicks));
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        if (open) {
            moduleCompoments.forEach(moduleCompoment -> moduleCompoment.mouseClicked(mouseX, mouseY, mouseButton));
        }

        if (ClickGUI.isHovering(mouseX, mouseY, x, y, x + width, y + height) && mouseButton == 1) open = !open;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
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

    public String getName() {
        return name;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
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

    public void setName(String name) {
        this.name = name;
    }

}
