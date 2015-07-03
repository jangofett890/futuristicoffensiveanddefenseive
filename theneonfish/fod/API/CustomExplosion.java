package futuristicoffensiveanddefenseive.theneonfish.fod.API;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import scala.reflect.internal.Trees.This;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class CustomExplosion extends Explosion{
	public CustomExplosion(World world, Entity entity, double x, double y, double z, float force1) {
		super(world, entity, x, y, z, force1);
		this.exploder = entity;
		this.force = force1;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}
	public static Entity exploder;
	public float force = 10;
	public int constraint = 50;
	public static World world;
    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public double posX;
    public double posY;
    public double posZ;
    private Map field_77288_k = new HashMap();

    @Override
    public void doExplosionB(boolean p_77279_1_)
    {
        /**
         * Play a sound effect. Many many parameters for this function. Not sure what they do, but a classic call is :
         * (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 'random.door_open', 1.0F, world.rand.nextFloat() * 0.1F +
         * 0.9F with i,j,k position of the block.*/
         
        //world.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "random.explode", 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);        
        /*if (this.force >= 2.0F && this.isSmoking)
        {
            this.world.spawnParticle("hugeexplosion", this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D);
        }
        else
        {
            this.world.spawnParticle("largeexplode", this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D);
        }*/
        Iterator iterator;
        ChunkPosition chunkposition;
        int i;
        int j;
        int k;
        Block block;

        if (this.isSmoking)
        {
            iterator = this.affectedBlockPositions.iterator();

            while (iterator.hasNext())
            {
                chunkposition = (ChunkPosition)iterator.next();
                i = chunkposition.chunkPosX;
                j = chunkposition.chunkPosY;
                k = chunkposition.chunkPosZ;
                block = this.world.getBlock(i, j, k);
                if (p_77279_1_)
                {
                    double d0 = (double)((float)i + this.world.rand.nextFloat());
                    double d1 = (double)((float)j + this.world.rand.nextFloat());
                    double d2 = (double)((float)k + this.world.rand.nextFloat());
                    double d3 = d0 - this.explosionX;
                    double d4 = d1 - this.explosionY;
                    double d5 = d2 - this.explosionZ;
                    double d6 = (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 /= d6;
                    d4 /= d6;
                    d5 /= d6;
                    double d7 = 0.5D / (d6 / (double)this.force + 0.1D);
                    d7 *= (double)(this.world.rand.nextFloat() * this.world.rand.nextFloat() + 0.3F);
                    d3 *= d7;
                    d4 *= d7;
                    d5 *= d7;
                    this.world.spawnParticle("explode", (d0 + this.explosionX * 1.0D) / 2.0D, (d1 + this.explosionY * 1.0D) / 2.0D, (d2 + this.explosionZ * 1.0D) / 2.0D, d3, d4, d5);
                    this.world.spawnParticle("smoke", d0, d1, d2, d3, d4, d5);
                }

                if (block.getMaterial() != Material.air)
                {
                    if (block.canDropFromExplosion(this))
                    {
                        block.dropBlockAsItemWithChance(this.world, i, j, k, this.world.getBlockMetadata(i, j, k), 1.0F / this.force, 0);
                    }

                    block.onBlockExploded(this.world, i, j, k, this);
                }
            }
        }
        }
    
    
	public void doCustomExplosionA()
    {
        float f = this.force;
        HashSet hashset = new HashSet();
        int i;
        int j;
        int k;
        double d5;
        double d6;
        double d7;

        for (i = 0; i < this.constraint; ++i)
        {
            for (j = 0; j < this.constraint; ++j)
            {
                for (k = 0; k < this.constraint; ++k)
                {
                    if (i == 0 || i == this.constraint - 1 || j == 0 || j == this.constraint - 1 || k == 0 || k == this.constraint - 1)
                    {
                        double d0 = (double)((float)i / ((float)this.constraint - 1.0F) * 2.0F - 1.0F);
                        double d1 = (double)((float)j / ((float)this.constraint - 1.0F) * 2.0F - 1.0F);
                        double d2 = (double)((float)k / ((float)this.constraint - 1.0F) * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f1 = this.force;
                        d5 = this.explosionX;
                        d6 = this.explosionY;
                        d7 = this.explosionZ;

                        for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F)
                        {
                            /*int j1 = MathHelper.floor_double(d5);
                            int k1 = MathHelper.floor_double(d6);
                            int l1 = MathHelper.floor_double(d7);
                            Block block = this.world.getBlock(j1, k1, l1);
                            if (block.getMaterial() != Material.air)
                            {
                               float f3 = this.exploder != null ? this.exploder.func_145772_a(this, this.world, (int)this.posX, (int)this.posY, (int)this.posZ, block) : block.getExplosionResistance(this.exploder, world, (int)this.posX, (int)this.posY, (int)this.posZ, explosionX, explosionY, explosionZ);
                                f1 -= (f3 + 0.3F) * f2;
                            }

                            if (f1 > 0.0F && (this.exploder == null || this.exploder.func_145774_a(this, this.world, (int)this.posX, (int)this.posY, (int)this.posZ, block, f1)))
                            {
                               hashset.add(new ChunkPosition((int)this.posX, (int)this.posY, (int)this.posZ));
                            }*/

                            d5 += d0 * (double)f2;
                            d6 += d1 * (double)f2;
                            d7 += d2 * (double)f2;
                        }
                    }
                }
            }
        }
        this.affectedBlockPositions.addAll(hashset);
        this.force *= 2.0F;
        i = MathHelper.floor_double(this.posX - (double)this.force - 1.0D);
        j = MathHelper.floor_double(this.posX + (double)this.force + 1.0D);
        k = MathHelper.floor_double(this.posY - (double)this.force - 1.0D);
        int i2 = MathHelper.floor_double(this.posY + (double)this.force + 1.0D);
        int l = MathHelper.floor_double(this.posZ - (double)this.force - 1.0D);
        int j2 = MathHelper.floor_double(this.posZ + (double)this.force + 1.0D);
        List list = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.getBoundingBox((double)i, (double)k, (double)l, (double)j, (double)i2, (double)j2));
        net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.world, this, list, this.force);
        Vec3 vec3 = Vec3.createVectorHelper(this.explosionX, this.explosionY, this.explosionZ);
        for (int i1 = 0; i1 < list.size(); ++i1)
        {
            Entity entity = (Entity)list.get(i1);
            double d4 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double)this.force;

            if (d4 <= 1.0D)
            {
                d5 = entity.posX - this.explosionX;
                d6 = entity.posY + (double)entity.getEyeHeight() - this.explosionY;
                d7 = entity.posZ - this.explosionZ;
                double d9 = (double)MathHelper.sqrt_double(d5 * d5 + d6 * d6 + d7 * d7);

                if (d9 != 0.0D)
                {
                    d5 /= d9;
                    d6 /= d9;
                    d7 /= d9;
                    double d10 = (double)this.world.getBlockDensity(vec3, entity.boundingBox);
                    double d11 = (1.0D - d4) * d10;
                    entity.attackEntityFrom(DamageSource.setExplosionSource(this), (float)((int)((d11 * d11 + d11) / 2.0D * 8.0D * (double)this.force + 1.0D)));
                    double d8 = EnchantmentProtection.func_92092_a(entity, d11);
                    entity.motionX += d5 * d8;
                    entity.motionY += d6 * d8;
                    entity.motionZ += d7 * d8;

                    if (entity instanceof EntityPlayer)
                    {
                        this.field_77288_k.put((EntityPlayer)entity, Vec3.createVectorHelper(d5 * d11, d6 * d11, d7 * d11));
                    }
                }
            }
        }

    }
	public static CustomExplosion createExplosion(Entity entity, double x, double y, double z, float force, boolean flaming)
    {
		CustomExplosion.exploder = entity;
        return newExplosion(entity, x, y, z, force, false, false);
    }

    /**
     * returns a new explosion. Does initiation (at time of writing Explosion is not finished)
     */
    public static CustomExplosion newExplosion(Entity entity, double x, double y, double z, float force, boolean flaming, boolean smoking)
    {
        CustomExplosion explosion = new CustomExplosion(world, entity, x, y, z, force);
        explosion.isFlaming = flaming;
        explosion.isSmoking = smoking;
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) return explosion;
        explosion.doCustomExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }

}
