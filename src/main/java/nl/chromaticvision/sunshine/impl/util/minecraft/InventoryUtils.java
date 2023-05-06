package nl.chromaticvision.sunshine.impl.util.minecraft;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import java.util.List;

public class InventoryUtils {

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
}
