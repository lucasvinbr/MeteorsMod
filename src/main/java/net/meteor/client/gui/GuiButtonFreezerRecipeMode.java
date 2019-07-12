package net.meteor.client.gui;

import java.util.ArrayList;

import net.meteor.common.MeteorItems;
import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import org.lwjgl.opengl.GL11;

public class GuiButtonFreezerRecipeMode extends GuiButton {

	private TileEntityFreezingMachine freezer;
	private GuiFreezingMachine gui;

	public GuiButtonFreezerRecipeMode(int id, int xPos, int yPos, int width, int height, TileEntityFreezingMachine freezer, GuiFreezingMachine gui) {
		super(id, xPos, yPos, width, height, "");
		this.freezer = freezer;
		this.gui = gui;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if (this.visible)
		{
			mc.getTextureManager().bindTexture(GuiFreezingMachine.freezingMachineTextures);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int k = this.getHoverState(this.hovered) - 1;
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.x, this.y, k * 20, 166, this.width, this.height);
			this.mouseDragged(mc, mouseX, mouseY);

			RenderItem renderitem = mc.getRenderItem();
			mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			int state = freezer.getRecipeMode().getID();

			TextureAtlasSprite waterTexture = mc.getTextureMapBlocks().getTextureExtry(FluidRegistry.WATER.getStill().toString());
			TextureAtlasSprite itemIcon = renderitem.getItemModelMesher().getParticleIcon(MeteorItems.FrozenIronIngot);
			//Nice shading ;)
			RenderHelper.enableGUIStandardItemLighting();

			if (state == 0) {	// Item Only - Frozen Iron
				
				renderitem.renderItemAndEffectIntoGUI(new ItemStack(MeteorItems.FrozenIronIngot), x + 2, y + 2);
			
			} else if (state == 1) { // Fluid only - Water

				this.drawTexturedModalRect(x + 2, y + 2, waterTexture, 16, 16);
				
			} else if (state == 2) { // Both - water and stone (slippery stone)
				
				this.drawTexturedModalRect(x + 2, y + 2, waterTexture, 16, 16);
				renderitem.renderItemAndEffectIntoGUI(new ItemStack(Blocks.STONE), x + 2, y + 2);
				
			} else if (state == 3) { // Either - water and frozen iron
				this.drawTexturedModalRect(x + 2, y + 2, waterTexture, 16, 16);
				this.drawTexturedModalRect(x + 3, y + 3, itemIcon, 14, 14);
			}

		}
	}

}
