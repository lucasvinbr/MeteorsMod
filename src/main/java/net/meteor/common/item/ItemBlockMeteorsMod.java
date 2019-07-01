package net.meteor.common.item;

import java.util.List;

import net.meteor.common.MeteorBlocks;
import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockMeteorsMod extends ItemBlock
{
	public ItemBlockMeteorsMod(Block bl)
	{
		super(bl);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if (this == Item.getItemFromBlock(MeteorBlocks.torchMeteorShieldActive)) {
			par3List.add(TextFormatting.LIGHT_PURPLE + I18n.translateToLocal("ProtectionTorch.usage"));
		}
	}
	
	public Item setTexture(String s) {
		return this.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	}
	
}