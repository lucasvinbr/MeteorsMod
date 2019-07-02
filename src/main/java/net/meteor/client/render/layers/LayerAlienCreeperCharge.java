package net.meteor.client.render.layers;

import net.meteor.client.model.ModelAlienCreeper;
import net.meteor.client.render.RenderAlienCreeper;
import net.meteor.common.entity.EntityAlienCreeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;


public class LayerAlienCreeperCharge implements LayerRenderer<EntityAlienCreeper> {

    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final RenderAlienCreeper alienCreeperRendered;
    private final ModelAlienCreeper alienCreeperModel = new ModelAlienCreeper(2.0F);

    public LayerAlienCreeperCharge(RenderAlienCreeper alienCreeperRenderedIn) {
        this.alienCreeperRendered = alienCreeperRenderedIn;
    }

    @Override
    public void doRenderLayer(EntityAlienCreeper entityAlienCreeper, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entityAlienCreeper.getPowered())
        {
            boolean flag = entityAlienCreeper.isInvisible();
            GlStateManager.depthMask(!flag);
            this.alienCreeperRendered.bindTexture(LIGHTNING_TEXTURE);
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.loadIdentity();
            float f = (float)entityAlienCreeper.ticksExisted + partialTicks;
            GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.enableBlend();
            GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            this.alienCreeperModel.setModelAttributes(this.alienCreeperRendered.getMainModel());
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            this.alienCreeperModel.render(entityAlienCreeper, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(flag);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

}
