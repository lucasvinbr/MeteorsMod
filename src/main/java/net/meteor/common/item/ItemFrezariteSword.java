package net.meteor.common.item;

import java.util.List;

import net.meteor.common.MeteorsMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemFrezariteSword extends ItemSword
{
	public ItemFrezariteSword(Item.ToolMaterial toolMaterial)
	{
		super(toolMaterial);
		this.setCreativeTab(MeteorsMod.meteorTab);
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving)
	{
		par2EntityLiving.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("slowness")), 40, 5));
		return super.hitEntity(par1ItemStack, par2EntityLiving, par3EntityLiving);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("\2473" + I18n.translateToLocal("enchantment.frezSword.one"));
		tooltip.add("\2473" + I18n.translateToLocal("enchantment.frezSword.two"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack par1ItemStack)
	{
		return true;
	}

	@Override
	public int getItemEnchantability()
	{
		return 0;
	}

	//TODO 1.12.2
	//public Item setTexture(String s) {
	//	return this.setTextureName(MeteorsMod.MOD_ID + ":" + s);
	//}
	
}