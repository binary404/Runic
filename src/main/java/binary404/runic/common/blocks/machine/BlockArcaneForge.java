package binary404.runic.common.blocks.machine;

import binary404.runic.common.blocks.ModBlocks;
import binary404.runic.common.items.ModItems;
import binary404.runic.common.tile.TileArcaneForge;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockArcaneForge extends Block {

    public static boolean ignore = false;

    public BlockArcaneForge(Properties properties) {
        super(properties);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        destroyForge(worldIn, pos, state, pos);
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        TileArcaneForge forge = (TileArcaneForge) worldIn.getTileEntity(pos);
        ItemStack stack = player.getHeldItem(handIn);
        boolean canAdd = !stack.isEmpty();
        if (forge.getStack().isEmpty()) {
            if (canAdd) {
                ItemStack toAdd = stack.copy();
                toAdd.setCount(1);
                forge.setStack(toAdd);
                stack.shrink(1);
                forge.syncTile(false);
            } else {
                return ActionResultType.PASS;
            }
        }
        if (stack.getItem() == ModItems.forge_hammer) {
            forge.tryCraft(player);
        }
        if (!canAdd && !forge.getStack().isEmpty()) {
            ItemStack playerStack = forge.getStack().copy();
            forge.setStack(ItemStack.EMPTY);
            player.setHeldItem(handIn, playerStack);
        }
        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileArcaneForge();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        return tileEntity != null && tileEntity.receiveClientEvent(id, param);
    }

    public static void destroyForge(World worldIn, BlockPos pos, BlockState state, BlockPos startPos) {
        if (BlockArcaneForge.ignore || worldIn.isRemote) {
            return;
        }
        BlockArcaneForge.ignore = true;
        for (int a = -2; a <= 2; ++a) {
            for (int b = -1; b <= 1; ++b) {
                for (int c = -2; c <= 2; ++c) {
                    if (pos.add(a, b, c) != startPos) {
                        BlockState bs = worldIn.getBlockState(pos.add(a, b, c));
                        if (bs.getBlock() == ModBlocks.storage) {
                            worldIn.setBlockState(pos.add(a, b, c), Blocks.OBSIDIAN.getDefaultState());
                            worldIn.setBlockState(pos.add(a, b + 1, c), Blocks.OBSIDIAN.getDefaultState());
                        }
                        if (bs.getBlock() == ModBlocks.arcane_anvil) {
                            worldIn.setBlockState(pos.add(a, b, c), Blocks.ANVIL.getDefaultState());
                        }
                        if (bs.getBlock() == ModBlocks.furnace) {
                            worldIn.setBlockState(pos.add(a, b, c), Blocks.OBSIDIAN.getDefaultState());
                            worldIn.setBlockState(pos.add(a, b + 1, c), Blocks.FURNACE.getDefaultState());
                        }
                    }
                }
            }
        }
        BlockArcaneForge.ignore = false;
    }

}
