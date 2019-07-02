package net.meteor.common;

import net.meteor.client.block.ShieldItemRenderer;
import net.meteor.client.block.SlipperyItemRenderer;
import net.meteor.client.block.TimerItemRenderer;
import net.meteor.client.effect.ParticleFrezaDust;
import net.meteor.client.effect.ParticleMeteorShield;
import net.meteor.client.effect.ParticleMeteorDust;
import net.meteor.client.render.RenderAlienCreeper;
import net.meteor.client.render.RenderComet;
import net.meteor.client.render.RenderCometKitty;
import net.meteor.client.render.RenderMeteor;
import net.meteor.client.render.RenderSummoner;
import net.meteor.client.tileentity.TileEntityMeteorShieldRenderer;
import net.meteor.client.tileentity.TileEntityMeteorTimerRenderer;
import net.meteor.client.tileentity.TileEntitySlipperyRenderer;
import net.meteor.common.entity.EntityAlienCreeper;
import net.meteor.common.entity.EntityComet;
import net.meteor.common.entity.EntityCometKitty;
import net.meteor.common.entity.EntityMeteor;
import net.meteor.common.entity.EntitySummoner;
import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.meteor.common.tileentity.TileEntityMeteorTimer;
import net.meteor.common.tileentity.TileEntitySlippery;
import net.meteor.plugin.baubles.Baubles;
import net.meteor.plugin.baubles.MagnetizationOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ClientProxy extends CommonProxy
{
	
	@Override
	public void registerTileEntities()
	{
		TileEntityMeteorShieldRenderer tileRend = new TileEntityMeteorShieldRenderer();
		ClientRegistry.registerTileEntity(TileEntityMeteorShield.class, "TileEntityMeteorShield", tileRend);
		TileEntityMeteorTimerRenderer timerRend = new TileEntityMeteorTimerRenderer();
		ClientRegistry.registerTileEntity(TileEntityMeteorTimer.class, "TileEntityMeteorTimer", timerRend);
		GameRegistry.registerTileEntity(TileEntityFreezingMachine.class, "TileEntityIceMaker");
		ClientRegistry.registerTileEntity(TileEntitySlippery.class, "TileEntitySlippery", new TileEntitySlipperyRenderer());
	}

	@Override
	public void loadStuff()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityMeteor.class, RenderMeteor.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityAlienCreeper.class, RenderAlienCreeper.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityCometKitty.class, RenderCometKitty.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntitySummoner.class, RenderSummoner.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityComet.class, RenderComet.FACTORY);

		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockMeteorShield), new ShieldItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockMeteorTimer), new TimerItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlippery), new SlipperyItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlipperyTwo), new SlipperyItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlipperyThree), new SlipperyItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlipperyFour), new SlipperyItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlipperyStairs), new SlipperyItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlipperyStairsTwo), new SlipperyItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlipperyStairsThree), new SlipperyItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.blockSlipperyStairsFour), new SlipperyItemRenderer());
		
		if (Baubles.isBaublesLoaded()) {
			MinecraftForge.EVENT_BUS.register(new MagnetizationOverlay());
		}
	}
	
	@Override
	public void preInit() {
		if (Baubles.isBaublesLoaded()) {
			Baubles.setupBaubleClient();
		}
	}

	public static void spawnParticle(String s, double xIn, double yIn, double zIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, World worldObj, int opt)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.getRenderViewEntity() == null || mc.effectRenderer == null) {
			return;
		}
		int i = mc.gameSettings.particleSetting;
		if (i == 1 && worldObj.rand.nextInt(3) == 0) {
			i = 2;
		}
		Entity renderViewEntity = mc.getRenderViewEntity();
		double d6 = renderViewEntity.posX - xIn;
		double d7 = renderViewEntity.posY - yIn;
		double d8 = renderViewEntity.posZ - zIn;
		Particle obj = null;
		double d9 = 16D;
		if (d6 * d6 + d7 * d7 + d8 * d8 > d9 * d9) {
			return;
		}
		if (i > 1) {
			return;
		}
		switch (s) {
			case "meteordust":
				obj = new ParticleMeteorDust(worldObj, xIn, yIn, zIn, (float) xSpeedIn, (float) ySpeedIn, (float) zSpeedIn);
				break;
			case "frezadust":
				obj = new ParticleFrezaDust(worldObj, xIn, yIn, zIn, (float) xSpeedIn, (float) ySpeedIn, (float) zSpeedIn);
				break;
			case "meteorshield":
				if (opt != -1)
					obj = new ParticleMeteorShield(worldObj, xIn, yIn, zIn, xSpeedIn, ySpeedIn, zSpeedIn, opt);
				else {
					obj = new ParticleMeteorShield(worldObj, xIn, yIn, zIn, xSpeedIn, ySpeedIn, zSpeedIn);
				}
				break;
		}
		if (obj != null) {
			mc.effectRenderer.addEffect(obj);
		}
	}
}