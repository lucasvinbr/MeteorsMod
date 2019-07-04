package net.meteor.client.tileentity;

import java.util.Random;

import net.meteor.client.model.ModelMeteorTimer;
import net.meteor.common.MeteorsMod;
import net.meteor.common.tileentity.TileEntityMeteorTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityMeteorTimerRenderer extends TileEntitySpecialRenderer<TileEntityMeteorTimer> {

	private static final ResourceLocation timerTex = new ResourceLocation(MeteorsMod.MOD_ID, "textures/entities/mettimer.png");

	public ModelMeteorTimer modelMetTimer;

	private Random rand = new Random();

	public TileEntityMeteorTimerRenderer() {
		this.modelMetTimer = new ModelMeteorTimer();
		modelMetTimer.element_meteorite.rotateAngleY = rand.nextFloat() * 360F;
		modelMetTimer.element_frezarite.rotateAngleY = rand.nextFloat() * 360F;
		modelMetTimer.element_kreknorite.rotateAngleY = rand.nextFloat() * 360F;
	}

	@Override
	// float f appears to be partialTickTime so that's what I should use for cos function for the rotation of the elements
	public void render(TileEntityMeteorTimer tileentity, double x, double y, double z, float partialTickTime, int destroyStage, float alpha) {
		renderMeteorTimer(tileentity, x, y, z, partialTickTime, true, true);
	}

	public void renderMeteorTimer(TileEntityMeteorTimer timer, double x, double y, double z, float partialTickTime, boolean bindTex, boolean displayInfo) {
		GL11.glPushMatrix();
		if (bindTex) {
			this.bindTexture(timerTex);
		}
		modelMetTimer.element_meteorite.rotateAngleY += 0.02F;
		modelMetTimer.element_meteorite.rotateAngleY %= 360F;

		modelMetTimer.element_frezarite.rotateAngleY += 0.01F;
		modelMetTimer.element_frezarite.rotateAngleY %= 360F;

		modelMetTimer.element_kreknorite.rotateAngleY += 0.003F;
		modelMetTimer.element_kreknorite.rotateAngleY %= 360F;

		GL11.glTranslatef((float)x, (float)y, (float)z);
		modelMetTimer.renderAll();
		GL11.glPopMatrix();

		if (displayInfo) {
			drawStringAbove(timer, x + 0.5D, y + 0.65D, z + 0.5D);
		}
	}

	private void drawStringAbove(TileEntityMeteorTimer timer, double x, double y, double z)
	{
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		RayTraceResult mop = Minecraft.getMinecraft().objectMouseOver;

		if (Minecraft.isGuiEnabled() && mop != null && timer.getPos().equals(mop.getBlockPos()))
		{
			float f = 1.6F;
			float f1 = 0.016666668F * f;
			double d3 = getDistanceSqToEntity(Minecraft.getMinecraft().player, timer);//TODO 1.12.2 verify
			float f2 = 32F;

			if (d3 < (double)(f2 * f2))
			{
				String s = timer.quickMode ? I18n.translateToLocal("info.meteorTimer.quickMode") : I18n.translateToLocal("info.meteorTimer.powerMode");
				FontRenderer fontrenderer = this.getFontRenderer();
				GlStateManager.pushMatrix();
				GlStateManager.translate((float)x + 0.0F, (float)y + 0.5F, (float)z);
				GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
				GlStateManager.scale(-f1, -f1, f1);
				GlStateManager.disableLighting();
				GlStateManager.depthMask(false);
				GlStateManager.disableDepth();
				GlStateManager.enableBlend();
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				Tessellator tessellator = Tessellator.getInstance();
				byte b0 = 0;
				GlStateManager.disableTexture2D();
				BufferBuilder buffer = tessellator.getBuffer();
				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
				int j = fontrenderer.getStringWidth(s) / 2;
				buffer.pos((double)(-j - 1), (double)(-1 + b0), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
				buffer.pos((double)(-j - 1), (double)(8 + b0), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
				buffer.pos((double)(j + 1), (double)(8 + b0), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
				buffer.pos((double)(j + 1), (double)(-1 + b0), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
				tessellator.draw();
				GlStateManager.enableTexture2D();
				fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, b0, 553648127);
				GlStateManager.enableDepth();
				GlStateManager.depthMask(true);
				fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, b0, -1);
				GlStateManager.enableLighting();
				GlStateManager.disableBlend();
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.popMatrix();
			}
		}
	}

	/**
	 * Returns the squared distance to the entity. Args: entity
	 */
	private double getDistanceSqToEntity(Entity entity, TileEntityMeteorTimer timer)
	{
		double d0 = timer.getPos().getX() + 0.5D - entity.posX;
		double d1 = timer.getPos().getY() + 0.5D - entity.posY;
		double d2 = timer.getPos().getZ() + 0.5D - entity.posZ;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

}
