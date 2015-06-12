package futuristicoffensiveanddefenseive.theneonfish.fod;

import cpw.mods.fml.common.registry.GameRegistry;
import mekanism.common.item.ItemEnergized;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import futuristicoffensiveanddefenseive.theneonfish.fod.items.basicItem;


public class FODItems {
	public static final ItemEnergized basicBattery = (ItemEnergized)new ItemEnergized(10000).setUnlocalizedName("Battery");
	public static final basicItem detonator = (basicItem) new basicItem().setUnlocalizedName("Detonator");
	public static final basicItem PowerUpgrade = (basicItem) new basicItem().setUnlocalizedName("Turret Power Upgrade");
			
			
	public static void register(){
		GameRegistry.registerItem(detonator, "Detonator");
		GameRegistry.registerItem(basicBattery, "Battery");
		GameRegistry.registerItem(PowerUpgrade, "Turret Power Upgrade");
	
	}
}
