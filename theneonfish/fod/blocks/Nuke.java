package futuristicoffensiveanddefenseive.theneonfish.fod.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.potion.Potion;
import futuristicoffensiveanddefenseive.theneonfish.fod.MainFOD;
import futuristicoffensiveanddefenseive.theneonfish.fod.API.BaseExplosivePrimed;
import futuristicoffensiveanddefenseive.theneonfish.fod.API.BaseExplosives;

public class Nuke extends BaseExplosives {
	public Nuke(Material arg0) {
		super(arg0);
		this.fuse = 20;
		this.force = 50;
		this.effectName = Potion.wither;
		this.hasEffect = true;
	}
	
}
