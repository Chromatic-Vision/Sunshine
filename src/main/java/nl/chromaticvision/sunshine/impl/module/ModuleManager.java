package nl.chromaticvision.sunshine.impl.module;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import nl.chromaticvision.sunshine.impl.module.modules.misc.ClickGUIModule;
import nl.chromaticvision.sunshine.impl.module.modules.misc.ExampleModule;
import nl.chromaticvision.sunshine.impl.module.modules.movement.Fly;
import nl.chromaticvision.sunshine.impl.module.modules.movement.LongJump;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

public class ModuleManager {

    private final ArrayList<Module> modules;

    public ModuleManager() {
        MinecraftForge.EVENT_BUS.register(this);
        modules = new ArrayList<>();

        modules.add(new ExampleModule());
        modules.add(new ClickGUIModule());

        modules.add(new Fly());
        modules.add(new LongJump());
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        modules.stream()
                .filter(Module::isEnabled)
                .forEach(Module::onTick);
    }

    @SubscribeEvent
    public void onFastTick(TickEvent event) {
        modules.stream()
                .filter(Module::isEnabled)
                .forEach(Module::onFastTick);
    }

    @SubscribeEvent
    public void onLiving(LivingEvent.LivingUpdateEvent event) {
        modules.stream()
                .filter(Module::isEnabled)
                .forEach(Module::onLiving);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        modules.stream()
                .filter(Module::isEnabled)
                .forEach(Module::onRenderOverlay);

        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            Minecraft.getMinecraft().fontRenderer.drawString("Sunshine!!", 2, 5, new Color(255, 228, 23).getRGB());
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        modules.stream()
                .filter(Module::isEnabled)
                .forEach(Module::onWorldRender);
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            modules.forEach(module -> {
                if (module.getKeybind().getKey() == Keyboard.getEventKey()) {
                    module.toggle();
                }
            });
        }
    }

    public ArrayList<Module> getModuleByCategory(Category category) {

        ArrayList<Module> categoryModules = new ArrayList<>();

        modules.forEach(m -> {
            if (m.getCategory() == category) {
                categoryModules.add(m);
            }
        });

        return categoryModules;
    }

    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) return module;
        }
        return null;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }
}
