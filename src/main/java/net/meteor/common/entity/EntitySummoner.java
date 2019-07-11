package net.meteor.common.entity;

import io.netty.buffer.ByteBuf;
import net.meteor.common.ClientHandler;
import net.meteor.common.EnumMeteor;
import net.meteor.common.IMeteorShield;
import net.meteor.common.MeteorItems;
import net.meteor.common.MeteorsMod;
import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.climate.HandlerWorld;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntitySummoner extends EntityThrowable implements IEntityAdditionalSpawnData
{
	private static final float[][] spellRGB = { 
		{0.4352941176470588F, 0.0784313725490196F, 0.6588235294117647F}, 
		{0.0588235294117647F, 0.6784313725490196F, 0.6784313725490196F}, 
		{0.6941176470588235F, 0.0470588235294118F, 0.0470588235294118F}, 
		{0.392156862745098F, 0.3725490196078431F, 0.3450980392156863F}, 
		{0.0941176470588235F, 0.6470588235294118F, 0.0941176470588235F} };

	public EnumMeteor meteorType;
	public boolean isRandom;

	private EntityLiving thrower;
	private String throwerName = null;

	public EntitySummoner(World world)
	{
		super(world);
	}

	public EntitySummoner(World world, EntityLivingBase entityliving)
	{
		super(world, entityliving);
	}

	public EntitySummoner(World world, EntityLivingBase entityliving, EnumMeteor meteorID, boolean r)
	{
		this(world, entityliving);
		this.meteorType = meteorID;
		this.isRandom = r;
	}

	public EntitySummoner(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (this.getEntityWorld().isRemote) {
			int rgbIndex = meteorType.getID();
			if (isRandom) {
				rgbIndex = this.getEntityWorld().rand.nextInt(5);
			}
			getEntityWorld().spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX, this.posY, this.posZ, spellRGB[rgbIndex][0], spellRGB[rgbIndex][1], spellRGB[rgbIndex][2]);
		}
	}

	@Override
	protected void onImpact(RayTraceResult rayTraceResult)
	{
		for (int i = 0; i < 8; i++)
		{
			this.getEntityWorld().spawnParticle(EnumParticleTypes.SNOWBALL, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		}

		EntityPlayer player = (EntityPlayer)this.getThrower();

		if (!MeteorsMod.instance.isDimensionWhitelisted(getEntityWorld().provider.getDimension())) {
			if ((player != null) && (!this.getEntityWorld().isRemote)) {
				player.sendMessage(new TextComponentString(I18n.translateToLocal("MeteorSummoner.wrongDimension")));
				if (!player.capabilities.isCreativeMode) {
					if (this.isRandom) {
						player.inventory.addItemStackToInventory(new ItemStack(MeteorItems.itemMeteorSummonerRandom, 1));
					} else {
						//TODO 1.12.2
						//player.inventory.addItemStackToInventory(new ItemStack(MeteorItems.itemMeteorSummoner, 1, this.meteorType + 1));
					}
				}
			}

			this.setDead();
			return;
		}

		if (!this.getEntityWorld().isRemote) {

			boolean canHit = true;

			if (!getEntityWorld().getGameRules().getBoolean(HandlerWorld.SUMMON_METEORS_GAMERULE)) {
				canHit = false;
				player.sendMessage(ClientHandler.createMessage(I18n.translateToLocal("MeteorSummoner.cannotSummon"), TextFormatting.RED));
				if (!player.capabilities.isCreativeMode) {
					if (this.isRandom) {
						player.inventory.addItemStackToInventory(new ItemStack(MeteorItems.itemMeteorSummonerRandom, 1));
					} else {
						//TODO 1.12.2
						//player.inventory.addItemStackToInventory(new ItemStack(MeteorItems.itemMeteorSummoner, 1, this.meteorType + 1));
					}
				}
			} else if ((!MeteorsMod.instance.allowSummonedMeteorGrief) && (player != null)) {
				IMeteorShield shield = MeteorsMod.proxy.metHandlers.get(getEntityWorld().provider.getDimension()).getShieldManager().getClosestShieldInRange((int)this.posX, (int)this.posZ);
				if (shield != null && (!player.getName().equalsIgnoreCase(shield.getOwner()))) {
					canHit = false;
					player.sendMessage(new TextComponentString(I18n.translateToLocal("MeteorSummoner.landProtected")));
					if (!player.capabilities.isCreativeMode) {
						if (this.isRandom) {
							player.inventory.addItemStackToInventory(new ItemStack(MeteorItems.itemMeteorSummonerRandom, 1));
						} else {
							//TODO 1.12.2
							//player.inventory.addItemStackToInventory(new ItemStack(MeteorItems.itemMeteorSummoner, 1, this.meteorType + 1));
						}
					}
				}

			}

			if (canHit) {
				if (player != null) {
					player.sendMessage(ClientHandler.createMessage(I18n.translateToLocal("MeteorSummoner.incomingMeteor"), TextFormatting.LIGHT_PURPLE));
					//TODO 1.12.2
					//player.triggerAchievement(HandlerAchievement.summonMeteor);
				}
				EntityMeteor meteorToSpawn = new EntityMeteor(this.getEntityWorld(), HandlerMeteor.getMeteorSize(), this.posX, this.posZ, this.meteorType, true);
				this.getEntityWorld().spawnEntity(meteorToSpawn);
			}
		}
		this.setDead();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("metType", this.meteorType.getID());
		par1NBTTagCompound.setBoolean("isRandom", this.isRandom);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);
		this.meteorType = EnumMeteor.getTypeFromID(par1NBTTagCompound.getInteger("metType"));
		this.isRandom = par1NBTTagCompound.getBoolean("isRandom");
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(this.meteorType.getID());
		buffer.writeBoolean(this.isRandom);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.meteorType = EnumMeteor.getTypeFromID(additionalData.readInt());
		this.isRandom = additionalData.readBoolean();
	}

}