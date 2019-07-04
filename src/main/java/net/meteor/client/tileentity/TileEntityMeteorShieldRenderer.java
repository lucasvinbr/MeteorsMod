package net.meteor.client.tileentity;

import net.meteor.client.model.ModelMeteorShield;
import net.meteor.common.MeteorsMod;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityMeteorShieldRenderer extends TileEntitySpecialRenderer<TileEntityMeteorShield>
{
	private static final ResourceLocation shieldTexture = new ResourceLocation(MeteorsMod.MOD_ID, "textures/entities/meteorshield.png");

	private ModelMeteorShield modelShield;

	public TileEntityMeteorShieldRenderer() {
		this.modelShield = new ModelMeteorShield();
	}

	@Override
	public void render(TileEntityMeteorShield shield, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (shield.getWorld().getTileEntity(shield.getPos()) == null) {
			return;
		}

		int level = shield.getPowerLevel();
		if (!shield.getWorld().isAirBlock(shield.getPos().up())) {
			level = 0;
		}
		int meta = shield.getBlockMetadata();

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
		this.bindTexture(shieldTexture);
		GL11.glPushMatrix();
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(meta * -90F, 0.0F, 1.0F, 0.0F);
		this.modelShield.render(level, partialTicks, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, shield.age);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	
}