package nl.chromaticvision.sunshine.impl.module.modules.render;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.chromaticvision.sunshine.impl.gui.shulkerpreview.GuiShulkerPreview;
import nl.chromaticvision.sunshine.impl.gui.shulkerpreview.ShulkerContainer;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.util.minecraft.InventoryUtils;
import org.lwjgl.input.Keyboard;

public class ShulkerPreview extends Module {

    public int lastX;
    public int lastY;
    public boolean inShulker = false;
    public ItemStack lastHoveredShulkerStack = null;

    public ShulkerPreview() {
        super("ShulkerPreview",
                "Allows you to preview the content of shulker boxes and containers",
                Category.RENDER
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @SubscribeEvent
    public void onKeyPress(GuiScreenEvent.KeyboardInputEvent event) {
        if (this.isEnabled()) {

            //push = true;

            if (Keyboard.getEventKey() == Keyboard.KEY_V) {
                inShulker = Keyboard.getEventKeyState();
            } else {
                inShulker = false;
            }
        }
    }

    @SubscribeEvent
    public void onRenderTooltip(RenderTooltipEvent.Pre event) {
        if (this.isDisabled() || GuiShulkerPreview.renderingTooltip) return;

        if (GuiShulkerPreview.isMouseInsideGui) event.setCanceled(true);
    }

    @SubscribeEvent
    public void onDrawGui(GuiScreenEvent.DrawScreenEvent.Post event) {

        if (this.isDisabled() || !(event.getGui() instanceof GuiContainer)) return;

        Slot hoveringSlot = ((GuiContainer) event.getGui()).getSlotUnderMouse();

        if (inShulker) {

            if (lastHoveredShulkerStack == null || !(lastHoveredShulkerStack.getItem() instanceof ItemShulkerBox)) {
                inShulker = false;
                return;
            }

        } else {

            lastX = event.getMouseX();
            lastY = event.getMouseY();

            if (hoveringSlot == null) {
                lastHoveredShulkerStack = null;
                return;
            }

            lastHoveredShulkerStack = hoveringSlot.getStack().getItem() instanceof ItemShulkerBox ? hoveringSlot.getStack() : null;

        }

        if (lastHoveredShulkerStack != null && lastHoveredShulkerStack != ItemStack.EMPTY) {

            GuiShulkerPreview currentGui = new GuiShulkerPreview(
                    new ShulkerContainer(
                            new ShulkerContainer.ShulkerInventory(
                                    InventoryUtils.getShulkerContents(lastHoveredShulkerStack))),
                    lastHoveredShulkerStack);

            currentGui.setX(lastX);
            currentGui.setY(lastY);
            currentGui.drawScreen(event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks());
        }
    }
}