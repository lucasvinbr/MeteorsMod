package net.meteor.common;

import net.meteor.client.block.models.SlipperyBlockBakedModel;
import net.meteor.client.effect.ParticleFrezaDust;
import net.meteor.client.effect.ParticleMeteorDust;
import net.meteor.client.effect.ParticleMeteorShield;
import net.meteor.client.render.*;
import net.meteor.client.tileentity.TileEntityMeteorShieldRenderer;
import net.meteor.client.tileentity.TileEntityMeteorTimerRenderer;
import net.meteor.common.entity.*;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.meteor.common.tileentity.TileEntityMeteorTimer;
import net.meteor.plugin.baubles.Baubles;
import net.meteor.plugin.baubles.MagnetizationOverlay;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.Arrays;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy
{

	@Override
	public void registerTileEntities()
	{
		super.registerTileEntities();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMeteorShield.class, new TileEntityMeteorShieldRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMeteorTimer.class, new TileEntityMeteorTimerRenderer());
	}

	@Override
	public void loadStuff()
	{
		//TODO 1.12.2
		/*
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.METEOR_SHIELD), new ShieldItemRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MeteorBlocks.METEOR_TIMER), new TimerItemRenderer());
		 */

		if (Baubles.isBaublesLoaded()) {
			MinecraftForge.EVENT_BUS.register(new MagnetizationOverlay());
		}
	}

	public static void registerSlipperyBlockModels() {

		final ModelResourceLocation slipperyBlock1Loc = new ModelResourceLocation("meteors:slippery_block1", "normal");
		final ModelResourceLocation slipperyBlock2Loc = new ModelResourceLocation("meteors:slippery_block2", "normal");
		final ModelResourceLocation slipperyBlock3Loc = new ModelResourceLocation("meteors:slippery_block3", "normal");
		final ModelResourceLocation slipperyBlock4Loc = new ModelResourceLocation("meteors:slippery_block4", "normal");
		final ModelResourceLocation slipperyStairs1Loc = new ModelResourceLocation("meteors:slippery_stairs1", "normal");
		final ModelResourceLocation slipperyStairs2Loc = new ModelResourceLocation("meteors:slippery_stairs2", "normal");
		final ModelResourceLocation slipperyStairs3Loc = new ModelResourceLocation("meteors:slippery_stairs3", "normal");
		final ModelResourceLocation slipperyStairs4Loc = new ModelResourceLocation("meteors:slippery_stairs4", "normal");

		ModelLoader.setCustomStateMapper(MeteorBlocks.blockSlippery, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return slipperyBlock1Loc;
			}
		});
		ModelLoader.setCustomStateMapper(MeteorBlocks.blockSlipperyTwo, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return slipperyBlock2Loc;
			}
		});
		ModelLoader.setCustomStateMapper(MeteorBlocks.blockSlipperyThree, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return slipperyBlock3Loc;
			}
		});
		ModelLoader.setCustomStateMapper(MeteorBlocks.blockSlipperyFour, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return slipperyBlock4Loc;
			}
		});
		ModelLoader.setCustomStateMapper(MeteorBlocks.blockSlipperyStairs, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return slipperyStairs1Loc;
			}
		});
		ModelLoader.setCustomStateMapper(MeteorBlocks.blockSlipperyStairsTwo, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return slipperyStairs2Loc;
			}
		});
		ModelLoader.setCustomStateMapper(MeteorBlocks.blockSlipperyStairsThree, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return slipperyStairs3Loc;
			}
		});
		ModelLoader.setCustomStateMapper(MeteorBlocks.blockSlipperyStairsFour, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return slipperyStairs4Loc;
			}
		});

		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MeteorBlocks.blockSlippery), 0, slipperyBlock1Loc);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MeteorBlocks.blockSlipperyTwo), 0, slipperyBlock2Loc);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MeteorBlocks.blockSlipperyThree), 0, slipperyBlock3Loc);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MeteorBlocks.blockSlipperyFour), 0, slipperyBlock4Loc);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MeteorBlocks.blockSlipperyStairs), 0, slipperyStairs1Loc);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MeteorBlocks.blockSlipperyStairsTwo), 0, slipperyStairs2Loc);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MeteorBlocks.blockSlipperyStairsThree), 0, slipperyStairs3Loc);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MeteorBlocks.blockSlipperyStairsFour), 0, slipperyStairs4Loc);

	}


	@SubscribeEvent
	public static void onModelBakeEvent(ModelBakeEvent event)
	{
		// Find the existing mapping for CamouflageBakedModel - it will have been added automatically because
		//  we registered a custom BlockStateMapper for it (using ModelLoader.setCustomStateMapper)
		// Replace the mapping with our CamouflageBakedModel.

		for(int i = 0; i <= 4; i++) {
			final ModelResourceLocation loc = new ModelResourceLocation("meteors:slippery_block"+i, "normal");
			final IBakedModel existingModel = event.getModelRegistry().getObject(loc);
			if (existingModel != null) {
				SlipperyBlockBakedModel customModel = new SlipperyBlockBakedModel(existingModel);
				event.getModelRegistry().putObject(loc, customModel);
			}
		}
		for(int i = 0; i <= 4; i++) {
			final ModelResourceLocation loc = new ModelResourceLocation("meteors:slippery_stairs"+i, "normal");
			final IBakedModel existingModel = event.getModelRegistry().getObject(loc);
			if (existingModel != null) {
				SlipperyBlockBakedModel customModel = new SlipperyBlockBakedModel(existingModel);
				event.getModelRegistry().putObject(loc, customModel);
			}
		}
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		Class<MeteorItems> meteorItemsClass = MeteorItems.class;
		Field[] fields = meteorItemsClass.getDeclaredFields();
		Arrays.stream(fields).forEach(field -> {
			try {
				Object o = field.get(null);
				if (o instanceof Item)
					ModelLoader.setCustomModelResourceLocation((Item) o, 0, new ModelResourceLocation(((Item) o).getRegistryName().toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		Class<MeteorBlocks> meteorBlocksClass = MeteorBlocks.class;
		fields = meteorBlocksClass.getDeclaredFields();
		Arrays.stream(fields).forEach(field -> {
			try {
				Object o = field.get(null);
				if (o instanceof Block) {
					ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock((Block) o), 0, new ModelResourceLocation(((Block) o).getRegistryName().toString()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		registerSlipperyBlockModels();

	}

	@Override
	public void preInit() {
		if (Baubles.isBaublesLoaded()) {
			Baubles.setupBaubleClient();
		}

		RenderingRegistry.registerEntityRenderingHandler(EntityMeteor.class, RenderMeteor.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityAlienCreeper.class, RenderAlienCreeper.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityCometKitty.class, RenderCometKitty.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntitySummoner.class, RenderSummoner.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityComet.class, RenderComet.FACTORY);
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