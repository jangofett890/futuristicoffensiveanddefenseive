package futuristicoffensiveanddefenseive.theneonfish.fod;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import futuristicoffensiveanddefenseive.theneonfish.fod.API.BaseExplosivePrimed;

public class FODEntities {
	public static void register(){
		EntityRegistry.registerGlobalEntityID(BaseExplosivePrimed.class, "Primed", EntityRegistry.findGlobalUniqueEntityId());
	}
}
