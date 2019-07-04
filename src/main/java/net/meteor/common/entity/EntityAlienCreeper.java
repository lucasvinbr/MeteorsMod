package net.meteor.common.entity;

import net.meteor.common.MeteorItems;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityAlienCreeper extends EntityCreeper
{
	public EntityAlienCreeper(World world)
	{
		super(world);
		this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityCometKitty.class, 6.0F, 0.25F, 0.3F));
		isImmuneToFire = true;
	}

	@Override
	protected Item getDropItem()
	{
		if (this.getEntityWorld().rand.nextBoolean()) {
			return MeteorItems.itemRedMeteorGem;
		}
		return MeteorItems.itemMeteorChips;
	}
	
	@Override
	public boolean getPowered()
    {
        return true;
    }
}