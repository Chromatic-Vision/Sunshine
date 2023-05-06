package nl.chromaticvision.sunshine.impl.gui.shulkerpreview;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import nl.chromaticvision.sunshine.Reference;

public class GuiShulkerPreview extends GuiContainer {

    private final ItemStack parentShulkerStack;
    public static boolean renderingTooltip;
    public static boolean isMouseInsideGui;
    private int x;
    private int y;

    public GuiShulkerPreview(Container inventorySlotsIn, ItemStack parentShulkerStack) {
        super(inventorySlotsIn);

        this.parentShulkerStack = parentShulkerStack;
        this.mc = Minecraft.getMinecraft();
        this.fontRenderer = mc.fontRenderer;
        this.width = mc.displayWidth;
        this.height = mc.displayHeight;
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

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if (parentShulkerStack == null || !(parentShulkerStack.getItem() instanceof ItemShulkerBox)) return;

        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        RenderHelper.disableStandardItemLighting();

        GlStateManager.color(1f, 1f, 1f, 1f);

        mc.getTextureManager().bindTexture(new ResourceLocation(Reference.MOD_ID, "images/shulker_box_top.png"));

        Gui.drawModalRectWithCustomSizedTexture(x + 8,
                y - 97,
                0,
                0,
                256,
                256,
                256,
                256
        );

        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();

        GlStateManager.enableLighting();
        RenderHelper.enableGUIStandardItemLighting();

        mc.getRenderItem().zLevel = 300.1f;

        Slot hoveringSlot = null;

        int ix = x + 16;
        int iy = y - 98;

        for (int j = 0; j < inventorySlots.inventorySlots.size(); j++) {

            Slot slot = inventorySlots.inventorySlots.get(j);

            if (slot == null) continue;

            mc.getRenderItem().renderItemAndEffectIntoGUI(slot.getStack(),
                    ShulkerContainer.getSlotPos(j)[0] + ix,
                    ShulkerContainer.getSlotPos(j)[1] + iy
            );

            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer,
                    slot.getStack(),
                    ShulkerContainer.getSlotPos(j)[0] + ix,
                    ShulkerContainer.getSlotPos(j)[1] + iy,
                    null
            );

            if (this.isPointInRegion(ShulkerContainer.getSlotPos(j)[0] + ix,
                    ShulkerContainer.getSlotPos(j)[1] + iy,
                    16,
                    16,
                    mouseX,
                    mouseY
            )) {
                hoveringSlot = slot;
            }
        }

        mc.getRenderItem().zLevel = 0f;

        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableRescaleNormal();

        if (hoveringSlot != null && hoveringSlot.getStack() != ItemStack.EMPTY) {

            GlStateManager.colorMask(true, true, true, false);
            this.drawGradientRect(
                    ix + hoveringSlot.xPos,
                    iy + hoveringSlot.yPos,
                    ix + hoveringSlot.xPos + 16,
                    iy + hoveringSlot.yPos + 16,
                    -2130706433,
                    -2130706433
            );
            GlStateManager.colorMask(true, true, true, true);

            renderingTooltip = true;
            this.renderToolTip(hoveringSlot.getStack(), mouseX, mouseY);
            renderingTooltip = false;

        }

        GlStateManager.disableBlend();

        isMouseInsideGui = this.isPointInRegion(x + 11,
                y - 88,
                x + 181,
                y + 77,
                mouseX,
                mouseY
        );
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int j) {

    }
}