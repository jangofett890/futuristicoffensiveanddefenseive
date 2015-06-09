package futuristicoffensiveanddefenseive.theneonfish.fod.base;


import mekanism.api.transmitters.IBlockableConnection;
import mekanism.api.transmitters.ITransmitterTile;
import futuristicoffensiveanddefenseive.theneonfish.fod.common.InventoryNetwork;

import net.minecraft.inventory.IInventory;

public interface ITransporterTile extends ITransmitterTile<IInventory,InventoryNetwork>, IBlockableConnection
{
	public ILogisticalTransporter getTransmitter();
}