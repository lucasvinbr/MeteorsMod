package net.meteor.client.block.models;

import com.google.common.collect.ImmutableList;
import net.meteor.common.block.BlockSlippery;
import net.meteor.common.block.BlockSlipperyStairs;
import net.meteor.common.item.ItemBlockSlippery;
import net.meteor.common.tileentity.TileEntitySlippery;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

/**
 * Created by TheGreyGhost on 19/04/2015.
 * This class is used to customise the rendering of the camouflage block, based on the block it is copying.
 */
public class SlipperyBlockBakedModel implements IBakedModel {

    public SlipperyBlockBakedModel(IBakedModel unCamouflagedModel)
    {
        modelWhenNotCamouflaged = unCamouflagedModel;
    }

    // return a list of the quads making up the model.
    // We choose the model based on the IBlockstate provided by the caller.
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        return handleBlockState(state).getQuads(state, side, rand);
    }

    // This method is used to create a suitable IBakedModel based on the IBlockState of the block being rendered.
    // If IBlockState is an instance of IExtendedBlockState, you can use it to pass in any information you want.

    private IBakedModel handleBlockState(@Nullable IBlockState iBlockState)
    {
        IBakedModel retval = modelWhenNotCamouflaged;  // default
        IBlockState UNCAMOUFLAGED_BLOCK = Blocks.AIR.getDefaultState();

        // Extract the block to be copied from the IExtendedBlockState, previously set by Block.getExtendedState()
        // If the block is null, the block is not camouflaged so use the uncamouflaged model.
        if (iBlockState instanceof IExtendedBlockState) {
            IExtendedBlockState iExtendedBlockState = (IExtendedBlockState) iBlockState;
            IBlockState copiedBlockIBlockState = iExtendedBlockState.getValue(BlockSlippery.COPIEDBLOCK);

            if(iBlockState.getBlock() instanceof BlockStairs)
                copiedBlockIBlockState = copiedBlockIBlockState
                        .withProperty(BlockStairs.FACING, iBlockState.getValue(BlockStairs.FACING))
                        .withProperty(BlockStairs.HALF, iBlockState.getValue(BlockStairs.HALF))
                        .withProperty(BlockStairs.SHAPE, iBlockState.getValue(BlockStairs.SHAPE));

            if (copiedBlockIBlockState != UNCAMOUFLAGED_BLOCK) {
                // Retrieve the IBakedModel of the copied block and return it.
                Minecraft mc = Minecraft.getMinecraft();
                BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
                BlockModelShapes blockModelShapes = blockRendererDispatcher.getBlockModelShapes();
                IBakedModel copiedBlockModel = blockModelShapes.getModelForState(copiedBlockIBlockState);
                retval = copiedBlockModel;
            }
        }

        return retval;
    }

    private IBakedModel modelWhenNotCamouflaged;

    // getTexture is used directly when player is inside the block.  The game will crash if you don't use something
    //   meaningful here.
    @Override
    public TextureAtlasSprite getParticleTexture() {
        return modelWhenNotCamouflaged.getParticleTexture();
    }

    // ideally, this should be changed for different blocks being camouflaged, but this is not supported by vanilla
    @Override
    public boolean isAmbientOcclusion()
    {
        return modelWhenNotCamouflaged.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        return modelWhenNotCamouflaged.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return modelWhenNotCamouflaged.isBuiltInRenderer();
    }

    private ItemOverrideList overrides = new ItemOverrideList(ImmutableList.of()) {
        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
            Minecraft mc = Minecraft.getMinecraft();
            BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
            BlockModelShapes blockModelShapes = blockRendererDispatcher.getBlockModelShapes();

            NBTTagCompound tagCompound = stack.getTagCompound();
            if(tagCompound == null || !tagCompound.hasKey(ItemBlockSlippery.FACADE_BLOCK_KEY))
                return originalModel;

            return blockModelShapes.getModelForState(TileEntitySlippery.getBlockFromName(tagCompound.getString(ItemBlockSlippery.FACADE_BLOCK_KEY)).getDefaultState());
        }
    };

    @Override
    public ItemOverrideList getOverrides()
    {
        return overrides;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return modelWhenNotCamouflaged.getItemCameraTransforms();
    }

    /** this method is necessary because Forge has deprecated getItemCameraTransforms(), and modelCore.getItemCameraTransforms()
     *    may not return anything meaningful.  But if the base model doesn't implement IPerspectiveAwareModel then you
     *    need to generate it.
     * @param cameraTransformType
     * @return
     */
    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
//    if (modelWhenNotCamouflaged instanceof IPerspectiveAwareModel) {
        Matrix4f matrix4f = modelWhenNotCamouflaged.handlePerspective(cameraTransformType).getRight();
        return Pair.of(this, matrix4f);
//    } else {
//      // If the parent model isn't an IPerspectiveAware, we'll need to generate the correct matrix ourselves using the
//      //  ItemCameraTransforms.
//
//      ItemCameraTransforms itemCameraTransforms = modelWhenNotCamouflaged.getItemCameraTransforms();
//      ItemTransformVec3f itemTransformVec3f = itemCameraTransforms.getTransform(cameraTransformType);
//      TRSRTransformation tr = new TRSRTransformation(itemTransformVec3f);
//      Matrix4f mat = null;
//      if (tr != null) { // && tr != TRSRTransformation.identity()) {
//        mat = tr.getMatrix();
//      }
//      // The TRSRTransformation for vanilla items have blockCenterToCorner() applied, however handlePerspective
//      //  reverses it back again with blockCornerToCenter().  So we don't need to apply it here.
//
//      return Pair.of(this, mat);
//    }
    }

}