package futuristicoffensiveanddefenseive.theneonfish.fod;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import futuristicoffensiveanddefenseive.theneonfish.fod.common.CommonProxy;

@net.minecraftforge.fml.common.Mod(modid = MainFOD.MODID, version = MainFOD.VERSION, name = "Futuristic Offensive and Deffensive", dependencies = "required-after:Mekanism" )
public class MainFOD
{
    public static final String MODID = "FuturisticOffensiveandDefensive";
    public static final String VERSION = "0.01";
    
    public static Configuration configuration;
	
	@net.minecraftforge.fml.common.Mod.Instance("FuturisticOffensiveandDefensive")
    public static MainFOD instance;
    
	@net.minecraftforge.fml.common.SidedProxy(clientSide="futuristicoffensiveanddefenseive.theneonfish.fod.common.ClientProxy", serverSide="futuristicoffensiveanddefenseive.theneonfish.fod.common.CommonProxy")
	public static CommonProxy proxy;
	
    public static CreativeTabs tabList = new CreativeTabs("FuturisticOffensiveandDefensive"){
    	@Override
    	@net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    	public Item getTabIconItem(){
    		return Items.ender_eye;
    	}
    };
    @net.minecraftforge.fml.common.Mod.EventHandler
    public void preInit(net.minecraftforge.fml.common.event.FMLPreInitializationEvent  event){
    	File config = event.getSuggestedConfigurationFile();
		
		//Set the mod's configuration
		configuration = new Configuration(config);
		
    	FODBlocks.register();
    	FODItems.register(); 
    }
    @net.minecraftforge.fml.common.Mod.EventHandler
    public void init(net.minecraftforge.fml.common.event.FMLInitializationEvent event)
    {    	
	proxy.loadConfiguration();
	proxy.onConfigSync(false);

    	FODEntities.register();

    }
}
