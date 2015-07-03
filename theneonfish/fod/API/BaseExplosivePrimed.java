package futuristicoffensiveanddefenseive.theneonfish.fod.API;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BaseExplosivePrimed extends Entity {
	public World world;
	private EntityLivingBase tntPlacedBy;
	public int fuse;
	public int force;
	public boolean hasEffect;
	public boolean hasDetonator;
	public static Potion effectName;
	public World worldObjCustom;
    public double posX;
    public double posY;
    public double posZ;
	
	public BaseExplosivePrimed(World world) {
		super(world);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
	}
    public static final String __OBFID = "CL_00001681";
    public BaseExplosivePrimed(World world, double x, double y, double z, EntityLivingBase entityLivingBase)
    {
        this(world);
        this.setPosition(x, y, z);
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        float f = (float)(Math.random() * Math.PI * 2.0D);
        this.motionX = (double)(-((float)Math.sin((double)f)) * 0.02F);
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double)(-((float)Math.cos((double)f)) * 0.02F);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = entityLivingBase;
    }
    protected void entityInit() {}
    protected boolean canTriggerWalking()
    {
        return false;
    }
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }
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
            	this.explode(this.posX, this.posY, this.posZ);
            }
        }
        else
        {
            //world.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }
    public void giveEffect(Potion name, double x, double y, double z, int duration, float radius){
    	List playerlist = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius,z + radius));	
    	Iterator playerIterator = playerlist.iterator();
    	EntityPlayer entityplayer;
    	
    	while (playerIterator.hasNext())
        {
            entityplayer = (EntityPlayer)playerIterator.next();
            entityplayer.addPotionEffect(new PotionEffect(name.id, duration, 1));
        }
    	
    }
    public void explode(double x, double y, double z)
    {
    	if(hasEffect == true){
    		giveEffect(this.effectName, x, y, z, 1000, force);
    	}
        CustomExplosion.createExplosion(this, x, y, z, force, true);
    }
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbtTag)
    {
        nbtTag.setByte("Fuse", (byte)this.fuse);
    }
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
    public EntityLivingBase getTntPlacedBy()
    {
        return this.tntPlacedBy;
    }
}
