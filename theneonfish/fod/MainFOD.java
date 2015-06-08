package futuristicoffensiveanddefenseive.theneonfish.fod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import futuristicoffensiveanddefenseive.theneonfish.fod.items.TestBlock;
import futuristicoffensiveanddefenseive.theneonfish.fod.items.TestItem;

@Mod(modid = MainFOD.MODID, version = MainFOD.VERSION)
public class MainFOD
{
    public static final String MODID = "FuturisticOffensiveandDefensive";
    public static final String VERSION = "0.01";
    
    public static Item testItem;
    public static Block testBlock;
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
		this.testItem = new TestItem().setCreativeTab(tabList).setUnlocalizedName("TestItem");
		this.testBlock = new TestBlock(Material.tnt).setCreativeTab(tabList).setBlockName("TestBlock");
		GameRegistry.registerItem(testItem, "TestItem");
		GameRegistry.registerBlock(testBlock, "TestBlock");
    }

}
