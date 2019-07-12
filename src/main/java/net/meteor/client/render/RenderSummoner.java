package net.meteor.client.render;

import net.meteor.common.EnumMeteor;
import net.meteor.common.MeteorItems;
import net.meteor.common.entity.EntitySummoner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSummoner extends Render<EntitySummoner>
{

	public static final Factory FACTORY = new Factory();

	@SuppressWarnings("WeakerAccess")
	public RenderSummoner(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
    public void doRender(EntitySummoner par1Entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		RenderItem renderitem = Minecraft.getMinecraft().getRenderItem();

		TextureAtlasSprite icon;
		if (!par1Entity.isRandom) {
			icon = renderitem.getItemModelMesher().getParticleIcon(EnumMeteor.getItemSummonerForMeteorType(par1Entity.getMeteorType()));
		} else {
			icon = renderitem.getItemModelMesher().getParticleIcon(MeteorItems.itemMeteorSummonerRandom);
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x, (float)y, (float)z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
        this.bindEntityTexture(par1Entity);
		Tessellator tess = Tessellator.getInstance();
		this.renderEntity(tess, icon);
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	private void renderEntity(Tessellator tess, TextureAtlasSprite sprite)
    {
		float f = sprite.getMinU();
        float f1 = sprite.getMaxU();
        float f2 = sprite.getMinV();
        float f3 = sprite.getMaxV();
        float f4 = 1.0F;
        float f5 = 0.5F;
        float f6 = 0.25F;
		GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		BufferBuilder buffer = tess.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
		buffer.pos((double)(0.0F - f5), (double)(0.0F - f6), 0.0D).tex((double)f, (double)f3).normal(0.0F, 1.0F, 0.0F).endVertex();
		buffer.pos((double)(f4 - f5), (double)(0.0F - f6), 0.0D).tex((double)f1, (double)f3).normal(0.0F, 1.0F, 0.0F).endVertex();
		buffer.pos((double)(f4 - f5), (double)(f4 - f6), 0.0D).tex((double)f1, (double)f2).normal(0.0F, 1.0F, 0.0F).endVertex();
		buffer.pos((double)(0.0F - f5), (double)(f4 - f6), 0.0D).tex((double)f, (double)f2).normal(0.0F, 1.0F, 0.0F).endVertex();
        tess.draw();
    }

	@Override
	protected ResourceLocation getEntityTexture(EntitySummoner entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

	public static class Factory implements IRenderFactory<EntitySummoner> {
		@Override
		public Render<? super EntitySummoner> createRenderFor(RenderManager manager) {
			return new RenderSummoner(manager);
		}
	}

}