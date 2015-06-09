package futuristicoffensiveanddefenseive.theneonfish.fod.common;
import java.util.ArrayList;
import java.util.List;

import mekanism.api.EnumColor;
import mekanism.api.gas.GasTank;
import futuristicoffensiveanddefenseive.theneonfish.fod.utils.LangUtils;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

public class SideData
{
	/** The color of this SideData */
	public EnumColor color;
	
	/** The name of this SideData */
	public String name;

	/** Int[] of available side slots, can be used for items, gases, or items */
	public int[] availableSlots;
	
	/** EnergyState representing this SideData */
	public EnergyState energyState;

	public SideData(String n, EnumColor colour, int[] slots)
	{
		name = n;
		color = colour;
		availableSlots = slots;
	}
	
	public SideData(String n, EnumColor colour, EnergyState state)
	{
		name = n;
		color = colour;
		energyState = state;
	}
	
	public String localize()
	{
		return LangUtils.localize("sideData." + name);
	}
	
	public boolean hasSlot(int... slots)
	{
		for(int i : availableSlots)
		{
			for(int slot : slots)
			{
				if(i == slot)
				{
					return true;
				}
			}
		}
		
		return false;
	}

	

	
	public static enum EnergyState
	{
		INPUT,
		OUTPUT,
		OFF;
	}
}