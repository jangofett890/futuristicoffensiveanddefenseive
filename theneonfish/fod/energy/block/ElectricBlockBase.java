package futuristicoffensiveanddefenseive.theneonfish.fod.energy.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ElectricBlockBase extends BlockContainer{

	protected ElectricBlockBase(Material material) {
		super(material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		// TODO Auto-generated method stub
		return null;
	}

}
