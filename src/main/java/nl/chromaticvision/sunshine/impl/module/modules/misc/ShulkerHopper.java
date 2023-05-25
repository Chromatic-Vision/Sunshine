package nl.chromaticvision.sunshine.impl.module.modules.misc;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;
import nl.chromaticvision.sunshine.impl.util.minecraft.BlockUtils;
import nl.chromaticvision.sunshine.impl.util.minecraft.InventoryUtils;

public class ShulkerHopper extends Module {

    public ShulkerHopper() {
        super("ShulkerHopper",
                "Places hopper and shulker instantly, so you can take the items out of the shulker box without opening it",
                Category.MISC
        );
    }

    private enum DetectMode {
        OFF,
        BED,
        SUPERWEAPON
    }

    private enum CloseMode {
        OFF,
        SECRETCLOSE,
        LEGIT
    }


    public final Setting<DetectMode> detectShulkerContent = register(new Setting<>("DetectShulkerContent", DetectMode.BED));
    public final Setting<Boolean> blockShulker = register(new Setting<>("BlockShulker", false));
    public final Setting<Boolean> placeSilent = register(new Setting<>("PlaceSilent", false));
    public final Setting<Integer> itemLimitCount = register(new Setting<>("ItemLimitCount", 3, 0, 10));
    public final Setting<CloseMode> closeMode = register(new Setting<>("CloseMode", CloseMode.LEGIT));


    private BlockPos hopperBasePos = null;
    private int phase = 0;
    private int shulker = -1;
    private int hopper = -1;
    private int block = -1;
    private int swappedCount = 0;


    @Override
    public void onEnable() {
        super.onEnable();

        if (mc.world == null || mc.player == null) return;

        phase = 0;
        swappedCount = 0;

        shulker = -1;
        hopper = -1;
        block = -1;

        //item checks
        for (int i = 8; i >= 0; i--) {

            ItemStack slotStack = mc.player.inventory.getStackInSlot(i);

            if (slotStack.getItem() == Item.getItemFromBlock(Blocks.HOPPER)) {
                hopper = i;
            }

            if (slotStack.getItem() instanceof ItemBlock
                    && !(slotStack.getItem() instanceof ItemShulkerBox)
                    && !(slotStack.getItem() instanceof ItemRedstone)) {
                block = i;
            }

            if (slotStack.getItem() instanceof ItemShulkerBox) {

                boolean valid = false;

                //check if required items are inside the shulker box
                if (detectShulkerContent.getValue() == DetectMode.BED) {

                    for (ItemStack content : InventoryUtils.getShulkerContents(slotStack)) {
                        if (content.getItem() instanceof ItemBed) {
                            valid = true;
                        }
                    }

                } else if (detectShulkerContent.getValue() == DetectMode.SUPERWEAPON) {

                    for (ItemStack content : InventoryUtils.getShulkerContents(slotStack)) {
                        if (InventoryUtils.isOverEnchantedItem(content)) {
                            valid = true;
                        }
                    }

                } else valid = true; //mode off

                shulker = valid ? i : -1;
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onTick() {

        if (phase == 0) {

            if (hopper == -1 || shulker == -1) {
                mc.player.sendMessage(new TextComponentString("Not enough materials."));
                disable();
                return;
            }

            for (int x = -3; x <= 3; ++x) {
                for (int y = -3; y <= 3; ++y) {
                    for (int z = -3; z <= 3; ++z) {

                        BlockPos blockPos = new BlockPos(new Vec3d(x + mc.player.posX,
                                y + mc.player.posY,
                                z + mc.player.posZ)
                        );

                        if (BlockUtils.isEmptyBlock(blockPos)
                                && BlockUtils.isEmptyBlock(blockPos.up())
                                && BlockUtils.validToPlace(blockPos)
                                && !BlockUtils.collideWith(blockPos, Blocks.REDSTONE_BLOCK)) {
                            hopperBasePos = blockPos;
                            phase = 1;
                            break;
                        }
                    }
                }
            }
        }

        if (hopperBasePos != null) {

            if (phase == 1) {
                InventoryUtils.update(hopper, placeSilent.getValue());
                BlockUtils.placeBlock(hopperBasePos);
                phase++;
                return;
            }

            if (phase == 2) {
                InventoryUtils.update(shulker, placeSilent.getValue());
                BlockUtils.placeBlock(hopperBasePos.up());
                phase++;
                return;
            }

            if (phase == 3) {
                if (blockShulker.getValue() && block != -1) {
                    if (mc.world.getBlockState(hopperBasePos.up()).getBlock() instanceof BlockShulkerBox) {
                        InventoryUtils.update(block, placeSilent.getValue());
                        BlockUtils.placeBlock(hopperBasePos
                                .up()
                                .offset(mc.world.getBlockState(hopperBasePos.up()).getValue(BlockDirectional.FACING)));
                    } else return;
                }

                phase++;
                return;
            }

            if (phase == 4) {
                BlockUtils.clickBlock(hopperBasePos, EnumHand.MAIN_HAND);
                phase++;
            }

            if (phase == 5) {

                if (!(mc.player.openContainer instanceof ContainerHopper)
                || mc.player.openContainer.inventorySlots == null) return;

                phase++;
            }

            if (phase == 6) {

                if (mc.currentScreen == null || detectShulkerContent.getValue() == DetectMode.OFF) {
                    disable();
                } else {

                    if (!(itemLimitCount.getValue() <= 0) && swappedCount >= itemLimitCount.getValue()) {
                        closeScreenBySetting();
                        disable();
                        return;
                    }

                    for (int i = 0; i < 5; i++) {

                        ItemStack slotStack = mc.player.openContainer.inventorySlots.get(0).inventory.getStackInSlot(i);

                        if (!slotStack.isEmpty()) {

                            if ((detectShulkerContent.getValue() == DetectMode.BED
                                    && slotStack.getItem() instanceof ItemBed)
                                    || detectShulkerContent.getValue() == DetectMode.SUPERWEAPON
                                    && InventoryUtils.isOverEnchantedItem(slotStack))
                            {
                                mc.playerController.windowClick(mc.player.openContainer.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
                                swappedCount++;
                            }
                        }
                    }

                }
            }

        }
    }

    private void closeScreenBySetting() {
        switch (closeMode.getValue()) {
            case LEGIT:
                mc.player.closeScreen();
            case SECRETCLOSE:
                mc.displayGuiScreen(null);
        }
    }
}