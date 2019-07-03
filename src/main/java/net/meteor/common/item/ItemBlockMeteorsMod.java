package net.meteor.common.item;

import java.util.List;

import net.meteor.common.MeteorBlocks;
import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemBlockMeteorsMod extends ItemBlock
{
	public ItemBlockMeteorsMod(Block bl)
	{
		super(bl);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (this == Item.getItemFromBlock(MeteorBlocks.METEOR_SHIELD_TORCH)) {
			tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.translateToLocal("ProtectionTorch.usage"));
		}
	}

	//TODO 1.12.2
	//public Item setTexture(String s) {
	//	return this.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	//}
	
}