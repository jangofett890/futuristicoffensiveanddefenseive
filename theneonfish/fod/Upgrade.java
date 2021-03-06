package futuristicoffensiveanddefenseive.theneonfish.fod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants.NBT;
import futuristicoffensiveanddefenseive.theneonfish.fod.utils.LangUtils;
import futuristicoffensiveanddefenseive.theneonfish.fod.API.EnumColor;
import futuristicoffensiveanddefenseive.theneonfish.fod.common.IUpgradeTile;

public enum Upgrade{
	SPEED("speed", 8, EnumColor.RED),
	ENERGY("energy", 8, EnumColor.BRIGHT_GREEN),
	POWER("power", 8, EnumColor.DARK_RED);
	
	private String name;
	private int maxStack;
	private EnumColor color;
	
	private Upgrade(String s, int max, EnumColor c)
	{
		name = s;
		maxStack = max;
		color = c;
	}
	
	public String getName()
	{
		return LangUtils.localize("upgrade." + name);
	}
	
	public String getDescription()
	{
		return LangUtils.localize("upgrade." + name + ".desc");
	}
	
	public int getMax()
	{
		return maxStack;
	}
	
	public EnumColor getColor()
	{
		return color;
	} 
	
	public boolean canMultiply()
	{
		return getMax() > 1;
	}
	/**
	public ItemStack getStack()
	{
		switch(this)
		{
			case SPEED:
				return new ItemStack(MekanismItems.SpeedUpgrade);
			case ENERGY:
				return new ItemStack(MekanismItems.EnergyUpgrade);
			case POWER:
				return new ItemStack(FODItems.PowerUpgrade);

		}
		
		return null;
	}**/
	
	/**public List<String> getInfo(TileEntity tile)
	{
		List<String> ret = new ArrayList<String>();
		
		if(tile instanceof IUpgradeTile)
		{
			if(tile instanceof IUpgradeInfoHandler)
			{
				return ((IUpgradeInfoHandler)tile).getInfo(this);
			}
			else {
				ret = getMultScaledInfo((IUpgradeTile)tile);
			}
		}
		
		return ret;
	}
	
	public List<String> getMultScaledInfo(IUpgradeTile tile)
	{
		List<String> ret = new ArrayList<String>();
		
		if(canMultiply())
		{
			double effect = Math.pow(general.maxUpgradeMultiplier, (float)tile.getComponent().getUpgrades(this)/(float)getMax());
			
			ret.add(LangUtils.localize("gui.upgrades.effect") + ": " + (Math.round(effect*100)/100F) + "x");
		}
		
		return ret;
	}
	
	public List<String> getExpScaledInfo(IUpgradeTile tile)
	{
		List<String> ret = new ArrayList<String>();
		
		if(canMultiply())
		{
			double effect = Math.pow(2, (float)tile.getComponent().getUpgrades(this));
			
			ret.add(LangUtils.localize("gui.upgrades.effect") + ": " + effect + "x");
		}
		
		return ret;
	}**/
	
	public static Map<Upgrade, Integer> buildMap(NBTTagCompound nbtTags)
	{
		Map<Upgrade, Integer> upgrades = new HashMap<Upgrade, Integer>();
		
		if(nbtTags != null)
		{
			if(nbtTags.hasKey("upgrades"))
			{
				NBTTagList list = nbtTags.getTagList("upgrades", NBT.TAG_COMPOUND);
				
				for(int tagCount = 0; tagCount < list.tagCount(); tagCount++)
				{
					NBTTagCompound compound = list.getCompoundTagAt(tagCount);
					
					Upgrade upgrade = Upgrade.values()[compound.getInteger("type")];
					upgrades.put(upgrade, compound.getInteger("amount"));
				}
			}
			else if(nbtTags.hasKey("energyMultiplier") && nbtTags.hasKey("speedMultiplier")) //TODO remove soon
			{
				upgrades.put(Upgrade.ENERGY, nbtTags.getInteger("energyMultiplier"));
				upgrades.put(Upgrade.SPEED, nbtTags.getInteger("speedMultiplier"));
			}
		}
		
		return upgrades;
	}
	
	public static void saveMap(Map<Upgrade, Integer> upgrades, NBTTagCompound nbtTags)
	{
		NBTTagList list = new NBTTagList();
		
		for(Map.Entry<Upgrade, Integer> entry : upgrades.entrySet())
		{
			list.appendTag(getTagFor(entry.getKey(), entry.getValue()));
		}
		
		nbtTags.setTag("upgrades", list);
	}
	
	public static NBTTagCompound getTagFor(Upgrade upgrade, int amount)
	{
		NBTTagCompound compound = new NBTTagCompound();
		
		compound.setInteger("type", upgrade.ordinal());
		compound.setInteger("amount", amount);
		
		return compound;
	}
	
	public static interface IUpgradeInfoHandler
	{
		public List<String> getInfo(Upgrade upgrade);
	}
}