package net.meteor.common;

import net.minecraft.stats.Achievement;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.AchievementPage;

public class AchPageMeteorsMod extends AchievementPage
{
	public AchPageMeteorsMod(String name, Achievement[] achievements)
	{
		super(name, achievements);
	}

	@Override
    public String getName()
	{
		return I18n.translateToLocal(super.getName());
	}

}