package net.meteor.client.render;

import net.meteor.client.model.ModelCometKitty;
import net.meteor.common.entity.EntityAlienCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderCometKitty extends RenderOcelot {

	public static final Factory FACTORY = new Factory();
	private static final ResourceLocation skin = new ResourceLocation("meteors", "textures/entities/cometKitty.png");

	public RenderCometKitty(RenderManager renderManager) {
		super(renderManager);
		mainModel = new ModelCometKitty();
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityOcelot par1Entity)
    {
        return skin;
    }

	public static class Factory implements IRenderFactory<EntityOcelot> {
		@Override
		public Render<? super EntityOcelot> createRenderFor(RenderManager manager) {
			return new RenderCometKitty(manager);
		}
	}
}
