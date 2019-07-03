package net.meteor.plugin.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.meteor.common.MeteorsMod;
import net.meteor.common.block.FreezariteBlock;
import net.meteor.common.block.MeteorBlock;
import net.meteor.common.block.MeteorShieldBlock;
import net.meteor.common.block.MeteorTimerBlock;

public class Waila {
	
	public static void register(IWailaRegistrar reg) {
		
		// Config
		reg.addConfig("Falling Meteors", "meteors.meteorTemp", "Show Meteor Temperature");
		reg.addConfig("Falling Meteors", "meteors.shieldData", "Show Meteor Shield Data");
		reg.addConfig("Falling Meteors", "meteors.timerMode", "Show Timer Mode");
		
		// Registration
		reg.registerBodyProvider(new TimerDataProvider(), MeteorTimerBlock.class);
		reg.registerBodyProvider(new MeteorDataProvider(), MeteorBlock.class);
		reg.registerBodyProvider(new MeteorDataProvider(), FreezariteBlock.class);
		reg.registerBodyProvider(new ShieldDataProvider(), MeteorShieldBlock.class);
		
		MeteorsMod.log.info("Waila mod found. Waila integration enabled.");
		
	}

}
