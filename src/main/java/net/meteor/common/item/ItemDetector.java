package net.meteor.common.item;

import java.util.List;

import net.meteor.common.ClientHandler;
import net.meteor.common.climate.CrashLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemDetector extends ItemMeteorsMod {
	
	public DetectorType type;// 0 = Proximity, 1 = Time, 2 = Crash
	private String dots = ".";
	private long lastDot = 0L;

	public enum DetectorType {
		PROXIMITY,
		TIME,
		CRASH
	}

	public ItemDetector(DetectorType type) {
		super();
		this.type = type;

		this.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter() {

			@SideOnly(Side.CLIENT)
			double rotation;
			@SideOnly(Side.CLIENT)
			double rota;
			@SideOnly(Side.CLIENT)
			long lastUpdateTick;
			@SideOnly(Side.CLIENT)
			@Override
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
			{
				if (entityIn == null && !stack.isOnItemFrame())
				{
					return 0.0F;
				}
				else
				{
					boolean flag = entityIn != null;
					Entity entity = flag ? entityIn : stack.getItemFrame();

					if (worldIn == null)
					{
						worldIn = entity.world;
					}

					double d0;

					if (worldIn.provider.isSurfaceWorld())
					{
						double d1 = flag ? (double)entity.rotationYaw : this.getFrameRotation((EntityItemFrame)entity);
						d1 = MathHelper.positiveModulo(d1 / 360.0D, 1.0D);
						double d2 = this.getAnglePosition(worldIn, entity) / (Math.PI * 2D);
						d0 = 0.5D - (d1 - 0.25D - d2);
					}
					else
					{
						d0 = Math.random();
					}

					if (flag)
					{
						d0 = this.wobble(worldIn, d0);
					}

					return MathHelper.positiveModulo((float)d0, 1.0F);
				}
			}
			@SideOnly(Side.CLIENT)
			private double wobble(World worldIn, double p_185093_2_)
			{
				if (worldIn.getTotalWorldTime() != this.lastUpdateTick)
				{
					this.lastUpdateTick = worldIn.getTotalWorldTime();
					double d0 = p_185093_2_ - this.rotation;
					d0 = MathHelper.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
					this.rota += d0 * 0.1D;
					this.rota *= 0.8D;
					this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
				}

				return this.rotation;
			}
			@SideOnly(Side.CLIENT)
			private double getFrameRotation(EntityItemFrame itemFrame)
			{
				return (double)MathHelper.wrapDegrees(180 + itemFrame.facingDirection.getHorizontalIndex() * 90);
			}
			@SideOnly(Side.CLIENT)
			private double getAnglePosition(World world, Entity entity)
			{
				//TODO point it at the things!
				BlockPos blockpos = null;
				if(type == DetectorType.PROXIMITY) {
					blockpos = ClientHandler.getClosestIncomingMeteor(entity.posX, entity.posZ);
				} else if(type == DetectorType.TIME) {
					blockpos = ClientHandler.nearestTimeLocation;
				} else {//Crash
					CrashLocation cl = ClientHandler.lastCrashLocation;
					if(cl != null)
						blockpos = cl.pos;
				}

				if(blockpos == null) {
					return 0;
				}

				return Math.atan2((double)blockpos.getZ() - entity.posZ, (double)blockpos.getX() - entity.posX);
			}
		});

	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(worldIn == null)
			return;

		long worldTime = worldIn.getTotalWorldTime();
		if (worldTime - lastDot > 10L) {
			lastDot = worldTime;
			dots = dots.length() > 5 ? "." : dots + ".";
		}
		
		if (type == DetectorType.TIME) {
			tooltip.add(TextFormatting.AQUA + I18n.translateToLocal("Detector.time"));
			if (ClientHandler.nearestTimeLocation == null) {
				tooltip.add(I18n.translateToLocal("Detector.scanning") + dots);
			} else {
				tooltip.add(TextFormatting.GREEN + I18n.translateToLocal("Detector.detected"));
			}
		} else if (type == DetectorType.PROXIMITY) {
			tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.translateToLocal("Detector.proximity"));
			if (ClientHandler.getClosestIncomingMeteor(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posZ) == null) {//TODO confirm for 1.12.2
				tooltip.add(I18n.translateToLocal("Detector.scanning") + dots);
			} else {
				tooltip.add(TextFormatting.GREEN + I18n.translateToLocal("Detector.detected"));
			}
		} else {//Crash
			tooltip.add(TextFormatting.RED + I18n.translateToLocal("Detector.crash"));
			if (ClientHandler.lastCrashLocation == null) {
				tooltip.add(I18n.translateToLocal("CrashDetector.noActivity"));
				tooltip.add(I18n.translateToLocal("Detector.scanning") + dots);
			} else {
				if (ClientHandler.lastCrashLocation.inOrbit) {
					tooltip.add(TextFormatting.GREEN + "Orbital Entrance Detected!");
				} else {
					tooltip.add(TextFormatting.GREEN + I18n.translateToLocal("CrashDetector.zoneLocated"));
				}
			}
		}
		
	}

}
