package binary404.runic.proxy;

import binary404.runic.client.gui.GuiResearchBrowser;
import binary404.runic.client.libs.ShaderHandler;
import binary404.runic.client.render.RenderBeholder;
import binary404.runic.client.render.RenderCultOrb;
import binary404.runic.client.render.RenderRuneZombie;
import binary404.runic.client.render.tile.RenderArcaneForge;
import binary404.runic.client.render.tile.RenderMobCrystal;
import binary404.runic.client.render.tile.RenderRuneMolder;
import binary404.runic.common.blocks.ModBlocks;
import binary404.runic.common.container.ModContainers;
import binary404.runic.common.entity.ModEntities;
import binary404.runic.common.tile.ModTiles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy implements IProxy {

    @Override
    public void registerEventHandlers() {
        ShaderHandler.initShaders();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        registerRendering();
    }

    private void registerRendering() {
        ClientRegistry.bindTileEntityRenderer(ModTiles.MOB_CRYSTAL, RenderMobCrystal::new);
        ClientRegistry.bindTileEntityRenderer(ModTiles.ARCANE_FORGE, RenderArcaneForge::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.CULT_ZOMBIE, RenderRuneZombie::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.RANGED_CULT_ZOMBIE, RenderRuneZombie::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.CULT_ORB, RenderCultOrb::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BEHOLDER, RenderBeholder::new);
    }

    @Override
    public void openGui(Screen screen) {
        Minecraft.getInstance().displayGuiScreen(screen);
    }

    @Override
    public void openGuide() {
        this.openGui(new GuiResearchBrowser());
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }
}
