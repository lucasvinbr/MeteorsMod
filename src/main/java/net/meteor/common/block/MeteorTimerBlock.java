package net.meteor.common.block;

import net.meteor.common.tileentity.TileEntityMeteorTimer;
import net.meteor.common.util.MeteorConstants;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class MeteorTimerBlock extends BlockContainerMeteorsMod {

	public static final PropertyInteger POWER = PropertyInteger.create("power", 0, MeteorConstants.MeteorTimer.MAX_TIMER_POWER);
	public MeteorTimerBlock() {
		super(Material.REDSTONE_LIGHT);
		this.setHardness(0.0F);
		this.setSoundType(SoundType.WOOD);
		this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, 0));
		// TODO - figure out this bit, not sure how it's supposed to work
//		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return blockState.getValue(POWER);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing,
		float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntity tEntity = world.getTileEntity(pos);
			if (!(tEntity instanceof TileEntityMeteorTimer)) {
				return true;
			}
			// Basically, if it's being activated, we want to flip the tile to have inverse quick edit mode
			final TileEntityMeteorTimer timer = (TileEntityMeteorTimer) tEntity;
			timer.quickMode = !timer.quickMode;
			timer.markDirty(); // Let the client know about the tile entity being updated
			if (timer.quickMode) {
				player.sendMessage(new TextComponentString(I18n.translateToLocal("MeteorTimer.modeChange.two")));
			} else {
				player.sendMessage(new TextComponentString(I18n.translateToLocal("MeteorTimer.modeChange.one")));
			}
		}

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityMeteorTimer();
	}

}
