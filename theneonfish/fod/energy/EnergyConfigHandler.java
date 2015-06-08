package futuristicoffensiveanddefenseive.theneonfish.fod.energy;

import net.minecraftforge.common.config.Configuration;

public class EnergyConfigHandler {
	
	public static Configuration config;
	
	
	/** 
	 * This is the conversion factor for Universial Electricity MJ (Our base energy) to EU
	 * EU is used by IC2
	 * so therefore EU = MJ * 2.43 So to get EU from our MJ we have the conversion factor here
	 * **/
	public static float IC2_RATIO = 2.43f;
	
	//This is the conversion factor for TE3 (Thermal Exspansion 3)
	public static float TE3_RATIO = 10.0f;
	
	//For Mekanism
	public static float M_RATIO = 0.04f;

}
