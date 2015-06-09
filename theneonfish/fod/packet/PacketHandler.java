package futuristicoffensiveanddefenseive.theneonfish.fod.packet;

import futuristicoffensiveanddefenseive.theneonfish.fod.MainFOD;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import mekanism.api.Range4D;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * Mekanism packet handler. As always, use packets sparingly!
 * @author AidanBrady
 *
 */
public class PacketHandler
{
	public SimpleNetworkWrapper netHandler = NetworkRegistry.INSTANCE.newSimpleChannel("MEK");
	
	public void initialize()
	{
	}
	
	/**
	 * Encodes an Object[] of data into a DataOutputStream.
	 * @param dataValues - an Object[] of data to encode
	 * @param output - the output stream to write to
	 */
	public static void encode(Object[] dataValues, ByteBuf output)
	{
		try {
			for(Object data : dataValues)
			{
				if(data instanceof Integer)
				{
					output.writeInt((Integer)data);
				}
				else if(data instanceof Boolean)
				{
					output.writeBoolean((Boolean)data);
				}
				else if(data instanceof Double)
				{
					output.writeDouble((Double)data);
				}
				else if(data instanceof Float)
				{
					output.writeFloat((Float)data);
				}
				else if(data instanceof String)
				{
					writeString(output, (String)data);
				}
				else if(data instanceof Byte)
				{
					output.writeByte((Byte)data);
				}
				else if(data instanceof ItemStack)
				{
					writeStack(output, (ItemStack)data);
				}
				else if(data instanceof NBTTagCompound)
				{
					writeNBT(output, (NBTTagCompound)data);
				}
				else if(data instanceof int[])
				{
					for(int i : (int[])data)
					{
						output.writeInt(i);
					}
				}
				else if(data instanceof byte[])
				{
					for(byte b : (byte[])data)
					{
						output.writeByte(b);
					}
				}
				else if(data instanceof ArrayList)
				{
					encode(((ArrayList)data).toArray(), output);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeString(ByteBuf output, String s)
	{
		output.writeInt(s.getBytes().length);
		output.writeBytes(s.getBytes());
	}
	
	public static String readString(ByteBuf input)
	{
		return new String(input.readBytes(input.readInt()).array());
	}
	
	public static void writeStack(ByteBuf output, ItemStack stack)
	{
		output.writeInt(stack != null ? Item.getIdFromItem(stack.getItem()) : -1);
		
		if(stack != null)
		{
			output.writeInt(stack.stackSize);
			output.writeInt(stack.getItemDamage());
			
			if(stack.getTagCompound() != null && stack.getItem().getShareTag())
			{
				output.writeBoolean(true);
				writeNBT(output, stack.getTagCompound());
			}
			else {
				output.writeBoolean(false);
			}
		}
	}
	
	public static ItemStack readStack(ByteBuf input)
	{
		int id = input.readInt();
		
		if(id >= 0)
		{
			ItemStack stack = new ItemStack(Item.getItemById(id), input.readInt(), input.readInt());
			
			if(input.readBoolean())
			{
				stack.setTagCompound(readNBT(input));
			}
			
			return stack;
		}
		
		return null;
	}
	
	public static void writeNBT(ByteBuf output, NBTTagCompound nbtTags)
	{
		try {
			byte[] buffer = CompressedStreamTools.compress(nbtTags);
			
			output.writeInt(buffer.length);
			output.writeBytes(buffer);
		} catch(Exception e) {}
	}
	
	public static NBTTagCompound readNBT(ByteBuf input)
	{
		try {
			byte[] buffer = new byte[input.readInt()];
			input.readBytes(buffer);
			
			return CompressedStreamTools.func_152457_a(buffer, new NBTSizeTracker(2097152L));
		} catch(Exception e) {
			return null;
		}
	}
	
	public static EntityPlayer getPlayer(MessageContext context)
	{
		return MainFOD.proxy.getPlayer(context);
	}


	/**
	 * Send this message to the specified player.
	 * @param message - the message to send
	 * @param player - the player to send it to
	 */
	public void sendTo(IMessage message, EntityPlayerMP player)
	{
		netHandler.sendTo(message, player);
	}

	/**
	 * Send this message to everyone within a certain range of a point.
	 *
	 * @param message - the message to send
	 * @param point - the TargetPoint around which to send
	 */
	public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point)
	{
		netHandler.sendToAllAround(message, point);
	}

	/**
	 * Send this message to everyone within the supplied dimension.
	 * @param message - the message to send
	 * @param dimensionId - the dimension id to target
	 */
	public void sendToDimension(IMessage message, int dimensionId)
	{
		netHandler.sendToDimension(message, dimensionId);
	}

	/**
	 * Send this message to the server.
	 * @param message - the message to send
	 */
	public void sendToServer(IMessage message)
	{
		netHandler.sendToServer(message);
	}
	
	/**
	 * Send this message to all players within a defined AABB cuboid.
	 * @param message - the message to send
	 * @param cuboid - the AABB cuboid to send the packet in
	 * @param dimId - the dimension the cuboid is in
	 */
	public void sendToCuboid(IMessage message, AxisAlignedBB cuboid, int dimId)
	{
		MinecraftServer server = MinecraftServer.getServer();

		if(server != null && cuboid != null)
		{
			for(EntityPlayerMP player : (List<EntityPlayerMP>)server.getConfigurationManager().playerEntityList)
			{
				if(player.dimension == dimId && cuboid.isVecInside(Vec3.createVectorHelper(player.posX, player.posY, player.posZ)))
				{
					sendTo(message, player);
				}
			}
		}
	}
	
	public void sendToReceivers(IMessage message, Range4D range)
	{
		MinecraftServer server = MinecraftServer.getServer();

		if(server != null)
		{
			for(EntityPlayerMP player : (List<EntityPlayerMP>)server.getConfigurationManager().playerEntityList)
			{
				if(player.dimension == range.dimensionId && Range4D.getChunkRange(player).intersects(range))
				{
					sendTo(message, player);
				}
			}
		}
	}
}