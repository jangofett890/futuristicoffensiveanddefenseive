package futuristicoffensiveanddefenseive.theneonfish.fod.items;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import futuristicoffensiveanddefenseive.theneonfish.fod.API.BaseExplosivePrimed;
import futuristicoffensiveanddefenseive.theneonfish.fod.API.BaseExplosives;

public class TestBlock extends BaseExplosives {

	public TestBlock(Material arg0) {
		super(arg0);
		this.fuse = 100;
		this.force = 30;
	}
	
}
