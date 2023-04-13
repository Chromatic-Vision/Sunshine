package nl.chromaticvision.sunshine.impl.gui.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import nl.chromaticvision.sunshine.Main;
import nl.chromaticvision.sunshine.impl.gui.clickgui.component.CategoryComponent;
import nl.chromaticvision.sunshine.impl.gui.clickgui.component.ModuleCompoment;
import nl.chromaticvision.sunshine.impl.module.Category;

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

        for (Category category : Category.values()) {

            CategoryComponent component = new CategoryComponent(x += 100, 5, 95, 20, category.name().toLowerCase());
            Main.moduleManager.getModuleByCategory(category).forEach(module -> component.addModuleComponents(new ModuleCompoment(module, 80, 18)));

            categoryComponents.add(component);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        categoryComponents.forEach(categoryComponent -> categoryComponent.drawScreen(mouseX, mouseY, partialTicks));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        categoryComponents.forEach(categoryComponent -> categoryComponent.mouseClicked(mouseX, mouseY, mouseButton));
    }
}
