package net.meteor.common.tileentity;

import net.meteor.common.FreezerRecipes;
import net.meteor.common.FreezerRecipes.FreezerRecipe;
import net.meteor.common.FreezerRecipes.RecipeType;
import net.meteor.common.MeteorItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// This class is sort of a mirror image of a vanilla furnace,
// but has some new attributes such as fluid handling and other recipes.
public class TileEntityFreezingMachine extends TileEntityNetworkBase implements ISidedInventory, IFluidHandler {

	private static int[] acccessibleSlots = {0, 1, 2, 3, 4};

	private ItemStack[] inv = new ItemStack[getSizeInventory()];
	private ItemStack lastKnownItem = null;
	private FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
	private RecipeType acceptedRecipeType = RecipeType.either;

	public int cookTime;
	public int burnTime;
	public int currentItemBurnTime;

	@Override
	public int getSizeInventory() {
		return 5;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (this.inv[slot] != null)
		{
			ItemStack itemstack;

			if (this.inv[slot].getCount() <= amount)
			{
				itemstack = this.inv[slot];
				this.inv[slot] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.inv[slot].splitStack(amount);

				if (this.inv[slot].getCount() == 0)
				{
					this.inv[slot] = null;
				}

				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack item) {
		
		this.inv[slot] = item;
		
		if (item != null && item.getCount() > this.getInventoryStackLimit())
		{
			item.setCount(this.getInventoryStackLimit());
		}

		if (slot == 3) {
			checkFluidContainer();
		}
		
	}
	
	private void checkFluidContainer() {
		ItemStack item = inv[3];

		if (item != null && item.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			if (!item.isEmpty()) {
				FluidStack fluid = FluidUtil.getFluidContained(item);
				if (fluid != null && (fluid.isFluidEqual(tank.getFluid())) || tank.getFluidAmount() == 0) {
					if (tank.fill(fluid, false) == fluid.amount) {

						// Try to insert it into the bottom slot
						ItemStack emptyContainer = null;
						FluidContainerData[] containerData = FluidContainerRegistry.getRegisteredFluidContainerData();
						for (FluidContainerData containerDatum : containerData) {
							if (containerDatum.filledContainer.isItemEqual(item)) {
								emptyContainer = containerDatum.emptyContainer.copy();
							}
						}

						if (emptyContainer != null) {
							if (inv[4] == null) {
								tank.fill(fluid, true);
								inv[4] = emptyContainer;
								decrStackSize(3, 1);
								this.getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
							} else if (inv[4].isItemEqual(emptyContainer) && inv[4].getCount() + 1 <= inv[4].getMaxStackSize()) {
								tank.fill(fluid, true);
								inv[4].grow(1);
								decrStackSize(3, 1);
								this.getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
							}
						}

					}
				}
			} else {
				FluidStack fluidInTank = tank.getFluid();
				if (fluidInTank != null) {
					ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(fluidInTank, item);
					if (filledContainer != null) {
						if (inv[4] == null) {
							tank.drain(FluidUtil.getFluidContained(filledContainer).amount, true);
							inv[4] = filledContainer;
							decrStackSize(3, 1);
							this.getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
						} else if (inv[4].isItemEqual(filledContainer) && inv[4].getCount() + 1 <= inv[4].getMaxStackSize()) {
							tank.drain(FluidUtil.getFluidContained(filledContainer).amount, true);
							inv[4].grow(1);
							decrStackSize(3, 1);
							this.getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
						}
					}
				}
			}
		}
	}

	@Override
	public String getInventoryName() {
		return "Freezer";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		this.inv = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.inv.length)
			{
				this.inv[b0] = new ItemStack(nbttagcompound1);
			}
		}

		this.burnTime = nbt.getShort("BurnTime");
		this.cookTime = nbt.getShort("CookTime");
		this.currentItemBurnTime = nbt.getShort("ItemFreezeTime");
		//this.currentItemBurnTime = getItemFreezeTime(this.inv[1]);
		this.acceptedRecipeType = RecipeType.values()[nbt.getShort("acceptedRecipeType")];
		tank.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort("BurnTime", (short)this.burnTime);
		nbt.setShort("CookTime", (short)this.cookTime);
		nbt.setShort("ItemFreezeTime", (short)this.currentItemBurnTime);
		nbt.setShort("acceptedRecipeType", (short)this.acceptedRecipeType.getID());
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.inv.length; ++i)
		{
			if (this.inv[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.inv[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbt.setTag("Items", nbttaglist);
		tank.writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound var1 = new NBTTagCompound();
		writeToNBT(var1);
		return new SPacketUpdateTileEntity(this.pos, 1, var1);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.getWorld().getTileEntity(this.getPos()) != this ? false : player.getDistanceSq((double)this.getPos().getX() + 0.5D, (double)this.getPos().getY() + 0.5D, (double)this.getPos().getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		if (slot == 3) return item.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		return slot == 2 ? false : (slot == 1 ? getItemFreezeTime(item) > 0 : true);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return acccessibleSlots;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack item, EnumFacing direction) {

		return item.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) ? slot == 3 : slot == 1;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side) {
		return slot == 2 || slot == 4;
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int i)
	{
		return this.cookTime * i / 200;
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int i)
	{
		if (this.currentItemBurnTime == 0)
		{
			this.currentItemBurnTime = 200;
		}

		return this.burnTime * i / this.currentItemBurnTime;
	}
	
	public RecipeType getRecipeMode() {
		return this.acceptedRecipeType;
	}
	
	public void setRecipeMode(RecipeType type) {
		this.acceptedRecipeType = type;
	}

	public boolean isFreezing() {
		return this.burnTime > 0;
	}

	@Override
	public void updateEntity() {
		boolean flag = this.burnTime > 0;
		boolean flag1 = false;

		if (this.burnTime > 0)
		{
			--this.burnTime;
		}

		if (!this.getWorld().isRemote)
		{
			
			if (inv[3] != null) {
				checkFluidContainer();
			}
			
			if (this.burnTime != 0 || this.inv[1] != null)
			{
				if (this.burnTime == 0 && this.canFreeze())
				{
					this.currentItemBurnTime = this.burnTime = getItemFreezeTime(this.inv[1]);

					if (this.burnTime > 0)
					{
						flag1 = true;

						if (this.inv[1] != null)
						{
							this.inv[1].shrink(1);

							if (this.inv[1].getCount() == 0)
							{
								this.inv[1] = inv[1].getItem().getContainerItem(inv[1]);
							}
						}
					}
				}

				if (this.isFreezing() && this.canFreeze())
				{
					
					
					++this.cookTime;

					if (this.cookTime == 200)
					{
						this.cookTime = 0;
						this.freezeItem();
						flag1 = true;
					}
				}
				else
				{
					this.cookTime = 0;
				}
			} else {
				this.cookTime = 0;
			}

			if (flag != this.burnTime > 0)
			{
				flag1 = true;
				int meta = getWorld().getBlockMetadata(xCoord, yCoord, zCoord);
				this.getWorld().setBlockMetadataWithNotify(xCoord, yCoord, zCoord, burnTime > 0 ? meta + 4 : meta - 4, 2);
			}
		}

		if (flag1)
		{
			this.markDirty();
			getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	public static int getItemFreezeTime(ItemStack itemStack) {
		if (itemStack != null) {
			Item item = itemStack.getItem();
			if (item == MeteorItems.itemFrezaCrystal) {
				return 1600;
			}
			if (item == Item.getItemFromBlock(Blocks.ICE)) {
				return 200;
			}
			if (item == Item.getItemFromBlock(Blocks.PACKED_ICE)) {
				return 400;
			}
		}
		return 0;
	}

	private boolean canFreeze() {
		FreezerRecipe recipe = FreezerRecipes.instance().getFreezingResult(this.inv[0], tank.getFluid(), this.acceptedRecipeType);
		if (recipe == null) return false;
		ItemStack result = recipe.getResult(inv[0]);
		if (this.inv[2] == null) {
			if (this.lastKnownItem == null) {
				this.lastKnownItem = result;
			} else if (!result.isItemEqual(this.lastKnownItem)) {
				this.cookTime = 0;
				this.lastKnownItem = result;
				this.markDirty();
				getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			return true;
		}
		if (!this.inv[2].isItemEqual(result)) return false;
		int resultSize = inv[2].getCount() + result.getCount();
		if (resultSize <= getInventoryStackLimit() && resultSize <= this.inv[2].getMaxStackSize()) {
			if (this.lastKnownItem == null) {
				this.lastKnownItem = result;
			} else if (!result.isItemEqual(this.lastKnownItem)) {
				this.cookTime = 0;
				this.lastKnownItem = result;
				this.markDirty();
				getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			return true;
		}
		return false;
	}

	public void freezeItem() {
		if (this.canFreeze())
		{
			FreezerRecipe recipe = FreezerRecipes.instance().getFreezingResult(this.inv[0], tank.getFluid(), this.acceptedRecipeType);

			if (this.inv[2] == null)
			{
				this.inv[2] = recipe.getResult(inv[0]).copy();
			}
			else if (this.inv[2].getItem() == recipe.getResult(inv[0]).getItem())
			{
				this.inv[2].grow(recipe.getResult(inv[0]).getCount());
			}

			if (recipe.requiresItem()) {
				this.inv[0].shrink(1);

				if (this.inv[0].getCount() <= 0)
				{
					this.inv[0] = null;
				}
			}

			if (recipe.requiresFluid()) {
				tank.drain(recipe.getFluidAmount(), true);

				if (tank.getFluidAmount() == 0) {
					getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}

		}
	}

	/* IFluidHandler */
	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		if (tank.getFluidAmount() == 0) {
			getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(tank.getFluid()))
		{
			getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
			return null;
		}
		return tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		if (doDrain && tank.getFluidAmount() - maxDrain <= 0) {
			getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		return tank.getTankProperties();
	}

	public FluidTankInfo getTankInfo() {
		return tank.getInfo();
	}

	@Override
	public void onButtonPress(int id) {
		if (id == 0) {
			
			int i = this.acceptedRecipeType.getID();
			if (i == 3) {
				i = 0;
			} else {
				i++;
			}
			this.acceptedRecipeType = RecipeType.values()[i];
		}
	}

}
