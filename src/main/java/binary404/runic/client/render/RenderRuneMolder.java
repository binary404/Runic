package binary404.runic.client.render;

import binary404.runic.client.libs.ClientTickHandler;
import binary404.runic.client.libs.RenderTypes;
import binary404.runic.common.items.ModItems;
import binary404.runic.common.tile.TileRuneMolder;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.MinecraftForgeClient;

public class RenderRuneMolder extends TileEntityRenderer<TileRuneMolder> {

    public RenderRuneMolder(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileRuneMolder te, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        stack.push();
        float y = 0.5F + func_229051_a_(te, partialTicks) / 4F;
        stack.rotate(Vector3f.XP.rotationDegrees(90F));
        stack.translate(0.0F, 0.0F, -1F);
        stack.translate(0.5F, 0.5F, -y);
        stack.rotate(Vector3f.ZP.rotationDegrees(te.progress));
        stack.translate(-0.5F, -0.5F, 0);
        Matrix4f mat = stack.getLast().getMatrix();
        IVertexBuilder buffer = bufferIn.getBuffer(RenderTypes.getTEOverlay(new ResourceLocation("runic", "textures/misc/rune_molder.png")));
        buffer.pos(mat, 0, 0 + 1, 0).color(1, 1, 1, 0.8F).tex(0, 1).endVertex();
        buffer.pos(mat, 0 + 1, 0 + 1, 0).color(1, 1, 1, 0.8F).tex(1, 1).endVertex();
        buffer.pos(mat, 0 + 1, 0, 0).color(1, 1, 1, 0.8F).tex(1, 0).endVertex();
        buffer.pos(mat, 0, 0, 0).color(1, 1, 1, 0.8F).tex(0, 0).endVertex();
        stack.pop();
        stack.push();
        y = 0.56F + func_229051_a_(te, partialTicks) / 4F;
        stack.rotate(Vector3f.XP.rotationDegrees(90F));
        stack.translate(0.0F, 0.0F, -1F);
        stack.translate(0.5F, 0.5, -y);
        stack.rotate(Vector3f.ZP.rotationDegrees(te.progress * -1F));
        stack.translate(-0.5F, -0.5F, 0.0F);
        Minecraft mc = Minecraft.getInstance();
        mc.getItemRenderer().renderItem(new ItemStack(ModItems.basic_mold), ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, stack, bufferIn);
        stack.pop();
    }

    public static float func_229051_a_(TileRuneMolder te, float p_229051_1_) {
        if (te.progress == 0)
            return 0F;
        float f = (float) ClientTickHandler.ticksInGame + p_229051_1_;
        float f1 = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
        f1 = (f1 * f1 + f1) * 0.4F;
        return f1 - 1.4F;
    }
}
