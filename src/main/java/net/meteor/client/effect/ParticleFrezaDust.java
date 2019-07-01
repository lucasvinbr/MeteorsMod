package net.meteor.client.effect;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class ParticleFrezaDust extends Particle
{
	private float reddustParticleScale;

	public ParticleFrezaDust(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		motionX *= 0.10000000149011612D;
		motionY *= 0.10000000149011612D;
		motionZ *= 0.10000000149011612D;
		particleRed = 0.2F;
		particleGreen = 1.0F;
		particleBlue = 0.8F;
		particleScale *= 0.75F;
		reddustParticleScale = particleScale;
		particleMaxAge = (int)(8D / (Math.random() * 0.80000000000000004D + 0.20000000000000001D));
		canCollide = true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		float f6 = (((float)particleAge + partialTicks) / (float)particleMaxAge) * 32F;
		if(f6 < 0.0F)
		{
			f6 = 0.0F;
		}
		if(f6 > 1.0F)
		{
			f6 = 1.0F;
		}
		particleScale = reddustParticleScale * f6;
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}

	@Override
	public void onUpdate()
	{
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if(particleAge++ >= particleMaxAge)
		{
			this.setExpired();
		}
		setParticleTextureIndex(7 - (particleAge * 8) / particleMaxAge);
		this.move(motionX, motionY, motionZ);
		if(posY == prevPosY)
		{
			motionX *= 1.1D;
			motionZ *= 1.1D;
		}
		motionX *= 0.95999997854232788D;
		motionY *= 0.95999997854232788D;
		motionZ *= 0.95999997854232788D;
		if(onGround)
		{
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
		}
	}

	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory
	{
		@Nullable
		@Override
		public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
			return new ParticleFrezaDust(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		}
	}
}