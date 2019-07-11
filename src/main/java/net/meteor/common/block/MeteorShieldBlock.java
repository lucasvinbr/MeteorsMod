package net.meteor.common.block;

import net.meteor.common.ClientHandler;
import net.meteor.common.MeteorsMod;
import net.meteor.common.tileentity.TileEntityMeteorShield;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class MeteorShieldBlock extends DirectionalContainerBlock {

    public MeteorShieldBlock() {
        this.setLightOpacity(0);
        this.setHardness(2.5F);
        this.setSoundType(SoundType.METAL);
        this.setLightLevel(0.5F);
//        this.setBlockBounds(0.0625F, 0.375F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity shield = world.getTileEntity(pos);

        if (!world.isRemote && shield instanceof TileEntityMeteorShield) {
            // TODO - make this more functional
            MeteorsMod.proxy.metHandlers.get(world.provider.getDimension()).getShieldManager().meteorShields.remove(shield);
            MeteorsMod.log.info("METEOR SHIELD SHOULD BE REMOVED");

            // TODO - finalize sound events
            final SoundEvent powerdown = new SoundEvent(new ResourceLocation("meteors:shield.powerdown"));
            world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, powerdown, SoundCategory.BLOCKS, 1.0F, 1.0F);

        }
        if (shield instanceof IInventory) {
            InventoryHelper.dropInventoryItems(world, pos, (IInventory) shield);
            world.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityMeteorShield();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if ((placer instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) placer;
            if (!worldIn.isRemote) {
                player.sendMessage(ClientHandler.createMessage(I18n.translateToLocal("MeteorShield.charging"), TextFormatting.YELLOW));
            }
            TileEntityMeteorShield shield = (TileEntityMeteorShield) worldIn.getTileEntity(pos);
            if (shield != null) {
                shield.owner = player.getName();
                // TODO - sound events
                final SoundEvent sound = new SoundEvent(new ResourceLocation("meteors:shield.humm"));
                worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), sound, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
            }
        }
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, REDSTONE wire, etc to this block.
     */
    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

}