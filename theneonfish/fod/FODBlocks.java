package futuristicoffensiveanddefenseive.theneonfish.fod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.potion.Potion;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;
import cpw.mods.fml.common.registry.LanguageRegistry;
import futuristicoffensiveanddefenseive.theneonfish.fod.API.*;
import futuristicoffensiveanddefenseive.theneonfish.fod.blocks.Turret;
import futuristicoffensiveanddefenseive.theneonfish.fod.blocks.TurretBase;


import futuristicoffensiveanddefenseive.theneonfish.fod.items.TurretBaseItem;
import futuristicoffensiveanddefenseive.theneonfish.fod.items.TurretItem;
import static futuristicoffensiveanddefenseive.theneonfish.fod.blocks.Turret.TurretBlock.TURRET_BLOCK_1;
import static futuristicoffensiveanddefenseive.theneonfish.fod.blocks.Turret.TurretBlock.TURRET_BLOCK_2;


@ObjectHolder("FuturisticOffensiveandDefensive")
public class FODBlocks {
	public static CreativeTabs tab = MainFOD.tabList;
	//public static final Block condensedExsplosives = new BaseExplosives(Material.tnt, 10, 30, false, "CondencedExplosive").setBlockName("CondensedExplosives").setCreativeTab(tab);
    //public static final Block nuke = new BaseExplosives(Material.tnt, 20, 1000, true, "Nuke").setBlockName("Nuke").setCreativeTab(tab);
	public static final Block Test = new BaseExplosives(Material.tnt, 4, 10, true, "Test").setBlockName("Test").setCreativeTab(tab);
	public static final Block turretBase = new TurretBase().setBlockName("TurretBase");
	public static final Block TurretBlock = new Turret(TURRET_BLOCK_1).setBlockName("Null");
	public static final Block TurretBlock2 = new Turret(TURRET_BLOCK_2).setBlockName("Null");
	
    
    
	public static void register(){
		//GameRegistry.registerBlock(condensedExsplosives, "Condensed Explosives");
		//GameRegistry.registerBlock(((BaseExplosives) nuke).setTab(tab).setEffect(Potion.poison).hasDetonator(true), "Nuke");
		GameRegistry.registerBlock(turretBase, TurretBaseItem.class, "Turret Base");
		GameRegistry.registerBlock(TurretBlock, TurretItem.class, "Turret");
		GameRegistry.registerBlock(Test, "Test");
		LanguageRegistry.addName(Test, "Test");
	}
}
