package futuristicoffensiveanddefenseive.theneonfish.fod.common;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public class CommonProxy {
	
	
	public EntityPlayer getPlayer(MessageContext context)
	{
		return context.getServerHandler().playerEntity;
	}
}
