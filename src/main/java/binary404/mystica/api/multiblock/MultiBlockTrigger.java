package binary404.mystica.api.multiblock;

import binary404.mystica.api.capability.CapabilityHelper;
import binary404.mystica.common.container.InventoryFake;
import binary404.mystica.common.core.event.MultiblockEvents;
import binary404.mystica.common.core.util.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerCraftingEvent;

public class MultiBlockTrigger implements IMultiBlockTrigger {

    MultiBlockComponent[][][] blueprint;
    String research;
    int ySize;
    int xSize;
    int zSize;

    public MultiBlockTrigger(final String research, final MultiBlockComponent[][][] blueprint) {
        this.blueprint = blueprint;
        this.research = research;
        this.ySize = this.blueprint.length;
        this.xSize = this.blueprint[0].length;
        this.zSize = this.blueprint[0][0].length;
    }

    @Override
    public List<BlockPos> sparkle(final World world, final PlayerEntity player, final BlockPos pos, final Placement placement) {
        final BlockPos p2 = pos.add(placement.xOffset, placement.yOffset, placement.zOffset);
        final ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        for (int y = 0; y < this.ySize; ++y) {
            final Matrix matrix = new Matrix(this.blueprint[y]);
            matrix.Rotate90DegRight(3 - placement.facing.getHorizontalIndex());
            for (int x = 0; x < matrix.rows; ++x) {
                for (int z = 0; z < matrix.cols; ++z) {
                    if (matrix.matrix[x][z] != null) {
                        final BlockPos p3 = p2.add(x, -y + (this.ySize - 1), z);
                        if (matrix.matrix[x][z].getSource() != null && BlockUtils.isBlockExposed(world, p3)) {
                            list.add(p3);
                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    public Placement getValidFace(final World world, final PlayerEntity player, final BlockPos pos, final Direction face) {
        if (this.research != null && !CapabilityHelper.getKnowledge(player).isResearchKnown(this.research)) {
            return null;
        }
        for (int yy = -this.ySize; yy <= this.ySize; ++yy) {
            for (int xx = -this.xSize; xx <= this.xSize; ++xx) {
                for (int zz = -this.zSize; zz <= zSize; ++zz) {
                    final BlockPos p2 = pos.add(xx, yy, zz);
                    final Direction f = this.fitMultiblock(world, p2);
                    if (f != null) {
                        return new Placement(xx, yy, zz, f);
                    }
                }
            }
        }
        return null;
    }

    private Direction fitMultiblock(final World world, final BlockPos pos) {
        final Direction[] horizontals = BlockUtils.HORIZONTALS;
        final int length = horizontals.length;
        int i = 0;
        Label_0011:
        while (i < length) {
            Direction face = horizontals[i];
            for (int y = 0; y < this.ySize; ++y) {
                Matrix matrix = new Matrix(this.blueprint[y]);
                matrix.Rotate90DegRight(3 - face.getHorizontalIndex());
                for (int x = 0; x < matrix.rows; ++x) {
                    for (int z = 0; z < matrix.cols; ++z) {
                        if (matrix.matrix[x][z] != null) {
                            final BlockState bsWo = world.getBlockState(pos.add(x, -y + (this.ySize - 1), z));
                            Label_0382:
                            {
                                if (!(matrix.matrix[x][z].getSource() instanceof Block) || bsWo.getBlock() == matrix.matrix[x][z].getSource()) {
                                    if (!(matrix.matrix[x][z].getSource() instanceof Material) || bsWo.getMaterial() == matrix.matrix[x][z].getSource()) {
                                        if (matrix.matrix[x][z].getSource() instanceof ItemStack) {
                                            if (bsWo.getBlock() != Block.getBlockFromItem(((ItemStack) matrix.matrix[x][z].getSource()).getItem())) {
                                                break Label_0382;
                                            }
                                        }
                                        if (!(matrix.matrix[x][z].getSource() instanceof BlockState) || bsWo == matrix.matrix[x][z].getSource()) {
                                            continue;
                                        }
                                    }
                                }
                            }
                            ++i;
                            continue Label_0011;
                        }
                    }
                }
            }
            return face;
        }
        return null;
    }

    @Override
    public void execute(final World world, final PlayerEntity player, final BlockPos pos, final Placement placement, final Direction side) {
        if (!world.isRemote) {
            final BlockPos p2 = pos.add(placement.xOffset, placement.yOffset, placement.zOffset);
            for (int y = 0; y < this.ySize; ++y) {
                final Matrix matrix = new Matrix(this.blueprint[y]);
                matrix.Rotate90DegRight(3 - placement.facing.getHorizontalIndex());
                for (int x = 0; x < matrix.rows; ++x) {
                    for (int z = 0; z < matrix.cols; ++z) {
                        if (matrix.matrix[x][z] != null && matrix.matrix[x][z].getTarget() != null) {
                            BlockState targetObject;
                            if (matrix.matrix[x][z].getTarget() instanceof Block) {
                                Direction side2 = side;
                                targetObject = ((Block) matrix.matrix[x][z].getTarget()).getDefaultState();
                            } else if (matrix.matrix[x][z].getTarget() instanceof ItemStack) {
                                targetObject = Block.getBlockFromItem(((ItemStack) matrix.matrix[x][z].getTarget()).getItem()).getDefaultState();
                            } else {
                                targetObject = null;
                            }
                            firePlayerCraftingEvent(player, new ItemStack(targetObject.getBlock()), (IInventory) new InventoryFake(1));
                            final BlockPos p3 = p2.add(x, -y + (this.ySize - 1), z);
                            Object sourceObject;
                            if (matrix.matrix[x][z].getSource() instanceof Block) {
                                sourceObject = world.getBlockState(p3);
                            } else if (matrix.matrix[x][z].getSource() instanceof Material) {
                                sourceObject = matrix.matrix[x][z].getSource();
                            } else if (matrix.matrix[x][z].getSource() instanceof BlockState) {
                                sourceObject = matrix.matrix[x][z].getSource();
                            } else {
                                sourceObject = null;
                            }
                            MultiblockEvents.addRunnableServer(world, new Runnable() {
                                @Override
                                public void run() {
                                    MultiblockEvents.addSwapper(world, p3, sourceObject, targetObject, false, 0, player, true, false, -9999, false, false, 0, MultiblockEvents.DEFAULT_PREDICATE);
                                }
                            }, matrix.matrix[x][z].getPriority());
                        }
                    }
                }
            }
        }
    }

}
