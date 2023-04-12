package nl.chromaticvision.sunshine.impl.module.modules.misc;

import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import org.lwjgl.input.Keyboard;

public class ExampleModule extends Module {

    public ExampleModule() {
        super("ExampleModule", "Simple scaffold module example", Category.MISC);
        setKeybind(Keyboard.KEY_Z);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onTick() {

        if (mc.player == null || mc.world == null) return;

        int block = -1;

        for (int i = 0; i < mc.player.inventory.mainInventory.size(); i++) {
            if (mc.player.inventory.mainInventory.get(i).getItem() instanceof ItemBlock) {
                block = i;
                break;
            }
        }

        if (block == -1) return;

        BlockPos blockPos = new BlockPos(new Vec3d(mc.player.posX, mc.player.posY - 1, mc.player.posZ));

        if (mc.world.getBlockState(blockPos).getBlock() instanceof BlockAir || mc.world.getBlockState(blockPos).getMaterial().isLiquid()) {

            boolean validside = false;

            for (EnumFacing neighbour : EnumFacing.values()) {
                if (mc.world.getBlockState(blockPos.offset(neighbour)).getMaterial().isSolid()) {
                    validside = true;
                    break;
                }
            }

            if (!validside) return;

            // mc.player.inventory.currentItem = block;
            mc.player.connection.sendPacket(new CPacketHeldItemChange(block));
            mc.playerController.updateController();

            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));

            mc.playerController.processRightClickBlock(mc.player,
                    mc.world,
                    blockPos,
                    EnumFacing.UP,
                    new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()),
                    EnumHand.MAIN_HAND
            );

            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
        }
    }
}
