package nl.chromaticvision.sunshine.impl.util.minecraft;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;

import java.util.Random;

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

    public static void rightClickBlockDirectly(BlockPos blockPos) {
        mc.playerController.processRightClickBlock(
                mc.player,
                mc.world,
                blockPos,
                EnumFacing.UP,
                new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()),
                EnumHand.MAIN_HAND);
    }

    public static void placeBlockDirectly(BlockPos blockPos) {

        EnumFacing side = BlockUtils.getPlaceableSide(blockPos);

        if (side == null) {
            System.out.println("excuse me??");
            return;
        }

        BlockPos neighbour = blockPos.offset(side);
        EnumFacing opposite = side.getOpposite();

        Vec3d hitVec = new Vec3d(neighbour).add((new Vec3d(0.5, 0.5, 0.5)).add(new Vec3d(opposite.getDirectionVec()).scale(0.5)));

        float f = (float)(hitVec.x - (double) blockPos.getX());
        float f1 = (float)(hitVec.y - (double) blockPos.getY());
        float f2 = (float)(hitVec.z - (double) blockPos.getZ());

        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(
                neighbour,
                opposite,
                EnumHand.MAIN_HAND,
                f,
                f1,
                f2
        ));

//        float f = (float)(hitVec.x - (double)blockPos.getX());
//        float f1 = (float)(hitVec.y - (double)blockPos.getY());
//        float f2 = (float)(hitVec.z - (double)blockPos.getZ());
//
//        mc.playerController.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(
//                blockPos,
//                side,
//                EnumHand.MAIN_HAND,
//                f,
//                f1,
//                f2
//        ));
//
//

        // mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);

//        mc.playerController.syncCurrentPlayItem();
//        ItemStack itemstack = mc.player.getHeldItem(EnumHand.MAIN_HAND);
//        float f = (float)(hitVec.x - (double)blockPos.getX());
//        float f1 = (float)(hitVec.y - (double)blockPos.getY());
//        float f2 = (float)(hitVec.z - (double)blockPos.getZ());
//        boolean flag = false;
//
//
//        mc.playerController.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(
//                blockPos,
//                opposite,
//                EnumHand.MAIN_HAND,
//                f,
//                f1,
//                f2
//        ));
//        if (!mc.world.getWorldBorder().contains(blockPos))
//        {
//            System.out.println("Fail");
//        }
//        else
//        {
//            if (mc.playerController.currentGameType != GameType.SPECTATOR)
//            {
//                IBlockState iblockstate = mc.world.getBlockState(blockPos);
//                boolean bypass = mc.player.getHeldItemMainhand().doesSneakBypassUse(mc.world, blockPos, mc.player) && mc.player.getHeldItemOffhand().doesSneakBypassUse(mc.world, blockPos, mc.player);
//                if ((!mc.player.isSneaking() || bypass) && iblockstate.getBlock().onBlockActivated(mc.world, blockPos, iblockstate, mc.player, EnumHand.MAIN_HAND, opposite, f, f1, f2))
//                {
//                    flag = true;
//                }
//
//                if (!flag && itemstack.getItem() instanceof ItemBlock)
//                {
//                    ItemBlock itemblock = (ItemBlock)itemstack.getItem();
//
//                    if (!itemblock.canPlaceBlockOnSide(mc.world, blockPos, opposite, mc.player, itemstack))
//                    {
//                        System.out.println("Fail");
//                    }
//                }
//            }
//
//            mc.playerController.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos, opposite, EnumHand.MAIN_HAND, f, f1, f2));
//
//            if (!flag && mc.playerController.currentGameType != GameType.SPECTATOR)
//            {
//                if (itemstack.isEmpty())
//                {
//                    System.out.println("Pazz");
//                }
//                else if (mc.player.getCooldownTracker().hasCooldown(itemstack.getItem()))
//                {
//                    System.out.println("pazz");
//                }
//                else
//                {
//                    if (itemstack.getItem() instanceof ItemBlock && !mc.player.canUseCommandBlock())
//                    {
//                        Block block = ((ItemBlock)itemstack.getItem()).getBlock();
//
//                        if (block instanceof BlockCommandBlock || block instanceof BlockStructure)
//                        {
//                            System.out.println("Fail");
//                        }
//                    }
//
//                    if (mc.playerController.currentGameType.isCreative())
//                    {
//                        int i = itemstack.getMetadata();
//                        int j = itemstack.getCount();
//                        EnumActionResult enumactionresult = itemstack.onItemUse(mc.player, mc.world, blockPos, EnumHand.MAIN_HAND, opposite, f, f1, f2);
//                        itemstack.setItemDamage(i);
//                        itemstack.setCount(j);
//                    }
//                    else
//                    {
//                        EnumActionResult res = itemstack.onItemUse(mc.player, mc.world, blockPos, EnumHand.MAIN_HAND, opposite, f, f1, f2);
//                    }
//                }
//            }
//            else
//            {
//                System.out.println("successs");
//            }
//        }
//    }
    }
}
