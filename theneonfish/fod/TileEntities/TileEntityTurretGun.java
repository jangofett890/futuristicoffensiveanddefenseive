package futuristicoffensiveanddefenseive.theneonfish.fod.TileEntities;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import futuristicoffensiveanddefenseive.theneonfish.fod.Tier.TurretBaseTier;
import futuristicoffensiveanddefenseive.theneonfish.fod.blocks.Turret.TurretType;
import scala.actors.threadpool.Arrays;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import mekanism.common.base.IRedstoneControl.RedstoneControl;
import mekanism.common.tile.TileEntityElectricBlock;
import mekanism.common.util.MekanismUtils;

public abstract class TileEntityTurretGun extends TileEntityElectricBlock{
	
	public TurretType type = TurretType.TURRET_CROSSBOW;

	public TileEntityTurretGun(EntityPlayer owner) {
		super("Turret", 0);
		
		safePlayers.add(owner);
	}
	
	
	public static String turretName;
	public static int maxEnergy;
	public static boolean isActive;
	
	public static ArrayList safePlayers;
	
	public static String safePlayersString = safePlayers.toString();	
	
	
	
	@Override
	public void onUpdate(){
		
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		// TODO Auto-generated method stub
		return false;
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
		return 100000;
	}
	
	
	@Override
	public void handlePacketData(ByteBuf dataStream)
	{
		type = TurretType.values()[dataStream.readInt()];

		super.handlePacketData(dataStream);

		MekanismUtils.updateBlock(worldObj, xCoord, yCoord, zCoord);
	}

	@Override
	public ArrayList getNetworkedData(ArrayList data)
	{
		data.add(type.ordinal());

		super.getNetworkedData(data);


		return data;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTags)
	{
		super.readFromNBT(nbtTags);
		List<String> safePlayers = new ArrayList<String>(Arrays.asList(nbtTags.getString(safePlayersString).split(",")));

	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTags)
	{
		super.writeToNBT(nbtTags);

		nbtTags.setString("safeplayers", safePlayersString);
		nbtTags.setBoolean("isActive", isActive);
	}
	
}
