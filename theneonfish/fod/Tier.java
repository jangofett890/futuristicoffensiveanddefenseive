package futuristicoffensiveanddefenseive.theneonfish.fod;
import io.netty.buffer.ByteBuf;
import mekanism.api.EnumColor;
import futuristicoffensiveanddefenseive.theneonfish.fod.blocks.Turret;
import futuristicoffensiveanddefenseive.theneonfish.fod.items.Upgrade;
import futuristicoffensiveanddefenseive.theneonfish.fod.utils.LangUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class Tier{
	
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
		BASIC(2000000, new Turret[]{}, new Upgrade()),
		ADVANCED(8000000, new Turret(Material.iron), new Upgrade()),
		ELITE(32000000, new Turret(Material.iron), new Upgrade()),
		ULTIMATE(128000000, new Turret(Material.iron), new Upgrade());
		
		public double maxEnergy;
		private double baseMaxEnergy;
		public Turret[] TURRETS;
		public Upgrade[] UPGRADES;
		
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

		private TurretBaseTier(double max, Turret[] turrets, Upgrade[] upgrades)
		{
				baseMaxEnergy = maxEnergy = max;
				if(this.getBaseTier() == BaseTier.BASIC){
					
				}
				turrets = TURRETS;
				upgrades = UPGRADES;
		}
	}
	
}
