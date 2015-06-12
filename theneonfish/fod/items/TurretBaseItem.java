package futuristicoffensiveanddefenseive.theneonfish.fod.items;

import java.util.ArrayList;
import java.util.List;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import futuristicoffensiveanddefenseive.theneonfish.fod.MainFOD;
import futuristicoffensiveanddefenseive.theneonfish.fod.Tier.TurretBaseTier;
import futuristicoffensiveanddefenseive.theneonfish.fod.TileEntities.TileEntityTurretBase;
import futuristicoffensiveanddefenseive.theneonfish.fod.common.ITurretBase;
import futuristicoffensiveanddefenseive.theneonfish.fod.utils.ISustainedInventory;
import futuristicoffensiveanddefenseive.theneonfish.fod.utils.LangUtils;
import mekanism.api.Coord4D;
import mekanism.api.EnumColor;
import mekanism.api.MekanismConfig.general;
import mekanism.api.Range4D;
import mekanism.api.energy.IEnergizedItem;
import mekanism.client.MekKeyHandler;
import mekanism.client.MekanismKeyHandler;
import mekanism.common.Mekanism;
import mekanism.common.util.MekanismUtils;
import net.minecraft.block.Block;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import mekanism.common.integration.IC2ItemManager;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;

public class TurretBaseItem extends ItemBlock implements IEnergizedItem, ITurretBase, ISpecialElectricItem, ISustainedInventory, IEnergyContainerItem
{
	public Block metaBlock;
	
	
	public TurretBaseItem(Block block) {
		super(block);
		metaBlock = block;
		setMaxStackSize(1);
		setMaxDamage(100);
		setNoRepair();
		setCreativeTab(MainFOD.tabList);
	
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
	{
		if(!MekKeyHandler.getIsKeyPressed(MekanismKeyHandler.sneakKey))
		{
			list.add(LangUtils.localize("tooltip.hold") + " " + EnumColor.AQUA + GameSettings.getKeyDisplayString(MekanismKeyHandler.sneakKey.getKeyCode()) + EnumColor.GREY + " " + LangUtils.localize("tooltip.forDetails") + ".");
		}
		else {
			list.add(EnumColor.BRIGHT_GREEN + LangUtils.localize("tooltip.storedEnergy") + ": " + EnumColor.GREY + MekanismUtils.getEnergyDisplay(getEnergy(itemstack)));
			list.add(EnumColor.AQUA + LangUtils.localize("tooltip.inventory") + ": " + EnumColor.GREY + LangUtils.transYesNo(getInventory(itemstack) != null && getInventory(itemstack).tagCount() != 0));
		}
	}
	
	public ItemStack getUnchargedItem(TurretBaseTier tier)
	{
		ItemStack stack = new ItemStack(this);
		setTurretBaseTier(stack, tier);
		stack.setItemDamage(100);
		return stack;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack itemstack)
	{
		return LangUtils.localize("tile.TurretBase" + getTurretBaseTier(itemstack).getBaseTier().getName() + ".name");
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		boolean place = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);

		if(place)
		{
			TileEntityTurretBase tileEntity = (TileEntityTurretBase)world.getTileEntity(x, y, z);
			tileEntity.tier = ((ITurretBase)stack.getItem()).getTurretBaseTier(stack);
			tileEntity.electricityStored = getEnergy(stack);

			((ISustainedInventory)tileEntity).setInventory(getInventory(stack));

			if(!world.isRemote)
			{
				Mekanism.packetHandler.sendToReceivers(new TileEntityMessage(Coord4D.get(tileEntity), tileEntity.getNetworkedData(new ArrayList())), new Range4D(Coord4D.get(tileEntity)));
			}
		}

		return place;
	}
	
	@Override
	public TurretBaseTier getTurretBaseTier(ItemStack itemstack)
	{
		if(itemstack.stackTagCompound == null)
		{
			return TurretBaseTier.BASIC;
		}

		if(itemstack.stackTagCompound.getString("tier") == null)
		{
			return TurretBaseTier.BASIC;
		}

		return TurretBaseTier.getFromName(itemstack.stackTagCompound.getString("tier"));
	}

	@Override
	public void setTurretBaseTier(ItemStack itemstack, TurretBaseTier tier)
	{
		if(itemstack.stackTagCompound == null)
		{
			itemstack.setTagCompound(new NBTTagCompound());
		}

		itemstack.stackTagCompound.setString("tier", tier.getBaseTier().getName());
	}

	@Override
	@Method(modid = "IC2")
	public boolean canProvideEnergy(ItemStack itemStack)
	{
		return false;
	}

	@Override
	@Method(modid = "IC2")
	public double getMaxCharge(ItemStack itemStack)
	{
		return 0;
	}

	@Override
	@Method(modid = "IC2")
	public int getTier(ItemStack itemStack)
	{
		return 4;
	}

	@Override
	@Method(modid = "IC2")
	public double getTransferLimit(ItemStack itemStack)
	{
		return 0;
	}

	@Override
	public void setInventory(NBTTagList nbtTags, Object... data)
	{
		if(data[0] instanceof ItemStack)
		{
			ItemStack itemStack = (ItemStack)data[0];

			if(itemStack.stackTagCompound == null)
			{
				itemStack.setTagCompound(new NBTTagCompound());
			}

			itemStack.stackTagCompound.setTag("Items", nbtTags);
		}
	}
	
	@Override
	public NBTTagList getInventory(Object... data)
	{
		if(data[0] instanceof ItemStack)
		{
			ItemStack itemStack = (ItemStack)data[0];

			if(itemStack.stackTagCompound == null)
			{
				return null;
			}

			return itemStack.stackTagCompound.getTagList("Items", NBT.TAG_COMPOUND);
		}

		return null;
	}

	@Override
	public double getEnergy(ItemStack itemStack)
	{
		if(itemStack.stackTagCompound == null)
		{
			return 0;
		}

		double electricityStored = itemStack.stackTagCompound.getDouble("electricity");
		itemStack.setItemDamage((int)Math.max(1, (Math.abs(((electricityStored/getMaxEnergy(itemStack))*100)-100))));

		return electricityStored;
	}

	@Override
	public void setEnergy(ItemStack itemStack, double amount)
	{
		if(getTurretBaseTier(itemStack) == TurretBaseTier.CREATIVE && amount != Integer.MAX_VALUE)
		{
			return;
		}
		
		if(itemStack.stackTagCompound == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		double electricityStored = Math.max(Math.min(amount, getMaxEnergy(itemStack)), 0);
		itemStack.stackTagCompound.setDouble("electricity", electricityStored);
		itemStack.setItemDamage((int)Math.max(1, (Math.abs(((electricityStored/getMaxEnergy(itemStack))*100)-100))));
	}

	@Override
	public double getMaxEnergy(ItemStack itemStack)
	{
		return getTurretBaseTier(itemStack).maxEnergy;
	}

	@Override
	public double getMaxTransfer(ItemStack itemStack)
	{
		return getMaxEnergy(itemStack)*0.005;
	}

	@Override
	public boolean canReceive(ItemStack itemStack)
	{
		return true;
	}

	@Override
	public boolean canSend(ItemStack itemStack)
	{
		return false;
	}
	
	@Override
	public int receiveEnergy(ItemStack theItem, int energy, boolean simulate)
	{
		if(canReceive(theItem))
		{
			double energyNeeded = getMaxEnergy(theItem)-getEnergy(theItem);
			double toReceive = Math.min(energy* general.FROM_TE, energyNeeded);

			if(!simulate)
			{
				setEnergy(theItem, getEnergy(theItem) + toReceive);
			}

			return (int)Math.round(toReceive* general.TO_TE);
		}

		return 0;
	}

	@Override
	public int extractEnergy(ItemStack theItem, int energy, boolean simulate)
	{
		if(canSend(theItem))
		{
			double energyRemaining = getEnergy(theItem);
			double toSend = Math.min((energy* general.FROM_TE), energyRemaining);

			if(!simulate)
			{
				setEnergy(theItem, getEnergy(theItem) - toSend);
			}

			return (int)Math.round(toSend* general.TO_TE);
		}

		return 0;
	}

	@Override
	public int getEnergyStored(ItemStack theItem)
	{
		return (int)(getEnergy(theItem)* general.TO_TE);
	}

	@Override
	public int getMaxEnergyStored(ItemStack theItem)
	{
		return (int)(getMaxEnergy(theItem)* general.TO_TE);
	}

	@Override
	public boolean isMetadataSpecific(ItemStack itemStack)
	{
		return false;
	}

	@Override
	@Method(modid = "IC2")
	public IElectricItemManager getManager(ItemStack itemStack)
	{
		return IC2ItemManager.getManager(this);
	}

	@Override
	@Method(modid = "IC2")
	public Item getChargedItem(ItemStack itemStack)
	{
		return this;
	}

	@Override
	@Method(modid = "IC2")
	public Item getEmptyItem(ItemStack itemStack)
	{
		return this;
	}

	@Override
	public int getRecipeType(ItemStack itemStack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setRecipeType(int type, ItemStack itemStack) {
		// TODO Auto-generated method stub
		
	}

	
}
