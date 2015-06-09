package futuristicoffensiveanddefenseive.theneonfish.fod.items;

import futuristicoffensiveanddefenseive.theneonfish.fod.MainFOD;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class basicItem extends Item{
	public basicItem(){
		super();
		setCreativeTab(MainFOD.tabList);
	}
	
	@Override
	public void registerIcons(IIconRegister register)
	{
		itemIcon = register.registerIcon("fod:" + getUnlocalizedName().replace("item.", ""));
	}
}
