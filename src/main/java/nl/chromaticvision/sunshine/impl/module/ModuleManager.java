package nl.chromaticvision.sunshine.impl.module;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import nl.chromaticvision.sunshine.impl.module.modules.exploit.AnvilSwap;
import nl.chromaticvision.sunshine.impl.module.modules.misc.ClickGUIModule;
import nl.chromaticvision.sunshine.impl.module.modules.misc.SilentPearl;
import nl.chromaticvision.sunshine.impl.module.modules.misc.ShulkerHopper;
import nl.chromaticvision.sunshine.impl.module.modules.render.ShulkerPreview;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class ModuleManager {

    private final ArrayList<Module> modules;

    public ModuleManager() {
        MinecraftForge.EVENT_BUS.register(this);
        modules = new ArrayList<>();

        //render
        modules.add(new ShulkerPreview());

        //misc
        modules.add(new ClickGUIModule());
        //modules.add(new ShulkerHopper());
        modules.add(new SilentPearl());

        //exploit
        modules.add(new AnvilSwap());
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        modules.stream()
                .filter(Module::isEnabled)
                .filter(Module::safeToUpdate)
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
                .filter(Module::safeToUpdate)
                .forEach(Module::onLiving);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {

        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) return;

        modules.stream()
                .filter(Module::isEnabled)
                .forEach(Module::onRenderOverlay);

        //Minecraft.getMinecraft().fontRenderer.drawString("Sunshine!!", 2, 5, new Color(255, 228, 23).getRGB());
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
