package binary404.runic.common.blocks.machine;

import binary404.runic.common.tile.TileRuneMolder;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraftforge.fluids.FluidUtil.tryEmptyContainer;

public class BlockRuneMolder extends ContainerBlock {

    public BlockRuneMolder(Properties builder) {
        super(builder);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileRuneMolder();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileRuneMolder();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote)
            return ActionResultType.SUCCESS;

        TileRuneMolder tile = (TileRuneMolder) worldIn.getTileEntity(pos);
        if (tile != null) {
            if (tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).cast() != LazyOptional.empty()) {
                ItemStack heldItem = player.getHeldItem(handIn);
                IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(new FluidTank(1000));

                if (heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).cast() != LazyOptional.empty()) {
                    if (interactWithHandler(heldItem, handler, player, handIn)) {
                        return ActionResultType.SUCCESS;
                    }
                }
            }
        }

        INamedContainerProvider container = this.getContainer(state, worldIn, pos);
        if (container != null) {
            if (!(player instanceof ServerPlayerEntity))
                return ActionResultType.FAIL;
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) player;
            NetworkHooks.openGui(playerEntity, container, (packetBuffer -> {
                packetBuffer.writeBlockPos(pos);
            }));
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    public boolean interactWithHandler(ItemStack stack, IFluidHandler handler, PlayerEntity player, Hand hand) {
        return empty(stack, handler, player, hand) || fill(stack, handler, player, hand);
    }

    public static boolean fill(ItemStack stack, IFluidHandler handler, PlayerEntity player, Hand hand) {
        IItemHandler playerInv = new InvWrapper(player.inventory);
        FluidActionResult result = FluidUtil.tryFillContainerAndStow(stack, handler, playerInv, Integer.MAX_VALUE, player, true);
        if (result.isSuccess()) {
            player.setHeldItem(hand, result.getResult());
            return true;
        }
        return false;
    }

    public static boolean empty(ItemStack stack, IFluidHandler handler, PlayerEntity player, Hand hand) {
        IItemHandler playerInv = new InvWrapper(player.inventory);
        FluidActionResult result = emptyContainerAndStow(stack, handler, playerInv, Integer.MAX_VALUE, player, true);
        if (result.isSuccess()) {
            player.setHeldItem(hand, result.getResult());
            return true;
        }
        return false;
    }

    public static FluidActionResult emptyContainerAndStow(@Nonnull ItemStack container, IFluidHandler fluidDestination, IItemHandler inventory, int maxAmount, @Nullable PlayerEntity player, boolean doDrain) {
        if (player != null && player.abilities.isCreativeMode) {
            FluidActionResult emptiedReal = tryEmptyContainer(container, fluidDestination, maxAmount, player, doDrain);
            if (emptiedReal.isSuccess()) {
                return new FluidActionResult(container); // creative mode: item does not change
            }
        } else if (container.getCount() == 1) // don't need to stow anything, just fill and edit the container stack
        {
            FluidActionResult emptiedReal = tryEmptyContainer(container, fluidDestination, maxAmount, player, doDrain);
            if (emptiedReal.isSuccess()) {
                return emptiedReal;
            }
        } else {
            FluidActionResult emptiedSimulated = tryEmptyContainer(container, fluidDestination, maxAmount, player, false);
            if (emptiedSimulated.isSuccess()) {
                // check if we can give the itemStack to the inventory
                ItemStack remainder = ItemHandlerHelper.insertItemStacked(inventory, emptiedSimulated.getResult(), true);
                if (remainder.isEmpty() || player != null) {
                    FluidActionResult emptiedReal = tryEmptyContainer(container, fluidDestination, maxAmount, player, doDrain);
                    remainder = ItemHandlerHelper.insertItemStacked(inventory, emptiedReal.getResult(), !doDrain);

                    // give it to the player or drop it at their feet
                    if (!remainder.isEmpty() && player != null && doDrain) {
                        ItemHandlerHelper.giveItemToPlayer(player, remainder);
                    }

                    ItemStack containerCopy = container.copy();
                    containerCopy.shrink(1);
                    return new FluidActionResult(containerCopy);
                }
            }
        }

        return FluidActionResult.FAILURE;
    }

}
