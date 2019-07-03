package net.meteor.client.tileentity;

import net.meteor.common.tileentity.TileEntitySlippery;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class TileEntitySlipperyRenderer extends TileEntitySpecialRenderer<TileEntitySlippery> {

	@Override
	public void render(TileEntitySlippery slippery, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		Block facadeBlock = slippery.getFacadeBlock();
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableBlend();
		if (Minecraft.isAmbientOcclusionEnabled()) {
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
		} else {
			GlStateManager.shadeModel(GL11.GL_FLAT);
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder buffer = tess.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);//TODO 1.12.2
		//TODO 1.12.2 errrrr
		//RenderBlocks renderBlocks = new RenderBlocks(slippery.getWorld());
		//buffer.setTranslation(-slippery.getPos().getX(), -slippery.getPos().getY(), -slippery.getPos().getZ());
		//renderBlocks.renderBlockByRenderType(facadeBlock, slippery.getPos().getX(), slippery.getPos().getY(), slippery.getPos().getZ());
		tess.draw();
		buffer.setTranslation(0, 0, 0);
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.disableBlend();
	}


}
