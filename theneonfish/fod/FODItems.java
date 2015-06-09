package futuristicoffensiveanddefenseive.theneonfish.fod;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import futuristicoffensiveanddefenseive.theneonfish.fod.energy.item.ElectricItemBase;
import futuristicoffensiveanddefenseive.theneonfish.fod.items.basicItem;


public class FODItems {
	public static final Item basicBattery = (ElectricItemBase)new ElectricItemBase(10000).setUnlocalizedName("Battery");
	public static final Item detonator = new basicItem().setUnlocalizedName("Detonator");
			
			
	public static void register(){
		GameRegistry.registerItem(detonator, "Detonator");
		GameRegistry.registerItem(basicBattery, "Battery");
	
	}
}
