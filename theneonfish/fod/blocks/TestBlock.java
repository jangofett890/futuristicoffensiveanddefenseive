package futuristicoffensiveanddefenseive.theneonfish.fod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import futuristicoffensiveanddefenseive.theneonfish.fod.MainFOD;
import futuristicoffensiveanddefenseive.theneonfish.fod.API.BaseExplosivePrimed;
import futuristicoffensiveanddefenseive.theneonfish.fod.API.BaseExplosives;

public class TestBlock extends BaseExplosives {

	public TestBlock(Material arg0) {
		super();
		this.fuse = 100;
		this.force = 30;
	}
}
