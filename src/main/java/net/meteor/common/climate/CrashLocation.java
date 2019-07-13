package net.meteor.common.climate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class CrashLocation {

	public final BlockPos pos;
	public final boolean inOrbit;
	public final CrashLocation prevCrash;
	
	public CrashLocation(int x, int y, int z, boolean inOrbit, CrashLocation prevCrash) {
		this.pos = new BlockPos(x, y, z);
		this.inOrbit = inOrbit;
		this.prevCrash = prevCrash;
	}
	
	public void toNBT(NBTTagCompound nbt) {
		nbt.setInteger("LCX", pos.getX());
		nbt.setInteger("LCY", pos.getY());
		nbt.setInteger("LCZ", pos.getZ());
		nbt.setBoolean("LCO", inOrbit);
	}
	
	public static CrashLocation fromNBT(NBTTagCompound nbt) {
		int x = nbt.getInteger("LCX");
		int y = nbt.getInteger("LCY");
		int z = nbt.getInteger("LCZ");
		boolean orbit = nbt.getBoolean("LCO");
		if (x == 0 && y == 0 && z == 0) {
			return null;
		}
		return new CrashLocation(x, y, z, orbit, null);
	}

}
