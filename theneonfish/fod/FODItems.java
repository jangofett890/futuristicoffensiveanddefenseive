package futuristicoffensiveanddefenseive.theneonfish.fod;

import cpw.mods.fml.common.registry.GameRegistry;
//import mekanism.common.item.ItemEnergized;
import net.minecraft.item.Item;
import futuristicoffensiveanddefenseive.theneonfish.fod.items.basicItem;


public class FODItems {
	//public static final ItemEnergized basicBattery = (ItemEnergized)new ItemEnergized(10000).setUnlocalizedName("Battery");
	public static final basicItem detonator = (basicItem) new basicItem().setUnlocalizedName("Detonator");
			
			
	public static void register(){
		GameRegistry.registerItem(detonator, "Detonator");
		//GameRegistry.registerItem(basicBattery, "Battery");
	
	}
}
