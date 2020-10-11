package binary404.mystica.client.render.entity;

import binary404.mystica.api.capability.CapabilityHelper;
import binary404.mystica.api.capability.IPlayerKnowledge;
import binary404.mystica.api.research.ResearchCategories;
import binary404.mystica.client.core.handler.RenderTypes;
import binary404.mystica.client.core.event.RenderEventHandler;
import binary404.mystica.client.utils.RenderingUtils;
import binary404.mystica.common.config.ResearchManager;
import binary404.mystica.common.core.network.PacketHandler;
import binary404.mystica.common.core.network.research.PacketAddPoints;
import binary404.mystica.common.core.util.EntityUtils;
import binary404.mystica.common.entity.weave.EntityWeaveThread;
import binary404.mystica.common.world.weave.ThreadType;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class RenderWeaveThread extends EntityRenderer<EntityWeaveThread> {

    public RenderWeaveThread(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(EntityWeaveThread entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (!entityIn.isAlive())
            return;

        double vr = 800.0D;

        boolean canSee = EntityUtils.hasRevealer(Minecraft.getInstance().getRenderViewEntity());

        vr = EntityUtils.getThreadViewDistance(Minecraft.getInstance().getRenderViewEntity());

        if (!canSee)
            return;

        double d = entityIn.getDistanceSq(Minecraft.getInstance().getRenderViewEntity());

        if (d > vr)
            return;

        ThreadType type = ThreadType.threadTypes[entityIn.getThreadType()];

        float size = 0.3F + entityIn.getThreadSize() / 100 * 1.5F;

        IVertexBuilder buffer = bufferIn.getBuffer(RenderTypes.thread);
        matrixStackIn.push();
        matrixStackIn.rotate(renderManager.getCameraOrientation());
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));

        matrixStackIn.push();
        matrixStackIn.scale(size, size, size);
        RenderingUtils.drawRect(matrixStackIn.getLast().getMatrix(), buffer, (entityIn.ticksExisted % 32), 32, type.r, type.g, type.b, 0.7F);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.scale(size / 3, size / 3, size / 3);
        RenderingUtils.drawRect(matrixStackIn.getLast().getMatrix(), buffer, 32 * entityIn.getThreadType() + entityIn.ticksExisted % 32, 32);
        matrixStackIn.pop();

        matrixStackIn.push();
        float s = 1.0F - MathHelper.sin((entityIn.ticksExisted + partialTicks) / 8.0F) / 5.0F;
        float scale = s * size * 0.7F;
        matrixStackIn.scale(scale, scale, scale);
        RenderingUtils.drawRect(matrixStackIn.getLast().getMatrix(), buffer, 800 + entityIn.ticksExisted % 16, 32, type.r, type.g, type.b, 0.7F);
        matrixStackIn.pop();

        matrixStackIn.pop();

        matrixStackIn.push();
        if (d < 30.0D) {
            matrixStackIn.rotate(renderManager.getCameraOrientation());
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180));
            float st = 1F / 200F;
            matrixStackIn.scale(st, st, st);
            matrixStackIn.translate(0.0F, 35F, 0.0F);
            matrixStackIn.translate(0F, 0F, -16.5F);

            String text = I18n.format("threadtype." + type.id);
            int sw = Minecraft.getInstance().fontRenderer.getStringWidth(text);
            this.renderManager.getFontRenderer().drawString(matrixStackIn, text, -sw / 2.0F, -72.0F, 16777215);
            matrixStackIn.translate(0.0F, 10F, 0.0F);
            text = RenderEventHandler.hudHandler.secondsFormatter.format(entityIn.getThreadSize());
            sw = Minecraft.getInstance().fontRenderer.getStringWidth(text);
            this.renderManager.getFontRenderer().drawString(matrixStackIn, text, -sw / 2.0F, -72.0F, 16777215);
            PacketHandler.sendToServer(new PacketAddPoints("BASICS", 10 + entityIn.world.rand.nextInt(20), "THREADS", true));
        }
        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityWeaveThread entity) {
        return new ResourceLocation("mystica", "textures/misc/threads.png");
    }
}
