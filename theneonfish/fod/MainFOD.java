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
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import futuristicoffensiveanddefenseive.theneonfish.fod.blocks.*;
import futuristicoffensiveanddefenseive.theneonfish.fod.common.CommonProxy;
import futuristicoffensiveanddefenseive.theneonfish.fod.energy.item.ElectricItemBase;
import futuristicoffensiveanddefenseive.theneonfish.fod.items.*;
import futuristicoffensiveanddefenseive.theneonfish.fod.packet.PacketHandler;

@Mod(modid = MainFOD.MODID, version = MainFOD.VERSION, name = "Futuristic Offensive and Deffensive", dependencies = "required-after:Mekanism" )
public class MainFOD
{
    public static final String MODID = "FuturisticOffensiveandDefensive";
    public static final String VERSION = "0.01";
    
    public static PacketHandler packetHandler = new PacketHandler();
    
	@SidedProxy(clientSide = "futuristicoffensiveanddefenseive.theneonfish.fod.client.ClientProxy", serverSide = "futuristicoffensiveanddefenseive.theneonfish.fod.common.CommonProxy")
	public static CommonProxy proxy;
    
    public static CreativeTabs tabList = new CreativeTabs("FuturisticOffensiveandDefensive"){
    	@Override
    	@SideOnly(Side.CLIENT)
    	public Item getTabIconItem(){
    		return Items.ender_eye;
    	}
    };
    @EventHandler
    public void preInit(FMLInitializationEvent event){
    	FODBlocks.register();
    	FODItems.register();
    }
    
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
}
