package net.meteor.common.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.meteor.common.ClientHandler;
import net.meteor.common.ClientProxy;
import net.meteor.common.EnumMeteor;
import net.meteor.common.HandlerAchievement;
import net.meteor.common.IMeteorShield;
import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.climate.MeteorShieldData;
import net.meteor.common.entity.EntityComet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMeteorShield extends TileEntityNetworkBase implements ISidedInventory, IMeteorShield, ITickable
{

	public static final int CHARGE_TIME = 1600;

	private boolean shieldedChunks;
	private boolean blockComets;
	public String owner = "None";
	private int range;
	private int powerLevel;
	private int cometX;
	private int cometZ;
	private int cometType = -1;

	public int age;

	// Inventory stuff
	private NonNullList<ItemStack> inv = NonNullList.withSize(13, ItemStack.EMPTY);


	public TileEntityMeteorShield() {
		this.range = 0;
		this.powerLevel = 0;
		this.age = 0;
		this.shieldedChunks = false;
		this.blockComets = false;
	}

	public TileEntityMeteorShield(String theOwner) {
		this();
		this.owner = theOwner;
	}

	@Override
	public void update()
	{
		++age;
		
		if (!this.shieldedChunks) {
			if (powerLevel > 0) {
				if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
					MeteorsMod.proxy.metHandlers.get(getWorld().provider.getDimension()).getShieldManager().addShield(this);
				}
				this.shieldedChunks = true;
				//TODO 1.12.2 don't think this is needed anymore
				//getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
				} else if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
				generateParticles(this.getWorld(), this.getX(), this.getY(), this.getZ(), this.getWorld().rand);
			} else if (age >= CHARGE_TIME) {
				setCharged();
				if (!getWorld().isRemote) {
					if (owner != null && owner.length() > 0) {
						EntityPlayer player = getWorld().getPlayerEntityByName(owner);
						if (player != null) {
							player.sendMessage(ClientHandler.createMessage(I18n.translateToLocal("MeteorShield.howToUpgrade"), TextFormatting.GOLD));
						}
					}
				}
			}
		}
		if (this.shieldedChunks) {
			range = powerLevel * MeteorsMod.instance.ShieldRadiusMultiplier;	// update range (may load as 0 for old updates, thus check with this)
		}
	}

	public void setCharged() {
		if (powerLevel == 0) {
			powerLevel = 1;
		}
	}
	
	@Override
	public boolean getPreventComets() {
		return this.blockComets;
	}
	
	public EnumMeteor getCometType() {
		if (cometType == -1) return EnumMeteor.METEORITE;
		return EnumMeteor.getTypeFromID(cometType);
	}

	public List<String> getDisplayInfo() {
		List<String> info = new ArrayList<>();
		
		if (powerLevel == 0) {
			info.add(I18n.translateToLocal("info.meteorShield.charging"));
			info.add(I18n.translateToLocalFormatted("info.meteorShield.charged", (int)((float)age / (float)CHARGE_TIME * 100)));
		} else {
			info.add(I18n.translateToLocalFormatted("info.meteorShield.powerLevel", powerLevel, 5));
			info.add(I18n.translateToLocalFormatted("info.meteorShield.range", range));
		}
		
		info.add(I18n.translateToLocalFormatted("info.meteorShield.owner", owner));
		
		if (powerLevel != 0) {
			if (blockComets) {
				info.add(I18n.translateToLocal("info.meteorShield.cometsBeingBlocked"));
			} else {
				if (cometType != -1) {
					info.add(I18n.translateToLocal("info.meteorShield.cometEnteredOrbit"));
					info.add(I18n.translateToLocalFormatted("info.meteorShield.cometX", cometX));
					info.add(I18n.translateToLocalFormatted("info.meteorShield.cometZ", cometZ));
				} else {
					info.add(I18n.translateToLocal("info.meteorShield.noComets"));
				}
			}
		}
		
		return info;
	}

	public void addMeteorMaterials(List<ItemStack> items) {

		for (ItemStack par1ItemStack : items) {
			ItemStack itemstack1;
			int k = 5;
			if (par1ItemStack.isStackable()) {
				while (par1ItemStack.getCount() > 0 && k >= 5 && k < this.getSizeInventory()) {
					itemstack1 = inv.get(k);

					if (itemstack1 != ItemStack.EMPTY && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1)) {
						int l = itemstack1.getCount() + par1ItemStack.getCount();

						if (l <= par1ItemStack.getMaxStackSize()) {
							par1ItemStack.setCount(0);
							itemstack1.setCount(l);
						} else if (itemstack1.getCount() < par1ItemStack.getMaxStackSize()) {
							par1ItemStack.shrink(par1ItemStack.getMaxStackSize() - itemstack1.getCount());
							itemstack1.setCount(par1ItemStack.getMaxStackSize());
						}
					}

					++k;
				}
			}

			if (par1ItemStack.getCount() > 0) {
				k = 5;

				while (k >= 5 && k < this.getSizeInventory()) {
					itemstack1 = inv.get(k);

					if (itemstack1 == ItemStack.EMPTY) {
						inv.set(k, par1ItemStack.copy());
						par1ItemStack.setCount(0);
						break;
					}

					++k;
				}
			}
		}
		//TODO 1.12.2 don't think this is needed anymore
		//getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
		this.markDirty();
	}
	
	public void detectComet(EntityComet comet) {
		this.cometX = (int)comet.posX;
		this.cometZ = (int)comet.posZ;
		this.cometType = comet.meteorType.getID();
		//TODO 1.12.2 don't think this is needed anymore
		//getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
		this.markDirty();
	}

	@SideOnly(Side.CLIENT)
	private void generateParticles(World world, int x, int y, int z, Random random)
	{
		if (world.getBlockState(new BlockPos(x, y + 1, z)).isOpaqueCube()) return;
		for (int currX = x - 2; currX <= x + 2; currX++)
		{
			for (int currZ = z - 2; currZ <= z + 2; currZ++)
			{
				if ((currX > x - 2) && (currX < x + 2) && (currZ == z - 1))
				{
					currZ = z + 2;
				}
				if (random.nextInt(100) == 25)
				{
					for (int currY = y; currY <= y + 1; currY++)
					{
						if (!world.isAirBlock(new BlockPos((currX - x) / 2 + x, currY, (currZ - z) / 2 + z)))
						{
							break;
						}
						ClientProxy.spawnParticle("meteorshield", x + 0.5D, y + 2.0D, z + 0.5D, currX - x + random.nextFloat() - 0.5D, currY - y - random.nextFloat() - 1.0F, currZ - z + random.nextFloat() - 0.5D, world, -1);
					}
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.owner = nbt.getString("owner");
		if (owner.trim().isEmpty()) {
			owner = "None";
		}
		
		this.powerLevel = nbt.getInteger("powerLevel");
		this.range = MeteorsMod.instance.ShieldRadiusMultiplier * powerLevel;
		this.blockComets = nbt.getBoolean("blockComets");
		
		if (nbt.hasKey("cometType")) {
			this.cometType = nbt.getInteger("cometType");
			this.cometX = nbt.getInteger("cometX");
			this.cometZ = nbt.getInteger("cometZ");
		}

		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		this.inv.clear();

		for (int tagIndex = 0; tagIndex < nbttaglist.tagCount(); tagIndex++) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(tagIndex);
			int slot = nbttagcompound1.getByte("Slot") & 255;

			if (slot >= 0 && slot < this.getSizeInventory()) {
				this.inv.set(slot, new ItemStack(nbttagcompound1));
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("owner", this.owner);
		nbt.setInteger("powerLevel", powerLevel);
		nbt.setBoolean("blockComets", blockComets);
		if (cometType != -1) {
			nbt.setInteger("cometType", cometType);
			nbt.setInteger("cometX", cometX);
			nbt.setInteger("cometZ", cometZ);
		}

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < getSizeInventory(); i++) {
			if (inv.get(i) != ItemStack.EMPTY) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.inv.get(i).writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbt.setTag("Items", nbttaglist);
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
		return new SPacketUpdateTileEntity(this.getPos(), 1, var1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getX(), getY(), getZ(), getX() + 1, getY() + 1.5, getZ() +1);
	}

	@Override
	public int getSizeInventory() {
		return inv.size();
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inv.get(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		ItemStack stack = getStackInSlot(i);
		if (stack != null) {
			if (stack.getCount() <= j) {
				setInventorySlotContents(i, null);
			} else {
				ItemStack stack2 = stack.splitStack(j);
				if (stack.getCount() <= 0) {
					setInventorySlotContents(i, null);
				}
				stack = stack2;
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null; // not needed, no items should be dropped
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if (itemstack == ItemStack.EMPTY) {
			inv.set(i, ItemStack.EMPTY);
			if (i > 0 && i < 5 && powerLevel > 1) {
				this.updateRange();
			}
		} else if (isItemValidForSlot(i, itemstack)) {
			if (i < 5 && itemstack.getCount() > 1) {
				itemstack.setCount(1);
			}
			if (i == 0) {
				this.setCharged();
				this.getWorld().playSound(getX() + 0.5D, getY() + 0.5D, getZ() + 0.5D, new SoundEvent(new ResourceLocation("meteors:shield.powerup")), SoundCategory.PLAYERS, 1.0F, 0.6F, true);
				this.markDirty();
			} else if (i > 0 && i < 5) {
				inv.set(i, itemstack);
				this.updateRange();
			} else {
				inv.set(i, itemstack);
			}
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	//TODO 1.12.2
	//@Override
	//public boolean isUseableByPlayer(EntityPlayer entityplayer) {
	//	return true; // TODO put in some proper checks for this
	//}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		if (itemstack.getItem() == MeteorItems.itemRedMeteorGem && i > 0 && i < 5) {
			return powerLevel > 0 && powerLevel < 5 && inv.get(i) == ItemStack.EMPTY;
		} else if (itemstack.getItem() == MeteorItems.itemMeteorChips && i == 0) {
			return powerLevel == 0;
		}

		return true;
	}

	@Override
	public String getName() {
		return "Meteor Shield";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}
	
	private void updateRange() {
		int powerCrystals = 0;
		for (int i = 1; i <= 4; i++) {
			if (inv.get(i) != ItemStack.EMPTY && inv.get(i).getItem() == MeteorItems.itemRedMeteorGem) {
				powerCrystals++;
			}
		}
		
		int oldLevel = this.powerLevel;
		this.powerLevel = 1 + powerCrystals;
		this.range = MeteorsMod.instance.ShieldRadiusMultiplier * powerLevel;
		//TODO 1.12.2 don't think this is needed anymore
		//getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);

		if (powerLevel > oldLevel) {
			this.getWorld().playSound(getX() + 0.5D, getY() + 0.5D, getZ() + 0.5D, new SoundEvent(new ResourceLocation("meteors:shield.powerup")), SoundCategory.BLOCKS, 1.0F, powerLevel / 10.0F + 0.5F, true);
			EntityPlayer player = getWorld().getPlayerEntityByName(owner);
			if (powerLevel == 5 && player != null) {
				//TODO 1.12.2
				//player.addStat(HandlerAchievement.shieldFullyUpgraded, 1);
			}
			if (MeteorsMod.instance.ShieldRadiusMultiplier <= 0 && !getWorld().isRemote) {
				if (player != null) {
					player.sendMessage(new TextComponentString(I18n.translateToLocal("MeteorShield.noUpgrade")));
				}	
			}
		} else if (powerLevel < oldLevel) {
			// TODO Add sound for power down
		}
		
		this.markDirty();
	}

	@Override
	public int getRange() {
		return this.range;
	}

	@Override
	public int getX() {
		return this.getPos().getX();
	}

	@Override
	public int getY() {
		return this.getPos().getY();
	}

	@Override
	public int getZ() {
		return this.getPos().getZ();
	}

	@Override
	public boolean isTileEntity() {
		return true;
	}

	@Override
	public String getOwner() {
		return this.owner;
	}

	@Override
	public int getPowerLevel() {
		return this.powerLevel;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof IMeteorShield) {
			IMeteorShield shield = (IMeteorShield)o;
			return (this.getX() == shield.getX()) && (this.getY() == shield.getY()) && (this.getZ() == shield.getZ());
		}
		return super.equals(o);
	}

	@Override
	public void onChunkUnload() {
		if (!this.getWorld().isRemote) {
			HandlerMeteor metHandler = MeteorsMod.proxy.metHandlers.get(getWorld().provider.getDimension());
			metHandler.getShieldManager().addShield(new MeteorShieldData(getPos(), powerLevel, owner, blockComets));
		}
		this.invalidate();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		int[] slots = new int[8];
		for (int i = 0; i < 8; i++) {
			slots[i] = i + 5;
		}
		
		return slots;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack item, EnumFacing side) {
		return slot < 5 && isItemValidForSlot(slot, item);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, EnumFacing side) {
		return slot > 4;
	}

	@Override
	public void onButtonPress(int id) {
		if (id == 0) {
			this.blockComets = !blockComets;
		}
	}

}