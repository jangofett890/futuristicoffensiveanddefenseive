package futuristicoffensiveanddefenseive.theneonfish.fod.API;

import java.util.HashSet;
import java.util.List;

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

public class CustomExplosion extends Explosion {
	public int radius;
	public double sphereArea = (3/4)*Math.PI*((radius)*(radius)*(radius));
	public CustomExplosion(World p_i1948_1_, Entity p_i1948_2_,
			double p_i1948_3_, double p_i1948_5_, double p_i1948_7_,
			float p_i1948_9_) {
		super(p_i1948_1_, p_i1948_2_, p_i1948_3_, p_i1948_5_, p_i1948_7_, p_i1948_9_);
	}
	public int field_77289_h = 16;
	@Override
    public void doExplosionA(){
    }
	@Override
    public void doExplosionB(boolean par1){
    }
    public void explode()
    {
        ExplosionConstructionEvent evt = new ExplosionConstructionEvent(this.world(), this);
        MinecraftForge.EVENT_BUS.post(evt);

        if (!evt.isCanceled())
        {
            if (this.proceduralInterval() > 0)
            {
                if (!this.world().isRemote)
                {
                    this.world().spawnEntityInWorld(new EntityExplosion(this));
                }
            }
            else
            {
                this.doPreExplode();
                this.doExplode();
                this.doPostExplode();
            }
        }
    }
}
