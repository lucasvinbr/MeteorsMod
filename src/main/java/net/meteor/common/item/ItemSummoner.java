package net.meteor.common.item;

import java.util.List;

import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.entity.EntitySummoner;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSummoner extends ItemMeteorsMod
{
	private String[] names = { "random", "meteorite", "frezarite", "kreknorite", "unknown", "kitty" };

	//TODO 1.12.2
//	private IIcon metIcon;
//	private IIcon frezIcon;
//	private IIcon krekIcon;
//	private IIcon unkIcon;
//	private IIcon kittyIcon;

	public ItemSummoner() {
		super();
		this.maxStackSize = 16;
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemStack = playerIn.getHeldItem(handIn);
		if (!playerIn.capabilities.isCreativeMode)
		{
			itemStack.shrink(1);
		}
		worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, new SoundEvent(new ResourceLocation("minecraft:random.bow")), SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if (!worldIn.isRemote)
		{
			int i = itemStack.getItemDamage();//TODO 1.12.2, need to remove meta :'(
			worldIn.spawnEntity(new EntitySummoner(worldIn, playerIn, i == 0 ? HandlerMeteor.getMeteorType().getID() : i - 1, i == 0));
		}
		return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}

	//TODO 1.12.2
	/*
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int i)
	{
		switch (i) {
		case 1:
			return this.metIcon;
		case 2:
			return this.frezIcon;
		case 3:
			return this.krekIcon;
		case 4:
			return this.unkIcon;
		case 5:
			return this.kittyIcon;
		default:
			return this.itemIcon;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
    public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon("meteors:summoner");
		this.metIcon = par1IconRegister.registerIcon("meteors:sum_met");
		this.frezIcon = par1IconRegister.registerIcon("meteors:sum_frez");
		this.krekIcon = par1IconRegister.registerIcon("meteors:sum_krek");
		this.unkIcon = par1IconRegister.registerIcon("meteors:sum_unk");
		this.kittyIcon = par1IconRegister.registerIcon("meteors:sum_kitty");
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		int var2 = par1ItemStack.getItemDamage();
		return super.getUnlocalizedName() + "." + this.names[var2];
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < 6; var4++) {
			par3List.add(new ItemStack(par1, 1, var4));
		}
	}*/
}