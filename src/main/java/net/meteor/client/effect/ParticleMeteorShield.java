package net.meteor.client.effect;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEnchantmentTable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class ParticleMeteorShield extends ParticleEnchantmentTable
{
	
    private double field_70568_aq;
    private double field_70567_ar;
    private double field_70566_as;
	
	public ParticleMeteorShield(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		this.field_70568_aq = this.posX = xCoordIn;
        this.field_70567_ar = this.posY = yCoordIn;
        this.field_70566_as = this.posZ = zCoordIn;
		if (this.rand.nextInt(8) != 0)
			setParticleTextureIndex((int)(Math.random() * 6.0D) + (int)(Math.random() * 5.0D) * 16);
		else
			setParticleTextureIndex((int)(Math.random() * 8.0D + 6.0D));

		//TODO register meteor sprite sheet with TextureMap.registerSprite() during TextureStitchEvent.Pre
	}

	public ParticleMeteorShield(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int meteorID)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		this.field_70568_aq = this.posX = xCoordIn;
        this.field_70567_ar = this.posY = yCoordIn;
        this.field_70566_as = this.posZ = zCoordIn;
		if (this.rand.nextInt(8) != 0)
			setParticleTextureIndex((int)(Math.random() * 6.0D) + meteorID * 16);
		else
			setParticleTextureIndex((int)(Math.random() * 8.0D + 6.0D));
	}

/*	@Override
/	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		tessellator1.startDrawingQuads();
		tessellator1.setBrightness(getBrightnessForRender(partialTicks));
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("meteors", "textures/particles/particles.png"));
		super.renderParticle(tessellator1, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
		tessellator1.draw();
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("textures/particle/particles.png"));
	}
*/
	@Override
	public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float f = (float)this.particleAge / (float)this.particleMaxAge;
        f = 1.0F - f;
        float f1 = 1.0F - f;
        f1 *= f1;
        f1 *= f1;
        this.posX = this.field_70568_aq + this.motionX * (double)f;
        this.posY = this.field_70567_ar + this.motionY * (double)f - (double)(f1 * 1.2F);
        this.posZ = this.field_70566_as + this.motionZ * (double)f;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Nullable
        @Override
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
            return new ParticleMeteorShield(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }

}