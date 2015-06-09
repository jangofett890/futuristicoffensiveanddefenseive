package futuristicoffensiveanddefenseive.theneonfish.fod.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import futuristicoffensiveanddefenseive.theneonfish.fod.common.CommonProxy;

public class ClientProxy extends CommonProxy {
	
	@Override
	public EntityPlayer getPlayer(MessageContext context)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			return context.getServerHandler().playerEntity;
		}
		else {
			return Minecraft.getMinecraft().thePlayer;
		}
	}

}
