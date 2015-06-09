package futuristicoffensiveanddefenseive.theneonfish.fod.intergration;

import cpw.mods.fml.common.Loader;

public class FODHooks {
	
	public static boolean IC2Loaded = false;
	public static boolean CoFHCoreLoaded = false;
	public boolean TELoaded = false;
	
	public void hook()
	{
		if(Loader.isModLoaded("CoFHCore")) CoFHCoreLoaded = true;
		if(Loader.isModLoaded("IC2")) IC2Loaded = true;
		if(Loader.isModLoaded("ThermalExpansion")) TELoaded = true;
	}
}
