package futuristicoffensiveanddefenseive.theneonfish.fod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = MainFOD.MODID, version = MainFOD.VERSION, name = "Futuristic Offensive and Deffensive", dependencies = "required-after:Mekanism" )
public class MainFOD
{
    public static final String MODID = "FuturisticOffensiveandDefensive";
    public static final String VERSION = "0.01";
	
	@Instance("FuturisticOffensiveandDefensive")
    public static MainFOD instance;
    
    public static CreativeTabs tabList = new CreativeTabs("FuturisticOffensiveandDefensive"){
    	@Override
    	@SideOnly(Side.CLIENT)
    	public Item getTabIconItem(){
    		return Items.ender_eye;
    	}
    };
    @EventHandler
    public void preInit(FMLInitializationEvent event){
    }
    
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {    	
    	FODEntities.register();
    	FODBlocks.register();
    	FODItems.register();    
    }
}
