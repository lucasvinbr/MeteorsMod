package net.meteor.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.meteor.common.block.BlockSlippery;
import net.meteor.common.block.BlockSlipperyStairs;
import net.meteor.common.item.ItemBlockSlippery;
import net.meteor.common.tileentity.TileEntitySlippery;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FreezerRecipes {
	
	private static FreezerRecipes theInstance = new FreezerRecipes();
	
	public enum RecipeType {
		item(0), fluid(1), both(2), either(3);
		
		private int id; // Use RecipeType.values() to get a RecipeType using this id
		
		RecipeType(int id) {
			this.id = id;
		}
		
		public int getID() { return id; }
	}
	
	public class FreezerRecipe {
		
		protected Fluid fluid;
		protected int fluidAmountNeeded;
		protected ItemStack itemNeeded;
		protected ItemStack result;
		protected RecipeType type = RecipeType.both;
		
		public FreezerRecipe(ItemStack item, Fluid fluid, int fluidAmount, ItemStack result) {
			this.itemNeeded = item;
			this.fluid = fluid;
			this.fluidAmountNeeded = fluidAmount;
			this.result = result;
		}
		
		public FreezerRecipe(ItemStack item, ItemStack result) {
			this(item, null, 0, result);
			setRecipeType(RecipeType.item);
		}
		
		public FreezerRecipe(Fluid fluid, int fluidAmount, ItemStack result) {
			this(null, fluid, fluidAmount, result);
			setRecipeType(RecipeType.fluid);
		}
		
		public FreezerRecipe setRecipeType(RecipeType type) {
			this.type = type;
			return this;
		}
		
		public RecipeType getRecipeType() { return type; }
		public Fluid getFluid() { return fluid; }
		public int getFluidAmount() { return fluidAmountNeeded; }
		public ItemStack getItemStack() { return itemNeeded; }
		public ItemStack getResult(ItemStack neededItem) { return result; }
		
		public boolean requiresFluid() { return fluid != null; }
		public boolean requiresItem() { return itemNeeded != null; }
		
		public boolean hasRequiredMaterials(ItemStack item, Fluid fluid, int fluidAmount) {
			if (fluidAmount >= fluidAmountNeeded) {
				if (requiresFluid() && this.fluid == fluid) {
					return requiresItem() ? (item != null ? this.itemNeeded.isItemEqual(item) : false) : true;
				} else if (requiresItem() && item != null) {
					return this.itemNeeded.isItemEqual(item);
				}
			}
			return false;
		}
		
	}
	
	public class SlipperyRecipe extends FreezerRecipe {

		public SlipperyRecipe() {
			super(FluidRegistry.WATER, 250, null);
			setRecipeType(RecipeType.both);
		}
		
		@Override
		public boolean requiresItem() { return true; }
		
		@Override
		public ItemStack getResult(ItemStack neededItem) {
			
			ItemBlock itemBlock = (ItemBlock) neededItem.getItem();
			Block block = itemBlock.getBlock();
			ItemStack res = new ItemStack(MeteorBlocks.blockSlippery, 1, neededItem.getItemDamage());
			
			if (block instanceof BlockSlippery) {
				
				if (block.slipperiness == 0.98F) {
					res = new ItemStack(MeteorBlocks.blockSlipperyTwo, 1, neededItem.getItemDamage());
				} else if (block.slipperiness == 1.03F) {
					res = new ItemStack(MeteorBlocks.blockSlipperyThree, 1, neededItem.getItemDamage());
				} else if (block.slipperiness == 1.07F) {
					res = new ItemStack(MeteorBlocks.blockSlipperyFour, 1, neededItem.getItemDamage());
				}
				
				NBTTagCompound nbt = res.hasTagCompound() ? res.getTagCompound() : new NBTTagCompound();
				nbt.setString(ItemBlockSlippery.FACADE_BLOCK_KEY, neededItem.getTagCompound().getString(ItemBlockSlippery.FACADE_BLOCK_KEY));
				res.setTagCompound(nbt);
				return res;
				
			} else if (block instanceof BlockSlipperyStairs) {
				
				if (block.slipperiness == 0.98F) {
					res = new ItemStack(MeteorBlocks.blockSlipperyStairsTwo, 1, neededItem.getItemDamage());
				} else if (block.slipperiness == 1.03F) {
					res = new ItemStack(MeteorBlocks.blockSlipperyStairsThree, 1, neededItem.getItemDamage());
				} else if (block.slipperiness == 1.07F) {
					res = new ItemStack(MeteorBlocks.blockSlipperyStairsFour, 1, neededItem.getItemDamage());
				}
				
				NBTTagCompound nbt = res.hasTagCompound() ? res.getTagCompound() : new NBTTagCompound();
				nbt.setString(ItemBlockSlippery.FACADE_BLOCK_KEY, neededItem.getTagCompound().getString(ItemBlockSlippery.FACADE_BLOCK_KEY));
				res.setTagCompound(nbt);
				return res;
				
			} /*else if (block.getRenderType() == 10) {//TODO 1.12.2
				
				res = new ItemStack(MeteorBlocks.blockSlipperyStairs, 1, neededItem.getItemDamage());
				
			}*/
			
			NBTTagCompound nbt = res.hasTagCompound() ? res.getTagCompound() : new NBTTagCompound();
			nbt.setString(ItemBlockSlippery.FACADE_BLOCK_KEY, TileEntitySlippery.getNameFromBlock(block).toString());
			res.setTagCompound(nbt);
			return res;
		}
		
		@Override
		public boolean hasRequiredMaterials(ItemStack item, Fluid fluid, int fluidAmount) {
			if (fluidAmount >= fluidAmountNeeded && this.fluid == fluid) {
				return isFreezableItem(item);
			}
			return false;
		}
		
	}
	
	private List<FreezerRecipe> recipes = new ArrayList<>();
	private Set<Item> neededItems = new HashSet<>();  		// Used for shift-clicking items into top slot
	
	public static FreezerRecipes instance() {
		return theInstance;
	}
	
	private FreezerRecipes() {
		addRecipe(new ItemStack(Blocks.ICE, 1), FluidRegistry.WATER, 1000, new ItemStack(Blocks.PACKED_ICE,  1));
		addRecipe(Items.IRON_INGOT, new ItemStack(MeteorItems.FrozenIronIngot, 1));
		addRecipe(new SlipperyRecipe());
		addRecipe(FluidRegistry.WATER, 1000, new ItemStack(Blocks.ICE, 1));
		addRecipe(FluidRegistry.LAVA, 1000, new ItemStack(Blocks.OBSIDIAN, 1));
	}
	
	public void addRecipe(Block block, ItemStack result) {
        this.addRecipe(Item.getItemFromBlock(block), result);
    }

    public void addRecipe(Item item, ItemStack result) {
        this.addRecipe(new ItemStack(item, 1), result);
    }
	
	public void addRecipe(ItemStack need, ItemStack result) {
		this.recipes.add(new FreezerRecipe(need, result));
		this.neededItems.add(need.getItem());
	}
	
	public void addRecipe(Fluid fluid, int fluidAmount, ItemStack result) {
		this.recipes.add(new FreezerRecipe(fluid, fluidAmount, result));
	}
	
	public void addRecipe(ItemStack need, Fluid fluid, int fluidAmount, ItemStack result) {
		this.recipes.add(new FreezerRecipe(need, fluid, fluidAmount, result));
		this.neededItems.add(need.getItem());
	}
	
	public void addRecipe(FreezerRecipe recipe) {
		this.recipes.add(recipe);
		if (recipe.requiresItem() && !(recipe instanceof SlipperyRecipe)) {
			this.neededItems.add(recipe.getItemStack().getItem());
		}
	}
	
	private boolean canUseRecipe(FreezerRecipe recipe, RecipeType typeAvailable) {
		if (typeAvailable != RecipeType.either) {
			return recipe.getRecipeType() == typeAvailable;
		}
		return true;
	}
	
	public FreezerRecipe getFreezingResult(ItemStack item, FluidStack fluidStack, RecipeType typeAvailable) {

		for (FreezerRecipe recipe : recipes) {
			if (canUseRecipe(recipe, typeAvailable)) {
				if (fluidStack != null) {
					if (recipe.hasRequiredMaterials(item, fluidStack.getFluid(), fluidStack.amount)) {
						return recipe;
					}
				} else {
					if (recipe.hasRequiredMaterials(item, null, 0)) {
						return recipe;
					}
				}
			}
		}
		return null;
	}
	
	public boolean isRequiredItem(ItemStack item) {
		return item != null && this.neededItems.contains(item.getItem());
	}
	
	public static boolean isFreezableItem(ItemStack item) {
		if (MeteorsMod.instance.slipperyBlocksEnabled && item != null && item.getItem() instanceof ItemBlock) {
			ItemBlock itemBlock = (ItemBlock) item.getItem();
			Block block = itemBlock.getBlock();
			return BlockSlippery.canBeSlippery(block);
		}
		return false;
	}

}
