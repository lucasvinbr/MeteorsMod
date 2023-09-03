package net.meteor.common;

import java.io.Serializable;

import net.meteor.common.item.ItemSummoner;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nullable;

public enum EnumMeteor
implements Serializable
{
	METEORITE(0, MeteorBlocks.HOT_METEOR, MeteorBlocks.HOT_RARE_METEOR, true, "met", TextFormatting.LIGHT_PURPLE, new ItemStack(MeteorBlocks.HOT_METEOR, 1, 1), MeteorBlocks.METEOR),
	FREZARITE(1, MeteorBlocks.FREEZARITE, Blocks.ICE, false, "freza", TextFormatting.AQUA, new ItemStack(MeteorBlocks.FREEZARITE, 1, 1), MeteorBlocks.FREEZARITE),
	KREKNORITE(2, MeteorBlocks.KRENKONITE, Blocks.LAVA, true, "krekno", TextFormatting.RED, new ItemStack(MeteorBlocks.KRENKONITE, 1, 1), MeteorBlocks.KRENKONITE),
	UNKNOWN(3, Blocks.AIR, Blocks.AIR, true, "unk", TextFormatting.GRAY, new ItemStack(Blocks.CHEST, 1), Blocks.GLOWSTONE),
	KITTY(4, Blocks.AIR, Blocks.AIR, false, "kitty", TextFormatting.GREEN, new ItemStack(Items.FISH, 1), MeteorBlocks.METEOR),
	CUSTOM(5, Blocks.AIR, Blocks.AIR, false, "custom", TextFormatting.GOLD, new ItemStack(Blocks.STONE, 1), Blocks.STONE);

	private final int ID;
	private final Block material;
	private final Block rareMaterial;
	private final boolean fieryExplosion;
	private final String beamTex;
	private final TextFormatting chatColor;
	private final ItemStack representingItem;
	private final Block representingBlock;

	EnumMeteor(int id, Block mat, Block rMat, boolean fiery, String bTex, TextFormatting color, ItemStack item, Block block) {
		this.ID = id;
		this.material = mat;
		this.rareMaterial = rMat;
		this.fieryExplosion = fiery;
		this.beamTex = bTex; 
		this.chatColor = color;
		this.representingItem = item;
		this.representingBlock = block;
	}

	public Block getMaterial()
	{
		return this.material;
	}

	public Block getRareMaterial() {
		return this.rareMaterial;
	}

	public int getID() {
		return this.ID;
	}

	public boolean getFieryExplosion() {
		return this.fieryExplosion;
	}
	
	public TextFormatting getChatColor() {
		return this.chatColor;
	}
	
	public ItemStack getRepresentingItem() {
		return this.representingItem;
	}
	
	public Block getRepresentingBlock() {
		return this.representingBlock;
	}

	public static EnumMeteor getTypeFromID(int i) {
		switch (i) {
		case 0:
			return METEORITE;
		case 1:
			return FREZARITE;
		case 2:
			return KREKNORITE;
		case 3:
			return UNKNOWN;
		case 4:
			return KITTY;
		}
		return METEORITE;
	}
	
	public static String getLocalName(EnumMeteor type) {
		String name = "meteor." + type.toString().toLowerCase() + ".name";
		return I18n.translateToLocal(name);
	}

	@Nullable
	public static EnumMeteor getMeteorForItem(ItemSummoner itemSummoner) {
		if(itemSummoner == MeteorItems.itemMeteorSummonerMeteorite) {
			return EnumMeteor.METEORITE;
		}
		if(itemSummoner == MeteorItems.itemMeteorSummonerFrezarite) {
			return EnumMeteor.FREZARITE;
		}
		if(itemSummoner == MeteorItems.itemMeteorSummonerKreknorite) {
			return EnumMeteor.KREKNORITE;
		}
		if(itemSummoner == MeteorItems.itemMeteorSummonerUnknown) {
			return EnumMeteor.UNKNOWN;
		}
		if(itemSummoner == MeteorItems.itemMeteorSummonerKitty) {
			return EnumMeteor.KITTY;
		}

		return null;
	}

	public static ItemSummoner getItemSummonerForMeteorType(EnumMeteor meteorType) {
		if(meteorType == EnumMeteor.METEORITE) {
			return (ItemSummoner) MeteorItems.itemMeteorSummonerMeteorite;
		}
		if(meteorType == EnumMeteor.FREZARITE) {
			return (ItemSummoner) MeteorItems.itemMeteorSummonerFrezarite;
		}
		if(meteorType == EnumMeteor.KREKNORITE) {
			return (ItemSummoner) MeteorItems.itemMeteorSummonerKreknorite;
		}
		if(meteorType == EnumMeteor.UNKNOWN) {
			return (ItemSummoner) MeteorItems.itemMeteorSummonerUnknown;
		}
		if(meteorType == EnumMeteor.KITTY) {
			return (ItemSummoner) MeteorItems.itemMeteorSummonerKitty;
		}

		System.out.println("Missing ItemSummoner for EnumMeteor: "+meteorType);
		return (ItemSummoner) MeteorItems.itemMeteorSummonerMeteorite;
	}
	
}