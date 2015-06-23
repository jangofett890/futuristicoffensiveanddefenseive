package futuristicoffensiveanddefenseive.theneonfish.fod.common;

import net.minecraft.item.ItemStack;
import futuristicoffensiveanddefenseive.theneonfish.fod.Tier.TurretBaseTier;
import futuristicoffensiveanddefenseive.theneonfish.fod.blocks.Turret.TurretType;

public interface ITurret {
	public TurretType getTurretType(ItemStack itemstack);

	/**
	 * Sets the tier of this energy cube
	 * @param itemstack - ItemStack to set
	 * @param type - tier to set
	 */
	public void setTurretType(ItemStack itemstack, TurretType type);


}
