package futuristicoffensiveanddefenseive.theneonfish.fod.items;

import futuristicoffensiveanddefenseive.theneonfish.fod.energy.item.ElectricItemBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import resonantengine.api.item.IEnergyItem;

public class TestItem extends ElectricItemBase {
	public TestItem(){
		super();
		
	}
	@Override
	public double getEnergyCapacity(ItemStack itemStack) {
		return 1000;
	}
}