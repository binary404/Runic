package binary404.runic.client.core.handler;

import binary404.runic.api.capability.CapabilityHelper;
import binary404.runic.api.internal.CommonInternals;
import binary404.runic.api.multiblock.BluePrint;
import binary404.runic.api.multiblock.Matrix;
import binary404.runic.api.multiblock.MultiBlockComponent;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class MultiBlockHandler {

    public static boolean hasMultiBlock = true;
    public static BlockPos pos = new BlockPos(10, 60, 10);
    public static BluePrint multiblock;
    public static Direction facing = Direction.NORTH;

    private static IRenderTypeBuffer.Impl buffers = null;

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        if (hasMultiBlock && multiblock != null) {
            renderMultiBlock(Minecraft.getInstance().world, event.getMatrixStack());
        }
    }

    public static void renderMultiBlock(World world, MatrixStack ms) {
        Minecraft mc = Minecraft.getInstance();
        EntityRendererManager erd = mc.getRenderManager();
        double renderPosX = erd.info.getProjectedView().getX();
        double renderPosY = erd.info.getProjectedView().getY();
        double renderPosZ = erd.info.getProjectedView().getZ();
        ms.translate(-renderPosX, -renderPosY, -renderPosZ);

        if (buffers == null) {
            buffers = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        }

        BlueprintBlockAccess ba = new BlueprintBlockAccess(multiblock.getMultiBlockComponents(), false);

        int blocksDone = 0;
        int totalBlocks = 0;
        ms.translate(pos.getX(), pos.getY(), pos.getZ());
        int ySize = multiblock.getMultiBlockComponents().length;
        int xSize = multiblock.getMultiBlockComponents()[0].length;
        int zSize = multiblock.getMultiBlockComponents()[0][0].length;
        ms.translate(xSize / 2, ySize / 2, zSize / 2);
        ms.rotate(Vector3f.YP.rotationDegrees(facing.getHorizontalIndex() * 90F));
        ms.translate(-xSize / 2, -ySize / 2, -zSize / 2);
        if (CapabilityHelper.knowsResearch(mc.player, multiblock.getResearch())) {
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    for (int z = 0; z < zSize; z++) {
                        BlockPos test = new BlockPos(x, y, z);
                        BlockPos checkPos = pos.add(x, y, z);
                        BlockState state = ba.getBlockState(test);
                        if (world.getBlockState(checkPos).getBlock() == state.getBlock() || state.getBlock() == Blocks.AIR)
                            blocksDone++;
                        if (world.getBlockState(checkPos).getBlock() != state.getBlock())
                            renderBlock(world, state, test, 1.0F, ms);
                        totalBlocks++;
                    }
                }
            }
            if (blocksDone >= totalBlocks) {
                hasMultiBlock = false;
            }
            buffers.finish();
        }
    }

    public static void renderBlock(World world, BlockState state, BlockPos pos, float alpha, MatrixStack ms) {
        if (pos != null) {
            ms.push();
            ms.translate(pos.getX(), pos.getY(), pos.getZ());
            Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(state, ms, buffers, 0xF000F0, OverlayTexture.NO_OVERLAY);
            ms.pop();
        }
    }


    public static class BlueprintBlockAccess implements IBlockReader {
        private MultiBlockComponent[][][] data;
        private BlockState[][][] structure;
        public int sliceLine;

        public BlueprintBlockAccess(MultiBlockComponent[][][] data, boolean target) {
            this.sliceLine = 0;

            this.data = new MultiBlockComponent[data.length][data[0].length][data[0][0].length];
            for (int y = 0; y < data.length; y++) {
                for (int x = 0; x < data[0].length; x++) {
                    for (int z = 0; z < data[0][0].length; z++)
                        this.data[y][x][z] = data[y][x][z];
                }
            }
            this.structure = new BlockState[data.length][data[0].length][data[0][0].length];
            if (target)
                for (int y = 0; y < this.data.length; y++) {
                    Matrix matrix = new Matrix(this.data[y]);
                    matrix.Rotate90DegRight(3);
                    this.data[y] = matrix.getMatrix();
                }
            for (int y = 0; y < data.length; y++) {
                for (int x = 0; x < data[0].length; x++) {
                    for (int z = 0; z < data[0][0].length; z++)
                        this.structure[data.length - y - 1][x][z] = target ? convertTarget(x, y, z) : convert(x, y, z);
                }
            }
        }

        public void rotate(int times) {
            for (int a = 0; a < times; a++) {
                MultiBlockComponent[][][] newArray = new MultiBlockComponent[data.length][data[0].length][data[0][0].length];
                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < data[0].length; j++) {
                        for (int k = 0; k < data[0][0].length; j++) {

                        }
                    }
                }
            }
        }

        private BlockState convert(int x, int y, int z) {
            if (this.data[y][x][z] == null || this.data[y][x][z].getSource() == null)
                return Blocks.AIR.getDefaultState();
            if (this.data[y][x][z].getSource() instanceof ItemStack &&
                    Block.getBlockFromItem(((ItemStack) this.data[y][x][z].getSource()).getItem()) != null) {
                return Block.getBlockFromItem(((ItemStack) this.data[y][x][z].getSource()).getItem()).getDefaultState();
            }
            if (this.data[y][x][z].getSource() instanceof Block) {
                return ((Block) this.data[y][x][z].getSource()).getDefaultState();
            }
            if (this.data[y][x][z].getSource() instanceof BlockState) {
                return (BlockState) this.data[y][x][z].getSource();
            }
            if (this.data[y][x][z].getSource() instanceof Material) {
                if ((Material) this.data[y][x][z].getSource() == Material.LAVA) {
                    return Blocks.LAVA.getDefaultState();
                }
                if ((Material) this.data[y][x][z].getSource() == Material.WATER) {
                    return Blocks.WATER.getDefaultState();
                }
            }
            return Blocks.AIR.getDefaultState();
        }


        private BlockState convertTarget(int x, int y, int z) {
            if (this.data[y][x][z] == null) return Blocks.AIR.getDefaultState();
            if (this.data[y][x][z].getTarget() == null) return convert(x, y, z);
            if (this.data[y][x][z].getTarget() instanceof ItemStack &&
                    Block.getBlockFromItem(((ItemStack) this.data[y][x][z].getTarget()).getItem()) != null) {
                return Block.getBlockFromItem(((ItemStack) this.data[y][x][z].getTarget()).getItem()).getDefaultState();
            }
            if (this.data[y][x][z].getTarget() instanceof Block) {
                return ((Block) this.data[y][x][z].getTarget()).getDefaultState();
            }
            if (this.data[y][x][z].getTarget() instanceof BlockState) {
                return (BlockState) this.data[y][x][z].getTarget();
            }
            if (this.data[y][x][z].getTarget() instanceof Material) {
                if ((Material) this.data[y][x][z].getTarget() == Material.LAVA) {
                    return Blocks.LAVA.getDefaultState();
                }
                if ((Material) this.data[y][x][z].getTarget() == Material.WATER) {
                    return Blocks.WATER.getDefaultState();
                }
            }
            return Blocks.AIR.getDefaultState();
        }

        public TileEntity getTileEntity(BlockPos pos) {
            return null;
        }

        @Override
        public int getHeight() {
            return this.data.length;
        }

        @Override
        public BlockRayTraceResult rayTraceBlocks(RayTraceContext context) {
            return null;
        }

        @Nullable
        @Override
        public BlockRayTraceResult rayTraceBlocks(Vec3d startVec, Vec3d endVec, BlockPos pos, VoxelShape shape, BlockState state) {
            return null;
        }

        @Override
        public int getMaxLightLevel() {
            return 15728880;
        }

        @Override
        public int getLightValue(BlockPos pos) {
            return 15728880;
        }

        public BlockState getBlockState(BlockPos pos) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            if (this.sliceLine > this.structure.length) this.sliceLine = 0;

            if (y >= 0 && y < this.structure.length - this.sliceLine &&
                    x >= 0 && x < this.structure[y].length &&
                    z >= 0 && z < this.structure[y][x].length) {
                return this.structure[y][x][z];
            }
            return Blocks.AIR.getDefaultState();
        }

        @Override
        public IFluidState getFluidState(BlockPos pos) {
            return Blocks.AIR.getFluidState(Blocks.AIR.getDefaultState());
        }

        public boolean isAirBlock(BlockPos pos) {
            return (getBlockState(pos).getBlock() == Blocks.AIR);
        }

    }

    public static Direction getRotation(Entity entity) {
        return entity.getHorizontalFacing();
    }

    public static Rotation rotationFromFacing(Direction facing) {
        switch (facing) {
            case EAST:
                return Rotation.CLOCKWISE_90;
            case SOUTH:
                return Rotation.CLOCKWISE_180;
            case WEST:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                return Rotation.NONE;
        }
    }

}
