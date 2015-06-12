package futuristicoffensiveanddefenseive.theneonfish.fod.TileEntities;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Map;

import cpw.mods.fml.common.Optional.Method;
import futuristicoffensiveanddefenseive.theneonfish.fod.Tier;
import futuristicoffensiveanddefenseive.theneonfish.fod.Tier.TurretBaseTier;
import futuristicoffensiveanddefenseive.theneonfish.fod.common.IUpgradeTile;
import futuristicoffensiveanddefenseive.theneonfish.fod.tile.component.TileComponentConfig;
import futuristicoffensiveanddefenseive.theneonfish.fod.tile.component.TileComponentUpgrade;
import futuristicoffensiveanddefenseive.theneonfish.fod.utils.FODChargeUtils;
import futuristicoffensiveanddefenseive.theneonfish.fod.utils.FODUtils;
import futuristicoffensiveanddefenseive.theneonfish.fod.utils.LangUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import mekanism.api.Coord4D;
import mekanism.api.Range4D;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasHandler;
import mekanism.api.gas.ITubeConnection;
import mekanism.common.Mekanism;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.base.IEjector;
import mekanism.common.base.IElectricMachine;
import mekanism.common.base.IRedstoneControl;
import mekanism.common.base.ISideConfiguration;
import mekanism.common.recipe.inputs.MachineInput;
import mekanism.common.recipe.machines.MachineRecipe;
import mekanism.common.tile.TileEntityElectricBlock;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.CableUtils;
import mekanism.common.util.MekanismUtils;

public class TileEntityTurretBase  extends TileEntityElectricBlockFOD implements  ISideConfiguration, IUpgradeTile, IRedstoneControl, IGasHandler, ITubeConnection{
	
	/** This Energy Cube's tier. */
	public TurretBaseTier tier = TurretBaseTier.BASIC;

	/** The redstone level this Energy Cube is outputting at. */
	public int currentRedstoneLevel;

	/** This machine's current RedstoneControl type. */
	public RedstoneControl controlType;

	public int prevScale;

	public TileEntityTurretBase() {
		super("Turret Base", 0);
	}


	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if(!worldObj.isRemote)
		{
			FODChargeUtils.charge(0, this);
			FODChargeUtils.discharge(1, this);
	
			if(FODUtils.canFunction(this))
			{
				CableUtils.emit(this);
			}
			
			int newScale = getScaledEnergyLevel(20);
	
			if(newScale != prevScale)
			{
				Mekanism.packetHandler.sendToReceivers(new TileEntityMessage(Coord4D.get(this), getNetworkedData(new ArrayList())), new Range4D(Coord4D.get(this)));
			}
	
			prevScale = newScale;
		}
	}

	@Override
	public String getInventoryName()
	{
		return LangUtils.localize("tile.TurretBase" + tier.getBaseTier().getName() + ".name");
	}

	@Override
	public double getMaxOutput()
	{
		return 0;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
	{
		if(slotID == 0)
		{
			return FODChargeUtils.canBeCharged(itemstack);
		}
		else if(slotID == 1)
		{
			return FODChargeUtils.canBeDischarged(itemstack);
		}

		return true;
	}

	@Override
	public EnumSet<ForgeDirection> getConsumingSides()
	{
		EnumSet set = EnumSet.allOf(ForgeDirection.class);
		set.removeAll(getOutputtingSides());
		set.remove(ForgeDirection.UNKNOWN);

		return set;
	}

	@Override
	public EnumSet<ForgeDirection> getOutputtingSides()
	{
		return EnumSet.of(ForgeDirection.getOrientation(facing));
	}

	@Override
	public boolean canSetFacing(int side)
	{
		return true;
	}

	@Override
	public double getMaxEnergy()
	{
		return tier.maxEnergy;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return side <= 1 ? new int[] {0} : new int[] {1};
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
	{
		if(slotID == 1)
		{
			return FODChargeUtils.canBeOutputted(itemstack, false);
		}
		else if(slotID == 0)
		{
			return FODChargeUtils.canBeOutputted(itemstack, true);
		}

		return false;
	}

	@Override
	public void handlePacketData(ByteBuf dataStream)
	{
		tier = TurretBaseTier.values()[dataStream.readInt()];

		super.handlePacketData(dataStream);

		controlType = RedstoneControl.values()[dataStream.readInt()];

		MekanismUtils.updateBlock(worldObj, xCoord, yCoord, zCoord);
	}

	@Override
	public ArrayList getNetworkedData(ArrayList data)
	{
		data.add(tier.ordinal());

		super.getNetworkedData(data);


		return data;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTags)
	{
		super.readFromNBT(nbtTags);

		tier = TurretBaseTier.getFromName(nbtTags.getString("tier"));
		controlType = RedstoneControl.values()[nbtTags.getInteger("controlType")];
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTags)
	{
		super.writeToNBT(nbtTags);

		nbtTags.setString("tier", tier.getBaseTier().getName());
		nbtTags.setInteger("controlType", controlType.ordinal());
	}

	@Override
	public void setEnergy(double energy)
	{
		if(tier == TurretBaseTier.CREATIVE && energy != Integer.MAX_VALUE)
		{
			return;
		}
		
		super.setEnergy(energy);

		int newRedstoneLevel = getRedstoneLevel();

		if(newRedstoneLevel != currentRedstoneLevel)
		{
			markDirty();
			currentRedstoneLevel = newRedstoneLevel;
		}
	}

	public int getRedstoneLevel()
	{
		double fractionFull = getEnergy()/getMaxEnergy();
		return MathHelper.floor_float((float)(fractionFull * 14.0F)) + (fractionFull > 0 ? 1 : 0);
	}

	@Override
	public RedstoneControl getControlType()
	{
		return controlType;
	}

	@Override
	public void setControlType(RedstoneControl type)
	{
		controlType = type;
	}

	@Override
	public boolean canPulse()
	{
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
}
