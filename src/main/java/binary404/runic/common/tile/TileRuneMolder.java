package binary404.runic.common.tile;

import binary404.runic.common.blocks.fluid.RegistryFluids;
import binary404.runic.common.container.RuneMolderContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileRuneMolder extends TileInventory implements ITickableTileEntity, INamedContainerProvider {

    public int progress = 0;
    final int maxProgress = 600;
    public int press = 0;

    protected ItemStackHandler inputSlot;
    protected ItemStackHandler outputSlot;
    public FluidTank tank;
    private final LazyOptional<IItemHandler> inputHolder = LazyOptional.of(() -> inputSlot);
    private final LazyOptional<IItemHandler> outputHolder = LazyOptional.of(() -> outputSlot);
    private final LazyOptional<IFluidHandler> tankHolder = LazyOptional.of(() -> tank);
    private final LazyOptional<IItemHandler> allHolder = LazyOptional.of(() -> new CombinedInvWrapper(inputSlot, outputSlot));

    public TileRuneMolder() {
        super(ModTiles.MOLDER);
        inputSlot = new InventoryStackHandler(this, true, true, 1);
        outputSlot = new InventoryStackHandler(this, false, true, 1);
        tank = new FluidTank(1000);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB((this.pos.getX() - 1), this.pos.getY(), (this.pos.getZ() - 1), (this.pos.getX() + 2), (this.pos.getY() + 2), (this.pos.getZ() + 2));
    }

    @Override
    public void tick() {
        if (!inputSlot.getStackInSlot(0).isEmpty() && tank.getFluid().getFluid() == RegistryFluids.SOLVENT_SOURCE && (this.outputSlot.getStackInSlot(0).getItem() == Items.DIAMOND || this.outputSlot.getStackInSlot(0).isEmpty()) && this.outputSlot.getStackInSlot(0).getCount() < 64) {
            if (tank.getFluidAmount() >= 100 && progress < maxProgress) {
                progress++;
            }
            if (this.world.isRemote) {
                if (this.press < 90 && this.progress > 0) {
                    this.press += 6;
                    if (this.press >= 60) {
                        //sounds
                    }
                }
                if (this.press >= 90 && this.world.rand.nextInt(8) == 0){
                    //sound
                }
                if (this.press > 0 && this.progress <= 0) {
                    this.press -= 3;
                }
            }

            if (progress >= maxProgress) {
                tank.drain(100, IFluidHandler.FluidAction.EXECUTE);
                this.inputSlot.getStackInSlot(0).shrink(1);
                if (this.outputSlot.getStackInSlot(0).isEmpty()) {
                    this.outputSlot.setStackInSlot(0, new ItemStack(Items.DIAMOND));
                } else {
                    this.outputSlot.getStackInSlot(0).setCount(this.outputSlot.getStackInSlot(0).getCount() + 1);
                }
                this.progress = 0;
                this.markDirty();
                this.syncTile(false);
            }
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            this.markDirty();
            if (side == Direction.UP)
                return inputHolder.cast();
            if (side == Direction.EAST)
                return inputHolder.cast();
            if (side == Direction.NORTH)
                return inputHolder.cast();
            if (side == Direction.WEST)
                return inputHolder.cast();
            if (side == Direction.SOUTH)
                return inputHolder.cast();
            if (side == Direction.DOWN)
                return outputHolder.cast();
        } else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            this.markDirty();
            return tankHolder.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT writePacketNBT(CompoundNBT cmp) {
        cmp.put("input", inputSlot.serializeNBT());
        cmp.put("output", outputSlot.serializeNBT());
        tank.writeToNBT(cmp);
        cmp.putInt("progress", this.progress);
        return cmp;
    }

    @Override
    public void readPacketNBT(CompoundNBT cmp) {
        super.readPacketNBT(cmp);
        inputSlot.deserializeNBT(cmp.getCompound("input"));
        outputSlot.deserializeNBT(cmp.getCompound("output"));
        tank.readFromNBT(cmp);
        this.progress = cmp.getInt("progress");
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT par1nbtTagCompound) {
        par1nbtTagCompound.putInt("press", this.press);
        return super.write(par1nbtTagCompound);
    }

    @Override
    public void read(CompoundNBT par1nbtTagCompound) {
        super.read(par1nbtTagCompound);
        this.press = par1nbtTagCompound.getInt("press");
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.runic.rune_molder");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new RuneMolderContainer(p_createMenu_1_, p_createMenu_2_, new CombinedInvWrapper(inputSlot, outputSlot), this);
    }
}
