package net.meteor.client.render;

import net.meteor.common.MeteorItems;
import net.meteor.common.entity.EntitySummoner;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderSummoner extends Render<EntitySummoner>
{

	public static final Factory FACTORY = new Factory();
	private int damage;
	private TextureAtlasSprite icon;

	public RenderSummoner(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
    public void doRender(EntitySummoner par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		if (!par1Entity.isRandom) {
			this.icon = MeteorItems.itemMeteorSummoner.getIconFromDamage(par1Entity.mID + 1);
		} else {
			this.icon = MeteorItems.itemMeteorSummoner.getIconFromDamage(0);
		}
		GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.bindEntityTexture(par1Entity);
		Tessellator tess = Tessellator.getInstance();
		this.renderEntity(tess, this.icon);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
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
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		BufferBuilder buffer = tess.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.normal(0.0F, 1.0F, 0.0F);
		buffer.pos((double)(0.0F - f5), (double)(0.0F - f6), 0.0D).tex((double)f, (double)f3).endVertex();
		buffer.pos((double)(f4 - f5), (double)(0.0F - f6), 0.0D).tex((double)f1, (double)f3).endVertex();
		buffer.pos((double)(f4 - f5), (double)(f4 - f6), 0.0D).tex((double)f1, (double)f2).endVertex();
		buffer.pos((double)(0.0F - f5), (double)(f4 - f6), 0.0D).tex((double)f, (double)f2).endVertex();
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