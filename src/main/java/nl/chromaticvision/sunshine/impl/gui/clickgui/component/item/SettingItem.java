package nl.chromaticvision.sunshine.impl.gui.clickgui.component.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class SettingItem {

    protected static final Minecraft mc = Minecraft.getMinecraft();

    private String name;
    private int x;
    private int y;
    private int width;
    private int height;

    private boolean visible = true;
    private boolean state;

    public SettingItem(String name) {
        this.name = name;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovering(mouseX, mouseY, x, y, x + width, y + height) && mouseButton == 0) {
            this.state = !state;
            toggle();

            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    public void toggle() {

    }

    public boolean getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isHovering(int mouseX, int mouseY, int x, int y, int x2, int y2) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}
