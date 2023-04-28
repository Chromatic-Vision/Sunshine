package nl.chromaticvision.sunshine.impl.gui.clickgui;

import net.minecraft.client.gui.GuiScreen;
import nl.chromaticvision.sunshine.Main;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.CategoryComponent;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.ModuleComponent;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.SettingPanelComponent;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.Button;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.buttons.StringButton;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.modules.misc.ClickGUIModule;
import nl.chromaticvision.sunshine.impl.util.system.FileUtils;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class ClickGUI extends GuiScreen {

    private final ArrayList<CategoryComponent> categoryComponents = new ArrayList<CategoryComponent>();
    public SettingPanelComponent settingPanelComponent;

    private static ClickGUI instance;

    public ClickGUI() {
        load();
        instance = this;
    }

    public static ClickGUI getInstance() {
        return instance == null ? new ClickGUI() : instance;
    }

    public void load() {

        int x = 0;
        int cx = 0;
        int cy = 23;

        settingPanelComponent = new SettingPanelComponent(x + 650, 100, 250, 600);

        for (Category category : Category.values()) {

            CategoryComponent component = new CategoryComponent(x, 5, 100, 16, category.name().toLowerCase());

            for (Module module : Main.moduleManager.getModuleByCategory(category)) {
                component.moduleComponents.add(new ModuleComponent(module, cx + 1, cy, 98, 18));
                cy += 19;
            }

            x += 102;
            cx += 102;
            cy = 23;

            categoryComponents.add(component);
        }
    }

    public void checkMouseWheel() { //bruh
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            categoryComponents.forEach(categoryComponent -> {
                categoryComponent.setY(categoryComponent.getY() - 10);
                categoryComponent.moduleComponents.forEach(moduleComponent -> moduleComponent.setY(moduleComponent.getY() - 10));
            });
        } else if (dWheel > 0) {
            categoryComponents.forEach(categoryComponent -> {
                categoryComponent.setY(categoryComponent.getY() + 10);
                categoryComponent.moduleComponents.forEach(moduleComponent -> moduleComponent.setY(moduleComponent.getY() + 10));
            });
        }
    }

    public static boolean isHovering(int mouseX, int mouseY, int x, int y, int x2, int y2) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        checkMouseWheel();
    }

    @Override
    public void onGuiClosed() {
        FileUtils.saveConfig();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.drawDefaultBackground();

        categoryComponents.forEach(categoryComponent -> categoryComponent.drawScreen(mouseX, mouseY, partialTicks));
        settingPanelComponent.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        categoryComponents.forEach(categoryComponent -> categoryComponent.mouseClicked(mouseX, mouseY, mouseButton));
        settingPanelComponent.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        settingPanelComponent.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        settingPanelComponent.keyTyped(typedChar, keyCode);

        //close gui
        if (keyCode == 1 || keyCode == Main.moduleManager.getModuleByName("ClickGUI").getKeybind().getKey()) {

            boolean mayClose = true;

            for (Button button : settingPanelComponent.buttons) {
                if (button instanceof StringButton) {
                    if (((StringButton) button).isListening()) {
                        mayClose = false;
                        break;
                    }
                }
            }

            if (mayClose) {

                mc.displayGuiScreen(null);

                if (mc.currentScreen == null) {
                    mc.setIngameFocus();
                }
            }
        }
    }
}
