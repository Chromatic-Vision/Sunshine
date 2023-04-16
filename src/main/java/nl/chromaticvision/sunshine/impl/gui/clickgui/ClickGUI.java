package nl.chromaticvision.sunshine.impl.gui.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import nl.chromaticvision.sunshine.Main;
import nl.chromaticvision.sunshine.impl.gui.clickgui.component.CategoryComponent;
import nl.chromaticvision.sunshine.impl.gui.clickgui.component.ModuleCompoment;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;
import nl.chromaticvision.sunshine.impl.util.system.FileUtils;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickGUI extends GuiScreen {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<CategoryComponent> categoryComponents = new ArrayList<CategoryComponent>();

    public ClickGUI() {
        load();
    }

    public void load() {

        int x = 0;
        int cx = 0;
        int cy = 23;

        for (Category category : Category.values()) {

            CategoryComponent component = new CategoryComponent(x, 5, 100, 16, category.name().toLowerCase());

            for (Module module : Main.moduleManager.getModuleByCategory(category)) {
                component.moduleCompoments.add(new ModuleCompoment(module, cx + 1, cy, 98, 18));
                cy += 19;
            }

            x += 102;
            cx += 102;
            cy = 23;

            categoryComponents.add(component);
        }
    }

    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            categoryComponents.forEach(categoryComponent -> {
                categoryComponent.setY(categoryComponent.getY() - 10);
                categoryComponent.moduleCompoments.forEach(moduleCompoment -> {
                    moduleCompoment.setY(moduleCompoment.getY() - 10);
                    moduleCompoment.items.forEach(item -> item.setY(item.getY() - 10));
                });
            });
        } else if (dWheel > 0) {
            categoryComponents.forEach(categoryComponent -> {
                categoryComponent.setY(categoryComponent.getY() + 10);
                categoryComponent.moduleCompoments.forEach(moduleCompoment -> {
                    moduleCompoment.setY(moduleCompoment.getY() + 10);
                    moduleCompoment.items.forEach(item -> item.setY(item.getY() + 10));
                });
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
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        categoryComponents.forEach(categoryComponent -> categoryComponent.mouseClicked(mouseX, mouseY, mouseButton));
    }


}
