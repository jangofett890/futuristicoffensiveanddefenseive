package futuristicoffensiveanddefenseive.theneonfish.fod.items;

import java.util.ArrayList;
import java.util.List;

import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import futuristicoffensiveanddefenseive.theneonfish.fod.FODBlocks;
import futuristicoffensiveanddefenseive.theneonfish.fod.MainFOD;
import futuristicoffensiveanddefenseive.theneonfish.fod.Tier.TurretBaseTier;
import futuristicoffensiveanddefenseive.theneonfish.fod.TileEntities.TileEntityTurretBase;
import futuristicoffensiveanddefenseive.theneonfish.fod.TileEntities.TileEntityTurretGun;
import futuristicoffensiveanddefenseive.theneonfish.fod.blocks.Turret.TurretType;
import futuristicoffensiveanddefenseive.theneonfish.fod.common.ITurret;
import futuristicoffensiveanddefenseive.theneonfish.fod.common.ITurretBase;
import futuristicoffensiveanddefenseive.theneonfish.fod.utils.ISustainedInventory;
import futuristicoffensiveanddefenseive.theneonfish.fod.utils.LangUtils;
import mekanism.api.Coord4D;
import mekanism.api.EnumColor;
import mekanism.api.Range4D;
import mekanism.api.energy.IEnergizedItem;
import mekanism.client.MekKeyHandler;
import mekanism.client.MekanismKeyHandler;
import mekanism.common.Mekanism;
import mekanism.common.integration.IC2ItemManager;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.util.MekanismUtils;
import net.minecraft.block.Block;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TurretItem  extends ItemBlock implements IEnergizedItem, ITurret, ISpecialElectricItem{

	public Block metaBlock;

	public TurretItem(Block block) {
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
		}
	}
	
	public ItemStack getUnchargedItem(TurretType type)
	{
		ItemStack stack = new ItemStack(this);
		setTurretType(stack, type);
		stack.setItemDamage(100);
		return stack;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack itemstack)
	{
		return LangUtils.localize(getTurretType(itemstack).getName() + ".name");
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		boolean place = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);

		if(place)
		{
			TileEntityTurretGun tileEntity = (TileEntityTurretGun)world.getTileEntity(x, y, z);
			tileEntity.type = ((ITurret)stack.getItem()).getTurretType(stack);
			tileEntity.electricityStored = getEnergy(stack);


			if(!world.isRemote)
			{
				Mekanism.packetHandler.sendToReceivers(new TileEntityMessage(Coord4D.get(tileEntity), tileEntity.getNetworkedData(new ArrayList())), new Range4D(Coord4D.get(tileEntity)));
			}
		}

		return place;
	}
	
	@Override
	public TurretType getTurretType(ItemStack itemstack)
	{
		if(itemstack.stackTagCompound == null)
		{
			return TurretType.TURRET_CROSSBOW;
		}

		if(itemstack.stackTagCompound.getString("type") == null)
		{
			return TurretType.TURRET_CROSSBOW;
		}

		return TurretType.getFromName(itemstack.stackTagCompound.getString("type"));
	}
	
	@Override
	public void setTurretType(ItemStack itemstack, TurretType type)
	{
		if(itemstack.stackTagCompound == null)
		{
			itemstack.setTagCompound(new NBTTagCompound());
		}

		itemstack.stackTagCompound.setString("type", type.getBaseType().getName());
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
		return 1000000000;
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
	@Method(modid = "IC2")
	public IElectricItemManager getManager(ItemStack itemStack)
	{
		return IC2ItemManager.getManager(this);
	}
	
	@Override
	public boolean isMetadataSpecific(ItemStack itemStack)
	{
		return false;
	}
	
	
}
