package futuristicoffensiveanddefenseive.theneonfish.fod.common;

import futuristicoffensiveanddefenseive.theneonfish.fod.Upgrade;

import net.minecraft.item.ItemStack;

public interface IUpgradeItem 
{
	public Upgrade getUpgradeType(ItemStack stack);
}