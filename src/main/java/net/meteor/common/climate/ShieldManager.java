package net.meteor.common.climate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.meteor.common.IMeteorShield;
import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.meteor.common.crash.CrashUnknown;
import net.meteor.common.entity.EntityCometKitty;
import net.meteor.common.packets.PacketBlockedMeteor;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ShieldManager {

	private World theWorld;
	private MeteorShieldSavedData shieldSavedData;
	
	private static Random random = new Random();
	
	public ArrayList<IMeteorShield> meteorShields = new ArrayList<>();
	
	public ShieldManager(World w) {
		this.theWorld = w;
		this.shieldSavedData = MeteorShieldSavedData.forWorld(theWorld, this);
	}
	
	public void addShield(IMeteorShield shield) {
		TileEntity tile = theWorld.getTileEntity(new BlockPos(shield.getX(), shield.getY(), shield.getZ()));
		if (tile instanceof TileEntityMeteorShield) {
			for (int i = 0; i < meteorShields.size(); i++) {
				IMeteorShield shield2 = meteorShields.get(i);
				if (shield.equals(shield2)) {
					meteorShields.remove(i);
					meteorShields.add(shield);
					return;
				}
			}
			meteorShields.add(shield);
		}
		
	}
	
	public IMeteorShield getClosestShield(int x, int z) {
		Iterator<IMeteorShield> iter = this.meteorShields.iterator();
		IMeteorShield closest = null;
		double distance = -1;
		while (iter.hasNext()) {
			IMeteorShield shield = iter.next();
			if (closest == null) {
				closest = shield;
				distance = getDistance(x, z, shield.getX(), shield.getZ());
			} else {
				double d = getDistance(x, z, shield.getX(), shield.getZ());
				if (d < distance) {
					distance = d;
					closest = shield;
				}
			}
		}
		return closest;
	}
	
	public IMeteorShield getClosestShieldInRange(int x, int z) {
		IMeteorShield shield = getClosestShield(x, z);
		if (shield != null) {
			double distance = getDistance(x, z, shield.getX(), shield.getZ());
			return distance <= shield.getRange() ? shield : null;
		}
		return null;
	}

	public List<IMeteorShield> getShieldsInRange(int x, int z) {
		List<IMeteorShield> shields = new ArrayList<>();
		for (IMeteorShield shield : this.meteorShields) {
			double d = getDistance(x, z, shield.getX(), shield.getZ());
			if (d <= shield.getRange()) {
				shields.add(shield);
			}
		}
		return shields;
	}
	
	public void sendMeteorMaterialsToShield(IMeteorShield shield, GhostMeteor gMeteor) {
		TileEntityMeteorShield tShield = (TileEntityMeteorShield) theWorld.getTileEntity(new BlockPos(shield.getX(), shield.getY(), shield.getZ()));
		if (tShield != null) {
			
			MeteorsMod.network.sendToAllAround(new PacketBlockedMeteor(shield.getX(), shield.getY(), shield.getZ(), gMeteor.type), new NetworkRegistry.TargetPoint(theWorld.provider.getDimension(), shield.getX(), shield.getY(), shield.getZ(), 64));
			
			List<ItemStack> items = new ArrayList<>();
			int r = random.nextInt(100);
			switch (gMeteor.type.getID()) {
				case 0:
					items.add(new ItemStack(MeteorItems.itemMeteorChips, random.nextInt(4 * gMeteor.size) + 1));
					if (r < 15) {
						items.add(new ItemStack(MeteorItems.itemRedMeteorGem, random.nextInt(2 * gMeteor.size) + 1));
					}
					break;
				case 1:
					items.add(new ItemStack(MeteorItems.itemFrezaCrystal, random.nextInt(4 * gMeteor.size) + 1));
					if (r < 20) {
						items.add(new ItemStack(Blocks.ICE, random.nextInt(3 * gMeteor.size) + 1));
					}
					break;
				case 2:
					items.add(new ItemStack(MeteorItems.itemKreknoChip, random.nextInt(3 * gMeteor.size) + 1));
					if (gMeteor.size >= MeteorsMod.instance.MinMeteorSizeForPortal) {
						if (r < 20) {
							items.add(new ItemStack(Items.NETHERBRICK, random.nextInt(4 * gMeteor.size) + 1));
						}
						if (r < 10) {
							items.add(new ItemStack(Items.GLOWSTONE_DUST, random.nextInt(3 * gMeteor.size) + 1));
						}
					}
					break;
				case 3:
					if (r < 50) {
						items.add(new ItemStack(Items.GLOWSTONE_DUST, random.nextInt(5 * gMeteor.size) + 1));
					}
					if (r < 75) {
						int r2 = random.nextInt(3);
						if (r2 == 0) {
							items.add(new ItemStack(MeteorItems.itemMeteorChips, random.nextInt(4) + 1));
						} else if (r2 == 1) {
							items.add(new ItemStack(MeteorItems.itemFrezaCrystal, random.nextInt(4) + 1));
						} else {
							items.add(new ItemStack(MeteorItems.itemKreknoChip, random.nextInt(4) + 1));
						}
					}
					if (r < 20) {
						ItemStack stack = CrashUnknown.getRandomLoot(random);
						if (stack != null) {
							items.add(stack);
						}
					}
					if (r < 5) {
						ItemStack stack = CrashUnknown.getRandomLoot(random);
						if (stack != null) {
							items.add(stack);
						}
					}
					break;
				case 4:
					items.add(new ItemStack(Items.COOKED_FISH, random.nextInt(2 * gMeteor.size) + 1));
					if (r < 5) {
						ItemStack spawnEggStack = new ItemStack(Items.SPAWN_EGG, 1, EntityList.getID(EntityCometKitty.class));
						
						NBTTagCompound eggStackTagCompound = spawnEggStack.getTagCompound();
						
						if(eggStackTagCompound == null){
							// MeteorsMod.log.info("kitty egg has no NBT data! we must create it");
							eggStackTagCompound = new NBTTagCompound();
						}

						ResourceLocation kittyRes = new ResourceLocation(MeteorsMod.MOD_ID, "cometkitty");
						NBTTagCompound nbttagcompound1 = eggStackTagCompound.getCompoundTag("EntityTag");

						if(nbttagcompound1 == null){
							// MeteorsMod.log.info("kitty egg has no EntityTag NBT data! we must create it");
							nbttagcompound1 = new NBTTagCompound();
						}

						if (!nbttagcompound1.hasKey("id", 8)) {
							nbttagcompound1.setString("id", kittyRes.toString());
						} else {
							String s = nbttagcompound1.getString("id");
							if (!s.contains(":")) {
							nbttagcompound1.setString("id", kittyRes.toString());
							}
						}

						eggStackTagCompound.setTag("EntityTag", nbttagcompound1);
						spawnEggStack.setTagCompound(eggStackTagCompound);

						items.add(spawnEggStack);
					}
					break;
			}
			tShield.addMeteorMaterials(items);
		}
	}
	
	private double getDistance(int x1, int z1, int x2, int z2) {
		int x = x1 - x2;
		int z = z1 - z2;
		return MathHelper.sqrt(x * x + z * z);
	}

}
