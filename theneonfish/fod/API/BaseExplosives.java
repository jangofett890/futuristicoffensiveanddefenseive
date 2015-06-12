package futuristicoffensiveanddefenseive.theneonfish.fod.API;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.potion.Potion;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import futuristicoffensiveanddefenseive.theneonfish.fod.FODItems;

public class BaseExplosives extends Block {
	public int fuse;
	public static int force;
	
	 private String unlocalizedName;
	
	public Potion effectName;
	public boolean hasEffect;
	public boolean hasDetonator;
	
	
	public BaseExplosives(Material mat, int fuse, int force) {
		super(mat);
		this.fuse = fuse;
		this.force = force;
	}
	
	
	public BaseExplosives setEffect(Potion effectName){
		this.hasEffect = true;
		this.effectName = effectName;
		return this;
	}
	
	public BaseExplosives hasDetonator(boolean hasDetonator){
		this.hasDetonator = hasDetonator;
		return this;
	}
	
	
    public BaseExplosives setName(String name)
    {
        this.unlocalizedName = name;
        return this;
    }
    public BaseExplosives setTab(CreativeTabs tab){
    	this.setCreativeTab(tab);
    	return this;
    }
	
	@SideOnly(Side.CLIENT)
    private IIcon field_150116_a;
    @SideOnly(Side.CLIENT)
    private IIcon field_150115_b;
    private static final String __OBFID = "CL_00000324";

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 0 ? this.field_150115_b : (p_149691_1_ == 1 ? this.field_150116_a : this.blockIcon);
    }
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
        if (world.isBlockIndirectlyGettingPowered(x, y, z )&& this.hasDetonator == false)
        {
            this.onBlockDestroyedByPlayer(world, x, y, z, 1);
            world.setBlockToAir(x, y, z);
        }
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        if (world.isBlockIndirectlyGettingPowered(x, y, z)&& this.hasDetonator == false)
        {
            this.onBlockDestroyedByPlayer(world, x, y, z, 1);
            
            world.setBlockToAir(x, y, z);
        }
    }

    public int quantityDropped(Random entity)
    {
        return 1;
    }

    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion)
    {
        if (!world.isRemote)
        {
            BaseExplosivePrimed entitytntprimed = new BaseExplosivePrimed(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), explosion.getExplosivePlacedBy());
            entitytntprimed.fuse = world.rand.nextInt(this.fuse / 4) + this.fuse / 8;
            entitytntprimed.force = this.force;
            world.spawnEntityInWorld(entitytntprimed);
        }
    }

    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metaData)
    {
        this.createExplosivePrimed(world, x, y, z, metaData, (EntityLivingBase)null);
    }
    public void createExplosivePrimed(World world, int x, int y, int z, int rand, EntityLivingBase livingEntity)
    {
        if (!world.isRemote)
        {
            if ((rand & 1) == 1)
            {
            	BaseExplosivePrimed entitytntprimed = new BaseExplosivePrimed(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), livingEntity);
                entitytntprimed.fuse = this.fuse;
                entitytntprimed.force = this.force;
                entitytntprimed.hasEffect = this.hasEffect;
                if (this.effectName != null){
                	BaseExplosivePrimed.effectName = this.effectName;
                }
                world.spawnEntityInWorld(entitytntprimed);
                world.playSoundAtEntity(entitytntprimed, "game.tnt.primed", 1.0F, 1.0F);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metaData, float sideX, float sideY, float sideZ)
    {
    	if(this.hasDetonator == true){

        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == FODItems.detonator)
        {
        		this.createExplosivePrimed(world, x, y, z, 1, player);
        		world.setBlockToAir(x, y, z);
            	return true;
        }
        else
        {
        	return super.onBlockActivated(world, x, y, z, player, metaData, sideX, sideY, sideZ);
        }
        }else {
        	return false;
        }
    }

    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if (entity instanceof EntityArrow && !world.isRemote)
        {
            EntityArrow entityarrow = (EntityArrow)entity;

            if (entityarrow.isBurning())
            {
                if(this.hasDetonator == false){
                	this.createExplosivePrimed(world, x, y, z, 1, entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)entityarrow.shootingEntity : null);
                	world.setBlockToAir(x, y, z);
                }
            }
        }
    }
    public boolean canDropFromExplosion(Explosion explosion)
    {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon)
    {
        this.blockIcon = icon.registerIcon(this.getTextureName() + "_side");
        this.field_150116_a = icon.registerIcon(this.getTextureName() + "_top");
        this.field_150115_b = icon.registerIcon(this.getTextureName() + "_bottom");
    }
}
