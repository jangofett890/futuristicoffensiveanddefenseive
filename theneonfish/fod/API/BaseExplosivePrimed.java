package futuristicoffensiveanddefenseive.theneonfish.fod.API;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BaseExplosivePrimed extends Entity {	
	private EntityLivingBase tntPlacedBy;
	public int fuse;
	public float force;
	public boolean hasEffect;
	public boolean hasDetonator;
	public static Potion effectName;
	
	public BaseExplosivePrimed(World p_i1729_1_) {
		super(p_i1729_1_);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
	}
    private static final String __OBFID = "CL_00001681";

    public BaseExplosivePrimed(World world, double x, double y, double z, EntityLivingBase entity)
    {
        this(world);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * Math.PI * 2.0D);
        this.motionX = (double)(-((float)Math.sin((double)f)) * 0.02F);
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double)(-((float)Math.cos((double)f)) * 0.02F);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = entity;
    }
    protected void entityInit() {}

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.03999999910593033D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        if (this.fuse-- <= 0)
        {
            this.setDead();

            if (!this.worldObj.isRemote)
            {
                
            	this.explode();
            }
        }
        else
        {
            this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }
    private void giveEffect(Potion name, double x, double y, double z, int duration, float radius){
    	List playerlist = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius,z + radius));	
    	Iterator playerIterator = playerlist.iterator();
    	EntityPlayer entityplayer;
    	
    	while (playerIterator.hasNext())
        {
            entityplayer = (EntityPlayer)playerIterator.next();
            entityplayer.addPotionEffect(new PotionEffect(name.id, duration, 1));
        }
    	
    }
    private void explode()
    {
    	if(hasEffect == true){
    		giveEffect(this.effectName, this.posX, this.posY, this.posZ, 1000, force);
    	}
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, force, true);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbtTag)
    {
        nbtTag.setByte("Fuse", (byte)this.fuse);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    protected void readEntityFromNBT(NBTTagCompound nbtTag)
    {
        this.fuse = nbtTag.getByte("Fuse");
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * returns null or the entityliving it was placed or ignited by
     */
    public EntityLivingBase getTntPlacedBy()
    {
        return this.tntPlacedBy;
    }
}
