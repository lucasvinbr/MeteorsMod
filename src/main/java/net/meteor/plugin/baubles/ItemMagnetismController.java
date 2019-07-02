package net.meteor.plugin.baubles;

import java.util.List;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.meteor.common.MeteorsMod;
import net.meteor.common.item.ItemMeteorsMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemMagnetismController extends ItemMeteorsMod implements IBauble {
	
	private final static String MAGNETIZED_TAG = "magnetEnabled";
	
	protected Enchantment enchantment;
	protected int level;
	
	public ItemMagnetismController() {
		super();
		setMaxStackSize(1);
		setEnch(MeteorsMod.Magnetization, 3);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.BELT;
	}

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (nbt == null || !nbt.hasKey(MAGNETIZED_TAG)) {
			setNBTData(itemstack, true);
		}
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		String state = getNBTData(stack) ? TextFormatting.GREEN + I18n.translateToLocal("options.on") : TextFormatting.RED + I18n.translateToLocal("options.off");
		tooltip.add(I18n.translateToLocalFormatted("info.magnetisationController.state", state));
		tooltip.add(TextFormatting.DARK_GRAY + I18n.translateToLocalFormatted("info.magnetisationController.one", HandlerKey.getKey()));
		tooltip.add(TextFormatting.DARK_GRAY + I18n.translateToLocal("info.magnetisationController.two"));
		tooltip.add("");
	}
	
	public static void setNBTData(ItemStack itemstack, boolean val) {
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
		}
		nbt.setBoolean(MAGNETIZED_TAG, val);
		itemstack.setTagCompound(nbt);
	}
	
	public static boolean getNBTData(ItemStack itemstack) {
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (nbt != null && nbt.hasKey(MAGNETIZED_TAG)) {
			return nbt.getBoolean(MAGNETIZED_TAG);
		}
		setNBTData(itemstack, true);
		return true;
	}
	
	public static int isMagnetizationEnabled(EntityPlayer player, int def) {
		IBaublesItemHandler inv = BaublesApi.getBaublesHandler(player);
		ItemStack stack = inv.getStackInSlot(3);
		if (stack.getItem() == Baubles.MagnetismController) {
			return (getNBTData(stack) ? Math.max(EnchantmentHelper.getEnchantmentLevel(MeteorsMod.Magnetization, stack), def) : 0);
		}
		return def;
	}
	
	public ItemMeteorsMod setEnch(Enchantment ench, int lvl) {
		this.enchantment = ench;
		this.level = lvl;
		return this;
	}
	
	@Override
	public int getDamage(ItemStack stack) {
		if (!stack.isItemEnchanted() && !isRestricted(stack)) {
			stack.addEnchantment(this.enchantment, this.level);
			NBTTagCompound tag = stack.getTagCompound();
			tag.setBoolean("enchant-set", true);
			stack.setTagCompound(tag);
		}
		return super.getDamage(stack);
	}
	
	private boolean isRestricted(ItemStack item) {
		if (item.hasTagCompound()) {
			NBTTagCompound tag = item.getTagCompound();
			if (tag.hasKey("enchant-set")) {
				return tag.getBoolean("enchant-set");
			} else {
				tag.setBoolean("enchant-set", false);
				item.setTagCompound(tag);
			}
		}
		return false;
	}

}
