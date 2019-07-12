package net.meteor.client.gui;

import net.meteor.common.FreezerRecipes.RecipeType;
import net.meteor.common.MeteorsMod;
import net.meteor.common.block.container.ContainerFreezingMachine;
import net.meteor.common.tileentity.TileEntityFreezingMachine;
import net.meteor.common.util.Util;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import java.util.ArrayList;
import java.util.List;

public class GuiFreezingMachine extends GuiContainer {
	
	static final ResourceLocation freezingMachineTextures = new ResourceLocation(MeteorsMod.MOD_ID, "textures/gui/container/freezing_machine.png");
	
	private TileEntityFreezingMachine freezer;
	private int fluidAmount = 0;
	private String fluidName;

	public GuiFreezingMachine(InventoryPlayer playerInv, TileEntityFreezingMachine freezer) {
		super(new ContainerFreezingMachine(playerInv, freezer));
		this.freezer = freezer;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiButtonFreezerRecipeMode(0, guiLeft + 149, guiTop + 60, 20, 20, freezer, this));
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(freezingMachineTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        
        // Draw Fluids in Tank
        FluidTankInfo tankInfo = freezer.getTankInfo();
        FluidStack fluidStack = tankInfo.fluid;
        
        if (fluidStack != null && fluidStack.amount > 0) {
        	
        	fluidAmount = fluidStack.amount;
        	Fluid fluid = fluidStack.getFluid();
        	
        	if (fluid != null) {
        		
        		fluidName = fluid.getLocalizedName(fluidStack);
				TextureAtlasSprite fluidTexture = mc.getTextureMapBlocks().getTextureExtry(fluid.getStill().toString());
				int height = (int) (((double)fluidAmount / (double)tankInfo.capacity) * 69);//69?
        		this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				int tankFluidHeight = l + 76 - height;
				//noinspection ConstantConditions - If this happens we have bigger issues
				this.drawTexturedModalRect(k + 13, tankFluidHeight, fluidTexture, 20, height);
        	}
        } else if (fluidAmount > 0) {
        	fluidAmount = 0;
        	fluidName = "";
        }


        this.mc.getTextureManager().bindTexture(freezingMachineTextures);
        //Draw tank lines
        this.drawTexturedModalRect(k + 13, l + 7, 176, 31, 20, 69);
        
        if (this.freezer.isFreezing()) {
        	int i1 = this.freezer.getBurnTimeRemainingScaled(13);
            this.drawTexturedModalRect(k + 73, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
            i1 = this.freezer.getCookProgressScaled(24);
            this.drawTexturedModalRect(k + 96, l + 34, 176, 14, i1 + 1, 16);
        }
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		if (isPointInRegion(13, 7, 20, 69, mouseX, mouseY)) {
			List<String> info = new ArrayList<>();
			if (fluidName != null && !fluidName.isEmpty()) {
				info.add(fluidName);
			}
			info.add(fluidAmount + " / 10000 mB");
			this.drawHoveringText(info, mouseX - guiLeft, mouseY - guiTop);
		}
		
		if (isPointInRegion(149, 60, 20, 20, mouseX, mouseY)) {
			List<String> info = new ArrayList<>();
			RecipeType type = freezer.getRecipeMode();
			if (type == RecipeType.item) {
				info.add(TextFormatting.AQUA + I18n.translateToLocal("info.freezer.itemMode"));
				info.addAll(Util.getFormattedLines("info.freezer.itemMode.desc", TextFormatting.WHITE));
			} else if (type == RecipeType.fluid) {
				info.add(TextFormatting.AQUA + I18n.translateToLocal("info.freezer.fluidMode"));
				info.addAll(Util.getFormattedLines("info.freezer.fluidMode.desc", TextFormatting.WHITE));
			} else if (type == RecipeType.both) {
				info.add(TextFormatting.AQUA + I18n.translateToLocal("info.freezer.bothMode"));
				info.addAll(Util.getFormattedLines("info.freezer.bothMode.desc", TextFormatting.WHITE));
			} else if (type == RecipeType.either) {
				info.add(TextFormatting.AQUA + I18n.translateToLocal("info.freezer.eitherMode"));
				info.addAll(Util.getFormattedLines("info.freezer.eitherMode.desc", TextFormatting.WHITE));
			}
			this.drawHoveringText(info, mouseX - guiLeft, mouseY - guiTop);
		}
		
		String s = I18n.translateToLocal("tile.freezingMachine.name");
		int i = this.fontRenderer.getStringWidth(s);
		this.fontRenderer.drawString(s, 168 - i, 6, 4210752, false);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		freezer.pressButton(button.id);
	}

}
