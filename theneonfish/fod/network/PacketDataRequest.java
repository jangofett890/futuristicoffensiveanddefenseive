package futuristicoffensiveanddefenseive.theneonfish.fod.network;

import java.util.ArrayList;

import mekanism.api.Coord4D;
import mekanism.api.transmitters.DynamicNetwork;
import mekanism.api.transmitters.IGridTransmitter;
import futuristicoffensiveanddefenseive.theneonfish.fod.MainFOD;
import futuristicoffensiveanddefenseive.theneonfish.fod.packet.PacketHandler;
import futuristicoffensiveanddefenseive.theneonfish.fod.base.ITileNetwork;
import futuristicoffensiveanddefenseive.theneonfish.fod.network.PacketDataRequest.DataRequestMessage;
import futuristicoffensiveanddefenseive.theneonfish.fod.network.PacketTileEntity.TileEntityMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketDataRequest implements IMessageHandler<DataRequestMessage, IMessage>
{
	@Override
	public IMessage onMessage(DataRequestMessage message, MessageContext context) 
	{
		EntityPlayer player = PacketHandler.getPlayer(context);
		World worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(message.coord4D.dimensionId);
		
		if(worldServer != null && message.coord4D.getTileEntity(worldServer) instanceof ITileNetwork)
		{
			TileEntity tileEntity = message.coord4D.getTileEntity(worldServer);


			if(tileEntity instanceof IGridTransmitter)
			{
				IGridTransmitter transmitter = (IGridTransmitter)tileEntity;

				if(transmitter.getTransmitterNetwork() instanceof DynamicNetwork)
				{
					((DynamicNetwork)transmitter.getTransmitterNetwork()).addUpdate(player);
				}
			}

			MainFOD.packetHandler.sendTo(new TileEntityMessage(Coord4D.get(tileEntity), ((ITileNetwork)tileEntity).getNetworkedData(new ArrayList())), (EntityPlayerMP)player);
		}
		
		return null;
	}
	
	public static class DataRequestMessage implements IMessage
	{
		public Coord4D coord4D;
		
		public DataRequestMessage() {}
	
		public DataRequestMessage(Coord4D coord)
		{
			coord4D = coord;
		}
		
		@Override
		public void toBytes(ByteBuf dataStream)
		{
			dataStream.writeInt(coord4D.xCoord);
			dataStream.writeInt(coord4D.yCoord);
			dataStream.writeInt(coord4D.zCoord);
	
			dataStream.writeInt(coord4D.dimensionId);
		}
	
		@Override
		public void fromBytes(ByteBuf dataStream)
		{
			coord4D = new Coord4D(dataStream.readInt(), dataStream.readInt(), dataStream.readInt(), dataStream.readInt());
		}
	}
}