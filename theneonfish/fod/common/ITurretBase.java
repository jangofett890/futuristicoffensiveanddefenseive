package futuristicoffensiveanddefenseive.theneonfish.fod.common;

import futuristicoffensiveanddefenseive.theneonfish.fod.Tier.TurretBaseTier;
import net.minecraft.item.ItemStack;

public interface ITurretBase {	/**
 * Gets the recipe type this Smelting Factory currently has.
 * @param itemStack - stack to check
 * @return RecipeType ordinal
 */
public int getRecipeType(ItemStack itemStack);

/**
 * Sets the recipe type of this Smelting Factory to a new value.
 * @param type - RecipeType ordinal
 * @param itemStack - stack to set
 */
public void setRecipeType(int type, ItemStack itemStack);

public TurretBaseTier getTurretBaseTier(ItemStack itemstack);

/**
 * Sets the tier of this energy cube
 * @param itemstack - ItemStack to set
 * @param tier - tier to set
 */
public void setTurretBaseTier(ItemStack itemstack, TurretBaseTier tier);


}