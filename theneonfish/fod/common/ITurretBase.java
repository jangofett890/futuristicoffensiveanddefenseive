package futuristicoffensiveanddefenseive.theneonfish.fod.common;

import futuristicoffensiveanddefenseive.theneonfish.fod.Tier.TurretBaseTier;
import net.minecraft.item.ItemStack;

public interface ITurretBase {	

public TurretBaseTier getTurretBaseTier(ItemStack itemstack);

/**
 * Sets the tier of this energy cube
 * @param itemstack - ItemStack to set
 * @param tier - tier to set
 */
public void setTurretBaseTier(ItemStack itemstack, TurretBaseTier tier);


}