package binary404.mystica.client.gui;

import binary404.mystica.api.item.IWand;
import binary404.mystica.api.recipe.IArcaneCraftingRecipe;
import binary404.mystica.common.container.ContainerArcaneWorkbench;
import binary404.mystica.common.core.recipe.ModRecipeTypes;
import binary404.mystica.common.tile.crafting.TileArcaneWorkbench;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.Mod;

public class GuiArcaneWorkbench extends ContainerScreen<ContainerArcaneWorkbench> {

    TileArcaneWorkbench tileEntity;

    public GuiArcaneWorkbench(ContainerArcaneWorkbench screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

        this.tileEntity = screenContainer.tile;
        this.xSize = 177;
        this.ySize = 167;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;

        this.minecraft.getTextureManager().bindTexture(new ResourceLocation("mystica", "textures/gui/arcanealtar.png"));

        blit(matrixStack, var5, var6, 0, 0, this.xSize, this.ySize);

        IWand wand = null;
        if (this.tileEntity.inventory.getStackInSlot(10) != null && this.tileEntity.inventory.getStackInSlot(10).getItem() instanceof IWand) {
            wand = (IWand) this.tileEntity.inventory.getStackInSlot(10).getItem();
        }

        IWand finalWand = wand;

        tileEntity.getWorld().getRecipeManager().getRecipe(ModRecipeTypes.ARCANE_CRAFTING_TYPE, this.tileEntity.inventory, this.tileEntity.getWorld()).ifPresent(recipe -> {
            int cost = recipe.getMystic(this.tileEntity.inventory);
            if (finalWand != null)
                cost *= finalWand.getConsumptionRate(this.tileEntity.inventory.getStackInSlot(10));
            matrixStack.push();
            matrixStack.translate(var5 + 128, var6 + 24, 0);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            this.font.drawString(matrixStack, String.valueOf(cost), 0, 0, 0xFFFFFF);
            matrixStack.pop();
        });
    }
}
