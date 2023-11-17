package nl.chromaticvision.sunshine.impl.module.modules.shulkerpvp;

import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;
import nl.chromaticvision.sunshine.impl.util.minecraft.BlockUtils;
import nl.chromaticvision.sunshine.impl.util.minecraft.InventoryUtils;

public class Grab32k extends Module {

    private boolean clickedHopper = false;

    public Grab32k() {
        super("32kGrab",
                "Grabs 32k's from hoppers nearby",
                Category.SHULKERPVP,
                new ItemStack(Blocks.HOPPER));
    }

    enum CloseMode {
        OFF,
        NORMAL,
        SECRET
    }

    public final Setting<CloseMode> autoClose = register(new Setting<>("AutoClose", CloseMode.SECRET));
    public final Setting<Boolean> select32k = register(new Setting<>("Select32kSlot", false));
    public final Setting<Integer> maxHopperRange = register(new Setting<>("MaxHopperRange", 4, 1, 7));

    public boolean droppedAll = false;
    public BlockPos hopperPos = null;

    @Override
    public void onEnable() {
        super.onEnable();

        droppedAll = false;
        clickedHopper = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onFastTick() {

        if (!safeToUpdate()) return;

        update();
    }

    public BlockPos getRandomHopper() {

        for (BlockPos blockPos : BlockPos.getAllInBox(
                new BlockPos(new Vec3d(
                        -maxHopperRange.getValue() + mc.player.posX,
                        -maxHopperRange.getValue() + mc.player.posY,
                        -maxHopperRange.getValue() + mc.player.posZ)),
                new BlockPos(new Vec3d(
                        maxHopperRange.getValue() + mc.player.posX,
                        maxHopperRange.getValue() + mc.player.posY,
                        maxHopperRange.getValue() + mc.player.posZ)))) {

            if (mc.world.getBlockState(blockPos).getBlock() instanceof BlockHopper && mc.world.getBlockState(blockPos.up()).getBlock() instanceof BlockShulkerBox) {
                return blockPos;
            }

        }

        return null;
    }

    public void update() {

        if (!clickedHopper) {

            hopperPos = getRandomHopper();

            if (hopperPos == null) {
                return;
            }

            System.out.println(hopperPos);

            BlockUtils.rightClickBlockDirectly(hopperPos);

            clickedHopper = true;
        } else {

            if (!(mc.player.openContainer instanceof ContainerHopper) || mc.player.openContainer.inventorySlots == null)
                return;

            if (!droppedAll) {
                for (int i = 0; i < 5; i++) {
                    mc.playerController.windowClick(mc.player.openContainer.windowId,
                            i,
                            i,
                            ClickType.THROW,
                            mc.player
                    );
                }

                droppedAll = true;
            } else {

                for (int i = 0; i < 5; i++) {
                    ItemStack slotStack = mc.player.openContainer.inventorySlots.get(0).inventory.getStackInSlot(i);

                    if (InventoryUtils.isOverEnchantedItem(slotStack)) {

                        int slot = InventoryUtils.findHotbarReverted32k() != -1
                                ? InventoryUtils.findHotbarReverted32k()

                                :

                                InventoryUtils.findHotbarItem(Item.getItemFromBlock(Blocks.AIR)) != -1
                                        ? InventoryUtils.findHotbarItem(Item.getItemFromBlock(Blocks.AIR))

                                        :

                                        mc.player.inventory.currentItem;

                        mc.playerController.windowClick(mc.player.openContainer.windowId,
                                i,
                                slot,
                                ClickType.SWAP,
                                mc.player
                        );

                        InventoryUtils.update(select32k.getValue() ? slot : mc.player.inventory.currentItem, false);

                        switch (autoClose.getValue()) {
                            case OFF:
                                break;
                            case NORMAL:
                                mc.player.closeScreen();
                                break;
                            case SECRET:
                                mc.displayGuiScreen(null);
                                break;
                        }

                        disable();
                        return;
                    }
                }
            }
        }
    }
}
