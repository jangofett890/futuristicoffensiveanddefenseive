package futuristicoffensiveanddefenseive.theneonfish.fod.API;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class CustomExplosion extends Explosion{
	public CustomExplosion(World world, Entity entity, double x, double y, double z, float force1) {
		super(world, entity, x, y, z, force1);
		this.force = force1;
		this.explosionX = x;
		this.explosionY = y;
		this.explosionZ = z;
	}
	public float force = 10;
	public int constraint = 1000;
	public static World world;
    public double explosionX;
    public double explosionY;
    public double explosionZ;

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
                            int j1 = MathHelper.floor_double(d5);
                            int k1 = MathHelper.floor_double(d6);
                            int l1 = MathHelper.floor_double(d7);
                            Block block = this.world.getBlock(j1, k1, l1);

                            if (block.getMaterial() != Material.air)
                            {
                                float f3 = this.exploder != null ? this.exploder.func_145772_a(this, this.world, j1, k1, l1, block) : block.getExplosionResistance(this.exploder, world, j1, k1, l1, explosionX, explosionY, explosionZ);
                                f1 -= (f3 + 0.3F) * f2;
                            }

                            if (f1 > 0.0F && (this.exploder == null || this.exploder.func_145774_a(this, this.world, j1, k1, l1, block, f1)))
                            {
                                hashset.add(new ChunkPosition(j1, k1, l1));
                            }

                            d5 += d0 * (double)f2;
                            d6 += d1 * (double)f2;
                            d7 += d2 * (double)f2;
                        }
                    }
                }
            }
        }
    }
	public static CustomExplosion createExplosion(Entity p_72876_1_, double p_72876_2_, double p_72876_4_, double p_72876_6_, float p_72876_8_, boolean p_72876_9_)
    {
        return newExplosion(p_72876_1_, p_72876_2_, p_72876_4_, p_72876_6_, p_72876_8_, false, p_72876_9_);
    }

    /**
     * returns a new explosion. Does initiation (at time of writing Explosion is not finished)
     */
    public static CustomExplosion newExplosion(Entity p_72885_1_, double p_72885_2_, double p_72885_4_, double p_72885_6_, float p_72885_8_, boolean p_72885_9_, boolean p_72885_10_)
    {
        CustomExplosion explosion = new CustomExplosion(world, p_72885_1_, p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_);
        explosion.isFlaming = p_72885_9_;
        explosion.isSmoking = p_72885_10_;
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) return explosion;
        explosion.doCustomExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }

}
