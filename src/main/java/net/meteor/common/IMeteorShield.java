package net.meteor.common;


public interface IMeteorShield {
	
	int getRange();
	
	int getPowerLevel();
	
	int getX();
	
	int getY();
	
	int getZ();
	
	boolean isTileEntity();
	
	String getOwner();
	
	boolean getPreventComets();

}
