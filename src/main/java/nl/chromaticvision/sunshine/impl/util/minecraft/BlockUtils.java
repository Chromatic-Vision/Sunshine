package nl.chromaticvision.sunshine.impl.util.minecraft;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class BlockUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isEmptyBlock(BlockPos blockPos, boolean checkEntity) {

        if (checkEntity) {

            AxisAlignedBB box = new AxisAlignedBB(blockPos);

            for (Entity entity : mc.world.getLoadedEntityList()) {
                if (entity instanceof EntityLivingBase && box.intersects(entity.getEntityBoundingBox())) {
                    return false;
                }
            }
        }

        return mc.world.getBlockState(blockPos).getBlock() instanceof BlockAir
                || mc.world.getBlockState(blockPos).getBlock() instanceof BlockLiquid;
    }

    public static boolean validToPlace(BlockPos blockPos) {

        if (!isEmptyBlock(blockPos, true)) return false;

        for (EnumFacing facing : EnumFacing.values()) {

            if (mc.world.getBlockState(blockPos.offset(facing)).getMaterial().isSolid()) {
                return true;
            }
        }

        return false;
    }

    public static boolean collideWith(BlockPos blockPos, Block block) {
        for (EnumFacing facing : EnumFacing.values()) {
            if (mc.world.getBlockState(blockPos.offset(facing)).getBlock() == block) {
                return true;
            }
        }

        return false;
    }

    public static EnumFacing getPlaceableSide(BlockPos blockPos) {
        for (EnumFacing facing : EnumFacing.values()) {
            if (!isEmptyBlock(blockPos.offset(facing), false)) {
                return facing;
            }
        }
        return null;
    }

    public static void placeBlock(BlockPos blockPos) {

        if (mc.player == null || mc.world == null) return;

        EnumFacing side = BlockUtils.getPlaceableSide(blockPos);

        if (side == null) {
            return;
        }

        final BlockPos neighbor = blockPos.offset(side);
        final Vec3d hitVec = new Vec3d((neighbor).add(0.5, 0.5, 0.5)).add(new Vec3d(side.getOpposite().getDirectionVec()).scale(0.5));

        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
        mc.playerController.processRightClickBlock(mc.player, mc.world, blockPos.offset(side), side.getOpposite(), hitVec, EnumHand.MAIN_HAND);
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
        mc.player.swingArm(EnumHand.MAIN_HAND);
//        mc.rightClickDelayTimer = 4;
    }

    public static void clickBlock(BlockPos blockPos, EnumHand hand) {
        mc.playerController.processRightClickBlock(mc.player,
                mc.world,
                blockPos,
                EnumFacing.getDirectionFromEntityLiving(blockPos, mc.player),
                new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()),
                hand
        );
        mc.player.swingArm(hand);
    }
}
