package futuristicoffensiveanddefenseive.theneonfish.fod;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import futuristicoffensiveanddefenseive.theneonfish.fod.common.CommonProxy;

@Mod(modid = MainFOD.MODID, version = MainFOD.VERSION, name = "Futuristic Offensive and Deffensive", dependencies = "required-after:Mekanism" )
public class MainFOD
{
    public static final String MODID = "FuturisticOffensiveandDefensive";
    public static final String VERSION = "0.01";
    
    public static Configuration configuration;
	
	@Instance("FuturisticOffensiveandDefensive")
    public static MainFOD instance;
    
	@SidedProxy(clientSide="futuristicoffensiveanddefenseive.theneonfish.fod.common.ClientProxy", serverSide="futuristicoffensiveanddefenseive.theneonfish.fod.common.CommonProxy")
	public static CommonProxy proxy;
	
    public static CreativeTabs tabList = new CreativeTabs("FuturisticOffensiveandDefensive"){
    	@Override
    	@SideOnly(Side.CLIENT)
    	public Item getTabIconItem(){
    		return Items.ender_eye;
    	}
    };
    @EventHandler
    public void preInit(FMLPreInitializationEvent  event){
    	File config = event.getSuggestedConfigurationFile();
		
		//Set the mod's configuration
		configuration = new Configuration(config);
		
    	FODBlocks.register();
    	FODItems.register(); 
    }
    @EventHandler
    public void init(FMLInitializationEvent event)
    {    	
	proxy.loadConfiguration();
	proxy.onConfigSync(false);

    	FODEntities.register();

    }
}
