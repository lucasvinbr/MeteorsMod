package net.meteor.common.entity;

import io.netty.buffer.ByteBuf;

import java.util.List;

import net.meteor.common.*;
import net.meteor.common.climate.CrashLocation;
import net.meteor.common.climate.GhostMeteor;
import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.crash.CrashFrezarite;
import net.meteor.common.crash.CrashKitty;
import net.meteor.common.crash.CrashKreknorite;
import net.meteor.common.crash.CrashMeteorite;
import net.meteor.common.crash.CrashUnknown;
import net.meteor.common.packets.PacketLastCrash;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityMeteor extends Entity implements IEntityAdditionalSpawnData
{
	public int size = 1;
	public EnumMeteor meteorType;
	public int spawnPauseTicks = 0;
	
	private int originX;
	private int originZ;

	private boolean summoned = false;

	public EntityMeteor(World world) {
		super(world);
		this.setSize(1f, 1f);

		this.preventEntitySpawning = true;
		//this.yOffset = (this.height / 2.0F);//TODO 1.12.2
		this.meteorType = EnumMeteor.METEORITE;

		this.motionX = (rand.nextDouble() - rand.nextDouble()) * 1.2D;
		this.motionZ = (rand.nextDouble() - rand.nextDouble()) * 1.2D;
		this.rotationYaw = (float)(Math.random() * 360D);
		this.rotationPitch = (float)(Math.random() * 360D);
	}

	public EntityMeteor(World world, int mSize, double originX, double originZ, EnumMeteor metType, boolean summon) {
		this(world);

		this.size = mSize;
		this.meteorType = metType;
		this.summoned = summon;
		this.originX = (int)originX;
		this.originZ = (int)originZ;
		this.setPosition(originX, 250.0D, originZ);
		this.prevPosX = originX;
		this.prevPosY = 250.0D;
		this.prevPosZ = originZ;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return !this.isDead;
	}

	@Override
	public void onUpdate()//1.12.2 TODO figure out why the meteor doesn't land where it appears to land..
	{
		if (!this.summoned) {
			if (!getEntityWorld().isRemote) {
				HandlerMeteor metHandler = MeteorsMod.proxy.metHandlers.get(getEntityWorld().provider.getDimension());
				IMeteorShield shield = metHandler.getShieldManager().getClosestShieldInRange((int)posX, (int)posZ);
				if (shield != null) {
					String owner = shield.getOwner();
					EntityPlayer playerOwner = getEntityWorld().getPlayerEntityByName(owner);
					if (playerOwner != null) {
						playerOwner.sendMessage(ClientHandler.createMessage(I18n.translateToLocal("MeteorShield.meteorBlocked"), TextFormatting.GREEN));
						HandlerAchievement.grantAdvancementAndStat((EntityPlayerMP) playerOwner, HandlerAchievement.meteorBlocked, HandlerAchievement.metsorsBlocked);
					}
					metHandler.getShieldManager().sendMeteorMaterialsToShield(shield, new GhostMeteor((int)posX, (int)posZ, size, 0, meteorType));
					this.getEntityWorld().playSound(posX, posY, posZ, new SoundEvent(new ResourceLocation("minecraft:random.explode")), SoundCategory.BLOCKS, 5F, (1.0F + (getEntityWorld().rand.nextFloat() - getEntityWorld().rand.nextFloat()) * 0.2F) * 0.7F, true);
					this.getEntityWorld().spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
					
					CrashLocation lastCrash = metHandler.getForecast().getLastCrashLocation();
					if (lastCrash != null && lastCrash.pos.getX() == originX && lastCrash.pos.getZ() == originZ) {
						metHandler.getForecast().setLastCrashLocation(null);
						MeteorsMod.network.sendToDimension(new PacketLastCrash(new CrashLocation(-1, -1, -1, false, null)), getEntityWorld().provider.getDimension());
					}
					
					this.setDead();
					return;
				}
			}
		}
		
		prevRotationPitch = rotationPitch;
		prevRotationYaw = rotationYaw;
		rotationPitch = (float)((rotationPitch + 3D) % 360D);
		rotationYaw = (float)((rotationPitch + 3D) % 360D);
		
		if (this.spawnPauseTicks > 0) {
			this.spawnPauseTicks -= 1;
			return;
		}

		if (summoned) {
			motionX = 0;
			motionZ = 0;
		}
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		motionY -= 0.039999999105930328D;
		move(MoverType.SELF, motionX, motionY, motionZ);
		motionY *= 0.98000001907348633D;

		if (onGround) {
			setDead();
			if(!getEntityWorld().isRemote) {
				if (!summoned) {
					
					AxisAlignedBB aabb = new AxisAlignedBB(posX - 40D, posY - 20D, posZ - 40D, posX + 40D, posY + 20D, posZ + 40D);
					List<EntityPlayer> players = this.getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class, aabb);
					for (EntityPlayer player : players) {
						HandlerAchievement.grantAdvancementAndStat((EntityPlayerMP) player, HandlerAchievement.foundMeteor, HandlerAchievement.meteorsFound);
					}
					
					HandlerMeteor metHandler = MeteorsMod.proxy.metHandlers.get(getEntityWorld().provider.getDimension());
					if (metHandler != null) {
						CrashLocation cc = metHandler.getForecast().getLastCrashLocation();
						if (cc != null && originX == cc.pos.getX() && originZ == cc.pos.getZ()) {
							metHandler.getForecast().setLastCrashLocation(new CrashLocation((int)posX, (int)posY, (int)posZ, false, cc.prevCrash));
							MeteorsMod.network.sendToDimension(new PacketLastCrash(metHandler.getForecast().getLastCrashLocation()), getEntityWorld().provider.getDimension());
						}
					}
					
				}
				CrashMeteorite worldGen = getWorldGen();

				if (worldGen.generate(getEntityWorld(), rand, new BlockPos((int)posX, (int)posY, (int)posZ))) {
					worldGen.afterCrashCompleted(getEntityWorld(), (int)posX, (int)posY, (int)posZ);
				}
			}
		} else {
			if (size == 1) {
				getEntityWorld().spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX, posY + 2.75D, posZ, 0.0D, 0.0D, 0.0D);
			} else {
				getEntityWorld().spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY + 4.0D, posZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	protected Explosion explode() {
		float f = (float) (this.size * MeteorsMod.instance.ImpactExplosionMultiplier);
		Explosion explosion = new Explosion(getEntityWorld(), this, posX, posY, posZ, f, meteorType.getFieryExplosion(), true);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
	}

	protected CrashMeteorite getWorldGen() {
		switch (this.meteorType.getID()) {
		case 0:
			return new CrashMeteorite(this.size, explode(), this.meteorType);
		case 1:
			return new CrashFrezarite(this.size, explode(), this.meteorType);
		case 2:
			return new CrashKreknorite(this.size, explode(), this.meteorType);
		case 3:
			return new CrashUnknown(this.size, explode(), this.meteorType);
		case 4:
			return new CrashKitty(this.size, explode(), this.meteorType);
		}
		return new CrashMeteorite(this.size, explode(), this.meteorType);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
		nbttagcompound.setInteger("size", this.size);
		nbttagcompound.setBoolean("summoned", this.summoned);
		nbttagcompound.setInteger("metTypeID", this.meteorType.getID());
		nbttagcompound.setInteger("pauseTicks", this.spawnPauseTicks);
		nbttagcompound.setInteger("originX", originX);
		nbttagcompound.setInteger("originZ", originZ);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
		this.size = nbttagcompound.getInteger("size");
		this.summoned = nbttagcompound.getBoolean("summoned");
		this.meteorType = EnumMeteor.getTypeFromID(nbttagcompound.getInteger("metTypeID"));
		this.spawnPauseTicks = nbttagcompound.getInteger("pauseTicks");
		this.originX = nbttagcompound.getInteger("originX");
		this.originZ = nbttagcompound.getInteger("originZ");

		this.setSize(this.size * 2f, this.size * 2f);

	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(this.meteorType.getID());
		buffer.writeInt(this.size);
		buffer.writeInt(this.spawnPauseTicks);
		buffer.writeBoolean(this.summoned);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.meteorType = EnumMeteor.getTypeFromID(additionalData.readInt());
		this.size = additionalData.readInt();
		this.spawnPauseTicks = additionalData.readInt();
		this.summoned = additionalData.readBoolean();
	}
}