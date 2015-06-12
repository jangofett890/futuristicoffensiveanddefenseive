package futuristicoffensiveanddefenseive.theneonfish.fod;

import java.util.HashMap;
import java.util.Map;

import mekanism.api.energy.IEnergizedItem;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import futuristicoffensiveanddefenseive.theneonfish.fod.blocks.Turret.TurretType;

public class FODRecipe implements IRecipe{
	
	private static final int MAX_CRAFT_GRID_WIDTH = 3;
	private static final int MAX_CRAFT_GRID_HEIGHT = 3;

	private ItemStack output = null;
	private Object[] input = null;

	public int width = 0;
	public int height = 0;

	private boolean mirrored = true;
	public FODRecipe(ItemStack result, Object... recipe){
		output = result.copy();
		
		
		String shape = "";
		int idx = 0;

		if(recipe[idx] instanceof Boolean)
		{
			mirrored = (Boolean)recipe[idx];

			if(recipe[idx+1] instanceof Object[])
			{
				recipe = (Object[])recipe[idx+1];
			}
			else {
				idx = 1;
			}
		}

		if(recipe[idx] instanceof String[])
		{
			String[] parts = ((String[])recipe[idx++]);

			for(String s : parts)
			{
				width = s.length();
				shape += s;
			}

			height = parts.length;
		}
		else {
			while(recipe[idx] instanceof String)
			{
				String s = (String)recipe[idx++];
				shape += s;
				width = s.length();
				height++;
			}
		}

		if(width * height != shape.length())
		{
			String ret = "Invalid shaped ore recipe: ";

			for(Object tmp :  recipe)
			{
				ret += tmp + ", ";
			}

			ret += output;

			throw new RuntimeException(ret);
		}

		HashMap<Character, Object> itemMap = new HashMap<Character, Object>();

		for(; idx < recipe.length; idx += 2)
		{
			Character chr = (Character)recipe[idx];
			Object in = recipe[idx + 1];

			if(in instanceof ItemStack)
			{
				itemMap.put(chr, ((ItemStack)in).copy());
			}
			else if(in instanceof Item)
			{
				itemMap.put(chr, new ItemStack((Item)in));
			}
			else if(in instanceof Block)
			{
				itemMap.put(chr, new ItemStack((Block)in, 1, OreDictionary.WILDCARD_VALUE));
			}
			else if(in instanceof String)
			{
				itemMap.put(chr, OreDictionary.getOres((String)in));
			}
			else {
				String ret = "Invalid shaped ore recipe: ";

				for(Object tmp :  recipe)
				{
					ret += tmp + ", ";
				}

				ret += output;
				throw new RuntimeException(ret);
			}
		}

		input = new Object[width * height];
		int x = 0;

		for(char chr : shape.toCharArray())
		{
			input[x++] = itemMap.get(chr);
		}
		
	}
	@Override
	public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack toReturn = output.copy();

		if(toReturn.getItem() instanceof IEnergizedItem)
		{
			double energyFound = 0;

			for(int i = 0; i < 9; i++)
			{
				ItemStack itemstack = inv.getStackInSlot(i);

				if(itemstack != null && itemstack.getItem() instanceof IEnergizedItem)
				{
					energyFound += ((IEnergizedItem)itemstack.getItem()).getEnergy(itemstack);
				}
			}

			((IEnergizedItem)toReturn.getItem()).setEnergy(toReturn, Math.min(((IEnergizedItem)toReturn.getItem()).getMaxEnergy(toReturn), energyFound));
		}
		
		if(toReturn.getItem() instanceof IGasItem)
		{
			GasStack gasFound = null;
			
			for(int i = 0; i < 9; i++)
			{
				ItemStack itemstack = inv.getStackInSlot(i);

				if(itemstack != null && itemstack.getItem() instanceof IGasItem)
				{
					GasStack stored = ((IGasItem)itemstack.getItem()).getGas(itemstack);
					
					if(stored != null)
					{
						if(!((IGasItem)toReturn.getItem()).canReceiveGas(toReturn, stored.getGas()))
						{
							return null;
						}
						
						if(gasFound == null)
						{
							gasFound = stored;
						}
						else {
							if(gasFound.getGas() != stored.getGas())
							{
								return null;
							}
							
							gasFound.amount += stored.amount;
						}
					}
				}
			}
			
			if(gasFound != null)
			{
				gasFound.amount = Math.min(((IGasItem)toReturn.getItem()).getMaxGas(toReturn), gasFound.amount);
				((IGasItem)toReturn.getItem()).setGas(toReturn, gasFound);
			}
		}

		if(TurretType.get(toReturn) != null && TurretType.get(toReturn).supportsUpgrades)
		{
			Map<Upgrade, Integer> upgrades = new HashMap<Upgrade, Integer>();

			for(int i = 0; i < 9; i++)
			{
				ItemStack itemstack = inv.getStackInSlot(i);

				if(itemstack != null && TurretType.get(itemstack) != null && TurretType.get(itemstack).supportsUpgrades)
				{
					Map<Upgrade, Integer> stackMap = Upgrade.buildMap(itemstack.stackTagCompound);
					
					for(Map.Entry<Upgrade, Integer> entry : stackMap.entrySet())
					{
						if(entry != null && entry.getKey() != null && entry.getValue() != null)
						{
							Integer val = upgrades.get(entry.getKey());
							
							upgrades.put(entry.getKey(), Math.min(entry.getKey().getMax(), (val != null ? val : 0) + entry.getValue()));
						}
					}
				}
			}
			
			if(toReturn.stackTagCompound == null)
			{
				toReturn.setTagCompound(new NBTTagCompound());
			}
			
			Upgrade.saveMap(upgrades, toReturn.stackTagCompound);
		}

		return toReturn;
	}

	@Override
	public int getRecipeSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getRecipeOutput() {
		// TODO Auto-generated method stub
		return null;
	}

}
