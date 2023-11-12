package nl.chromaticvision.sunshine.impl.module.modules.shulkerpvp;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;
import nl.chromaticvision.sunshine.impl.util.minecraft.BlockUtils;
import nl.chromaticvision.sunshine.impl.util.minecraft.InventoryUtils;
import nl.chromaticvision.sunshine.impl.util.minecraft.MessageUtils;

public class Grab32k extends Module {

    private boolean clickedHopper = false;

    public Grab32k() {
        super("32kGrab", "Grabs 32k's from hoppers nearby", Category.SHULKERPVP);
    }

    public final Setting<Boolean> autoClose = register(new Setting<>("Auto Close", true));
    public final Setting<Integer> maxHopperRange = register(new Setting<>("Max Hopper Range", 4, 1, 7));
    public final Setting<Boolean> swap = register(new Setting<>("Swap 32k to empty slot", true));

    public boolean started = false;
    public int phase = 0;
    public BlockPos hopperPos = null;
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        started = false;
        phase = 0;
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
        for (int x = -maxHopperRange.getValue(); x <= maxHopperRange.getValue(); x++) {
            for (int y = -maxHopperRange.getValue(); y <= maxHopperRange.getValue(); y++) {
                for (int z = -maxHopperRange.getValue(); z <= maxHopperRange.getValue(); z++) {
                    BlockPos basePos = new BlockPos(new Vec3d(x + mc.player.posX, y + mc.player.posY, z + mc.player.posZ));

                    if (mc.world.getBlockState(basePos).getBlock() instanceof BlockHopper && mc.world.getBlockState(basePos.up()).getBlock() instanceof BlockShulkerBox) {
                        return basePos;
                    }
                }
            }
        }

        return null;
    }

    public void update() {

        if (phase == 0) {

            hopperPos = getRandomHopper();

            if (hopperPos == null) {
                // MessageUtils.sendClientChatMessage("No hoppers found!");
                // disable();
                return;
            }
            //mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtils.rightClickBlockDirectly(hopperPos);
            //mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));

            phase = 1;
        }

        if (phase == 1) {

            if (!(mc.player.openContainer instanceof ContainerHopper) || mc.player.openContainer.inventorySlots == null) return;

            for (int i = 0; i < 5; i++) {
                ItemStack slotStack = mc.player.openContainer.inventorySlots.get(0).inventory.getStackInSlot(i);

                if (!InventoryUtils.isOverEnchantedItem(slotStack)) {

                    mc.playerController.windowClick(mc.player.openContainer.windowId,
                            i,
                            i,
                            ClickType.THROW,
                            mc.player
                    );
                } else {

                    MessageUtils.sendClientChatMessage("Just opened hopper but somehow there was a 32k inside??");

                    mc.playerController.windowClick(mc.player.openContainer.windowId,
                            i,
                            mc.player.inventory.currentItem,
                            ClickType.SWAP,
                            mc.player
                    );
                    disable();
                    return;
                }
            }

            phase = 2;

        }

        if (phase == 2) {

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
                    phase = 3;

                    if (autoClose.getValue()) mc.displayGuiScreen(null);

                    break;
                }
            }
        }
    }
}
