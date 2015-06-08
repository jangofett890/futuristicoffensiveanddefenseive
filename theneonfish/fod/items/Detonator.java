package futuristicoffensiveanddefenseive.theneonfish.fod.items;

import cpw.mods.fml.common.Mod.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import futuristicoffensiveanddefenseive.theneonfish.fod.MainFOD;
import futuristicoffensiveanddefenseive.theneonfish.fod.API.BaseExplosives;
import futuristicoffensiveanddefenseive.theneonfish.fod.blocks.Nuke;

public class Detonator extends BaseExplosives {
	public Block nuke = MainFOD.Nuke;
	boolean detonate = Nuke.hasDetonator;
	public Detonator(){
		super();
	}
	public void detonatorInteract(World w, int x, int y, int z, EntityPlayer p){
		if(p.getCurrentEquippedItem() != null && p.getCurrentEquippedItem().getItem() == Items.arrow){
			this.onBlockDestroyedByPlayer(w, z, y, z, 1);
			w.setBlockToAir(x, y, z);
		}
		return;
	}
	
}
