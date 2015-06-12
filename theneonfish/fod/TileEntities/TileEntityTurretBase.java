package futuristicoffensiveanddefenseive.theneonfish.fod.TileEntities;

import java.util.Map;

import futuristicoffensiveanddefenseive.theneonfish.fod.Tier;
import futuristicoffensiveanddefenseive.theneonfish.fod.Tier.TurretBaseTier;
import futuristicoffensiveanddefenseive.theneonfish.fod.common.IUpgradeTile;
import futuristicoffensiveanddefenseive.theneonfish.fod.tile.component.TileComponentConfig;
import futuristicoffensiveanddefenseive.theneonfish.fod.tile.component.TileComponentUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasHandler;
import mekanism.api.gas.ITubeConnection;
import mekanism.common.base.IEjector;
import mekanism.common.base.IElectricMachine;
import mekanism.common.base.IRedstoneControl;
import mekanism.common.base.ISideConfiguration;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.MachineRecipe;
import mekanism.common.tile.TileEntityElectricBlock;
import mekanism.common.tile.component.TileComponentEjector;

public class TileEntityTurretBase  extends TileEntityElectricBlock implements  ISideConfiguration, IUpgradeTile, IRedstoneControl, IGasHandler, ITubeConnection{
	
	public TurretBaseTier tier;
	
	/** How much energy this machine uses per tick, un-upgraded. */
	public double BASE_ENERGY_PER_TICK;

	/**	How much energy this machine uses per tick including upgrades */
	public double energyPerTick;

	/** How many ticks this machine has operated for. */
	public int operatingTicks = 0;

	/** Ticks required including upgrades */
	public int ticksRequired;

	/** How many ticks must pass until this block's active state can sync with the client. */
	public int updateDelay;

	/** Whether or not this block is in it's active state. */
	public boolean isActive;

	/** The client's current active state. */
	public boolean clientActive;

	/** The GUI texture path for this machine. */
	public ResourceLocation guiLocation;

	/** This machine's current RedstoneControl type. */
	public RedstoneControl controlType = RedstoneControl.DISABLED;

	/** This machine's previous amount of energy. */
	public double prevEnergy;

	public TileComponentUpgrade upgradeComponent;
	public TileComponentConfig configComponent;

	/** The redstone level this Energy Cube is outputting at. */
	public int currentRedstoneLevel;
	
	public Tier.TurretBaseTier turretTier;


	public int prevScale;

	public TileEntityTurretBase(String name, double perTick, double maxEnergy, EntityPlayer owner, Tier.TurretBaseTier tier) {
		super(name, maxEnergy);
		BASE_ENERGY_PER_TICK = perTick;
		energyPerTick = perTick;
		isActive = false;
		turretTier = tier;
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
		return tier.getBaseTier().getLocalizedName() + " " + "Turret";
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return true;
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
	public boolean canTubeConnect(ForgeDirection side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int receiveGas(ForgeDirection side, GasStack stack,
			boolean doTransfer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int receiveGas(ForgeDirection side, GasStack stack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public GasStack drawGas(ForgeDirection side, int amount, boolean doTransfer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GasStack drawGas(ForgeDirection side, int amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canReceiveGas(ForgeDirection side, Gas type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDrawGas(ForgeDirection side, Gas type) {
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

	@Override
	public TileComponentUpgrade getComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public mekanism.common.tile.component.TileComponentConfig getConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEjector getEjector() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOrientation() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}


}
