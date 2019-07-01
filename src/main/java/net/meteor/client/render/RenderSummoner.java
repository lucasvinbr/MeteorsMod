package net.meteor.client.render;

import net.meteor.common.MeteorItems;
import net.meteor.common.entity.EntitySummoner;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderSummoner extends Render<EntitySummoner>
{
	
	private int damage;
	private IIcon icon;

	public RenderSummoner() {}

	@Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		EntitySummoner summoner = (EntitySummoner)par1Entity;
		if (!summoner.isRandom)
			this.icon = MeteorItems.itemMeteorSummoner.getIconFromDamage(summoner.mID + 1);
		else {
			this.icon = MeteorItems.itemMeteorSummoner.getIconFromDamage(0);
		}
		GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.bindEntityTexture(par1Entity);
		Tessellator var10 = Tessellator.instance;
		this.func_77026_a(var10, this.icon);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
	}

	//TODO why is this even here?
	//public net.minecraft.client.renderer.entity.RenderSnowball func_77026_a(Lnet/minecraft/client/renderer/Tessellator;Lnet/minecraft/util/IIcon;)V #renderEntity
	/*private void func_77026_a(Tessellator par1Tessellator, IIcon par2Icon)
    {
		float f = par2Icon.getMinU();
        float f1 = par2Icon.getMaxU();
        float f2 = par2Icon.getMinV();
        float f3 = par2Icon.getMaxV();
        float f4 = 1.0F;
        float f5 = 0.5F;
        float f6 = 0.25F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, 1.0F, 0.0F);
        par1Tessellator.addVertexWithUV((double)(0.0F - f5), (double)(0.0F - f6), 0.0D, (double)f, (double)f3);
        par1Tessellator.addVertexWithUV((double)(f4 - f5), (double)(0.0F - f6), 0.0D, (double)f1, (double)f3);
        par1Tessellator.addVertexWithUV((double)(f4 - f5), (double)(f4 - f6), 0.0D, (double)f1, (double)f2);
        par1Tessellator.addVertexWithUV((double)(0.0F - f5), (double)(f4 - f6), 0.0D, (double)f, (double)f2);
        par1Tessellator.draw();
    }*/

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TextureMap.locationItemsTexture;
	}
}