package net.meteor.common.climate;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;

public class HandlerMeteorTick
{
	private HashMap<Integer, ClimateUpdater> climateUpdaters = new HashMap<>();

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		int dim = event.world.provider.getDimension();
		if (climateUpdaters.containsKey(dim)) {
			climateUpdaters.get(dim).onWorldTick(event);
		}
	}
	
	public void registerUpdater(int dimension, ClimateUpdater updater) {
		climateUpdaters.put(dimension, updater);
	}

}