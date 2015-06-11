package futuristicoffensiveanddefenseive.theneonfish.fod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.potion.Potion;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;
import futuristicoffensiveanddefenseive.theneonfish.fod.API.*;
import futuristicoffensiveanddefenseive.theneonfish.fod.blocks.TurretBase;


@ObjectHolder("FuturisticOffensiveandDefensive")
public class FODBlocks {
		
    public static final BaseExplosives condensedExsplosives = new BaseExplosives(Material.tnt, 100, 30).setBlockName("CondensedExplosives");
    public static final BaseExplosives nuke = new BaseExplosives(Material.tnt, 20, 500).setBlockName("Nuke").setEffect(Potion.poison).hasDetonator(true);
	public static final Block turretBase = new TurretBase().setBlockName("TurretBase");
	
    
    
	public static void register(){
		GameRegistry.registerBlock(condensedExsplosives, "Condensed Explosives");
		GameRegistry.registerBlock(nuke, "Nuke");
		GameRegistry.registerBlock(turretBase, "Turret Base");
	}
}
