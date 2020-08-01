package binary404.runic.client.gui;

import binary404.runic.api.capability.CapabilityHelper;
import binary404.runic.api.capability.IPlayerKnowledge;
import binary404.runic.api.research.ResearchCategories;
import binary404.runic.api.research.ResearchCategory;
import binary404.runic.api.research.ResearchEntry;
import binary404.runic.client.utils.RenderingUtils;
import binary404.runic.common.config.ResearchManager;
import binary404.runic.common.core.network.PacketHandler;
import binary404.runic.common.core.network.research.PacketSyncProgressToServer;
import binary404.runic.common.core.network.research.PacketSyncResearchFlagsToServer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class GuiResearchBrowser extends Screen {
    private static int guiBoundsLeft;
    private static int guiBoundsTop;
    private static int guiBoundsRight;
    private static int guiBoundsBottom;
    protected double mouseX;
    protected double mouseY;
    protected float zoom = 1.0f;
    protected double curMouseX;
    protected double curMouseY;
    protected double guiMapX;
    protected double guiMapY;
    protected double tempMapX;
    protected double tempMapY;
    private int isMouseButtonDown = 0;
    public static double lastX = -9999.0;
    public static double lastY = -9999.0;
    GuiResearchBrowser instance = null;
    private int screenX;
    private int screenY;
    private final int startX = 16;
    private final int startY = 16;
    long t = 0L;
    private LinkedList<ResearchEntry> researchEntries = new LinkedList<>();
    static String selectedCategory = null;
    private ResearchEntry currentHighlight = null;
    private PlayerEntity player = null;
    long popuptime = 0L;
    String popupmessage = "";
    private ArrayList<String> categoriesBase = new ArrayList<>();
    public int addonShift = 0;
    static int catScrollPos = 0;
    private ArrayList<String> invisible = new ArrayList<>();
    ResourceLocation tx1 = new ResourceLocation("runic", "textures/gui/gui_research_browser.png");

    @OnlyIn(Dist.CLIENT)
    public GuiResearchBrowser() {
        super(new StringTextComponent("runic_compendium"));
        this.guiMapX = this.tempMapX = lastX;
        this.curMouseX = this.tempMapX;
        this.guiMapY = this.tempMapY = lastY;
        this.curMouseY = this.tempMapY;
        this.player = Minecraft.getInstance().player;
        this.instance = this;
    }

    @OnlyIn(Dist.CLIENT)
    public GuiResearchBrowser(double x, double y) {
        super(new StringTextComponent("runic_compendium"));
        this.guiMapX = this.tempMapX = x;
        this.curMouseX = this.tempMapX;
        this.guiMapY = this.tempMapY = y;
        this.curMouseY = this.tempMapY;
        this.player = Minecraft.getInstance().player;
        this.instance = this;
    }

    @Override
    protected void init() {
        this.updateResearch();
    }

    public void updateResearch() {
        if (this.minecraft == null) {
            this.minecraft = Minecraft.getInstance();
        }
        this.buttons.clear();
        this.screenX = this.width - 32;
        this.screenY = this.height - 32;
        this.researchEntries.clear();
        if (selectedCategory == null) {
            Set<String> cats = ResearchCategories.researchCategories.keySet();
            selectedCategory = cats.iterator().next();
        }
        int limit = (int) Math.floor((this.screenY - 28) / 24.0F);
        this.addonShift = 0;
        int count = 0;
        this.categoriesBase.clear();
        boolean top = true;
        block0:
        for (String rcl : ResearchCategories.researchCategories.keySet()) {
            int rt = 0;
            int rco = 0;
            Collection<ResearchEntry> col = ResearchCategories.getResearchCategory(rcl).research.values();
            for (ResearchEntry res : col) {
                if (res.hasMeta(ResearchEntry.EnumResearchMeta.AUTOUNLOCK))
                    continue;
                rt++;
                if (!CapabilityHelper.knowsResearch(this.player, res.getKey()))
                    continue;
                rco++;
            }
            int v = (int) ((float) rco / (float) rt * 100.0F);
            ResearchCategory rc = ResearchCategories.getResearchCategory(rcl);
            if (rc.researchKey != null) {
                if (!CapabilityHelper.knowsResearchStrict(this.player, rc.researchKey))
                    continue;
            }
            if (top) {
                this.categoriesBase.add(rcl);
                addButton(new GuiCategoryButton(rc, v, rcl, false, 10 + this.categoriesBase.size() * 24, 1, 16, 16, I18n.format("runic.research_category." + rcl), this::category));
                top = false;
            } else if (!top) {
                this.categoriesBase.add(rcl);
                addButton(new GuiCategoryButton(rc, v, rcl, true, 10 + this.categoriesBase.size() * 24, this.height - 17, 16, 15, I18n.format("runic.research_category." + rcl), this::category));
                top = true;
            }
            ++count;
        }
        if (count > limit || count < catScrollPos) {
            this.addonShift = (this.screenY - 28) % 24 / 2;
        }
        if (selectedCategory == null || selectedCategory.equals("")) {
            return;
        }
        Collection<ResearchEntry> col = ResearchCategories.getResearchCategory(selectedCategory).research.values();
        for (ResearchEntry res : col) {
            this.researchEntries.add(res);
        }
        guiBoundsLeft = 99999;
        guiBoundsTop = 99999;
        guiBoundsRight = -99999;
        guiBoundsBottom = -99999;
        for (ResearchEntry res : this.researchEntries) {
            if (res == null)
                continue;
            if (res.getDisplayColumn() * 24 - this.screenX + 48 < guiBoundsLeft) {
                guiBoundsLeft = res.getDisplayColumn() * 24 - this.screenX + 48;
            }
            if (res.getDisplayColumn() * 24 - 24 > guiBoundsRight) {
                guiBoundsRight = res.getDisplayColumn() * 24 - 24;
            }
            if (res.getDisplayRow() * 24 - this.screenY + 48 < guiBoundsTop) {
                guiBoundsTop = res.getDisplayRow() * 24 - this.screenY + 48;
            }
            if (res.getDisplayRow() * 24 - 24 <= guiBoundsBottom)
                continue;
            guiBoundsBottom = res.getDisplayColumn() * 24 - 24;
        }
    }

    @Override
    public void tick() {
        this.curMouseX = this.guiMapX;
        this.curMouseY = this.guiMapY;
        double var1 = this.tempMapX - this.guiMapX;
        double var3 = this.tempMapY - this.guiMapY;
        if (var1 * var1 + var3 * var3 < 4.0) {
            this.guiMapX += var1;
            this.guiMapY += var3;
        } else {
            this.guiMapX += var1 * 0.85;
            this.guiMapY += var3 * 0.85;
        }
    }

    @Override
    public void init(Minecraft p_init_1_, int p_init_2_, int p_init_3_) {
        super.init(p_init_1_, p_init_2_, p_init_3_);
        this.updateResearch();
        if (lastX == -9999.0 || this.guiMapX > (double) guiBoundsRight || this.guiMapX < (double) guiBoundsLeft) {
            this.guiMapX = this.tempMapX = (double) ((guiBoundsLeft + guiBoundsRight) / 2);
        }
        if (lastY == -9999.0 || this.guiMapY > (double) guiBoundsBottom || this.guiMapY < (double) guiBoundsTop) {
            this.guiMapY = this.tempMapY = (double) ((guiBoundsBottom + guiBoundsTop) / 2);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected void category(Button button) {
        if (button instanceof GuiCategoryButton) {
            if (((GuiCategoryButton) button).key != selectedCategory) {
                selectedCategory = ((GuiCategoryButton) button).key;
                this.updateResearch();
                this.guiMapX = this.tempMapX = ((guiBoundsLeft + guiBoundsRight) / 2);
                this.guiMapY = this.tempMapY = ((guiBoundsBottom + guiBoundsTop) / 2);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double scroll) {
        if (scroll < 0) {
            this.zoom += 0.25f;
        } else if (scroll > 0) {
            this.zoom -= 0.25f;
        }

        this.mouseX = mx;
        this.mouseY = my;

        return super.mouseScrolled(mx, my, scroll);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int p_mouseReleased_5_) {
        if (p_mouseReleased_5_ == 0)
            isMouseButtonDown = 0;

        this.mouseX = mx;
        this.mouseY = my;
        return super.mouseReleased(mx, my, p_mouseReleased_5_);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int par3) {
        if (par3 == 0)
            isMouseButtonDown = 1;
        this.mouseX = mx;
        this.mouseY = my;

        this.popuptime = System.currentTimeMillis() - 1L;
        if (this.currentHighlight != null && !CapabilityHelper.knowsResearch(this.player, this.currentHighlight.getKey()) && this.canUnlockResearch(this.currentHighlight)) {
            this.updateResearch();
            PacketHandler.INSTANCE.sendToServer(new PacketSyncProgressToServer(this.currentHighlight.getKey(), true));
            this.minecraft.displayGuiScreen(new GuiResearchPage(this.currentHighlight, null, this.guiMapX, this.guiMapY));
            this.popuptime = System.currentTimeMillis() + 3000L;
            this.popupmessage = I18n.format("runic.research.popup");
        } else if (this.currentHighlight != null && CapabilityHelper.knowsResearch(this.player, this.currentHighlight.getKey())) {
            CapabilityHelper.getKnowledge(this.player).clearResearchFlag(this.currentHighlight.getKey(), IPlayerKnowledge.EnumResearchFlag.RESEARCH);
            CapabilityHelper.getKnowledge(this.player).clearResearchFlag(this.currentHighlight.getKey(), IPlayerKnowledge.EnumResearchFlag.PAGE);
            PacketHandler.INSTANCE.sendToServer(new PacketSyncResearchFlagsToServer(this.minecraft.player, this.currentHighlight.getKey()));
            final int stage = CapabilityHelper.getKnowledge(this.player).getResearchStage(this.currentHighlight.getKey());
            if (stage > 1 && stage >= this.currentHighlight.getStages().length) {
                PacketHandler.INSTANCE.sendToServer(new PacketSyncProgressToServer(this.currentHighlight.getKey(), false, true, false));
            }
            this.minecraft.displayGuiScreen(new GuiResearchPage(this.currentHighlight, null, this.guiMapX, this.guiMapY));
        }
        return super.mouseClicked(mx, my, par3);
    }

    @Override
    public void render(MatrixStack stack, int mx, int my, float par3) {
        if (this.isMouseButtonDown == 1) {
            if ((this.isMouseButtonDown == 1) && mx >= this.startX && mx < this.startX + this.screenX && my >= this.startY && my < this.startY + this.screenY) {
                this.guiMapX -= (double) (mx - this.mouseX) * (double) this.zoom;
                this.guiMapY -= (double) (my - this.mouseY) * (double) this.zoom;
                this.tempMapX = this.curMouseX = this.guiMapX;
                this.tempMapY = this.curMouseY = this.guiMapY;
                this.mouseX = mx;
                this.mouseY = my;
            }
            if (this.tempMapX < (double) guiBoundsLeft * (double) this.zoom) {
                this.tempMapX = (double) guiBoundsLeft * (double) this.zoom;
            }
            if (this.tempMapY < (double) guiBoundsTop * (double) this.zoom) {
                this.tempMapY = (double) guiBoundsTop * (double) this.zoom;
            }
            if (this.tempMapX >= (double) guiBoundsRight * (double) this.zoom) {
                this.tempMapX = (float) guiBoundsRight * this.zoom - 1.0F;
            }
            if (this.tempMapY >= (double) guiBoundsBottom * (double) this.zoom) {
                this.tempMapY = (float) guiBoundsBottom * this.zoom - 1.0F;
            }
        } else {
            this.isMouseButtonDown = 0;
        }
        this.zoom = MathHelper.clamp(this.zoom, 1.0F, 2.0F);
        this.renderBackground(stack);
        this.t = System.nanoTime() / 50000000L;
        int locX = MathHelper.floor((this.curMouseX + (this.guiMapX - this.curMouseX) * (double) par3));
        int locY = MathHelper.floor((this.curMouseY + (this.guiMapY - this.curMouseY) * (double) par3));
        if ((float) locX < (float) guiBoundsLeft * this.zoom) {
            locX = (int) ((float) guiBoundsLeft * this.zoom);
        }
        if ((float) locY < (float) guiBoundsTop * this.zoom) {
            locY = (int) ((float) guiBoundsTop * this.zoom);
        }
        if ((float) locX >= (float) guiBoundsRight * this.zoom) {
            locX = (int) ((float) guiBoundsRight * this.zoom - 1.0f);
        }
        if ((float) locY >= (float) guiBoundsBottom * this.zoom) {
            locY = (int) ((float) guiBoundsBottom * this.zoom - 1.0f);
        }
        this.genResearchBackgroundFixedPre();
        GL11.glPushMatrix();
        GL11.glScalef((1.0F / this.zoom), (1.0F / this.zoom), 1.0F);
        this.genResearchBackgroundZoomable(stack, mx, my, locX, locY);
        GL11.glPopMatrix();
        this.genResearchBackgroundFixedPost(stack, mx, my, par3);
        if (this.popuptime > System.currentTimeMillis()) {
            ArrayList<String> text = new ArrayList<>();
            text.add(this.popupmessage);
            RenderingUtils.drawCustomTooltip(stack, this.font, text, 10, 32);
        }
    }

    private void genResearchBackgroundFixedPre() {
        this.setBlitOffset(0);
        RenderSystem.pushMatrix();
        RenderSystem.depthFunc(518);
        RenderSystem.translatef(0.0F, 0.0F, -200.0F);
        RenderSystem.enableTexture();
        RenderSystem.disableLighting();
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableColorMaterial();
    }

    protected void genResearchBackgroundZoomable(MatrixStack stack, int mx, int my, int locX, int locY) {
        this.minecraft.getTextureManager().bindTexture(this.tx1);
        if (CapabilityHelper.getKnowledge(this.player).getResearchList() != null) {
            for (int index = 0; index < this.researchEntries.size(); ++index) {
                int a;
                ResearchEntry source = this.researchEntries.get(index);
                if (source.getParents() != null && source.getParents().length > 0) {
                    for (a = 0; a < source.getParents().length; ++a) {
                        ResearchEntry parent;
                        if (source.getParents()[a] == null || ResearchCategories.getResearch(source.getParentsClean()[a]) == null || !ResearchCategories.getResearch(source.getParentsClean()[a]).getCategory().equals(selectedCategory) || (parent = ResearchCategories.getResearch(source.getParentsClean()[a])).getSiblings() != null && Arrays.asList(parent.getSiblings()).contains(source.getKey()))
                            continue;
                        boolean knowsParent = CapabilityHelper.knowsResearchStrict(this.player, source.getParents()[a]);
                        boolean b = this.isVisible(source) && !source.getParents()[a].startsWith("~");
                        if (!b)
                            continue;
                        if (knowsParent) {
                            this.drawLine(stack, source.getDisplayColumn(), source.getDisplayRow(), parent.getDisplayColumn(), parent.getDisplayRow(), 0.6f, 0.6f, 0.6f, locX, locY, 3.0f, true, source.hasMeta(ResearchEntry.EnumResearchMeta.REVERSE));
                            continue;
                        }
                        if (!this.isVisible(parent))
                            continue;
                        this.drawLine(stack, source.getDisplayColumn(), source.getDisplayRow(), parent.getDisplayColumn(), parent.getDisplayRow(), 0.2f, 0.2f, 0.2f, locX, locY, 2.0f, true, source.hasMeta(ResearchEntry.EnumResearchMeta.REVERSE));
                    }
                }
                if (source.getSiblings() == null || source.getSiblings().length <= 0)
                    continue;
                for (a = 0; a < source.getSiblings().length; ++a) {
                    if (source.getSiblings()[a] == null || ResearchCategories.getResearch(source.getSiblings()[a]) == null || !ResearchCategories.getResearch(source.getSiblings()[a]).getCategory().equals(selectedCategory))
                        continue;
                    ResearchEntry sibling = ResearchCategories.getResearch(source.getSiblings()[a]);
                    boolean knowsSibling = CapabilityHelper.knowsResearchStrict(this.player, sibling.getKey());
                    if (!this.isVisible(source) || source.getSiblings()[a].startsWith("~")) continue;
                    if (knowsSibling) {
                        this.drawLine(stack, sibling.getDisplayColumn(), sibling.getDisplayRow(), source.getDisplayColumn(), source.getDisplayRow(), 0.3f, 0.3f, 0.4f, locX, locY, 1.0f, false, source.hasMeta(ResearchEntry.EnumResearchMeta.REVERSE));
                        continue;
                    }
                    if (!this.isVisible(sibling)) continue;
                    this.drawLine(stack, sibling.getDisplayColumn(), sibling.getDisplayRow(), source.getDisplayColumn(), source.getDisplayRow(), 0.1875f, 0.1875f, 0.25f, locX, locY, 0.0f, false, source.hasMeta(ResearchEntry.EnumResearchMeta.REVERSE));
                }
            }
        }
        this.currentHighlight = null;
        this.updateResearch();
        for (int var24 = 0; var24 < this.researchEntries.size(); ++var24) {
            float var38;
            RenderSystem.blendFunc((int) 770, (int) 771);
            ResearchEntry iconResearch = this.researchEntries.get(var24);
            int var26 = iconResearch.getDisplayColumn() * 24 - locX;
            int var27 = iconResearch.getDisplayRow() * 24 - locY;

            int iconX = this.startX + var26;
            int iconY = this.startY + var27;
            if (!this.isVisible(iconResearch))
                continue;
            if (CapabilityHelper.getKnowledge(this.player).isResearchComplete(iconResearch.getKey())) {
                var38 = 1.0f;
                RenderSystem.color4f((float) var38, (float) var38, (float) var38, (float) 1.0f);
            } else if (this.canUnlockResearch(iconResearch)) {
                var38 = (float) Math.sin((double) (System.currentTimeMillis() % 600L) / 600.0 * 3.141592653589793 * 2.0) * 0.25f + 0.75f;
                RenderSystem.color4f((float) var38, (float) var38, (float) var38, (float) 1.0f);
            } else {
                var38 = 0.3f;
                RenderSystem.color4f((float) var38, (float) var38, (float) var38, (float) 1.0f);
            }
            this.minecraft.getTextureManager().bindTexture(this.tx1);
            GL11.glEnable((int) 2884);
            GL11.glEnable((int) 3042);
            RenderSystem.blendFunc((int) 770, (int) 771);
            if (iconResearch.hasMeta(ResearchEntry.EnumResearchMeta.ROUND)) {
                this.blit(stack, iconX - 8, iconY - 8, 144, 48, 32, 32);
            } else {
                int ix = 80;
                int iy = 48;
                if (iconResearch.hasMeta(ResearchEntry.EnumResearchMeta.HIDDEN)) {
                    iy += 32;
                }
                if (iconResearch.hasMeta(ResearchEntry.EnumResearchMeta.HEX)) {
                    ix += 32;
                }
                this.blit(stack, iconX - 8, iconY - 8, ix, iy, 32, 32);
            }
            boolean bw = false;
            if (!this.canUnlockResearch(iconResearch)) {
                float var40 = 0.1f;
                RenderSystem.color4f((float) var40, (float) var40, (float) var40, (float) 1.0f);
                bw = true;
            }
            if (CapabilityHelper.getKnowledge(this.player).hasResearchFlag(iconResearch.getKey(), IPlayerKnowledge.EnumResearchFlag.RESEARCH)) {
                RenderSystem.pushMatrix();
                RenderSystem.color4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
                RenderSystem.translatef((float) (iconX - 9), (float) (iconY - 9), (float) 0.0f);
                RenderSystem.scaled((double) 0.5, (double) 0.5, (double) 1.0);
                this.blit(stack, 0, 0, 176, 16, 32, 32);
                RenderSystem.popMatrix();
            }
            if (CapabilityHelper.getKnowledge(this.player).hasResearchFlag(iconResearch.getKey(), IPlayerKnowledge.EnumResearchFlag.PAGE)) {
                RenderSystem.pushMatrix();
                RenderSystem.color4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
                RenderSystem.translatef((float) (iconX - 9), (float) (iconY + 9), (float) 0.0f);
                RenderSystem.scaled((double) 0.5, (double) 0.5, (double) 1.0);
                this.blit(stack, 0, 0, 208, 16, 32, 32);
                RenderSystem.popMatrix();
            }
            GuiResearchBrowser.drawResearchIcon(iconResearch, iconX, iconY, this.getBlitOffset(), bw);
            if (mx >= this.startX && my >= this.startY && mx < this.startX + this.screenX && my < this.startY + this.screenY && (float) mx >= (float) (iconX - 2) / this.zoom && (float) mx <= (float) (iconX + 18) / this.zoom && (float) my >= (float) (iconY - 2) / this.zoom && (float) my <= (float) (iconY + 18) / this.zoom) {
                this.currentHighlight = iconResearch;
            }
            RenderSystem.color4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
        }
        GL11.glDisable(2929);
    }

    private void genResearchBackgroundFixedPost(MatrixStack stack, int mx, int my, float par3) {
        int p, c;
        this.minecraft.getTextureManager().bindTexture(this.tx1);
        GL11.glEnable(3042);
        RenderSystem.blendFunc(770, 771);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        for (c = 16; c < this.width - 16; c += 64) {
            p = 64;
            if (c + p > this.width - 16) {
                p = this.width - 16 - c;
            }
            if (p <= 0)
                continue;
            this.blit(stack, c, -2, 48, 13, p, 22);
            this.blit(stack, c, this.height - 20, 48, 13, p, 22);
        }
        for (c = 16; c < this.height - 16; c += 64) {
            p = 64;
            if (c + p > this.height - 16) {
                p = this.height - 16 - c;
            }
            if (p <= 0)
                continue;
            this.blit(stack, -2, c, 13, 48, 22, p);
            this.blit(stack, this.width - 20, c, 13, 48, 22, p);
        }
        this.blit(stack, -2, -2, 13, 13, 22, 22);
        this.blit(stack, -2, this.height - 20, 13, 13, 22, 22);
        this.blit(stack, this.width - 20, -2, 13, 13, 22, 22);
        this.blit(stack, this.width - 20, this.height - 20, 13, 13, 22, 22);
        RenderSystem.popMatrix();
        this.setBlitOffset(0);
        RenderSystem.depthFunc(515);
        GL11.glDisable(2929);
        GL11.glEnable(3553);
        super.render(stack, mx, my, par3);
        if (this.currentHighlight != null) {
            ArrayList<String> text = new ArrayList<>();
            text.add("\u00a76" + this.currentHighlight.getLocalizedName());
            if (this.canUnlockResearch(this.currentHighlight)) {
                if (!CapabilityHelper.getKnowledge(this.player).isResearchComplete(this.currentHighlight.getKey()) && this.currentHighlight.getStages() != null) {
                    int stage = CapabilityHelper.getKnowledge(this.player).getResearchStage(this.currentHighlight.getKey());
                    if (stage > 0) {
                        text.add("@@" + (Object) TextFormatting.AQUA + I18n.format((String) "runic.research.stage") + " " + stage + "/" + this.currentHighlight.getStages().length + (Object) TextFormatting.RESET);
                    } else {
                        text.add("@@" + (Object) TextFormatting.GREEN + I18n.format((String) "runic.research.begin") + (Object) TextFormatting.RESET);
                    }
                }
            } else {
                text.add("@@\u00a7c" + I18n.format("runic.researchmissing"));
                int a = 0;
                for (String p2 : this.currentHighlight.getParents()) {
                    if (!CapabilityHelper.knowsResearchStrict(this.player, p2)) {
                        String s = "?";
                        try {
                            s = ResearchCategories.getResearch(this.currentHighlight.getParentsClean()[a]).getLocalizedName();
                        } catch (Exception exception) {
                            // empty catch block
                        }
                        text.add("@@\u00a7e - " + s);
                    }
                    ++a;
                }
            }
            if (CapabilityHelper.getKnowledge(this.player).hasResearchFlag(this.currentHighlight.getKey(), IPlayerKnowledge.EnumResearchFlag.RESEARCH)) {
                text.add(I18n.format("runic.research.newresearch"));
            }
            if (CapabilityHelper.getKnowledge(this.player).hasResearchFlag(this.currentHighlight.getKey(), IPlayerKnowledge.EnumResearchFlag.PAGE)) {
                text.add(I18n.format((String) "runic.research.newpage"));
            }
            RenderingUtils.drawCustomTooltip(stack, this.font, text, mx + 3, my - 3);
        }
        RenderSystem.enableDepthTest();
        RenderSystem.enableLighting();
        RenderHelper.disableStandardItemLighting();
    }

    private boolean canUnlockResearch(ResearchEntry res) {
        return ResearchManager.doesPlayerHaveRequisites(this.player, res.getKey());
    }

    private boolean isVisible(ResearchEntry res) {
        if (CapabilityHelper.knowsResearch(this.player, res.getKey())) {
            return true;
        }
        if (this.invisible.contains(res.getKey()) || res.hasMeta(ResearchEntry.EnumResearchMeta.HIDDEN) && !this.canUnlockResearch(res)) {
            return false;
        }
        if (res.getParents() != null) {
            for (String r : res.getParents()) {
                ResearchEntry ri = ResearchCategories.getResearch(r);
                if (ri == null || this.isVisible(ri))
                    continue;
                this.invisible.add(r);
                return false;
            }
        }
        return true;
    }

    private void drawLine(MatrixStack stack, int x, int y, int x2, int y2, float r, float g, float b, int locX, int locY, float zMod, boolean arrow, boolean flipped) {
        int yy, xx, ym, xm, yd, xd;
        float zt = this.getBlitOffset();

        this.setBlitOffset((int) (this.getBlitOffset() + zMod));

        boolean bigCorner = false;


        if (flipped) {
            xd = Math.abs(x2 - x);
            yd = Math.abs(y2 - y);
            xm = (xd == 0) ? 0 : ((x2 - x > 0) ? -1 : 1);
            ym = (yd == 0) ? 0 : ((y2 - y > 0) ? -1 : 1);
            if (xd > 1 && yd > 1) {
                bigCorner = true;
            }
            xx = x2 * 24 - 4 - locX + this.startX;
            yy = y2 * 24 - 4 - locY + this.startY;
        } else {
            xd = Math.abs(x - x2);
            yd = Math.abs(y - y2);
            xm = (xd == 0) ? 0 : ((x - x2 > 0) ? -1 : 1);
            ym = (yd == 0) ? 0 : ((y - y2 > 0) ? -1 : 1);
            if (xd > 1 && yd > 1) {
                bigCorner = true;
            }
            xx = x * 24 - 4 - locX + this.startX;
            yy = y * 24 - 4 - locY + this.startY;
        }

        RenderSystem.pushMatrix();
        RenderSystem.alphaFunc(516, 0.003921569F);
        GL11.glEnable(3042);
        RenderSystem.blendFunc(770, 771);
        RenderSystem.color4f(r, g, b, 1.0F);

        if (arrow) {
            if (flipped) {
                int xx3 = x * 24 - 8 - locX + this.startX;
                int yy3 = y * 24 - 8 - locY + this.startY;
                if (xm < 0) {
                    blit(stack, xx3, yy3, 160, 112, 32, 32);
                } else if (xm > 0) {
                    blit(stack, xx3, yy3, 128, 112, 32, 32);
                } else if (ym > 0) {
                    blit(stack, xx3, yy3, 64, 112, 32, 32);
                } else if (ym < 0) {
                    blit(stack, xx3, yy3, 96, 112, 32, 32);
                }
            } else if (ym < 0) {
                blit(stack, xx - 4, yy - 4, 64, 112, 32, 32);
            } else if (ym > 0) {
                blit(stack, xx - 4, yy - 4, 96, 112, 32, 32);
            } else if (xm > 0) {
                blit(stack, xx - 4, yy - 4, 160, 112, 32, 32);
            } else if (xm < 0) {
                blit(stack, xx - 4, yy - 4, 128, 112, 32, 32);
            }

        }

        int v = 1;
        int h = 0;
        for (; v < yd - (bigCorner ? 1 : 0); v++) {
            blit(stack, xx + xm * 24 * h, yy + ym * 24 * v, 0, 228, 24, 24);
        }

        if (bigCorner) {
            if (xm < 0 && ym > 0) blit(stack, xx + xm * 24 * h - 24, yy + ym * 24 * v, 0, 180, 48, 48);
            if (xm > 0 && ym > 0) blit(stack, xx + xm * 24 * h, yy + ym * 24 * v, 48, 180, 48, 48);
            if (xm < 0 && ym < 0) blit(stack, xx + xm * 24 * h - 24, yy + ym * 24 * v - 24, 96, 180, 48, 48);
            if (xm > 0 && ym < 0) blit(stack, xx + xm * 24 * h, yy + ym * 24 * v - 24, 144, 180, 48, 48);
        } else {
            if (xm < 0 && ym > 0) blit(stack, xx + xm * 24 * h, yy + ym * 24 * v, 48, 228, 24, 24);
            if (xm > 0 && ym > 0) blit(stack, xx + xm * 24 * h, yy + ym * 24 * v, 72, 228, 24, 24);
            if (xm < 0 && ym < 0) blit(stack, xx + xm * 24 * h, yy + ym * 24 * v, 96, 228, 24, 24);
            if (xm > 0 && ym < 0) blit(stack, xx + xm * 24 * h, yy + ym * 24 * v, 120, 228, 24, 24);

        }
        v += (bigCorner ? 1 : 0);
        h += (bigCorner ? 2 : 1);
        for (; h < xd; h++) {
            blit(stack, xx + xm * 24 * h, yy + ym * 24 * v, 24, 228, 24, 24);
        }
        RenderSystem.blendFunc(770, 771);
        GL11.glDisable(3042);
        RenderSystem.alphaFunc(516, 0.1F);
        RenderSystem.popMatrix();

        this.setBlitOffset((int) zt);
    }

    public static void drawResearchIcon(ResearchEntry iconResearch, int iconX, int iconY, float zLevel, boolean bw) {
        if (iconResearch.getIcons() != null && iconResearch.getIcons().length > 0) {
            int idx = (int) (System.currentTimeMillis() / 1000L % (long) iconResearch.getIcons().length);
            RenderSystem.pushMatrix();
            GL11.glEnable((int) 3042);
            RenderSystem.blendFunc((int) 770, (int) 771);
            if (iconResearch.getIcons()[idx] instanceof ResourceLocation) {
                Minecraft.getInstance().textureManager.bindTexture((ResourceLocation) iconResearch.getIcons()[idx]);
                if (bw) {
                    RenderSystem.color4f((float) 0.2f, (float) 0.2f, (float) 0.2f, (float) 1.0f);
                }
                int w = GL11.glGetTexLevelParameteri((int) 3553, (int) 0, (int) 4096);
                int h = GL11.glGetTexLevelParameteri((int) 3553, (int) 0, (int) 4097);
                if (h > w && h % w == 0) {
                    int m = h / w;
                    float q = 16.0f / (float) m;
                    float idx1 = (float) (System.currentTimeMillis() / 150L % (long) m) * q;
                    RenderingUtils.drawTexturedQuadF(iconX, iconY, 0.0f, idx1, 16.0f, q, zLevel);
                } else if (w > h && w % h == 0) {
                    int m = w / h;
                    float q = 16.0f / (float) m;
                    float idx1 = (float) (System.currentTimeMillis() / 150L % (long) m) * q;
                    RenderingUtils.drawTexturedQuadF(iconX, iconY, idx1, 0.0f, q, 16.0f, zLevel);
                } else {
                    RenderingUtils.drawTexturedQuadFull(iconX, iconY, zLevel);
                }
            } else if (iconResearch.getIcons() instanceof ItemStack[]) {
                GL11.glDisable((int) 2896);
                GL11.glEnable((int) 32826);
                GL11.glEnable((int) 2903);
                GL11.glEnable((int) 2896);
                Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI((ItemStack) iconResearch.getIcons()[idx], iconX, iconY);
                GL11.glDisable((int) 2896);
                RenderSystem.depthMask((boolean) true);
                GL11.glEnable((int) 2929);
            }
            GL11.glDisable((int) 3042);
            RenderSystem.popMatrix();
        }
    }


    private class GuiCategoryButton extends Button {
        ResearchCategory rc;
        String key;
        boolean flip;
        int completion;

        public GuiCategoryButton(ResearchCategory rc, int completion, String key, boolean flip, int x, int y, int w, int h, String text, IPressable onPress) {
            super(x, y, w, h, ITextComponent.func_241827_a_(text), onPress);
            this.rc = rc;
            this.key = key;
            this.flip = flip;
            this.completion = completion;
        }

        @Override
        public void renderButton(MatrixStack stack, int mouseX, int mouseY, float p_render_3_) {
            if (this.visible) {
                this.isHovered = mouseX >= this.x + GuiResearchBrowser.this.addonShift && mouseY >= this.y && mouseX < this.x + this.width + GuiResearchBrowser.this.addonShift && mouseY < this.y + this.height;
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate((int) 770, (int) 771, (int) 1, (int) 0);
                RenderSystem.blendFunc((int) 770, (int) 771);
                minecraft.textureManager.bindTexture(GuiResearchBrowser.this.tx1);
                RenderSystem.pushMatrix();
                if (!selectedCategory.equals(this.key)) {
                    RenderSystem.color4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
                } else {
                    RenderSystem.color4f((float) 0.6f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
                }
                this.blit(stack, this.x - 3 + GuiResearchBrowser.this.addonShift, this.y - 3, 13, 13, 22, 22);
                RenderSystem.popMatrix();
                RenderSystem.pushMatrix();
                minecraft.textureManager.bindTexture(this.rc.icon);
                if (selectedCategory.equals(this.key) || this.isHovered) {
                    RenderSystem.color4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 0.3f);
                } else {
                    RenderSystem.color4f((float) 0.66f, (float) 0.66f, (float) 0.66f, (float) 0.6f);
                }
                RenderingUtils.drawTexturedQuadFull(this.x + GuiResearchBrowser.this.addonShift, this.y, -80.0);
                RenderSystem.popMatrix();
                minecraft.textureManager.bindTexture(GuiResearchBrowser.this.tx1);
                boolean nr = false;
                boolean np = false;
                for (String rk : this.rc.research.keySet()) {
                    if (!CapabilityHelper.knowsResearch(GuiResearchBrowser.this.player, rk))
                        continue;
                    if (!nr && CapabilityHelper.getKnowledge(GuiResearchBrowser.this.player).hasResearchFlag(rk, IPlayerKnowledge.EnumResearchFlag.RESEARCH)) {
                        nr = true;
                    }
                    if (!np && CapabilityHelper.getKnowledge(GuiResearchBrowser.this.player).hasResearchFlag(rk, IPlayerKnowledge.EnumResearchFlag.PAGE)) {
                        np = true;
                    }
                    if (!nr || !np) continue;
                    break;
                }
                if (nr) {
                    RenderSystem.pushMatrix();
                    RenderSystem.color4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 0.7f);
                    RenderSystem.translated((double) (this.x + GuiResearchBrowser.this.addonShift - 2), (double) (this.y - 2), (double) 0.0);
                    RenderSystem.scaled((double) 0.25, (double) 0.25, (double) 1.0);
                    this.blit(stack, 0, 0, 176, 16, 32, 32);
                    RenderSystem.popMatrix();
                }
                if (np) {
                    RenderSystem.pushMatrix();
                    RenderSystem.color4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 0.7f);
                    RenderSystem.translated((double) (this.x + GuiResearchBrowser.this.addonShift + 9), (double) (this.y - 2), (double) 0.0);
                    RenderSystem.scaled((double) 0.25, (double) 0.25, (double) 1.0);
                    this.blit(stack, 0, 0, 208, 16, 32, 32);
                    RenderSystem.popMatrix();
                }
                if (this.isHovered) {
                    String dp = this.getMessage().getString() + " (" + this.completion + "%)";
                    this.drawString(stack, minecraft.fontRenderer, dp, !this.flip ? this.x + 22 : GuiResearchBrowser.this.screenX + 9 - minecraft.fontRenderer.getStringWidth(dp), this.y + 4 + GuiResearchBrowser.this.addonShift, 16777215);
                    int t = 9;
                    if (nr) {
                        this.drawString(stack, minecraft.fontRenderer, I18n.format((String) "runic.research.newresearch"), !this.flip ? this.x + 22 : GuiResearchBrowser.this.screenX + 9 - minecraft.fontRenderer.getStringWidth(I18n.format((String) "runic.research.newresearch")), this.y + 4 + t + GuiResearchBrowser.this.addonShift, 16777215);
                        t += 9;
                    }
                    if (np) {
                        this.drawString(stack, minecraft.fontRenderer, I18n.format((String) "runic.research.newpage"), !this.flip ? this.x + 22 : GuiResearchBrowser.this.screenX + 9 - minecraft.fontRenderer.getStringWidth(I18n.format((String) "runic.research.newpage")), this.y + 4 + t + GuiResearchBrowser.this.addonShift, 16777215);
                    }
                }
            }
        }
    }

}
