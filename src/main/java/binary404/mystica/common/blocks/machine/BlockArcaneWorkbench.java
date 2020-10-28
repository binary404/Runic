package binary404.mystica.common.blocks.machine;

import binary404.mystica.common.container.ContainerArcaneWorkbench;
import binary404.mystica.common.tile.crafting.TileArcaneWorkbench;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockArcaneWorkbench extends Block {

    public BlockArcaneWorkbench(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileArcaneWorkbench();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (player instanceof ServerPlayerEntity) {
            TileArcaneWorkbench tile = (TileArcaneWorkbench) worldIn.getTileEntity(pos);

            INamedContainerProvider provider = new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new StringTextComponent("");
                }

                @Nullable
                @Override
                public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                    return new ContainerArcaneWorkbench(p_createMenu_2_, tile, p_createMenu_1_);
                }
            };
            NetworkHooks.openGui((ServerPlayerEntity) player, provider, buffer -> {
                buffer.writeBlockPos(pos);
            });
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }
}
