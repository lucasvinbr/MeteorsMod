package net.meteor.common.climate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.meteor.common.ClientHandler;
import net.meteor.common.EnumMeteor;
import net.meteor.common.HandlerAchievement;
import net.meteor.common.IMeteorShield;
import net.meteor.common.MeteorsMod;
import net.meteor.common.entity.EntityMeteor;
import net.meteor.common.packets.PacketGhostMeteor;
import net.meteor.common.packets.PacketLastCrash;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class HandlerMeteor
{
	private WorldServer theWorld;
	private ClimateUpdater climateUpdater;
	private GhostMeteorData gMetData;
	private CrashedChunkSetData ccSetData;
	private final MeteorForecast forecast;
	private ShieldManager shieldManager;

	private static Random random = new Random();

	public ArrayList<GhostMeteor> ghostMets = new ArrayList<>();
	public ArrayList<CrashedChunkSet> crashedChunks = new ArrayList<>();

	public static EnumMeteor defaultType;

	public HandlerMeteor(WorldEvent.Load event, HandlerMeteorTick worldTickHandler) {
		MeteorsMod.instance.setClientStartConfig();
		this.theWorld = (WorldServer) event.getWorld();
		this.gMetData = GhostMeteorData.forWorld(theWorld, this);
		this.ccSetData = CrashedChunkSetData.forWorld(theWorld, this);
		this.climateUpdater = new ClimateUpdater(this);
		this.forecast = new MeteorForecast(climateUpdater, ghostMets, ccSetData.getLoadedCrashLocation(), theWorld);
		this.shieldManager = new ShieldManager(theWorld);
		worldTickHandler.registerUpdater(theWorld.provider.getDimension(), climateUpdater);
	}

	public void updateMeteors() {
		if (this.theWorld == null) {
			return;
		}

		for (int i = 0; i < this.ghostMets.size(); i++) {
			if (theWorld.getWorldTime() % 24000L >= 12000L || (!MeteorsMod.instance.meteorsFallOnlyAtNight)) {
				GhostMeteor gMeteor = this.ghostMets.get(i);
				ChunkPos coords = new ChunkPos(new BlockPos(gMeteor.x, 0, gMeteor.z));
				if (!canSpawnNewMeteorAt(coords)) {
					sendGhostMeteorRemovePacket(gMeteor);
					this.ghostMets.remove(i);
					forecast.updateNearestTimeForClients();
				} else {
					gMeteor.update();
					if (gMeteor.ready) {
						IMeteorShield shield = shieldManager.getClosestShieldInRange(gMeteor.x, gMeteor.z);
						if (shield != null) {
							String owner = shield.getOwner();
							EntityPlayer player = theWorld.getPlayerEntityByName(owner);
							if (player != null) {
								player.sendMessage(ClientHandler.createMessage(I18n.translateToLocal("MeteorShield.meteorBlocked"), TextFormatting.GREEN));
								player.addStat(HandlerAchievement.meteorBlocked, 1);
							}
							shieldManager.sendMeteorMaterialsToShield(shield, gMeteor);
						} else if (gMeteor.type == EnumMeteor.KITTY) {
							kittyAttack();
						} else {
							EntityMeteor meteor = new EntityMeteor(this.theWorld, gMeteor.size, gMeteor.x, gMeteor.z, gMeteor.type, false);
							this.theWorld.spawnEntity(meteor);
							applyMeteorCrash(gMeteor.x, 0, gMeteor.z);
							playCrashSound(meteor);
						}
						sendGhostMeteorRemovePacket(gMeteor);
						this.ghostMets.remove(i);
						forecast.updateNearestTimeForClients();
					}
				}
			}
		}
	}

	public void kittyAttack() {
		theWorld.getMinecraftServer().getPlayerList().sendMessage(ClientHandler.createMessage(I18n.translateToLocal("Meteor.kittiesIncoming"), TextFormatting.DARK_RED));
		for (int i = 0; i < this.theWorld.playerEntities.size(); i++) {
			EntityPlayer player = this.theWorld.playerEntities.get(i);
			if (player != null) {
				for (int r = random.nextInt(64) + 50; r >= 0; r--) {
					int x = random.nextInt(64);
					int z = random.nextInt(64);
					if (random.nextBoolean()) x = -x;
					if (random.nextBoolean()) z = -z;
					x = (int)(x + player.posX);
					z = (int)(z + player.posZ);
					IMeteorShield shield = shieldManager.getClosestShieldInRange(x, z);
					if (shield != null) {
						String owner = shield.getOwner();
						EntityPlayer playerOwner = theWorld.getPlayerEntityByName(owner);
						if (playerOwner != null) {
							playerOwner.sendMessage(ClientHandler.createMessage(I18n.translateToLocal("MeteorShield.meteorBlocked"), TextFormatting.GREEN));
							playerOwner.addStat(HandlerAchievement.meteorBlocked, 1);
						}
						shieldManager.sendMeteorMaterialsToShield(shield, new GhostMeteor(x, z, 1, 0, EnumMeteor.KITTY));
					} else {
						EntityMeteor fKitty = new EntityMeteor(this.theWorld, 1, x, z, EnumMeteor.KITTY, false);
						fKitty.spawnPauseTicks = random.nextInt(100);
						this.theWorld.spawnEntity(fKitty);
					}
				}
				player.addStat(HandlerAchievement.kittyEvent, 1);
			}	
		}
	}

	public void applyMeteorCrash(int x, int y, int z) {
		ArrayList<CrashedChunkSet> tbr = new ArrayList<>();

		for (CrashedChunkSet set : this.crashedChunks) {
			set.age += 1;
			if (set.age >= 20) {
				tbr.add(set);
			}
		}
		for (CrashedChunkSet crashedChunkSet : tbr) {
			this.crashedChunks.remove(crashedChunkSet);
		}

		ChunkPos coords = new ChunkPos(x, z);
		this.crashedChunks.add(new CrashedChunkSet(coords.x, coords.z, x, y, z));

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			forecast.setLastCrashLocation(new CrashLocation(x, y, z, true, forecast.getLastCrashLocation()));
			MeteorsMod.network.sendToDimension(new PacketLastCrash(forecast.getLastCrashLocation()), theWorld.provider.getDimension());
			if (MeteorsMod.instance.textNotifyCrash) {
				theWorld.getMinecraftServer().getPlayerList().sendMessage(new TextComponentString(I18n.translateToLocal("Meteor.crashed")));
			}
			
			AxisAlignedBB aabb = new AxisAlignedBB(x - 60D, 0, z - 60D, x + 60D, theWorld.getHeight(), z + 60D);
			List<EntityPlayer> players = this.theWorld.getEntitiesWithinAABB(EntityPlayer.class, aabb);
			for (EntityPlayer player : players) {
				player.addStat(HandlerAchievement.foundMeteor, 1);
			}
		}
	}
	
	public MeteorForecast getForecast() {
		return this.forecast;
	}
	
	public ShieldManager getShieldManager() {
		return this.shieldManager;
	}

	public boolean canSpawnNewMeteor() {
		return this.ghostMets.size() < 3;
	}

	public boolean canSpawnNewMeteorAt(ChunkPos coords) {
		for (CrashedChunkSet crashedChunk : this.crashedChunks) {
			if (crashedChunk.containsChunk(coords)) {
				return false;
			}
		}
		return true;
	}

	public void readyNewMeteor(int x, int z, int size, int tGoal, EnumMeteor type) {
		if (canSpawnNewMeteor()) {
			GhostMeteor gMeteor = new GhostMeteor(x, z, size, tGoal, type);
			this.ghostMets.add(gMeteor);
			sendGhostMeteorAddPacket(gMeteor);
			forecast.updateNearestTimeForClients();
			if (type == EnumMeteor.KITTY) {
				for (EntityPlayer player : theWorld.playerEntities) {
					player.sendMessage(ClientHandler.createMessage(I18n.translateToLocal("Meteor.kittiesDetected.one"), TextFormatting.DARK_RED));
					player.sendMessage(ClientHandler.createMessage(I18n.translateToLocal("Meteor.kittiesDetected.two"), TextFormatting.DARK_RED));
				}
			}
		}
	}

	public static int getMeteorSize() {
		int r = random.nextInt(26);
		int maxSize = MeteorsMod.instance.MaxMeteorSize;
		int minSize = MeteorsMod.instance.MinMeteorSize;
		if ((maxSize == 3) && (minSize == 1)) {
			if (r == 25)
				return 3;
			if (r > 15) {
				return 2;
			}
			return 1;
		}
		if ((maxSize == 3) && (minSize == 2)) {
			if (r > 15) {
				return 3;
			}
			return 2;
		}
		if ((minSize == 1) && (maxSize == 2)) {
			if (r > 15) {
				return 2;
			}
			return 1;
		}

		return minSize;
	}

	public static EnumMeteor getMeteorType() {
		int r = random.nextInt(63);
		MeteorsMod mod = MeteorsMod.instance;
		if ((r >= 60) && (mod.unknownEnabled)) {
			return EnumMeteor.UNKNOWN;
		}
		if ((r >= 52) && (mod.kreknoriteEnabled)) {
			return EnumMeteor.KREKNORITE;
		}
		if ((r >= 35) && (mod.frezariteEnabled)) {
			return EnumMeteor.FREZARITE;
		}
		return defaultType;
	}
	
	public static EnumMeteor getCometType() {
		EnumMeteor type = getMeteorType();
		if (type == EnumMeteor.UNKNOWN) {
			return EnumMeteor.getTypeFromID(random.nextInt(3));
		}
		return type;
	}

	public void sendGhostMeteorPackets(EntityPlayerMP player) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			ArrayList<GhostMeteor> mets = this.ghostMets;
			for (GhostMeteor met : mets) {
				MeteorsMod.network.sendTo(new PacketGhostMeteor(true, met), player);
			}
		}
	}

	private void sendGhostMeteorAddPacket(GhostMeteor met) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			MeteorsMod.network.sendToDimension(new PacketGhostMeteor(true, met), theWorld.provider.getDimension());
		}
	}

	private void sendGhostMeteorRemovePacket(GhostMeteor met) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			MeteorsMod.network.sendToDimension(new PacketGhostMeteor(false, met), theWorld.provider.getDimension());
		}
	}

	private void playCrashSound(EntityMeteor meteor) {
		for (EntityPlayer player : theWorld.playerEntities) {
			double xDiff = meteor.posX - player.posX;
			double zDiff = meteor.posZ - player.posZ;
			double xMod = xDiff / 128.0D * 4.0D;
			double zMod = zDiff / 128.0D * 4.0D;
			theWorld.playSound(player.posX + xMod, player.posY + 1.0D, player.posZ + zMod, new SoundEvent(new ResourceLocation(MeteorsMod.MOD_ID + ":meteor.crash")), SoundCategory.BLOCKS, 1.0F, 1.0F, true);
		}
	}
}
