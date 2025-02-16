package net.meteor.common;

import java.util.Random;

import net.meteor.common.climate.HandlerMeteor;
import net.meteor.common.climate.HandlerWorld;
import net.meteor.common.command.CommandDebugMeteors;
import net.meteor.common.command.CommandDebugShields;
import net.meteor.common.command.CommandKittyAttack;
import net.meteor.common.command.CommandSpawnComet;
import net.meteor.common.command.CommandSpawnMeteor;
import net.meteor.common.enchantment.EnchantmentColdTouch;
import net.meteor.common.enchantment.EnchantmentMagnetized;
import net.meteor.common.entity.EntityAlienCreeper;
import net.meteor.common.entity.EntityComet;
import net.meteor.common.entity.EntityCometKitty;
import net.meteor.common.entity.EntityMeteor;
import net.meteor.common.entity.EntitySummoner;
import net.meteor.plugin.baubles.Baubles;
import net.meteor.plugin.thaumcraft.Thaumcraft;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
@Mod(modid=MeteorsMod.MOD_ID, name=MeteorsMod.MOD_NAME, version=MeteorsMod.VERSION, dependencies="after:waila;after:baubles;after:thaumcraft")
public class MeteorsMod implements IWorldGenerator
{

	//1.12.2 TODO seems meteors arnt falling randomly like they do on 1.7.10

	public static final String MOD_ID 	= "meteors";
	public static final String MOD_NAME = "Falling Meteors";
	public static final String VERSION 	= "@VERSION@";
	
	public static final boolean loggable = false;		// For Debugging Purposes Only TODO change to false when releasing
	public static final Random RANDOM = new Random();

	public static Logger log;

	public static SimpleNetworkWrapper network;
	
	public static Enchantment Magnetization;
	public static Enchantment ColdTouch;
	
	public static final CreativeTabs meteorTab = new CreativeTabMeteor("Falling Meteors Mod");

	@SidedProxy(clientSide="net.meteor.common.ClientProxy", serverSide="net.meteor.common.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Instance(MOD_ID)
	public static MeteorsMod instance;
	public HandlerAchievement achHandler;
	public HandlerPlayerTick playerTickHandler;
	public int MinTicksUntilMeteorSpawn;
	public int RandTicksUntilMeteorSpawn;
	public int MinTicksUntilMeteorCrashes;
	public int RandTicksUntilMeteorCrashes;
	public boolean meteorsFallOnlyAtNight;
	public boolean allowSummonedMeteorGrief = false;
	public int meteorFallDistance;
	public int MaxMeteorSize;
	public int MinMeteorSize;
	public int ShieldRadiusMultiplier;
	public int kittyAttackChance;
	public boolean textNotifyCrash;
	public boolean meteoriteEnabled;
	public boolean frezariteEnabled;
	public boolean kreknoriteEnabled;
	public boolean unknownEnabled;
	public boolean meteorShieldSound;
	private int chunkChecks;
	private int oreGenSize;
	public int MinMeteorSizeForPortal;
	public double ImpactExplosionMultiplier;
	public int ImpactSpread;
	public int cometFallChance;
	public int[] whitelistedDimensions;
	public boolean slipperyBlocksEnabled;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		log = event.getModLog();
		ModConfig.instance.load(event.getSuggestedConfigurationFile());
		loadStaticConfigurationValues();

		MeteorItems.readyItems();
		loadPlugins();
		
		HandlerMeteor.defaultType = EnumMeteor.METEORITE;
		if (!this.meteoriteEnabled) {
			HandlerMeteor.defaultType = EnumMeteor.FREZARITE;
			if (!this.frezariteEnabled) {
				HandlerMeteor.defaultType = EnumMeteor.KREKNORITE;
				if (!this.kreknoriteEnabled) {
					HandlerMeteor.defaultType = EnumMeteor.UNKNOWN;
					if (!this.unknownEnabled)
						HandlerMeteor.defaultType = EnumMeteor.METEORITE;
				}
			}
		}
		
		this.achHandler = new HandlerAchievement();
		network = NetworkRegistry.INSTANCE.newSimpleChannel("METEORS");
		ClientHandler cHandler = new ClientHandler();
		cHandler.registerPackets();
		MinecraftForge.EVENT_BUS.register(cHandler);
		proxy.preInit();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		registerEntities();
		HandlerRecipe recipeHandler = new HandlerRecipe();
		recipeHandler.addRecipes();

		this.playerTickHandler = new HandlerPlayerTick();

		proxy.loadStuff();

		MinecraftForge.EVENT_BUS.register(new HandlerPlayerBreakSpeed());
		MinecraftForge.EVENT_BUS.register(new HandlerWorld());
		MinecraftForge.EVENT_BUS.register(new TooltipProvider());
		// Ore Dictionary
		OreDictionary.registerOre("oreMeteorite", MeteorBlocks.METEOR_ORE);
		OreDictionary.registerOre("oreFrezarite", MeteorBlocks.FREEZARITE_ORE);
		// TODO - investigate whether this is correct for decoration blocks, I've yet to work on making this stateful
		OreDictionary.registerOre("blockMeteorite", MeteorBlocks.DECORATOR_METEORITE);
		OreDictionary.registerOre("blockFrezarite", MeteorBlocks.DECORATOR_FREEZARITE);
		OreDictionary.registerOre("blockKreknorite", MeteorBlocks.DECORATOR_KREKNORITE);
		OreDictionary.registerOre("blockRedMeteorGem", MeteorBlocks.RED_METEOR_GEM);
		FMLCommonHandler.instance().bus().register(recipeHandler);
		FMLCommonHandler.instance().bus().register(achHandler);
		GameRegistry.registerFuelHandler(recipeHandler);
		GameRegistry.registerWorldGenerator(this, 1);
		FMLCommonHandler.instance().bus().register(new HandlerPlayerTick());
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new HandlerGui());
	}


	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {}

	private void loadStaticConfigurationValues() {
		ModConfig config = ModConfig.instance;
		// Enchantments 
		Magnetization = new EnchantmentMagnetized(Enchantment.Rarity.RARE).setRegistryName("magnetization").setName("Magnetization");
		ColdTouch 	  = new EnchantmentColdTouch(Enchantment.Rarity.RARE).setRegistryName("cold_touch").setName("Cold Touch");
		// General Configuration
		meteorFallDistance		= config.get("Meteor Fall Radius", 350, "When determining where a meteor falls, it chooses within this radius (blocks) of a random player.");
		kittyAttackChance		= config.get("Kitty Attack Chance", 1, "Ranges from 0 to 100");
		textNotifyCrash			= config.get("Text Crash Notification", false, "Pops up a chat message when a meteor falls.");
		meteoriteEnabled		= config.get("Meteorite Meteor Enabled", true, "Allow Meteorite Meteors to Fall?");
		frezariteEnabled		= config.get("Frezarite Meteor Enabled", true, "Allow Frezarite Meteors to Fall?");
		kreknoriteEnabled		= config.get("Kreknorite Meteor Enabled", true, "Allow Kreknorite Meteors to Fall?");
		unknownEnabled			= config.get("Unknown Meteor Enabled", true, "Allow Unknown Meteors to Fall?");
		chunkChecks 			= config.get("Chunk Generation Checks", 4, "How many veins of meteorite ore per chunk?");
		oreGenSize  			= config.get("Meteor Ore Gen Size", 6, "How much ore per vein?");
		meteorShieldSound 		= config.get("Meteor Shield Humming Noise Enabled", true, "Allows a humming sound for the Meteor Shield too happen on occasion.");
		cometFallChance			= config.get("Comet Fall Chance", 20, "Ranges from 0 to 100");
		whitelistedDimensions 	= config.get("Whitelisted Dimensions", new int[] {0, 1, 7}, "Dimensions that meteors are allowed to naturally fall in. 0 = Surface, 1 = The End, 7 = Twilight Forest");
		int configTicks 		= config.get("Meteor Fall Deterrence", 25, "For more info, refer here: http://fallingmeteorsmod.wikia.com/wiki/Falling_Meteors#Configuration") * 100;
		int mSpawn = (int)(configTicks * 0.25D);
		int mCrash = (int)(configTicks * 0.75D);
		this.MinTicksUntilMeteorSpawn = ((int)(mSpawn * 0.25D));
		this.RandTicksUntilMeteorSpawn = ((int)(mSpawn * 0.75D));
		this.MinTicksUntilMeteorCrashes = ((int)(mCrash * 0.5D));
		this.RandTicksUntilMeteorCrashes = ((int)(mCrash * 0.5D));
		setClientStartConfig();
	}

	// Values loaded every new world Load and initially when mod is constructed
	public void setClientStartConfig() {
		this.meteorsFallOnlyAtNight = ModConfig.instance.get("Meteors Only Fall at Night", true, "The amount of time left until a meteor falls will only tick down at night.");
		this.allowSummonedMeteorGrief = ModConfig.instance.get("Allow Summoned Meteor Grief", false, "Players can't summon meteors on land protected by other players with a Meteor Shield");
		this.ShieldRadiusMultiplier = ModConfig.instance.get("Shield Radius in Blocks", 64, "The range of the meteor shield is determined by this times the shield's power level.");
		this.MinMeteorSize = ModConfig.instance.get("Minimum Meteor Size", 1, "Minimum Size of a falling meteor. Ranges from 1 to 3.");
		this.MaxMeteorSize = ModConfig.instance.get("Maximum Meteor Size", 3, "Maximum Size of a falling meteor. Ranges from 1 to 3.");
		this.MinMeteorSize = MathHelper.clamp(this.MinMeteorSize, 1, 3);
		this.MaxMeteorSize = MathHelper.clamp(this.MaxMeteorSize, 1, 3);
		if (this.MinMeteorSize > this.MaxMeteorSize)
			this.MinMeteorSize = this.MaxMeteorSize;
		else if (this.MaxMeteorSize < this.MinMeteorSize) {
			this.MaxMeteorSize = this.MinMeteorSize;
		}
		this.MinMeteorSizeForPortal = ModConfig.instance.get("Minimum Meteor Size To Spawn Nether Portal", 2, "When a Kreknorite Meteor falls, if the meteor is this size or bigger, it will generate a nether portal.");
		if (this.MinMeteorSizeForPortal < this.MinMeteorSize)
			this.MinMeteorSizeForPortal = this.MinMeteorSize;
		this.ImpactExplosionMultiplier = ModConfig.instance.get("Meteor Impact Explosion Multiplier", 5.0D, "This times the meteor's size is how big the explosion will be.");
		if (this.ImpactExplosionMultiplier > 20.0) {
			this.ImpactExplosionMultiplier = 20.0;
		} else if (this.ImpactExplosionMultiplier < 0.0) {
			this.ImpactExplosionMultiplier = 0.0;
		}
		this.ImpactSpread = MathHelper.abs(ModConfig.instance.get("Meteor Impact Spread", 4, "This times the meteor size determines how big of an impact the meteor's crater will have to spread ore."));
		this.slipperyBlocksEnabled = ModConfig.instance.get("Slippery Blocks Enabled", true, "Setting to false will disallow the creation of Slippery Blocks with the Freezer.");
	}

	

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent evt) {
		evt.registerServerCommand(new CommandKittyAttack());
		evt.registerServerCommand(new CommandDebugShields());
		evt.registerServerCommand(new CommandDebugMeteors());
		evt.registerServerCommand(new CommandSpawnMeteor());
		evt.registerServerCommand(new CommandSpawnComet());
	}

	private void registerEntities() {
		proxy.registerTileEntities();

		//Register Entities
		int id = 0;
		EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "fallingmeteor"), EntityMeteor.class, "FallingMeteor", id++, this, 64, 8, true);
		EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "aliencreeper"), EntityAlienCreeper.class, "AlienCreeper", id++, this, 80, 3, true, 7864485, 16732697);
		EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "cometkitty"), EntityCometKitty.class, "CometKitty", id++, this, 80, 3, true, 2239283, 884535);
		EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "meteorsummoner"), EntitySummoner.class, "MeteorSummoner", id++, this, 64, 8, true);
		EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "fallingcomet"), EntityComet.class, "FallingComet", id++, this, 64, 8, true);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Enchantment> event) {
		event.getRegistry().register(Magnetization);
		event.getRegistry().register(ColdTouch);
	}
	
	private void loadPlugins() {//TODO 1.12.2
		if (Loader.isModLoaded("Waila")) {
			FMLInterModComms.sendMessage("Waila", "register", "net.meteor.plugin.waila.Waila.register");
		} else {
			log.info("Waila not found. Waila integration disabled.");
		}
		if (Loader.isModLoaded("Baubles")) {
			Baubles.setupBaubleItems();
		} else {
			log.info("Baubles not found. Baubles integration disabled.");
		}
		if (Loader.isModLoaded("Thaumcraft")) {
			Thaumcraft.incorporateThaumcraft();
		} else {
			log.info("Thaumcraft not found. Thaumcraft integration disabled.");
		}
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if ((chunkGenerator instanceof ChunkProviderServer)) {//TODO 1.12.2 verify this is right
			int x = chunkX << 4;
			int z = chunkZ << 4;
			for (int i = 0; i < this.chunkChecks; i++) {
				int randX = x + rand.nextInt(16);
				int randY = rand.nextInt(16) + 6;
				int randZ = z + rand.nextInt(16);
				(new WorldGenMinable(MeteorBlocks.METEOR_ORE.getDefaultState(), this.oreGenSize)).generate(world, rand, new BlockPos(randX, randY, randZ));
			}
			
			for (int i = 0; i < this.chunkChecks; i++) {
				int randX = x + rand.nextInt(16);
				int randY = rand.nextInt(20) + 32;
				int randZ = z + rand.nextInt(16);
				if (world.getBiomeForCoordsBody(new BlockPos(randX, 0, randZ)).getDefaultTemperature() <= 0.15F) {
					(new WorldGenMinable(MeteorBlocks.FREEZARITE_ORE.getDefaultState(), this.oreGenSize)).generate(world, rand, new BlockPos(randX, randY, randZ));
				}
			}
		}
	}
	
	public boolean isDimensionWhitelisted(int dim) {
		for (int whitelistedDimension : whitelistedDimensions) {
			if (dim == whitelistedDimension) {
				return true;
			}
		}
		return false;
	}

	public static void whatSide(Side side, String s)
	{
		if (!loggable) return;
		if (side == Side.SERVER)
			log.info(s + " called on server side");
		else if (side == Side.CLIENT)
			log.info(s + " called on client side");
	}
}