package futuristicoffensiveanddefenseive.theneonfish.fod.utils;

import futuristicoffensiveanddefenseive.theneonfish.fod.MainFOD;
import futuristicoffensiveanddefenseive.theneonfish.fod.base.IActiveState;
import futuristicoffensiveanddefenseive.theneonfish.fod.intergration.FODHooks;
import ic2.api.energy.EnergyNet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mekanism.api.Chunk3D;
import mekanism.api.Coord4D;
import mekanism.api.EnumColor;
import mekanism.api.IMekWrench;
import mekanism.api.MekanismConfig.client;
import mekanism.api.MekanismConfig.general;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.transmitters.TransmissionType;
import mekanism.api.util.UnitDisplayUtils;
import mekanism.api.util.UnitDisplayUtils.ElectricUnit;
import mekanism.api.util.UnitDisplayUtils.TemperatureUnit;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;

/**
 * Utilities used by Mekanism. All miscellaneous methods are located here.
 * @author AidanBrady
 *
 */
public final class FODUtils
{
	public static final ForgeDirection[] SIDE_DIRS = new ForgeDirection[] {ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST};

	public static final Map<String, Class<?>> classesFound = new HashMap<String, Class<?>>();



	
	/**
	 * Checks if a block is directly getting powered by any of its neighbors without loading any chunks.
	 * @param world - the world to perform the check in
	 * @param coord - the Coord4D of the block to check
	 * @return if the block is directly getting powered
	 */
	public static boolean isDirectlyGettingPowered(World world, Coord4D coord)
	{
		for(ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
		{
			Coord4D sideCoord = coord.getFromSide(side);
			
			if(sideCoord.exists(world))
			{
				if(world.isBlockProvidingPowerTo(coord.xCoord, coord.yCoord, coord.zCoord, side.ordinal()) > 0)
				{
					return true;
				}
			}
		}
		
		return false;
	}

	public static String localize(String s)
	{
		return StatCollector.translateToLocal(s);
	}
	
	
	
	public static void notifyLoadedNeighborsOfTileChange(World world, Coord4D coord)
	{
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			Coord4D offset = coord.getFromSide(dir);

			if(offset.exists(world))
			{
				Block block1 = offset.getBlock(world);
				block1.onNeighborChange(world, offset.xCoord, offset.yCoord, offset.zCoord, coord.xCoord, coord.yCoord, coord.zCoord);
				
				if(block1.isNormalCube(world, offset.xCoord, offset.yCoord, offset.zCoord))
				{
					offset = offset.getFromSide(dir);
					
					if(offset.exists(world))
					{
						block1 = offset.getBlock(world);

						if(block1.getWeakChanges(world, offset.xCoord, offset.yCoord, offset.zCoord))
						{
							block1.onNeighborChange(world, offset.xCoord, offset.yCoord, offset.zCoord, coord.xCoord, coord.yCoord, coord.zCoord);
						}
					}
				}
			}
		}
	}

	public static void updateBlock(World world, int x, int y, int z)
	{
		if(!(world.getTileEntity(x, y, z) instanceof IActiveState) || ((IActiveState)world.getTileEntity(x, y, z)).renderUpdate())
		{
			world.func_147479_m(x, y, z);
		}

		if(!(world.getTileEntity(x, y, z) instanceof IActiveState) || ((IActiveState)world.getTileEntity(x, y, z)).lightUpdate() && client.machineEffects)
		{
			updateAllLightTypes(world, x, y, z);
		}
	}



	/**
	 * Updates a block's light value and marks it for a render update.
	 * @param world - world the block is in
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 */

	
	/**
	 * Updates all light types at the given coordinates.
	 * @param world - the world to perform the lighting update in
	 * @param x - x coordinate of the block to update
	 * @param y - y coordinate of the block to update
	 * @param z - z coordinate of the block to update
	 */
	public static void updateAllLightTypes(World world, int x, int y, int z)
	{
		world.updateLightByType(EnumSkyBlock.Block, x, y, z);
		world.updateLightByType(EnumSkyBlock.Sky, x, y, z);
	}

	/**
	 * Whether or not a certain block is considered a fluid.
	 * @param world - world the block is in
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param z - z coordinate
	 * @return if the block is a fluid
	 */
	public static boolean isFluid(World world, int x, int y, int z)
	{
		return getFluid(world, x, y, z, false) != null;
	}

	/**
	 * Gets a fluid from a certain location.
	 * @param world - world the block is in
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param z - z coordinate
	 * @return the fluid at the certain location, null if it doesn't exist
	 */
	public static FluidStack getFluid(World world, int x, int y, int z, boolean filter)
	{
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);

		if(block == null)
		{
			return null;
		}

		if((block == Blocks.water || block == Blocks.flowing_water) && meta == 0)
		{
			if(!filter)
			{
				return new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
			}
			else {
				return new FluidStack(FluidRegistry.getFluid("heavywater"), 10);
			}
		}
		else if((block == Blocks.lava || block == Blocks.flowing_lava) && meta == 0)
		{
			return new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME);
		}
		else if(block instanceof IFluidBlock)
		{
			IFluidBlock fluid = (IFluidBlock)block;

			if(meta == 0)
			{
				return fluid.drain(world, x, y, z, false);
			}
		}

		return null;
	}

	/**
	 * Gets the fluid ID at a certain location, 0 if there isn't one
	 * @param world - world the block is in
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param z - z coordinate
	 * @return fluid ID
	 */
	public static int getFluidId(World world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);

		if(block == null)
		{
			return 0;
		}

		if(block == Blocks.water || block == Blocks.flowing_water)
		{
			return FluidRegistry.WATER.getID();
		}
		else if(block == Blocks.lava || block == Blocks.flowing_lava)
		{
			return FluidRegistry.LAVA.getID();
		}

		for(Fluid fluid : FluidRegistry.getRegisteredFluids().values())
		{
			if(fluid.getBlock() == block)
			{
				return fluid.getID();
			}
		}

		return 0;
	}
	
	public static boolean useIC2()
	{
		return FODHooks.IC2Loaded && EnergyNet.instance != null && !general.blacklistIC2;
	}
	

	/**
	 * Whether or not a block is a dead fluid.
	 * @param world - world the block is in
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param z - z coordinate
	 * @return if the block is a dead fluid
	 */
	public static boolean isDeadFluid(World world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);

		if(block == null || meta == 0)
		{
			return false;
		}

		if((block == Blocks.water || block == Blocks.flowing_water))
		{
			return true;
		}
		else if((block == Blocks.lava || block == Blocks.flowing_lava))
		{
			return true;
		}
		else if(block instanceof IFluidBlock)
		{
			return true;
		}

		return false;
	}
	
	/**
	 * Gets the flowing block type from a Forge-based fluid. Incorporates the MC system of fliuds as well.
	 * @param fluid - the fluid type
	 * @return the block corresponding to the given fluid
	 */
	public static Block getFlowingBlock(Fluid fluid)
	{
		if(fluid == null)
		{
			return null;
		}
		else if(fluid == FluidRegistry.WATER)
		{
			return Blocks.flowing_water;
		}
		else if(fluid == FluidRegistry.LAVA)
		{
			return Blocks.flowing_lava;
		}
		else {
			return fluid.getBlock();
		}
	}



	/**
	 * Retrieves a private value from a defined class and field.
	 * @param obj - the Object to retrieve the value from, null if static
	 * @param c - Class to retrieve field value from
	 * @param fields - possible names of field to iterate through
	 * @return value as an Object, cast as necessary
	 */
	public static Object getPrivateValue(Object obj, Class c, String[] fields)
	{
		for(String field : fields)
		{
			try {
				Field f = c.getDeclaredField(field);
				f.setAccessible(true);
				return f.get(obj);
			} catch(Exception e) {
				continue;
			}
		}

		return null;
	}

	/**
	 * Sets a private value from a defined class and field to a new value.
	 * @param obj - the Object to perform the operation on, null if static
	 * @param value - value to set the field to
	 * @param c - Class the operation will be performed on
	 * @param fields - possible names of field to iterate through
	 */
	public static void setPrivateValue(Object obj, Object value, Class c, String[] fields)
	{
		for(String field : fields)
		{
			try {
				Field f = c.getDeclaredField(field);
				f.setAccessible(true);
				f.set(obj, value);
			} catch(Exception e) {
				continue;
			}
		}
	}

	/**
	 * Retrieves a private method from a class, sets it as accessible, and returns it.
	 * @param c - Class the method is located in
	 * @param methods - possible names of the method to iterate through
	 * @param params - the Types inserted as parameters into the method
	 * @return private method
	 */
	public static Method getPrivateMethod(Class c, String[] methods, Class... params)
	{
		for(String method : methods)
		{
			try {
				Method m = c.getDeclaredMethod(method, params);
				m.setAccessible(true);
				return m;
			} catch(Exception e) {
				continue;
			}
		}

		return null;
	}

	/**
	 * Gets a ResourceLocation with a defined resource type and name.
	 * @param type - type of resource to retrieve
	 * @param name - simple name of file to retrieve as a ResourceLocation
	 * @return the corresponding ResourceLocation
	 */
	public static ResourceLocation getResource(ResourceType type, String name)
	{
		return new ResourceLocation("mekanism", type.getPrefix() + name);
	}

	/**
	 * Removes all recipes that are used to create the defined ItemStacks.
	 * @param itemStacks - ItemStacks to perform the operation on
	 * @return if any recipes were removed
	 */
	public static boolean removeRecipes(ItemStack... itemStacks)
	{
		boolean didRemove = false;

		for(Iterator itr = CraftingManager.getInstance().getRecipeList().iterator(); itr.hasNext();)
		{
			Object obj = itr.next();

			if(obj instanceof IRecipe && ((IRecipe)obj).getRecipeOutput() != null)
			{
				for(ItemStack itemStack : itemStacks)
				{
					if(((IRecipe)obj).getRecipeOutput().isItemEqual(itemStack))
					{
						itr.remove();
						didRemove = true;
						break;
					}
				}
			}
		}

		return didRemove;
	}

	/**
	 * Marks the chunk this TileEntity is in as modified. Call this method to be sure NBT is written by the defined tile entity.
	 * @param tileEntity - TileEntity to save
	 */
	public static void saveChunk(TileEntity tileEntity)
	{
		if(tileEntity == null || tileEntity.isInvalid() || tileEntity.getWorldObj() == null)
		{
			return;
		}

		tileEntity.getWorldObj().markTileEntityChunkModified(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, tileEntity);
	}

	/**
	 * Whether or not a certain TileEntity can function with redstone logic. Illogical to use unless the defined TileEntity implements
	 * IRedstoneControl.
	 * @param tileEntity - TileEntity to check
	 * @return if the TileEntity can function with redstone logic
	 */



	/**
	 * Gets the head vector of a player for a ray trace.
	 * @param player - player to check
	 * @return head location
	 */
	private static Vec3 getHeadVec(EntityPlayer player)
	{
		Vec3 vec = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);

		if(!player.worldObj.isRemote)
		{
			vec.yCoord += player.getEyeHeight();

			if(player instanceof EntityPlayerMP && player.isSneaking())
			{
				vec.yCoord -= 0.08;
			}
		}

		return vec;
	}

	/**
	 * Gets a rounded energy display of a defined amount of energy.
	 * @param energy - energy to display
	 * @return rounded energy display
	 */
	public static String getEnergyDisplay(double energy)
	{
		if(energy == Integer.MAX_VALUE)
		{
			return LangUtils.localize("gui.infinite");
		}
		
		switch(general.activeType)
		{
			case J:
				return UnitDisplayUtils.getDisplayShort(energy, ElectricUnit.JOULES);
			case RF:
				return UnitDisplayUtils.getDisplayShort(energy * general.TO_TE, ElectricUnit.REDSTONE_FLUX);
			case EU:
				return UnitDisplayUtils.getDisplayShort(energy * general.TO_IC2, ElectricUnit.ELECTRICAL_UNITS);
			case MJ:
				return UnitDisplayUtils.getDisplayShort(energy * general.TO_TE / 10, ElectricUnit.MINECRAFT_JOULES);
		}

		return "error";
	}
	
	/**
	 * Convert from the unit defined in the configuration to joules.
	 * @param energy - energy to convert
	 * @return energy converted to joules
	 */
	public static double convertToJoules(double energy)
	{
		switch(general.activeType)
		{
			case RF:
				return energy * general.FROM_TE;
			case EU:
				return energy * general.FROM_IC2;
			case MJ:
				return energy * general.FROM_TE * 10;
			default:
				return energy;
		}
	}
	
	/**
	 * Convert from joules to the unit defined in the configuration.
	 * @param energy - energy to convert
	 * @return energy converted to configured unit
	 */
	public static double convertToDisplay(double energy)
	{
		switch(general.activeType)
		{
			case RF:
				return energy * general.TO_TE;
			case EU:
				return energy * general.TO_IC2;
			case MJ:
				return energy * general.TO_TE / 10;
			default:
				return energy;
		}
	}

	/**
	 * Gets a rounded energy display of a defined amount of energy.
	 * @param T - temperature to display
	 * @return rounded energy display
	 */


	/**
	 * Gets a clean view of a coordinate value without the dimension ID.
	 * @param obj - coordinate to check
	 * @return coordinate display
	 */
	public static String getCoordDisplay(Coord4D obj)
	{
		return "[" + obj.xCoord + ", " + obj.yCoord + ", " + obj.zCoord + "]";
	}

	/**
	 * Splits a string of text into a list of new segments, using the splitter "!n."
	 * @param s - string to split
	 * @return split string
	 */
	

	/**
	 * Finds the output of a defined InventoryCrafting grid. Taken from CofhCore.
	 * @param inv - InventoryCrafting to check
	 * @param world - world reference
	 * @return output ItemStack
	 */
	public static ItemStack findMatchingRecipe(InventoryCrafting inv, World world)
	{
		ItemStack[] dmgItems = new ItemStack[2];

		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			if(inv.getStackInSlot(i) != null)
			{
				if(dmgItems[0] == null)
				{
					dmgItems[0] = inv.getStackInSlot(i);
				}
				else {
					dmgItems[1] = inv.getStackInSlot(i);
					break;
				}
			}
		}

		if((dmgItems[0] == null) || (dmgItems[0].getItem() == null))
		{
			return null;
		}

		if((dmgItems[1] != null) && (dmgItems[0].getItem() == dmgItems[1].getItem()) && (dmgItems[0].stackSize == 1) && (dmgItems[1].stackSize == 1) && dmgItems[0].getItem().isRepairable())
		{
			Item theItem = dmgItems[0].getItem();
			int dmgDiff0 = theItem.getMaxDamage() - dmgItems[0].getItemDamageForDisplay();
			int dmgDiff1 = theItem.getMaxDamage() - dmgItems[1].getItemDamageForDisplay();
			int value = dmgDiff0 + dmgDiff1 + theItem.getMaxDamage() * 5 / 100;
			int solve = Math.max(0, theItem.getMaxDamage() - value);
			return new ItemStack(dmgItems[0].getItem(), 1, solve);
		}

		for(IRecipe recipe : (List<IRecipe>)CraftingManager.getInstance().getRecipeList())
		{
			if(recipe.matches(inv, world))
			{
				return recipe.getCraftingResult(inv);
			}
		}

		return null;
	}
	
	/**
	 * Whether or not the provided chunk is being vibrated by a Seismic Vibrator.
	 * @param chunk - chunk to check
	 * @return if the chunk is being vibrated
	 */

	/**
	 * Whether or not a given EntityPlayer is considered an Op.
	 * @param player - player to check
	 * @return if the player has operator privileges
	 */
	public static boolean isOp(EntityPlayerMP player)
	{
		return general.opsBypassRestrictions && player.mcServer.getConfigurationManager().func_152596_g(player.getGameProfile());
	}
	
	/**
	 * Gets the mod ID of the mod owning the given ItemStack.
	 * @param stack - ItemStack to check
	 * @return mod ID of the ItemStack's owner
	 */
	public static String getMod(ItemStack stack)
	{
		try {
			ModContainer mod = GameData.findModOwner(GameData.getItemRegistry().getNameForObject(stack.getItem()));
			return mod == null ? "Minecraft" : mod.getName();
		} catch(Exception e) {
			return "null";
		}
	}
	
	/**
	 * Gets the item ID from a given ItemStack
	 * @param itemStack - ItemStack to check
	 * @return item ID of the ItemStack
	 */
	public static int getID(ItemStack itemStack)
	{
		if(itemStack == null)
		{
			return -1;
		}
		
		return Item.getIdFromItem(itemStack.getItem());
	}

	public static boolean classExists(String className)
	{
		if(classesFound.containsKey(className))
		{
			return classesFound.get(className) != null;
		}

		Class<?> found;

		try
		{
			found = Class.forName(className);
		}
		catch(ClassNotFoundException e)
		{
			found = null;
		}

		classesFound.put(className, found);

		return found != null;
	}

	public static boolean existsAndInstance(Object obj, String className)
	{
		Class<?> theClass;

		if(classesFound.containsKey(className))
		{
			theClass = classesFound.get(className);
		}
		else
		{
			try
			{
				theClass = Class.forName(className);
				classesFound.put(className, theClass);
			} catch(ClassNotFoundException e)
			{
				classesFound.put(className, null);
				return false;
			}
		}

		return theClass != null && theClass.isInstance(obj);
	}

	public static boolean isBCWrench(Item tool)
	{
		return existsAndInstance(tool, "buildcraft.api.tools.IToolWrench");
	}

	public static boolean isCoFHHammer(Item tool)
	{
		return existsAndInstance(tool, "cofh.api.item.IToolHammer");
	}

	public static enum ResourceType
	{
		GUI("gui"),
		GUI_ELEMENT("gui/elements"),
		SOUND("sound"),
		RENDER("render"),
		TEXTURE_BLOCKS("textures/blocks"),
		TEXTURE_ITEMS("textures/items"),
		MODEL("models"),
		INFUSE("infuse");

		private String prefix;

		private ResourceType(String s)
		{
			prefix = s;
		}

		public String getPrefix()
		{
			return prefix + "/";
		}
	}
}