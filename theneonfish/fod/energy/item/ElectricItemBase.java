package futuristicoffensiveanddefenseive.theneonfish.fod.energy.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.world.World;
import resonantengine.api.item.IEnergyItem;

public abstract class ElectricItemBase extends Item implements IEnergyItem {
    public float transferMax;

    public ElectricItemBase()
    {
        super();
        this.setMaxStackSize(1);
        this.setMaxDamage(100);
        this.setNoRepair();
        this.setMaxTransfer();
    }

    protected void setMaxTransfer()
    {
        this.transferMax = 200;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4)
    {
        String color = "";
        double energy = this.getEnergy(itemStack);

        if (energy <= this.getEnergyCapacity(itemStack) / 3)
        {
            color = "\u00a74";
        }
        else if (energy > this.getEnergyCapacity(itemStack) * 2 / 3)
        {
            color = "\u00a72";
        }
        else
        {
            color = "\u00a76";
        }
    }

    /**
     * Makes sure the item is uncharged when it is crafted and not charged.
     * Change this if you do not want this to happen!
     */
    @Override
    public void onCreated(ItemStack itemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        this.setEnergy(itemStack, 0);
    }

    @Override
    public double recharge(ItemStack itemStack, double energy, boolean doRecharge)
    {
        double rejectedEnergy = Math.max(this.getEnergy(itemStack) + energy - this.getEnergyCapacity(itemStack), 0);
        double energyToReceive = energy - rejectedEnergy;
        if (energyToReceive > this.transferMax)
        {
            rejectedEnergy += energyToReceive - this.transferMax;
            energyToReceive = this.transferMax;
        }

        if (doRecharge)
        {
            this.setEnergy(itemStack, this.getEnergy(itemStack) + energyToReceive);
        }

        return energyToReceive;
    }

    @Override
    public double discharge(ItemStack itemStack, double energy, boolean doDischarge)
    {
        double energyToTransfer = Math.min(Math.min(this.getEnergy(itemStack), energy), this.transferMax);

        if (doDischarge)
        {
            this.setEnergy(itemStack, this.getEnergy(itemStack) - energyToTransfer);
        }

        return energyToTransfer;
    }

    @Override
    public ItemStack setEnergy(ItemStack itemStack, double energy)
    {
        // Saves the frequency in the ItemStack
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        double electricityStored = Math.max(Math.min(energy, this.getEnergyCapacity(itemStack)), 0);
        itemStack.getTagCompound().setDouble("electricity", electricityStored);

        /** Sets the damage as a percentage to render the bar properly. */
        itemStack.setItemDamage((int) (100 - electricityStored / this.getEnergyCapacity(itemStack) * 100));
		return itemStack;
    }

    /**
     * Gets the energy stored in the item. Energy is stored using item NBT
     */
    @Override
    public double getEnergy(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        float energyStored = 0f;
        if (itemStack.getTagCompound().hasKey("electricity"))
        {
            NBTBase obj = itemStack.getTagCompound().getTag("electricity");
            if (obj instanceof NBTTagDouble)
            {
                energyStored = ((NBTTagDouble) obj).func_150288_h();
            }
            else if (obj instanceof NBTTagFloat)
            {
                energyStored = ((NBTTagFloat) obj).func_150288_h();
            }
        }

        /** Sets the damage as a percentage to render the bar properly. */
        itemStack.setItemDamage((int) (100 - energyStored / this.getEnergyCapacity(itemStack) * 100));
        return energyStored;
    }

    public static boolean isElectricItem(Item item)
    {
        if (item instanceof ElectricItemBase)
        {
            return true;
        }

        return false;
    }

    public static boolean isElectricItemEmpty(ItemStack itemstack)
    {
        if (itemstack == null) return false;        
    	Item item = itemstack.getItem();
    	
    	if (item instanceof ElectricItemBase)
        {
            return ((ElectricItemBase) item).getEnergy(itemstack) <= 0;
        }

        return false;
    }

    public boolean canSend(ItemStack itemStack)
    {
        return true;
    }

    public boolean isMetadataSpecific(ItemStack itemStack)
    {
        return false;
    }
    }
