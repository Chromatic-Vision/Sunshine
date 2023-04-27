package nl.chromaticvision.sunshine.impl.gui.clickgui.components.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.SoundEvents;
import nl.chromaticvision.sunshine.impl.gui.clickgui.ClickGUI;

public class Button {

    public String name;
    public int x;
    public int y;
    public int width;
    public int height;

    protected final Minecraft mc = Minecraft.getMinecraft();
    protected final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

    public Button(String name) {
        this.name = name;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    public boolean isHoveringComponent(int mouseX, int mouseY) {
        return ClickGUI.isHovering(mouseX, mouseY, x, y, x + width, y + height);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
