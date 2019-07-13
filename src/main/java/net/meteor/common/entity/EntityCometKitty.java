package net.meteor.common.entity;

import net.meteor.common.HandlerAchievement;
import net.meteor.common.MeteorItems;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityCometKitty extends EntityOcelot
{
	public EntityCometKitty(World par1World)
	{
		super(par1World);
		this.isImmuneToFire = true;
	}

	@Override
	protected Item getDropItem()
	{
		return MeteorItems.itemRedMeteorGem;
	}

	@Override
	public EntityOcelot createChild(EntityAgeable par1EntityAgeable)
	{
		EntityOcelot child;
		if (this.getEntityWorld().rand.nextBoolean()) {
			child = new EntityOcelot(this.getEntityWorld());
		} else {
			child = new EntityCometKitty(this.getEntityWorld());
		}

		if (this.isTamed())
		{
			child.setOwnerId(this.getOwnerId());
			child.setTamed(true);
			child.setTameSkin(this.getTameSkin());
		}

		return child;
	}

	@Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0D);
    }
	
	@Override
	public void setOwnerId(UUID uuid) {
		super.setOwnerId(uuid);
		EntityPlayer player = this.getEntityWorld().getPlayerEntityByUUID(uuid);
		if (player != null) {
			HandlerAchievement.grantAdvancementAndStat((EntityPlayerMP) player, HandlerAchievement.kittyTame, HandlerAchievement.kittysTamed);
		}
	}
	
}