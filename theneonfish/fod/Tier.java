package futuristicoffensiveanddefenseive.theneonfish.fod;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.netty.buffer.ByteBuf;
import mekanism.api.EnumColor;
import futuristicoffensiveanddefenseive.theneonfish.fod.blocks.Turret;
import futuristicoffensiveanddefenseive.theneonfish.fod.blocks.Turret.TurretType;
import futuristicoffensiveanddefenseive.theneonfish.fod.items.Upgrade;
import futuristicoffensiveanddefenseive.theneonfish.fod.utils.LangUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Property;

public final class Tier{
	public static List allowedTurretsFor1;
	public static List allowedTurretsFor2;
	public static List allowedTurretsFor3;
	
	public Tier(){
		allowedTurretsFor1.add(TurretType.TURRET_CROSSBOW);
		allowedTurretsFor1.add(TurretType.TURRET_MACHINEGUN);
		allowedTurretsFor1.add(TurretType.TURRET_ROCKETLAUNCHER);
		allowedTurretsFor2.addAll(allowedTurretsFor1);
		allowedTurretsFor2.add(TurretType.TURRET_PLASMA);
		allowedTurretsFor2.add(TurretType.TURRET_LASER);
		allowedTurretsFor3.addAll(allowedTurretsFor2);
		allowedTurretsFor3.add(TurretType.TURRET_IRIDIUM);
		allowedTurretsFor3.add(TurretType.TURRET_ANTIMATTER);	
	}
	
	
	public static enum BaseTier
	{
		BASIC("Basic", EnumColor.BRIGHT_GREEN),
		ADVANCED("Advanced", EnumColor.DARK_RED),
		ELITE("Elite", EnumColor.DARK_BLUE),
		ULTIMATE("Ultimate", EnumColor.PURPLE),
		CREATIVE("Creative", EnumColor.BLACK);
		
		public String getName()
		{
			return name;
		}
		
		public String getLocalizedName()
		{
			return LangUtils.localize("tier." + getName());
		}
		
		public EnumColor getColor()
		{
			return color;
		}
		
		public boolean isObtainable()
		{
			return this != CREATIVE;
		}
		
		private String name;
		private EnumColor color;
		
		private BaseTier(String s, EnumColor c)
		{
			name = s;
			color = c;
		}
	}
	
	public static enum TurretBaseTier{
		BASIC(2000000, allowedTurretsFor1),
		ADVANCED(8000000, allowedTurretsFor2),
		ELITE(32000000, allowedTurretsFor2),
		ULTIMATE(128000000, allowedTurretsFor3), 
		CREATIVE(Integer.MAX_VALUE, allowedTurretsFor3);
		
		public double maxEnergy;
		private double baseMaxEnergy;
		public List TURRETS;
		public String TURRETSTRING1;
		public Property TURRETSTRINGS;
		
		protected void loadConfig()
		{
			maxEnergy = MainFOD.configuration.get("tier", getBaseTier().getName() + "TurretBaseMaxEnergy", baseMaxEnergy).getDouble();
		}
		protected void readConfig(ByteBuf dataStream)
		{
			maxEnergy = dataStream.readDouble();
		}
		
		protected void writeConfig(ByteBuf dataStream)
		{
			dataStream.writeDouble(maxEnergy);
		}
		
		
		public static TurretBaseTier getFromName(String tierName)
		{
			for(TurretBaseTier tier : values())
			{
				if(tierName.contains(tier.getBaseTier().getName()))
				{
					return tier;
				}
			}
			
			return BASIC;
		}
		
		public BaseTier getBaseTier()
		{
			return BaseTier.values()[ordinal()];
		}

		private TurretBaseTier(double max, List turrets)
		{
				baseMaxEnergy = maxEnergy = max;
				turrets = TURRETS;
				

		}
	}

	public static void loadConfig()
	{
		for(TurretBaseTier tier : TurretBaseTier.values())
		{
			tier.loadConfig();
		}
	}
	
	public static void readConfig(ByteBuf dataStream)
	{		
		for(TurretBaseTier tier : TurretBaseTier.values())
		{
			tier.readConfig(dataStream);
		}
	}
	
	public static void writeConfig(ByteBuf dataStream)
	{		
		for(TurretBaseTier tier : TurretBaseTier.values())
		{
			tier.writeConfig(dataStream);
		}
	}
	
}
