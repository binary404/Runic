package binary404.mystica.client.gui;

import binary404.mystica.api.research.ResearchEntry;
import binary404.mystica.api.research.ResearchEvent;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class ResearchToast implements IToast {

    ResearchEntry entry;
    private long firstDrawTime;
    private boolean newDisplay;
    ResourceLocation tex;

    public ResearchToast(ResearchEntry entry) {
        this.tex = new ResourceLocation("mystica", "textures/gui/hud.png");
        this.entry = entry;
    }

    @Override
    public Visibility func_230444_a_(MatrixStack stack, ToastGui toast, long delta) {
        if (this.newDisplay) {
            this.firstDrawTime = delta;
            this.newDisplay = false;
        }
        toast.getMinecraft().getTextureManager().bindTexture(this.tex);
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        toast.blit(stack, 0, 0, 0, 224, 160, 32);
        GuiResearchBrowser.drawResearchIcon(this.entry, 6, 8, 0.0F, false);

        toast.getMinecraft().fontRenderer.drawString(stack, I18n.format("research.complete"), 30, 7, 10631665);

        String s = this.entry.getLocalizedName();
        float w = toast.getMinecraft().fontRenderer.getStringWidth(s);
        if (w > 124.0F) {
            w = 124.0F / w;
            stack.push();
            stack.translate(30.0F, 18.0F, 0.0F);
            stack.scale(w, w, w);
            toast.getMinecraft().fontRenderer.drawString(stack, s, 0, 0, 16755465);
            stack.pop();
        } else {
            toast.getMinecraft().fontRenderer.drawString(stack, s, 30, 18, 16755465);
        }
        return (delta - this.firstDrawTime < 5000L) ? Visibility.SHOW : Visibility.HIDE;
    }
}
