package nl.chromaticvision.sunshine.impl.module.modules.shulkerpvp;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
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

to Snow
idk how well the code works but meh cant be too bad fixed hopper thing and made the rotations accurate also added the options below
Air Place Range (Place range on air so you dont accidentally get out of range while hopping)
Strafe Fix (places the 32k anywhere but where you are walking to the sides/back)
Safe Place (makes sure not to place the 32k anywhere withing 6 blocks of other players)

 */

public class Auto32k extends Module {

    public Auto32k() {
        super("Auto32k", "Automatically creates 32k setup using dispensers", Category.SHULKERPVP);
    }

    enum TickMode {
        NORMAL,
        FAST
    }

    enum PlaceType {
        NORMAL,
        DOWN
    }

    public final Setting<Boolean> silent = register(new Setting<>("Silent", false));
    public final Setting<TickMode> tickMode = register(new Setting<>("TickMode", TickMode.NORMAL));
    public final Setting<Integer> placeRange = register(new Setting<>("PlaceRange", 3, 2, 6));
    public final Setting<Double> airPlaceRange = register(new Setting<>("AirPlaceRange", 2.0, 0.0,5.0));
    public final Setting<Boolean> strafeFix = register(new Setting<>("StrafeFix", false));
    public final Setting<Boolean> safePlace = register(new Setting<>("SafePlace", false));

    @Override
    public void onEnable() {
        super.onEnable();
        execute();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }


    public int dispenser = -1;
    public int redstone = -1;
    public int shulker = -1;
    public int hopper = -1;
    public EnumFacing dispenserRotation = null;
    public boolean waitingForSuperweapon = false;
    public boolean waitingForShulker = false;

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

    public BlockPos getAvailableDispenserPos() {

        for (int x = -placeRange.getValue(); x <= placeRange.getValue(); x++) {
            for (int y = -placeRange.getValue(); y <= placeRange.getValue(); y++) {
                for (int z = -placeRange.getValue(); z <= placeRange.getValue(); z++) {
                    BlockPos basePos = new BlockPos(new Vec3d(x + mc.player.posX, y + mc.player.posY, z + mc.player.posZ));
                    if (basePos.distanceSq(mc.player.getPosition()) <= airPlaceRange.getValue() * airPlaceRange.getValue() && checkViableDispenserPos(basePos)) {
                        if (!isPlayerWithinRange(basePos, 6.0)) {
                            return basePos;
                        }
                    }
                }
            }
        }

        return null;
    }

    public boolean checkViableDispenserPos(BlockPos blockPos) {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {

            if (BlockUtils.isEmptyBlock(blockPos, true)
                    && BlockUtils.validToPlace(blockPos)
                    && EnumFacing.getDirectionFromEntityLiving(blockPos, mc.player) != EnumFacing.UP
                    && EnumFacing.getDirectionFromEntityLiving(blockPos, mc.player) != EnumFacing.DOWN
                    && BlockUtils.isEmptyBlock(blockPos.up(), true)) { // check first if base pos is valid

                BlockPos dispenserOffsetBlock = blockPos.offset(facing);

                if (BlockUtils.isEmptyBlock(dispenserOffsetBlock, true)
                        && BlockUtils.isEmptyBlock(dispenserOffsetBlock.down(), true)
                        && BlockUtils.validToPlace(dispenserOffsetBlock.down())
                        && !BlockUtils.collideWith(dispenserOffsetBlock.down(), Blocks.REDSTONE_BLOCK)
                        && !BlockUtils.collideWith(blockPos, Blocks.HOPPER)) {

                    dispenserRotation = facing.getOpposite();
                    return true;
                }
            }
        }

        return false;
    }

    public void execute() {

        if (!safeToUpdate()) return;

        if (!checkItems()) {
            MessageUtils.sendClientChatMessage("Missing items.");
            disable();
            return;
        }

        waitingForSuperweapon = false;
        waitingForShulker = false;

        dispenserRotation = null;

        dispenserPos = getAvailableDispenserPos();

        if (dispenserPos == null) {
            MessageUtils.sendClientChatMessage("No viable placement found, disabling...");
            disable();
            return;
        }

        // Send rotating packet and start placing dispenser

        float yaw = 0.0f; // default

        if (dispenserRotation != null) {
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
            }
        }

        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, mc.player.rotationPitch, mc.player.onGround));

        // Place dispenser
        InventoryUtils.update(dispenser, silent.getValue());
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
        BlockUtils.placeBlockDirectly(dispenserPos);
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, mc.player.rotationPitch, mc.player.onGround));

        // Place hopper
        InventoryUtils.update(hopper, silent.getValue());
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
        BlockUtils.placeBlockDirectly(dispenserPos.down().offset(dispenserRotation.getOpposite()));
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));

        // Open dispenser and swap shulker
        BlockUtils.rightClickBlockDirectly(dispenserPos);
        waitingForShulker = true;
    }

    public void finish() {
        // Place redstone block
        InventoryUtils.update(redstone, silent.getValue());
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
        BlockUtils.rightClickBlockDirectly(dispenserPos.up());
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));

        // Open hopper
        BlockUtils.rightClickBlockDirectly(dispenserPos.down().offset(dispenserRotation.getOpposite()));
        waitingForSuperweapon = true;
    }

    @Override
    public void onTick() {

        if (waitingForShulker) {

            if (!(mc.player.openContainer instanceof ContainerDispenser) || mc.player.openContainer.inventorySlots == null) return;

            mc.playerController.windowClick(mc.player.openContainer.windowId, shulker, 0, ClickType.QUICK_MOVE, mc.player);
            mc.player.closeScreen();

            finish();

            waitingForShulker = false;
        }

        if (waitingForSuperweapon) {
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
                    waitingForSuperweapon = false;
                    disable();
                    break;
                }
            }
        }
    }

    private boolean isPlayerWithinRange(BlockPos pos, double range) {
        if (!safePlace.getValue()) return false;

        for (EntityPlayer player : mc.world.playerEntities) {
            if (player != null) {
                if (player.getDistanceSq(pos) < range * range) {
                    return true;
                }
            }
        }
        return false;
    }
}