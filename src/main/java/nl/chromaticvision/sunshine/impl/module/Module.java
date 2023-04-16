package nl.chromaticvision.sunshine.impl.module;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import nl.chromaticvision.sunshine.Reference;
import nl.chromaticvision.sunshine.api.toast.CustomToast;
import nl.chromaticvision.sunshine.impl.module.settings.Bind;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;

import java.util.ArrayList;
import java.util.List;

public class Module {

    public final Minecraft mc = Minecraft.getMinecraft();

    public String name;
    public String description;
    public Category category;

    public boolean enabled;
    public List<Setting> settings = new ArrayList<Setting>();

    public Setting register(Setting setting) {
        this.settings.add(setting);
        return setting;
    }

    public Setting<Bind> bind = register(new Setting<>("Keybind", new Bind(-1)));

    public Module(String name, Category category) {
        this.name = name;
        this.description = "No description found!";
        this.category = category;
    }

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);

        mc.getToastGui().add(new CustomToast(Reference.NAME,
                TextFormatting.RESET + this.getName() + TextFormatting.GREEN + " enabled.",
                new ItemStack(Item.getItemFromBlock(Blocks.CONCRETE), 1, 13),
                3000L
        ));
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);

        mc.getToastGui().add(new CustomToast(Reference.NAME,
                TextFormatting.RESET + this.getName() + TextFormatting.RED + " disabled.",
                new ItemStack(Item.getItemFromBlock(Blocks.CONCRETE), 1, 14),
                3000L
        ));
    }

    public void enable() {
        if (!this.enabled) {
            onEnable();
            this.enabled = true;
        }
    }

    public void disable() {
        if (this.enabled) {
            onDisable();
            this.enabled = false;
        }
    }

    public void toggle() {
        if (this.enabled) {
            disable();
        } else {
            enable();
        }
    }

    public void setEnabled(boolean enabled, boolean toggle) {
        if (toggle) {
            if (enabled) {
                disable();
            } else {
                enable();
            }
        } else {
            this.enabled = enabled;
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bind getKeybind() {
        return bind.getValue();
    }

    public void setKeybind(int keybind) {
        this.bind.setValue(new Bind(keybind));
    }

    public List<Setting> getSettings() {
        return this.settings;
    }

    public boolean hasSettings() {
        return !this.settings.isEmpty();
    }

    public Setting getSettingByName(String name) {
        for (Setting setting : this.settings) {
            if (!setting.getName().equalsIgnoreCase(name)) continue;
            return setting;
        }
        return null;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return this.category;
    }

    public void onTick() {}
    public void onFastTick() {}
    public void onLiving() {}
    public void onRenderOverlay() {}
    public void onWorldRender() {}

}
