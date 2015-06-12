package futuristicoffensiveanddefenseive.theneonfish.fod.common;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import futuristicoffensiveanddefenseive.theneonfish.fod.MainFOD;
import futuristicoffensiveanddefenseive.theneonfish.fod.Tier;

public class CommonProxy {

	   public void preInit(FMLPreInitializationEvent e) {

	   }

	   public void init(FMLInitializationEvent e) {

	   }

	   public void postInit(FMLPostInitializationEvent e) {

	   }
		public void loadConfiguration()
		{
			Tier.loadConfig();
			
			if(MainFOD.configuration.hasChanged())
			{
				MainFOD.configuration.save();
			}
		}

		public void onConfigSync(boolean fromPacket) {
			// TODO Auto-generated method stub
			
		}



}
