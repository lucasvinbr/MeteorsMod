package net.meteor.client.gui.slots;

import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFreezeFuel extends Slot {

    public SlotFreezeFuel(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(ItemStack stack)
    {
        return TileEntityFreezingMachine.getItemFreezeTime(stack) > 0;
    }

}
