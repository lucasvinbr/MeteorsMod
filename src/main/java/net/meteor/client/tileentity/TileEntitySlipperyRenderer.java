package net.meteor.client.tileentity;

import net.meteor.common.tileentity.TileEntitySlippery;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

import org.lwjgl.opengl.GL11;

public class TileEntitySlipperyRenderer extends TileEntitySpecialRenderer<TileEntitySlippery> {

	@Override
	public void render(TileEntitySlippery slippery, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		Block facadeBlock = slippery.getFacadeBlock();
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		RenderHelper.disableStandardItemLighting();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		if (Minecraft.isAmbientOcclusionEnabled()) {
			GL11.glShadeModel(GL11.GL_SMOOTH);
		} else {
			GL11.glShadeModel(GL11.GL_FLAT);
		}
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		Tessellator tess = Tessellator.getInstance();
		tess.startDrawingQuads();
		RenderBlocks renderBlocks = new RenderBlocks(slippery.getWorldObj());
		tess.setTranslation(-slippery.xCoord, -slippery.yCoord, -slippery.zCoord);
		renderBlocks.renderBlockByRenderType(facadeBlock, slippery.xCoord, slippery.yCoord, slippery.zCoord);
		tess.draw();
		tess.setTranslation(0, 0, 0);
		GL11.glPopMatrix();
		RenderHelper.enableStandardItemLighting();
		GL11.glDisable(GL11.GL_BLEND);
	}


}
