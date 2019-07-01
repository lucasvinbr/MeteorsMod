package net.meteor.common.climate;

import java.util.List;
import java.util.Random;

import net.meteor.common.EnumMeteor;
import net.meteor.common.IMeteorShield;
import net.meteor.common.MeteorsMod;
import net.meteor.common.entity.EntityComet;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClimateUpdater {
	
	private static Random random = new Random();
	private int ticks = 0;
	private int tickGoal = -1;
	private HandlerMeteor meteorHandler;
	private long lastTick = 0L;
	
	public ClimateUpdater(HandlerMeteor metHandler) {
		this.meteorHandler = metHandler;
	}

	//TODO where is my subscribeevent O_o
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		World world = event.world;
		if (!world.isRemote) {
			int dim = world.provider.getDimension();
			if (world.getGameRules().getBoolean(HandlerWorld.METEORS_FALL_GAMERULE) && MeteorsMod.instance.isDimensionWhitelisted(dim)) {
				long wTime = world.getTotalWorldTime();
				if (wTime % 20L == 0L && wTime != lastTick) {
					lastTick = wTime;
					if (this.tickGoal == -1) {
						this.tickGoal = getNewTickGoal();
					}
					MeteorsMod mod = MeteorsMod.instance;
					meteorHandler.updateMeteors();
					if ((world.getWorldTime() % 24000L >= 12000L || (!mod.meteorsFallOnlyAtNight))) {
						this.ticks++;
						if (this.ticks >= this.tickGoal) {
							if ((meteorHandler.canSpawnNewMeteor()) && (world.playerEntities.size() > 0)) {
								int x = world.rand.nextInt(mod.meteorFallDistance);
								int z = world.rand.nextInt(mod.meteorFallDistance);
								if (world.rand.nextBoolean()) x = -x;
								if (world.rand.nextBoolean()) z = -z;
								EntityPlayer player = world.playerEntities.get(world.rand.nextInt(world.playerEntities.size()));
								x = (int)(x + player.posX);
								z = (int)(z + player.posZ);

								ChunkPos coords = new ChunkPos(new BlockPos(x, 0, z));
								if (meteorHandler.canSpawnNewMeteorAt(coords)) {
									if (random.nextInt(100) < MeteorsMod.instance.kittyAttackChance) {
										meteorHandler.readyNewMeteor(x, z, HandlerMeteor.getMeteorSize(), 90, EnumMeteor.KITTY);
									} else {
										meteorHandler.readyNewMeteor(x, z, HandlerMeteor.getMeteorSize(), random.nextInt(mod.RandTicksUntilMeteorCrashes + 1) + mod.MinTicksUntilMeteorCrashes, HandlerMeteor.getMeteorType());
									}
								}
							}
							
							if (random.nextInt(100) < MeteorsMod.instance.cometFallChance && world.playerEntities.size() > 0) {
								
								int x = world.rand.nextInt(mod.meteorFallDistance / 4);
								int z = world.rand.nextInt(mod.meteorFallDistance / 4);
								if (world.rand.nextBoolean()) x = -x;
								if (world.rand.nextBoolean()) z = -z;
								EntityPlayer player = world.playerEntities.get(world.rand.nextInt(world.playerEntities.size()));
								x = (int)(x + player.posX);
								z = (int)(z + player.posZ);
								EntityComet comet = new EntityComet(world, x, z, HandlerMeteor.getCometType());
								
								boolean blocked = false;
								List<IMeteorShield> shields = meteorHandler.getShieldManager().getShieldsInRange(x, z);
                                for (IMeteorShield ims : shields) {
                                    if (ims.getPreventComets()) {
                                        blocked = true;
                                        break;
                                    }
                                }
								
								if (!blocked) {
                                    for (IMeteorShield ims : shields) {
                                        TileEntityMeteorShield shield = (TileEntityMeteorShield) world.getTileEntity(ims.getX(), ims.getY(), ims.getZ());
                                        if (shield != null) {
                                            shield.detectComet(comet);
                                        }
                                    }
									
									world.spawnEntity(comet);
								}
								
							}

							this.ticks = 0;
							this.tickGoal = getNewTickGoal();
						}
					}
				}
			}
		}
		
	}

	private int getNewTickGoal() {
		return random.nextInt(MeteorsMod.instance.RandTicksUntilMeteorSpawn + 1) + MeteorsMod.instance.MinTicksUntilMeteorSpawn;
	}
	
	public int getSecondsUntilNewMeteor() {
		return tickGoal - ticks;
	}

}
