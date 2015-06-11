package futuristicoffensiveanddefenseive.theneonfish.fod.TileEntities;

import futuristicoffensiveanddefenseive.theneonfish.fod.Tier.TurretBaseTier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import mekanism.common.base.IRedstoneControl;
import mekanism.common.tile.TileEntityElectricBlock;

public class TileEntityTurretBase  extends TileEntityElectricBlock implements IRedstoneControl{
	
	public TurretBaseTier tier = TurretBaseTier.BASIC;

	/** The redstone level this Energy Cube is outputting at. */
	public int currentRedstoneLevel;

	/** This machine's current RedstoneControl type. */
	public RedstoneControl controlType;

	public int prevScale;

	public TileEntityTurretBase(String name, double baseMaxEnergy) {
		super(name, baseMaxEnergy);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_,
			int p_102007_3_) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_,
			int p_102008_3_) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canPulse() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RedstoneControl getControlType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setControlType(RedstoneControl arg0) {
		// TODO Auto-generated method stub
		
	}

}
