package futuristicoffensiveanddefenseive.theneonfish.fod.API;

import futuristicoffensiveanddefenseive.theneonfish.fod.FODItems;
import futuristicoffensiveanddefenseive.theneonfish.fod.MainFOD;
import ibxm.Player;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BaseExplosives extends Block {
	public int fuse;
	public static float force;
	
	 private String unlocalizedName;
	
	public Potion effectName;
	public boolean hasEffect;
	public boolean hasDetonator;
	
	
	public BaseExplosives(Material matt, int fuse, float force) {
		super(matt);
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
	
	
    public BaseExplosives setBlockName(String name)
    {
        this.unlocalizedName = name;
        return this;
    }
	
	@SideOnly(Side.CLIENT)
    private IIcon field_150116_a;
    @SideOnly(Side.CLIENT)
    private IIcon field_150115_b;
    private static final String __OBFID = "CL_00000324";

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 0 ? this.field_150115_b : (p_149691_1_ == 1 ? this.field_150116_a : this.blockIcon);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
        if (world.isBlockIndirectlyGettingPowered(x, y, z )&& this.hasDetonator == false)
        {
            this.onBlockDestroyedByPlayer(world, x, y, z, 1);
            world.setBlockToAir(x, y, z);
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        if (world.isBlockIndirectlyGettingPowered(x, y, z)&& this.hasDetonator == false)
        {
            this.onBlockDestroyedByPlayer(world, x, y, z, 1);
            
            world.setBlockToAir(x, y, z);
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random entity)
    {
        return 1;
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
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

    /**
     * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
     */
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

    /**
     * Called upon block activation (right click on the block.)
     */
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

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
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

    /**
     * Return whether this block can drop from an explosion.
     */
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
