package futuristicoffensiveanddefenseive.theneonfish.fod.blocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import futuristicoffensiveanddefenseive.theneonfish.fod.FODBlocks;
import futuristicoffensiveanddefenseive.theneonfish.fod.FODRecipe;
import futuristicoffensiveanddefenseive.theneonfish.fod.MainFOD;
import futuristicoffensiveanddefenseive.theneonfish.fod.TileEntities.TileEntityTurretGun;
import futuristicoffensiveanddefenseive.theneonfish.fod.client.render.ICustomBlockIcon;
import futuristicoffensiveanddefenseive.theneonfish.fod.utils.LangUtils;
import mekanism.api.MekanismConfig.machines;
import mekanism.common.CTMData;
import mekanism.common.Mekanism;
import mekanism.common.base.IBlockCTM;
import mekanism.common.base.ISpecialBounds;
import mekanism.common.tile.TileEntityBasicBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Turret extends BlockContainer implements ISpecialBounds, IBlockCTM, ICustomBlockIcon{

	
	public TurretBlock blockType;
	


	
	public Turret(TurretBlock type) {
		super(Material.iron);
		setCreativeTab(MainFOD.tabList);
		setHardness(3.5F);
		setResistance(16F);
		blockType = type;
	
	}
	
	public static enum TurretBlock
	{
		TURRET_BLOCK_1,
		TURRET_BLOCK_2;

		public Block getBlock()
		{
			switch(this)
			{
				case TURRET_BLOCK_1:
					return FODBlocks.TurretBlock;
				case TURRET_BLOCK_2:
					return FODBlocks.TurretBlock2;
				default:
					return null;
			}
		}
	}
	
	
	public static enum TurretType
	{

		TURRET_CROSSBOW(TurretBlock.TURRET_BLOCK_1, 0, "Crossbow Turret", TileEntityTurretGun.class, true, true, false),
		TURRET_MACHINEGUN(TurretBlock.TURRET_BLOCK_1, 1, "Turretgun Turret", TileEntityTurretGun.class, true, true, true),
		TURRET_ROCKETLAUNCHER(TurretBlock.TURRET_BLOCK_1, 2, "Rocket Launcher Turret", TileEntityTurretGun.class, true, true, true),
		TURRET_PLASMA(TurretBlock.TURRET_BLOCK_1, 3, "Plasma Turret", TileEntityTurretGun.class, true, true, true),
		TURRET_LASER(TurretBlock.TURRET_BLOCK_2, 4, "Laser Turret", TileEntityTurretGun.class, true, true, true),
		TURRET_IRIDIUM(TurretBlock.TURRET_BLOCK_1, 5, "Railgun Turret", TileEntityTurretGun.class, true, true, true),
		TURRET_ANTIMATTER(TurretBlock.TURRET_BLOCK_1, 6, "Anti-Matter Turret", TileEntityTurretGun.class, true, true, true);

		public TurretBlock typeBlock;
		public int meta;
		public String name;
		public double baseEnergy;
		public Class<? extends TileEntity> tileEntityClass;
		public boolean isElectric;
		public boolean hasModel;
		public boolean supportsUpgrades;
		public Collection<FODRecipe> turretRecipes = new HashSet<FODRecipe>();
		

		private TurretType(TurretBlock block, int i, String s, Class<? extends TileEntity> tileClass, boolean electric, boolean model, boolean upgrades)
		{
			typeBlock = block;
			meta = i;
			name = s;
			tileEntityClass = tileClass;
			isElectric = electric;
			hasModel = model;
			supportsUpgrades = upgrades;

		}
		
		public boolean isEnabled()
		{
			return machines.isEnabled(this.name);
		}
		
		public void addRecipes(Collection<FODRecipe> recipes)
		{
			turretRecipes.addAll(recipes);
		}
		
		public void addRecipe(FODRecipe recipe)
		{
			turretRecipes.add(recipe);
		}
		
		public Collection<FODRecipe> getRecipes()
		{
			return turretRecipes;
		}
		
		public static List<TurretType> getValidTurrets()
		{
			List<TurretType> ret = new ArrayList<TurretType>();
			
			for(TurretType type : TurretType.values())
			{
					ret.add(type);
			}
			
			return ret;
		}

		public static TurretType get(Block block, int meta)
		{
			if(block instanceof Turret)
			{
				return get(((Turret)block).blockType, meta);
			}

			return null;
		}

		public static TurretType get(TurretBlock block, int meta)
		{
			for(TurretType type : values())
			{
				if(type.meta == meta && type.typeBlock == block)
				{
					return type;
				}
			}

			return null;
		}

		public TileEntity create()
		{
			try {
				return tileEntityClass.newInstance();
			} catch(Exception e) {
				Mekanism.logger.error("Unable to indirectly create tile entity.");
				e.printStackTrace();
				return null;
			}
		}

		/** Used for getting the base energy storage. */
		public double getUsage()
		{
			switch(this)
			{
				case TURRET_CROSSBOW:
					return 100;
				case TURRET_MACHINEGUN:
					return 10000;
				case TURRET_ROCKETLAUNCHER:
					return 10000;
				case TURRET_PLASMA:
					return 100000;
				case TURRET_LASER:
					return 100000000;
				case TURRET_IRIDIUM:
					return 100000000;
				case TURRET_ANTIMATTER:
					return 1000000000;
				default:
					return 0;
			}
		}

		public static void updateAllUsages()
		{
			for(TurretType type : values())
			{
				type.updateUsage();
			}
		}

		public void updateUsage()
		{
			baseEnergy = 400 * getUsage();
		}

		public String getDescription()
		{
			return LangUtils.localize("tooltip." + name);
		}

		public ItemStack getStack()
		{
			return new ItemStack(typeBlock.getBlock(), 1, meta);
		}

		public static TurretType get(ItemStack stack)
		{
			return get(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
		}
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		if(TurretType.get(this, metadata) == null)
		{
			return null;
		}

		return TurretType.get(this, metadata).create();
	}
	
	
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack itemstack)
	{
		TileEntityBasicBlock tileEntity = (TileEntityBasicBlock)world.getTileEntity(x, y, z);
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


		
		
		tileEntity.setFacing((short)change);
		tileEntity.redstone = world.isBlockIndirectlyGettingPowered(x, y, z);
	}



	@Override
	public CTMData getCTMData(IBlockAccess arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public boolean doDefaultBoundSetting(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void setRenderBounds(Block arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		return null;
	}


}
