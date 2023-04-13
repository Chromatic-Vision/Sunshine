package nl.chromaticvision.sunshine.impl.gui.clickgui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;

public class CategoryComponent {

    public int x;
    public int y;
    public int x2;
    public int y2;
    public int width;
    public int height;
    public boolean open = true;
    public String name;

    private static final Minecraft mc = Minecraft.getMinecraft();
    public ArrayList<ModuleCompoment> moduleCompoments = new ArrayList<>();

    public CategoryComponent(int x, int y, int width, int height, String name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
    }

    public void addModuleComponents(ModuleCompoment moduleCompoment) {
        moduleCompoments.add(moduleCompoment);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        x2 = x + width;
        y2 = y + height;

        Gui.drawRect(x, y, x2, y2, new Color(244, 166, 34).getRGB());
        mc.fontRenderer.drawString(this.getName(), x + 2, y + 5, -1);

        if (open) {

            int ry = y + 25;

            for (ModuleCompoment moduleCompoment : moduleCompoments) {
                Gui.drawRect(x, ry, x2, ry + 25, new Color(34, 155, 66).getRGB());
                mc.fontRenderer.drawString(moduleCompoment.parentModule.getName(), x + 2, ry + 5, -1);

                ry += 30;
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseX > x && mouseX < x2 && mouseY > y && mouseY < y2) {
            if (mouseButton == 1) {
                open = !isOpen();
            }
        }
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
