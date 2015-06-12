package futuristicoffensiveanddefenseive.theneonfish.fod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.potion.Potion;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;
import futuristicoffensiveanddefenseive.theneonfish.fod.API.*;
import futuristicoffensiveanddefenseive.theneonfish.fod.blocks.Turret;
import futuristicoffensiveanddefenseive.theneonfish.fod.blocks.TurretBase;

import static futuristicoffensiveanddefenseive.theneonfish.fod.blocks.Turret.TurretBlock.TURRET_BLOCK_1;
import static futuristicoffensiveanddefenseive.theneonfish.fod.blocks.Turret.TurretBlock.TURRET_BLOCK_2;


@ObjectHolder("FuturisticOffensiveandDefensive")
public class FODBlocks {
		
    public static final BaseExplosives condensedExsplosives = new BaseExplosives(Material.tnt, 100, 30).setBlockName("CondensedExplosives");
    public static final BaseExplosives nuke = new BaseExplosives(Material.tnt, 20, 500).setBlockName("Nuke").setEffect(Potion.poison).hasDetonator(true);
	public static final Block turretBase = new TurretBase().setBlockName("TurretBase");
	
	public static final Block TurretBlock = new Turret(TURRET_BLOCK_1).setBlockName("Null");
	public static final Block TurretBlock2 = new Turret(TURRET_BLOCK_2).setBlockName("Null");
	
    
    
	public static void register(){
		GameRegistry.registerBlock(condensedExsplosives, "Condensed Explosives");
		GameRegistry.registerBlock(nuke, "Nuke");
		GameRegistry.registerBlock(turretBase, "Turret Base");
	}
}
