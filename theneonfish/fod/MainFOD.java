package futuristicoffensiveanddefenseive.theneonfish.fod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import futuristicoffensiveanddefenseive.theneonfish.fod.blocks.*;
import futuristicoffensiveanddefenseive.theneonfish.fod.energy.item.ElectricItemBase;
import futuristicoffensiveanddefenseive.theneonfish.fod.items.*;

@Mod(modid = MainFOD.MODID, version = MainFOD.VERSION)
public class MainFOD
{
    public static final String MODID = "FuturisticOffensiveandDefensive";
    public static final String VERSION = "0.01";
    
    public static Item testItem;
    public static Item detonator;
    public static Block testBlock;
    public static Block Nuke;
    
    public static ElectricItemBase basicBattery;
    public static CreativeTabs tabList = new CreativeTabs("FuturisticOffensiveandDefensive"){
    	@Override
    	@SideOnly(Side.CLIENT)
    	public Item getTabIconItem(){
    		return Items.ender_eye;
    	}
    };
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		this.basicBattery = (ElectricItemBase)new ElectricItemBase(10000).setUnlocalizedName("Battery");
		this.testBlock = new TestBlock(Material.tnt).setCreativeTab(tabList).setBlockName("TestBlock").setBlockTextureName("tnt");
		this.Nuke = new Nuke(Material.tnt).setCreativeTab(tabList).setBlockName("Nuke").setBlockTextureName("tnt");
		GameRegistry.registerBlock(Nuke, "Nuke");
		GameRegistry.registerItem(basicBattery, "basicBattery");
		this.detonator = new TestItem().setCreativeTab(tabList).setUnlocalizedName("Detonator");
		GameRegistry.registerItem(detonator, "Detonator");
		GameRegistry.registerBlock(testBlock, "TestBlock");
    }
}
