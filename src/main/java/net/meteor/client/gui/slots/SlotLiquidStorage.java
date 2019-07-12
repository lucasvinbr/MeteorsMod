package net.meteor.client.gui.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class SlotLiquidStorage extends Slot {

    public SlotLiquidStorage(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(ItemStack stack)
    {
        return canStoreLiquid(stack);
    }

    public int getItemStackLimit(ItemStack stack)
    {
        return 1;
    }

    public static boolean canStoreLiquid(ItemStack stack)
    {
        return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
    }

}
