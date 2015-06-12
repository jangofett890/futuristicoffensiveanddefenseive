package futuristicoffensiveanddefenseive.theneonfish.fod.TileEntities;

import futuristicoffensiveanddefenseive.theneonfish.fod.Upgrade;
import mekanism.common.base.ISustainedInventory;
import mekanism.common.tile.TileEntityBasicBlock;
import mekanism.common.tile.TileEntityContainerBlock;
import mekanism.common.util.InventoryUtils;
import mekanism.common.util.LangUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public abstract class TileEntityContainerBlockFOD extends TileEntityContainerBlock
{
	

	public TileEntityContainerBlockFOD(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void recalculateUpgradables(Upgrade upgradeType) {}
}