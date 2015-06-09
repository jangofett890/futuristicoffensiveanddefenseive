package futuristicoffensiveanddefenseive.theneonfish.fod.common;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommonProxy {
	
	public static int CTM_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
	
	
	public EntityPlayer getPlayer(MessageContext context)
	{
		return context.getServerHandler().playerEntity;
	}

	
	public double getReach(EntityPlayer player)
	{
		if(player instanceof EntityPlayerMP)
		{
			return ((EntityPlayerMP)player).theItemInWorldManager.getBlockReachDistance();
		}

		return 0;
	}
}
