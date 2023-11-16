package nl.chromaticvision.sunshine.impl.module.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Bind;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;
import nl.chromaticvision.sunshine.impl.util.minecraft.BlockUtils;
import nl.chromaticvision.sunshine.impl.util.minecraft.InventoryUtils;
import nl.chromaticvision.sunshine.impl.util.minecraft.RenderUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ShulkerPreview extends Module {

    public int lastX;
    public int lastY;
    public boolean inShulker = false;
    public ItemStack lastHoveredShulkerStack = null;

    public ShulkerPreview() {
        super("ShulkerPreview",
                "Allows you to preview the content of shulker boxes in the containers. Hold the key you've bound to preview the shulker box.",
                Category.RENDER,
                new ItemStack(Blocks.PURPLE_SHULKER_BOX)
        );
    }

    public final Setting<Bind> viewShulker = register(new Setting<>("ViewShulker", new Bind(Keyboard.KEY_V)));
    public final Setting<Boolean> color = register(new Setting<>("ColorShulker", true));


    @Override
    public void onEnable() {
        super.onEnable();

        this.setDisplayStack(new ItemStack(BlockUtils.shulkerList.get(new Random().nextInt(BlockUtils.shulkerList.size()))));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @SubscribeEvent
    public void onKeyPress(GuiScreenEvent.KeyboardInputEvent event) {
        if (this.isEnabled()) {
            if (Keyboard.getEventKey() == viewShulker.getValue().getKey()) {
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
                    new ShulkerContainer(new ShulkerContainer.ShulkerInventory(InventoryUtils.getShulkerContents(lastHoveredShulkerStack))),
                    lastHoveredShulkerStack,
                    color.getValue()
            );

            currentGui.setX(lastX);
            currentGui.setY(lastY);
            currentGui.drawScreen(event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks());
        }
    }

    public static class ShulkerContainer extends Container {

        public static int[] getSlotPos(int s) {

            int[] res = new int[2];

            res[0] = s % 9 * 18;
            res[1] = ((s / 9 + 1) * 18) + 1;

            return res;
        }

        public ShulkerContainer(ShulkerInventory shulkerInventory) {
            for (int i = 0; i < shulkerInventory.contents.size(); i++) {
                addSlotToContainer(new Slot(shulkerInventory, i, getSlotPos(i)[0], getSlotPos(i)[1]));
            }
        }

        @Override
        public boolean canInteractWith(EntityPlayer entityPlayer) {
            return false;
        }

        public static class ShulkerInventory implements IInventory {

            private final List<ItemStack> contents;

            public ShulkerInventory(List<ItemStack> contents) {
                this.contents = contents;
            }

            @Override
            public int getSizeInventory() {
                return contents.size();
            }

            @Override
            public boolean isEmpty() {
                return contents.isEmpty();
            }

            @Override
            public ItemStack getStackInSlot(int i) {
                return contents.get(i);
            }

            @Override
            public ItemStack decrStackSize(int i, int i1) {
                return null;
            }

            @Override
            public ItemStack removeStackFromSlot(int i) {
                return null;
            }

            @Override
            public void setInventorySlotContents(int i, ItemStack itemStack) {

            }

            @Override
            public int getInventoryStackLimit() {
                return contents.size();
            }

            @Override
            public void markDirty() {

            }

            @Override
            public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
                return false;
            }

            @Override
            public void openInventory(EntityPlayer entityPlayer) {

            }

            @Override
            public void closeInventory(EntityPlayer entityPlayer) {

            }

            @Override
            public boolean isItemValidForSlot(int i, ItemStack itemStack) {
                return false;
            }

            @Override
            public int getField(int i) {
                return 0;
            }

            @Override
            public void setField(int i, int i1) {

            }

            @Override
            public int getFieldCount() {
                return 0;
            }

            @Override
            public void clear() {

            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public boolean hasCustomName() {
                return false;
            }

            @Override
            public ITextComponent getDisplayName() {
                return null;
            }
        }
    }

    public static class GuiShulkerPreview extends GuiContainer {

        private final ItemStack parentShulkerStack;
        public static boolean renderingTooltip;
        public static boolean isMouseInsideGui;
        public final ResourceLocation SHULKER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");

        private int x;
        private int y;
        private boolean colorShulker;

        public GuiShulkerPreview(Container inventorySlotsIn, ItemStack parentShulkerStack, boolean colorShulker) {
            super(inventorySlotsIn);

            this.parentShulkerStack = parentShulkerStack;
            this.colorShulker = colorShulker;
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

        public Color getShulkerColorRGB(ItemStack shulkerStack) {

            String shulkerColor = Objects.requireNonNull(shulkerStack.getItem().getRegistryName())
                    .toString()
                    .replace("minecraft:", "")
                    .replace("_shulker_box", "");

            switch (shulkerColor) {
                case "red":
                    return new Color(156, 37, 34);
                case "orange":
                    return new Color(245, 116, 16);
                case "yellow":
                    return new Color(252, 199, 36);
                case "lime":
                    return new Color(113, 188, 24);
                case "green":
                    return new Color(84, 109, 28);
                case "cyan":
                    return new Color(22, 135, 146);
                case "light_blue":
                    return new Color(60, 182, 220);
                case "blue":
                    return new Color(51, 53, 155);
                case "purple":
                    return new Color(151, 105, 151);
                case "pink":
                    return new Color(243, 139, 170);
                case "magenta":
                    return new Color(185, 62, 174);
                case "brown":
                    return new Color(114, 71, 40);
                case "black":
                    return new Color(31, 31, 35);
                case "gray":
                    return new Color(61, 66, 69);
                case "silver":
                    return new Color(140, 140, 131);
                default:
                    return new Color(255, 255, 255);
            }
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {

            if (parentShulkerStack == null || !(parentShulkerStack.getItem() instanceof ItemShulkerBox)) return;

            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();

            mc.getRenderItem().zLevel = 500.1f;

            float red = getShulkerColorRGB(parentShulkerStack).getRed() / 255f;
            float green = getShulkerColorRGB(parentShulkerStack).getGreen() / 255f;
            float blue = getShulkerColorRGB(parentShulkerStack).getBlue() / 255f;

            GlStateManager.color(colorShulker ? red : 1f, colorShulker ? green : 1f, colorShulker ? blue : 1f, 1f);

            GlStateManager.enableTexture2D();

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );

            mc.getTextureManager().bindTexture(SHULKER_GUI_TEXTURE);

            int fx = x + 8;
            int fy = y - 97;

            RenderUtils.drawTexturedRect(fx, fy + 6, 0, 0, 176, 10, 500); //top
            RenderUtils.drawTexturedRect(fx, fy + 16, 0, 16, 176, 58, 500); //middle
            RenderUtils.drawTexturedRect(fx, fy + 16 + 58, 0, 160, 176, 8, 500); //bottom

            GlStateManager.enableDepth();
            GlStateManager.enableLighting();
            GlStateManager.enableRescaleNormal();
            RenderHelper.enableGUIStandardItemLighting();

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

            if (hoveringSlot != null && hoveringSlot.getStack() != ItemStack.EMPTY) {

                GlStateManager.disableLighting();
                GlStateManager.disableDepth();

                this.drawGradientRect(
                        ix + hoveringSlot.xPos,
                        iy + hoveringSlot.yPos,
                        ix + hoveringSlot.xPos + 16,
                        iy + hoveringSlot.yPos + 16,
                        -2130706433,
                        -2130706433
                );

                renderingTooltip = true;
                this.renderToolTip(hoveringSlot.getStack(), mouseX, mouseY);
                renderingTooltip = false;

                GlStateManager.enableDepth();
            }

            GlStateManager.disableBlend();
            GlStateManager.disableLighting();

            isMouseInsideGui = this.isPointInRegion(x + 11,
                    y - 88,
                    x + 181,
                    y - 240,
                    mouseX,
                    mouseY
            ); // ???
        }

        @Override
        protected void drawGuiContainerBackgroundLayer(float v, int i, int j) {

        }
    }
}