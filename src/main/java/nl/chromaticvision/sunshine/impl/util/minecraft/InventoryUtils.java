package nl.chromaticvision.sunshine.impl.util.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.NonNullList;

import java.util.List;

public class InventoryUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static List<ItemStack> getShulkerContents(ItemStack itemStack) {

        NonNullList<ItemStack> shulkerList = NonNullList.withSize(27, ItemStack.EMPTY);
        NBTTagCompound compound = itemStack.getTagCompound();

        if (compound != null && compound.hasKey("BlockEntityTag", 10)) {

            NBTTagCompound tags = compound.getCompoundTag("BlockEntityTag");

            if (tags.hasKey("Items", 9)) {
                ItemStackHelper.loadAllItems(tags, shulkerList);
            }
        }

        return shulkerList;
    }

    public static boolean isOverEnchantedItem(ItemStack itemStack) {

        if (itemStack.getTagCompound() == null) {
            return false;
        }

        return EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, itemStack) > Enchantments.SHARPNESS.getMaxLevel();
    }

    public static void update(int index, boolean silent) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(index));
        if (!silent) mc.player.inventory.currentItem = index;
        mc.playerController.updateController();
    }
}
