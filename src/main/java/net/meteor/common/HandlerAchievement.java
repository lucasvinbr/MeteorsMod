package net.meteor.common;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber
public class HandlerAchievement
{

    public static final ResourceLocation materialGather = new ResourceLocation("meteors:main/root");
    public static final ResourceLocation meteorManipulator = new ResourceLocation("meteors:main/true_power");
    public static final ResourceLocation shieldCrafted = new ResourceLocation("meteors:main/were_covered");
    public static final ResourceLocation shieldFullyUpgraded = new ResourceLocation("meteors:main/upgrade_complete");
    public static final ResourceLocation meteorBlocked = new ResourceLocation("meteors:main/dmeteor_destroyed");
    public static final ResourceLocation attractedDrop = new ResourceLocation("meteors:main/magnetized");
    public static final ResourceLocation craftedKreknoSword = new ResourceLocation("meteors:main/flames_of_fury");
    public static final ResourceLocation craftedDetector = new ResourceLocation("meteors:main/well_informed");
    public static final ResourceLocation foundMeteor = new ResourceLocation("meteors:main/meet_a_meteor");
    public static final ResourceLocation craftedMeteorTimer = new ResourceLocation("meteors:main/timely_detection");
    public static final ResourceLocation summonMeteor = new ResourceLocation("meteors:main/drop_the_rock");
    public static final ResourceLocation kittyEvent = new ResourceLocation("meteors:main/foreign_feline");
    public static final ResourceLocation kittyTame = new ResourceLocation("meteors:main/friendly_feline");
    public static final ResourceLocation craftedFreezer = new ResourceLocation("meteors:main/a_cool_machine");
    public static final ResourceLocation freezeWater = new ResourceLocation("meteors:main/chilled_cube");
    public static final ResourceLocation freezeBlocks = new ResourceLocation("meteors:main/slip_and_slide");
    public static final ResourceLocation freezeIce = new ResourceLocation("meteors:main/compact_cold_cube");

    public static final StatBase kittysTamed = new StatBasic("stat.meteorkittystamed", new TextComponentTranslation("stat.meteorkittystamed")).initIndependentStat().registerStat();
    public static final StatBase metsorsBlocked = new StatBasic("stat.meteorsblocked", new TextComponentTranslation("stat.meteorsblocked")).initIndependentStat().registerStat();
    public static final StatBase kittyEventStat = new StatBasic("stat.meteorkittysseen", new TextComponentTranslation("stat.meteorkittysseen")).initIndependentStat().registerStat();
    public static final StatBase meteorsFound = new StatBasic("stat.meteorsfound", new TextComponentTranslation("stat.meteorsfound")).initIndependentStat().registerStat();
    public static final StatBase waterFrozen = new StatBasic("stat.waterfrozen", new TextComponentTranslation("stat.waterfrozen")).initIndependentStat().registerStat();
    public static final StatBase iceFrozen = new StatBasic("stat.icefrozen", new TextComponentTranslation("stat.icefrozen")).initIndependentStat().registerStat();
    public static final StatBase blocksFrozen = new StatBasic("stat.blocksfrozen", new TextComponentTranslation("stat.blocksfrozen")).initIndependentStat().registerStat();



    @SubscribeEvent
	public void notifyPickup(PlayerEvent.ItemPickupEvent event) {
		
		EntityPlayer player = event.player;
		if (HandlerPlayerTick.getMagnetizationLevel(player) > 0) {
			grantAdvancement((EntityPlayerMP) player, attractedDrop);
		}

		ItemStack iStack = event.getStack();
		Item item = iStack.getItem();
		if (player == null) return;
		if (item == MeteorItems.itemMeteorChips || item == MeteorItems.itemFrezaCrystal || item == MeteorItems.itemKreknoChip) {
            grantAdvancement((EntityPlayerMP) player, materialGather);
        } else if (item == MeteorItems.itemRedMeteorGem) {
            grantAdvancement((EntityPlayerMP) player, meteorManipulator);
        }


	}

	public static void grantAdvancementAndStat(EntityPlayerMP playerMP, ResourceLocation advancementResource, StatBase statBase) {
        playerMP.addStat(statBase, 1);
        grantAdvancement(playerMP, advancementResource);
    }

	public static void grantAdvancement(EntityPlayerMP playerMP, ResourceLocation advancementResource) {
        Advancement advancement = playerMP.getServer().getAdvancementManager().getAdvancement(advancementResource);
        AdvancementProgress progress = playerMP.getAdvancements().getProgress(advancement);
        if(progress.isDone())
            return;

        for (String s : progress.getRemaningCriteria())
        {
            playerMP.getAdvancements().grantCriterion(advancement, s);
        }

    }

}