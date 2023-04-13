package nl.chromaticvision.sunshine.impl.gui.clickgui.component;

import nl.chromaticvision.sunshine.impl.module.Module;

public class ModuleCompoment {

    public Module parentModule;
    public int x;
    public int y;
    public int x2;
    public int y2;
    public int width;
    public int height;
    public boolean open = true;

    public ModuleCompoment(Module parentModule, int width, int height) {
        this.parentModule = parentModule;
        //this.x = x;
        //this.y = y;
        this.width = width;
        this.height = height;
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

}
