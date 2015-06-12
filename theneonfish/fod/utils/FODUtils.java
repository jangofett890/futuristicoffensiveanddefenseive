package futuristicoffensiveanddefenseive.theneonfish.fod.utils;

import ic2.api.energy.EnergyNet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import futuristicoffensiveanddefenseive.theneonfish.fod.FODBlocks;
import futuristicoffensiveanddefenseive.theneonfish.fod.Tier.TurretBaseTier;
import futuristicoffensiveanddefenseive.theneonfish.fod.Upgrade;
import futuristicoffensiveanddefenseive.theneonfish.fod.common.IUpgradeTile;
import futuristicoffensiveanddefenseive.theneonfish.fod.items.TurretBaseItem;
import mekanism.api.Chunk3D;
import mekanism.api.Coord4D;
import mekanism.api.IMekWrench;
import mekanism.api.MekanismConfig.client;
import mekanism.api.MekanismConfig.general;
import mekanism.api.util.UnitDisplayUtils;
import mekanism.api.util.UnitDisplayUtils.ElectricUnit;
import mekanism.api.util.UnitDisplayUtils.TemperatureUnit;
import mekanism.common.Mekanism;
import mekanism.common.OreDictCache;
import mekanism.common.base.IActiveState;
import mekanism.common.base.IRedstoneControl;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class FODUtils {
	public static final ForgeDirection[] SIDE_DIRS = new ForgeDirection[] {ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST};

	public static final Map<String, Class<?>> classesFound = new HashMap<String, Class<?>>();




	public static String merge(List<String> text)
	{
		StringBuilder builder = new StringBuilder();

		for(String s : text)
		{
			builder.append(s);
		}

		return builder.toString();
	}

	
	public static boolean isOffline()
	{
		try {
			new URL("http://www.apple.com").openConnection().connect();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Creates a fake explosion at the declared player, with only sounds and effects. No damage is caused to either blocks or the player.
	 * @param entityplayer - player to explode
	 */
	public static void doFakeEntityExplosion(EntityPlayer entityplayer)
	{
		World world = entityplayer.worldObj;
		world.spawnParticle("hugeexplosion", entityplayer.posX, entityplayer.posY, entityplayer.posZ, 0.0D, 0.0D, 0.0D);
		world.playSoundAtEntity(entityplayer, "random.explode", 1.0F, 1.0F);
	}

	/**
	 * Creates a fake explosion at the declared coords, with only sounds and effects. No damage is caused to either blocks or the player.
	 * @param world - world where the explosion will occur
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 */
	public static void doFakeBlockExplosion(World world, int x, int y, int z)
	{
		world.spawnParticle("hugeexplosion", x, y, z, 0.0D, 0.0D, 0.0D);
		world.playSound(x, y, z, "random.explode", 1.0F, 1.0F, true);
	}

	/**
	 * Copies an ItemStack and returns it with a defined stackSize.
	 * @param itemstack - stack to change size
	 * @param size - size to change to
	 * @return resized ItemStack
	 */
	public static ItemStack size(ItemStack itemstack, int size)
	{
		ItemStack newStack = itemstack.copy();
		newStack.stackSize = size;
		return newStack;
	}

	/**
	 * Adds a recipe directly to the CraftingManager that works with the Forge Ore Dictionary.
	 * @param output the ItemStack produced by this recipe
	 * @param params the items/blocks/itemstacks required to create the output ItemStack
	 */
	public static void addRecipe(ItemStack output, Object[] params)
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output, params));
	}

	/**
	 * Retrieves an empty Energy Cube with a defined tier.
	 * @param tier - tier to add to the Energy Cube
	 * @return empty Energy Cube with defined tier
	 */
	public static ItemStack getTurretBase(TurretBaseTier tier)
	{
		return ((TurretBaseItem)new ItemStack(FODBlocks.turretBase).getItem()).getUnchargedItem(tier);
	}
	
	public static boolean isActive(IBlockAccess world, int x, int y, int z)
	{
		TileEntity tileEntity = (TileEntity)world.getTileEntity(x, y, z);

		if(tileEntity != null)
		{
			if(tileEntity instanceof IActiveState)
			{
				return ((IActiveState)tileEntity).getActive();
			}
		}

		return false;
	}

	/**
	 * Gets the left side of a certain orientation.
	 * @param orientation
	 * @return left side
	 */
	public static ForgeDirection getLeft(int orientation)
	{
		switch(orientation)
		{
			case 2:
				return ForgeDirection.EAST;
			case 3:
				return ForgeDirection.WEST;
			case 4:
				return ForgeDirection.NORTH;
			default:
				return ForgeDirection.SOUTH;
		}
	}

	/**
	 * Gets the right side of a certain orientation.
	 * @param orientation
	 * @return right side
	 */
	public static ForgeDirection getRight(int orientation)
	{
		return getLeft(orientation).getOpposite();
	}

	/**
	 * Gets the opposite side of a certain orientation.
	 * @param orientation
	 * @return opposite side
	 */
	public static ForgeDirection getBack(int orientation)
	{
		return ForgeDirection.getOrientation(orientation).getOpposite();
	}

	/**
	 * Checks to see if a specified ItemStack is stored in the Ore Dictionary with the specified name.
	 * @param check - ItemStack to check
	 * @param oreDict - name to check with
	 * @return if the ItemStack has the Ore Dictionary key
	 */
	public static boolean oreDictCheck(ItemStack check, String oreDict)
	{
		boolean hasResource = false;

		for(ItemStack ore : OreDictionary.getOres(oreDict))
		{
			if(ore.isItemEqual(check))
			{
				hasResource = true;
			}
		}

		return hasResource;
	}

	/**
	 * Gets the ore dictionary name of a defined ItemStack.
	 * @param check - ItemStack to check OreDict name of
	 * @return OreDict name
	 */
	public static List<String> getOreDictName(ItemStack check)
	{
		return OreDictCache.getOreDictName(check);
	}

	/**
	 * Returns an integer facing that converts a world-based orientation to a machine-based oriention.
	 * @param side - world based
	 * @param blockFacing - what orientation the block is facing
	 * @return machine orientation
	 */
	public static int getBaseOrientation(int side, int blockFacing)
	{
		if(blockFacing == 3 || side == 1 || side == 0)
		{
			if(side == 2 || side == 3)
			{
				return ForgeDirection.getOrientation(side).getOpposite().ordinal();
			}

			return side;
		}
		else if(blockFacing == 2)
		{
			if(side == 2 || side == 3)
			{
				return side;
			}

			return ForgeDirection.getOrientation(side).getOpposite().ordinal();
		}
		else if(blockFacing == 4)
		{
			if(side == 2 || side == 3)
			{
				return getRight(side).ordinal();
			}

			return getLeft(side).ordinal();
		}
		else if(blockFacing == 5)
		{
			if(side == 2 || side == 3)
			{
				return getLeft(side).ordinal();
			}

			return getRight(side).ordinal();
		}

		return side;
	}

	

	/**
	 * Gets the maximum energy for a machine's item form via it's upgrades.
	 * @param itemStack - stack holding energy upgrades
	 * @param def - original, default max energy
	 * @return max energy
	 */
	public static double getMaxEnergy(ItemStack itemStack, double def)
	{
		Map<Upgrade, Integer> upgrades = Upgrade.buildMap(itemStack.stackTagCompound);
		float numUpgrades =  upgrades.get(Upgrade.ENERGY) == null ? 0 : (float)upgrades.get(Upgrade.ENERGY);
		return def * Math.pow(general.maxUpgradeMultiplier, numUpgrades/(float)Upgrade.ENERGY.getMax());
	}
	
	/**
	 * Better version of the World.isBlockIndirectlyGettingPowered() method that doesn't load chunks.
	 * @param world - the world to perform the check in
	 * @param coord - the coordinate of the block performing the check
	 * @return if the block is indirectly getting powered by LOADED chunks
	 */
	public static boolean isGettingPowered(World world, Coord4D coord)
	{
		for(ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
		{
			Coord4D sideCoord = coord.getFromSide(side);
			
			if(sideCoord.exists(world) && sideCoord.getFromSide(side).exists(world))
			{
				Block block = sideCoord.getBlock(world);
				boolean weakPower = block.shouldCheckWeakPower(world, coord.xCoord, coord.yCoord, coord.zCoord, side.ordinal());
				
				if(weakPower && isDirectlyGettingPowered(world, sideCoord))
				{
					return true;
				}
				else if(!weakPower && block.isProvidingWeakPower(world, sideCoord.xCoord, sideCoord.yCoord, sideCoord.zCoord, side.ordinal()) > 0)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
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

	/**
	 * Notifies neighboring blocks of a TileEntity change without loading chunks.
	 * @param world - world to perform the operation in
	 * @param coord - Coord4D to perform the operation on
	 */
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




	/**
	 * Updates a block's light value and marks it for a render update.
	 * @param world - world the block is in
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 */
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


	public static ResourceLocation getResource(ResourceType type, String name)
	{
		return new ResourceLocation("fod", type.getPrefix() + name);
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
	public static boolean canFunction(TileEntity tileEntity)
	{
		if(!(tileEntity instanceof IRedstoneControl))
		{
			return true;
		}else

		return false;
	}

	/**
	 * Ray-traces what block a player is looking at.
	 * @param world - world the player is in
	 * @param player - player to raytrace
	 * @return raytraced value
	 */
	public static MovingObjectPosition rayTrace(World world, EntityPlayer player)
	{
		double reach = Mekanism.proxy.getReach(player);

		Vec3 headVec = getHeadVec(player);
		Vec3 lookVec = player.getLook(1);
		Vec3 endVec = headVec.addVector(lookVec.xCoord*reach, lookVec.yCoord*reach, lookVec.zCoord*reach);

		return world.rayTraceBlocks(headVec, endVec, true);
	}

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
	public static String getTemperatureDisplay(double T, TemperatureUnit unit)
	{
		double TK = unit.convertToK(T);
		switch(general.tempUnit)
		{
			case K:
				return UnitDisplayUtils.getDisplayShort(TK, TemperatureUnit.KELVIN);
			case C:
				return UnitDisplayUtils.getDisplayShort(TK, TemperatureUnit.CELSIUS);
			case R:
				return UnitDisplayUtils.getDisplayShort(TK, TemperatureUnit.RANKINE);
			case F:
				return UnitDisplayUtils.getDisplayShort(TK, TemperatureUnit.FAHRENHEIT);
			case STP:
				return UnitDisplayUtils.getDisplayShort(TK, TemperatureUnit.AMBIENT);
		}

		return "error";
	}

	/**
	 * Whether or not IC2 power should be used, taking into account whether or not it is installed or another mod is
	 * providing its API.
	 * @return if IC2 power should be used
	 */
	public static boolean useIC2()
	{
		return Mekanism.hooks.IC2Loaded && EnergyNet.instance != null && !general.blacklistIC2;
	}

	/**
	 * Whether or not RF power should be used, taking into account whether or not it is installed or another mod is
	 * providing its API.
	 * @return if RF power should be used
	 */
	public static boolean useRF()
	{
		return Mekanism.hooks.CoFHCoreLoaded && !general.blacklistRF;
	}

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
	public static List<String> splitLines(String s)
	{
		ArrayList ret = new ArrayList();

		String[] split = s.split("!n");
		ret.addAll(Arrays.asList(split));

		return ret;
	}


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
	public static boolean isChunkVibrated(Chunk3D chunk)
	{
		for(Coord4D coord : Mekanism.activeVibrators)
		{
			if(coord.getChunk3D().equals(chunk))
			{
				return true;
			}
		}
		
		return false;
	}
	
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
