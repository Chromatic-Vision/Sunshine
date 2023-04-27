package nl.chromaticvision.sunshine.impl.module.modules.misc;

import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.chromaticvision.sunshine.impl.gui.clickgui.components.button.buttons.NumberButton;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;
import nl.chromaticvision.sunshine.impl.util.system.FileUtils;
import org.lwjgl.input.Keyboard;

public class ExampleModule extends Module {

    public ExampleModule() {
        super("ExampleModule", "Simple scaffold module example", Category.MISC);
        setKeybind(Keyboard.KEY_Z);
    }

    public final Setting<Boolean> silent = register(new Setting<>("Silent", false));
    public final Setting<Integer> testIntSetting = register(new Setting<>("TestInt", 4, 1, 26));
    public final Setting<Integer> testIntRSetting = register(new Setting<>("TestIntR", 23, 0, 30));
    public final Setting<Double> testDoubleSetting = register(new Setting<>("TestDouble", 4.0, 2.2, 14.5));
    public final Setting<Long> testLongSetting = register(new Setting<>("TestLong", 1250L, 0L, 5000L));
    public final Setting<Float> testFloatSetting = register(new Setting<>("TestFloat", 4.2f, 0.0f, 14.6f));
    public final Setting<Short> testShortSetting = register(new Setting<>("TestShort", (short) 6, (short) 0, Short.MAX_VALUE));
    public final Setting<Byte> testByteSetting = register(new Setting<>("TestByte", (byte) 1, (byte) 0, Byte.MAX_VALUE));

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

            if (!silent.getValue()) mc.player.inventory.currentItem = block;
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
