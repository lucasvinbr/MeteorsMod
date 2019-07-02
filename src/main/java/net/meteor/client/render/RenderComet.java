package net.meteor.client.render;

import net.meteor.client.model.ModelMeteor;
import net.meteor.common.entity.EntityComet;
import net.meteor.common.entity.EntitySummoner;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderComet extends Render<EntityComet> {

	public static final Factory FACTORY = new Factory();
	private ModelMeteor modelMeteor;
	private int metID = 0;
	
	public RenderComet(RenderManager renderManager) {
		super(renderManager);
		modelMeteor = new ModelMeteor();
	}
	
	@Override
	public void doRender(EntityComet entity, double x, double y, double z, float entityYaw, float partialTicks) {
		renderMeteor(entity, x, y, z, entityYaw, partialTicks);
	}

	public void renderMeteor(EntityComet entityComet, double x, double y, double z, float entityYaw, float partialTicks) {
		this.metID = entityComet.meteorType.getID();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x, (float)y, (float)z);
		GlStateManager.rotate(entityComet.prevRotationYaw + (entityComet.rotationYaw - entityComet.prevRotationYaw) * partialTicks, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(entityComet.prevRotationPitch + (entityComet.rotationPitch - entityComet.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
		this.bindTexture(RenderMeteor.skins.get(metID));
		modelMeteor.renderWithSize(1, 0.0625F);
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityComet entity) {
		return RenderMeteor.skins.get(metID);
	}

	public static class Factory implements IRenderFactory<EntityComet> {
		@Override
		public Render<? super EntityComet> createRenderFor(RenderManager manager) {
			return new RenderComet(manager);
		}
	}
}
