package nl.chromaticvision.sunshine.impl.gui.shulkerpreview;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

public class ShulkerContainer extends Container {

    public static int[] getSlotPos(int s) {

        int[] res = new int[2];

        res[0] = s % 9 * 18;
        res[1] = ((s / 9 + 1) * 18) + 1;

        return res;
    }

    public ShulkerContainer(ShulkerInventory shulkerInventory) {
        for (int i = 0; i < shulkerInventory.contents.size(); i++) {
            addSlotToContainer(new Slot(shulkerInventory, i, getSlotPos(i)[0], getSlotPos(i)[1]));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return false;
    }

    public static class ShulkerInventory implements IInventory {

        private final List<ItemStack> contents;

        public ShulkerInventory(List<ItemStack> contents) {
            this.contents = contents;
        }

        @Override
        public int getSizeInventory() {
            return contents.size();
        }

        @Override
        public boolean isEmpty() {
            return contents.isEmpty();
        }

        @Override
        public ItemStack getStackInSlot(int i) {
            return contents.get(i);
        }

        @Override
        public ItemStack decrStackSize(int i, int i1) {
            return null;
        }

        @Override
        public ItemStack removeStackFromSlot(int i) {
            return null;
        }

        @Override
        public void setInventorySlotContents(int i, ItemStack itemStack) {

        }

        @Override
        public int getInventoryStackLimit() {
            return contents.size();
        }

        @Override
        public void markDirty() {

        }

        @Override
        public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
            return false;
        }

        @Override
        public void openInventory(EntityPlayer entityPlayer) {

        }

        @Override
        public void closeInventory(EntityPlayer entityPlayer) {

        }

        @Override
        public boolean isItemValidForSlot(int i, ItemStack itemStack) {
            return false;
        }

        @Override
        public int getField(int i) {
            return 0;
        }

        @Override
        public void setField(int i, int i1) {

        }

        @Override
        public int getFieldCount() {
            return 0;
        }

        @Override
        public void clear() {

        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public boolean hasCustomName() {
            return false;
        }

        @Override
        public ITextComponent getDisplayName() {
            return null;
        }
    }
}
