package futuristicoffensiveanddefenseive.theneonfish.fod.blocks;

import java.util.Random;

import futuristicoffensiveanddefenseive.theneonfish.fod.FODBlocks;
import futuristicoffensiveanddefenseive.theneonfish.fod.MainFOD;
import futuristicoffensiveanddefenseive.theneonfish.fod.Tier;
import futuristicoffensiveanddefenseive.theneonfish.fod.Tier.TurretBaseTier;
import futuristicoffensiveanddefenseive.theneonfish.fod.TileEntities.TileEntityBasicBlockFOD;
import futuristicoffensiveanddefenseive.theneonfish.fod.TileEntities.TileEntityTurretBase;
import futuristicoffensiveanddefenseive.theneonfish.fod.common.ITurretBase;
import futuristicoffensiveanddefenseive.theneonfish.fod.utils.ISustainedInventory;
import mekanism.api.energy.IEnergizedItem;
import mekanism.common.tile.TileEntityBasicBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class TurretBase extends BlockContainer{

	public TurretBase() {
		super(Material.iron);
		setHardness(2F);
		setResistance(4F);
		setCreativeTab(MainFOD.tabList);
	}
	
	public static boolean hasTurret(int meta){
		return (meta & 8) != 0;
	}
	
	public static EntityPlayer owner;
	public static int baseEnergy;
	public static double perTick;
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack itemstack)
	{
		TileEntityBasicBlockFOD tileEntity = (TileEntityBasicBlockFOD)world.getTileEntity(x, y, z);
		int side = MathHelper.floor_double((double)(entityliving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int height = Math.round(entityliving.rotationPitch);
		int change = 3;

		if(height >= 65)
		{
			change = 1;
		}
		else if(height <= -65)
		{
			change = 0;
		}
		else {
			switch(side)
			{
				case 0: change = 2; break;
				case 1: change = 5; break;
				case 2: change = 3; break;
				case 3: change = 4; break;
			}
		}
		if(entityliving instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) entityliving;
			this.owner = player;
		}

		tileEntity.setFacing((short)change);
		tileEntity.redstone = world.isBlockIndirectlyGettingPowered(x, y, z);
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}


	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
	{
		if(!player.capabilities.isCreativeMode && !world.isRemote && canHarvestBlock(player, world.getBlockMetadata(x, y, z)))
		{
			float motion = 0.7F;
			double motionX = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
			double motionY = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
			double motionZ = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;

			EntityItem entityItem = new EntityItem(world, x + motionX, y + motionY, z + motionZ, getPickBlock(null, world, x, y, z));

			world.spawnEntityInWorld(entityItem);
		}

		return world.setBlockToAir(x, y, z);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{	
		TileEntityTurretBase tile = new TileEntityTurretBase();
		return tile;
	}
	
	
	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
	{
	        if (player.capabilities.isCreativeMode)
	        {
	            Block block = world.getBlock(x, y, z +1 );

	            if (block == FODBlocks.TurretBlock || block == FODBlocks.TurretBlock2)
	            {
	                world.setBlockToAir(x, y, z + 1);
	            }
	        }

	        super.onBlockHarvested(world, x, y, z, meta, player);
	 }
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
	        super.breakBlock(world, x, y, z, block, meta);
	        Block block1 = world.getBlock(x, y, z + 1);

	        if (block == FODBlocks.TurretBlock || block == FODBlocks.TurretBlock2)
	        {
	            meta = world.getBlockMetadata(x, y, z + 1);

	            if (this.hasTurret(meta))
	            {
	                block1.dropBlockAsItem(world, x, y, z + 1, meta, 0);
	                world.setBlockToAir(x, y, z + 1);
	            }
	        }
	}
	
	
	
	

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return Blocks.piston.getRenderType();
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		TileEntityTurretBase tileEntity = (TileEntityTurretBase)world.getTileEntity(x, y, z);
		ItemStack itemStack = new ItemStack(FODBlocks.turretBase);

		ITurretBase turretBase = (ITurretBase)itemStack.getItem();
		turretBase.setTurretBaseTier(itemStack, tileEntity.tier);

		IEnergizedItem energizedItem = (IEnergizedItem)itemStack.getItem();
		energizedItem.setEnergy(itemStack, tileEntity.electricityStored);

		ISustainedInventory inventory = (ISustainedInventory)itemStack.getItem();
		inventory.setInventory(((ISustainedInventory)tileEntity).getInventory(), itemStack);

		return itemStack;
	}

	public ItemStack dismantleBlock(World world, int x, int y, int z, boolean returnBlock)
	{
		ItemStack itemStack = getPickBlock(null, world, x, y, z);

		world.setBlockToAir(x, y, z);

		if(!returnBlock)
		{
			float motion = 0.7F;
			double motionX = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
			double motionY = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
			double motionZ = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;

			EntityItem entityItem = new EntityItem(world, x + motionX, y + motionY, z + motionZ, itemStack);

			world.spawnEntityInWorld(entityItem);
		}

		return itemStack;
	}
}
