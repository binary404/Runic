package binary404.runic.client.render;

import binary404.runic.Runic;
import binary404.runic.common.tile.TileRuneMolder;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.EmptyModelData;

public class RenderRuneMolder extends TileEntityRenderer<TileRuneMolder> {

    private final DynamicModel<Void> press = DynamicModel.createSimple(
            new ResourceLocation(Runic.modid, "block/rune_molder_press.obj"), "rune_molder_press", DynamicModel.ModelType.OBJ
    );

    private final DynamicModel<Void> normal = DynamicModel.createSimple(
            new ResourceLocation(Runic.modid, "block/rune_molder.obj"), "rune_molder", DynamicModel.ModelType.OBJ
    );

    public RenderRuneMolder(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileRuneMolder te, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        stack.push();
        stack.translate(0.5, 0.0, 0.5);

        IBakedModel model = press.get(null);

        BlockPos blockPos = te.getPos();
        BlockState state = te.getWorld().getBlockState(blockPos);

        BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
        stack.push();
        float h = te.press;
        double s = Math.sin(Math.toRadians(h)) * 0.625D;
        stack.translate(0.0D, -s, 0.0D);
        blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), model, state, blockPos.up(), stack,
                bufferIn.getBuffer(RenderType.getSolid()), true,
                te.getWorld().rand, 0, combinedOverlayIn, EmptyModelData.INSTANCE);
        stack.pop();
        blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), normal.get(null), state, blockPos, stack, bufferIn.getBuffer(RenderType.getSolid()), true,
                te.getWorld().rand, 0, combinedOverlayIn, EmptyModelData.INSTANCE);
        stack.pop();
    }
}
