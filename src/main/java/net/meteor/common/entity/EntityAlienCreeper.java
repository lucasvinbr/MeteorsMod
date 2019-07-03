package net.meteor.common.entity;

import net.meteor.common.MeteorItems;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.Item;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityAlienCreeper extends EntityCreeper
{

	private static final DataParameter<Boolean> POWERED = EntityDataManager.<Boolean>createKey(EntityCreeper.class, DataSerializers.BOOLEAN);

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
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(POWERED, true);
	}
	
	@Override
	public boolean getPowered()
    {
        return true;
    }
}