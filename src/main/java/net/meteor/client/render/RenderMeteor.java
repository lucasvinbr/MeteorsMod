package net.meteor.client.render;

import java.util.HashMap;

import net.meteor.client.model.ModelMeteor;
import net.meteor.common.MeteorsMod;
import net.meteor.common.entity.EntityMeteor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderMeteor extends Render<EntityMeteor> {

	private ModelMeteor modelMeteor;
	private int metID = 0;
	
	public static final HashMap<Integer, ResourceLocation> skins = new HashMap<>();
	
	static {
		skins.put(0, new ResourceLocation(MeteorsMod.MOD_ID, "textures/entities/fallingMeteor.png"));
		skins.put(1, new ResourceLocation(MeteorsMod.MOD_ID, "textures/entities/frezaMeteor.png"));
		skins.put(2, new ResourceLocation(MeteorsMod.MOD_ID, "textures/entities/kreknoMeteor.png"));
		skins.put(3, new ResourceLocation(MeteorsMod.MOD_ID, "textures/entities/unknownMeteor.png"));
		skins.put(4, new ResourceLocation(MeteorsMod.MOD_ID, "textures/entities/kitty.png"));
	}

	public RenderMeteor() {
		modelMeteor = new ModelMeteor();
	}

	@Override
	public void doRender(EntityMeteor entity, double d, double d1, double d2, float f, float f1) {
		renderMeteor(entity, d, d1, d2, f, f1);
	}

	public void renderMeteor(EntityMeteor entityMeteor, double d, double d1, double d2, float f, float f1) {
		this.metID = entityMeteor.meteorType.getID();
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glRotatef(entityMeteor.prevRotationYaw + (entityMeteor.rotationYaw - entityMeteor.prevRotationYaw) * f1, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(entityMeteor.prevRotationPitch + (entityMeteor.rotationPitch - entityMeteor.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);
		this.bindTexture(skins.get(metID));
		float f2 = 1.0F * (float)entityMeteor.size;
		GL11.glScalef(f2, f2, f2);
		modelMeteor.renderWithSize(entityMeteor.size, 0.0625F);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMeteor entity) {
		return skins.get(metID);
	}

}