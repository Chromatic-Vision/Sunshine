package nl.chromaticvision.sunshine.impl.module.modules.shulkerpvp;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;
import nl.chromaticvision.sunshine.impl.util.minecraft.BlockUtils;
import nl.chromaticvision.sunshine.impl.util.minecraft.InventoryUtils;
import nl.chromaticvision.sunshine.impl.util.minecraft.MessageUtils;

/*

to Violentqm

The code works fine, except the hopper won't place sometimes and rotation is a little bit glitched. I will fix it soon.

 */

public class Auto32k extends Module {

    public Auto32k() {
        super("Auto32k", "Automatically creates 32k setup using dispensers", Category.SHULKERPVP);
    }

    enum TickMode {
        NORMAL,
        FAST
    }

    public final Setting<Boolean> silent = register(new Setting<>("Silent", false));
    public final Setting<TickMode> tickMode = register(new Setting<>("TickMode", TickMode.NORMAL));
    public final Setting<Integer> placeRange = register(new Setting<>("PlaceRange", 3, 2, 6));

    @Override
    public void onEnable() {
        super.onEnable();

        initialized = false;
        phase = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onTick() {
        if (tickMode.getValue() == TickMode.NORMAL) {
            if (!initialized) {
                start();
            } else {
                update();
            }
        }
    }

    @Override
    public void onFastTick() {
        if (tickMode.getValue() == TickMode.FAST) {
            if (!initialized) {
                start();
            } else {
                update();
            }
        }
    }

    private boolean initialized = false;
    public int dispenser = -1;
    public int redstone = -1;
    public int shulker = -1;
    public int hopper = -1;
    public EnumFacing dispenserRotation = null;

    public BlockPos dispenserPos = null;
    public int phase = 0;


    public boolean checkItems() { // returns true if items are found

        if (mc.player == null || mc.world == null) return false;

        dispenser = InventoryUtils.findHotbarItem(Item.getItemFromBlock(Blocks.DISPENSER));
        redstone = InventoryUtils.findHotbarItem(Item.getItemFromBlock(Blocks.REDSTONE_BLOCK));
        shulker = InventoryUtils.findShulkerWith32ksInside();
        hopper = InventoryUtils.findHotbarItem(Item.getItemFromBlock(Blocks.HOPPER));

        return dispenser != -1 && redstone != -1 && shulker != -1 && hopper != -1;
    }

    public BlockPos getAvaiableDispenserPos() {
        for (int x = -placeRange.getValue(); x <= placeRange.getValue(); x++) {
            for (int y = -placeRange.getValue(); y <= placeRange.getValue(); y++) {
                for (int z = -placeRange.getValue(); z <= placeRange.getValue(); z++) {

                    BlockPos basePos = new BlockPos(new Vec3d(x + mc.player.posX, y + mc.player.posY, z + mc.player.posZ));

                    if (checkViableDispenserPos(basePos)) {
                        return basePos;
                    }
                }
            }
        }

        return null;
    }

    public boolean checkViableDispenserPos(BlockPos blockPos) {

        for (EnumFacing facing : EnumFacing.HORIZONTALS) {

            BlockPos dispenserOffsetBlock = blockPos.offset(facing);

            if (BlockUtils.isEmptyBlock(dispenserOffsetBlock)
            && BlockUtils.isEmptyBlock(dispenserOffsetBlock.down())
            && BlockUtils.validToPlace(dispenserOffsetBlock.down())
            && !BlockUtils.collideWith(dispenserOffsetBlock.down(), Blocks.REDSTONE_BLOCK)) {

                if (BlockUtils.isEmptyBlock(blockPos)
                        && BlockUtils.validToPlace(blockPos)
                        && BlockUtils.isEmptyBlock(blockPos.up())) {
                    dispenserRotation = facing.getOpposite();
                    return true;
                }
            }
        }

        return false;
    }

    public void start() {

        if (!safeToUpdate()) return;

        if (!checkItems()) {
            MessageUtils.sendClientChatMessage("Missing items.");
            disable();
            return;
        }

        dispenserRotation = null;

        // yeah, ik it's best code
        dispenserPos = getAvaiableDispenserPos();

        if (dispenserPos == null) {
            MessageUtils.sendClientChatMessage("No viable placement found, disabling...");
            disable();
            return;
        }

        initialized = true;
    }

    public void update() {

        if (phase == 0) {

            float yaw;

            if (dispenserRotation == null) {
                System.out.println("no");
            }

            switch (dispenserRotation) {
                case NORTH:
                    yaw = -180.0f;
                    break;
                case EAST:
                    yaw = -90.0f;
                    break;
                case SOUTH:
                    yaw = 0.0f;
                    break;
                case WEST:
                    yaw = 90.0f;
                    break;
                default:
                    yaw = mc.player.rotationYaw;
            }


            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, mc.player.rotationPitch, mc.player.onGround));

            InventoryUtils.update(dispenser, silent.getValue());
            BlockUtils.placeBlock(dispenserPos);

            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, mc.player.rotationPitch, mc.player.onGround));

            BlockUtils.clickBlock(dispenserPos, EnumHand.MAIN_HAND);

            phase = 1;
        }

        if (phase == 1) {

            if (!(mc.player.openContainer instanceof ContainerDispenser) || mc.player.openContainer.inventorySlots == null) return;

            mc.playerController.windowClick(mc.player.openContainer.windowId, shulker, 0, ClickType.QUICK_MOVE, mc.player);

            InventoryUtils.update(redstone, silent.getValue());
            BlockUtils.placeBlock(dispenserPos.up());

            mc.player.closeScreen();

            phase = 2;
        }

        if (phase == 2) {

            InventoryUtils.update(hopper, silent.getValue());
            BlockUtils.placeBlock(dispenserPos.down().offset(dispenserRotation.getOpposite()));
            BlockUtils.clickBlock(dispenserPos.down().offset(dispenserRotation.getOpposite()), EnumHand.MAIN_HAND);

            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
            mc.playerController.updateController();

            phase = 3;
        }

        if (phase == 3) {

            if (!(mc.player.openContainer instanceof ContainerHopper) || mc.player.openContainer.inventorySlots == null) return;

            for (int i = 0; i < 5; i++) {

                ItemStack slotStack = mc.player.openContainer.inventorySlots.get(0).inventory.getStackInSlot(i);

                if (InventoryUtils.isOverEnchantedItem(slotStack)) {
                    mc.playerController.windowClick(mc.player.openContainer.windowId,
                            i,
                            mc.player.inventory.currentItem,
                            ClickType.SWAP,
                            mc.player
                    );
                    mc.displayGuiScreen(null);
                    phase = 4;
                    break;
                }
            }
        }
    }
}
