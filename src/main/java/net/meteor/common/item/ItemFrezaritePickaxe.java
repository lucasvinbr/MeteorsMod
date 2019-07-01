package net.meteor.common.item;

import java.util.List;

import net.meteor.common.MeteorsMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFrezaritePickaxe extends ItemPickaxe
{
	public ItemFrezaritePickaxe(Item.ToolMaterial toolMaterial)
	{
		super(toolMaterial);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("\2473" + I18n.translateToLocal("enchantment.frezPickaxe.one"));
		par3List.add("\2473" + I18n.translateToLocal("enchantment.frezPickaxe.two"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack par1ItemStack, int pass)
	{
		return true;
	}

	@Override
	public int getItemEnchantability()
	{
		return 0;
	}
	
	public Item setTexture(String s) {
		return this.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	}
	
}