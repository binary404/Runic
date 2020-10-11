package binary404.mystica.proxy;

import binary404.mystica.client.gui.GuiResearchBrowser;
import binary404.mystica.client.core.handler.ShaderHandler;
import binary404.mystica.client.render.RenderBeholder;
import binary404.mystica.client.render.RenderCultOrb;
import binary404.mystica.client.render.RenderRuneZombie;
import binary404.mystica.client.render.entity.RenderWeaveThread;
import binary404.mystica.client.render.tile.RenderMobCrystal;
import binary404.mystica.common.blocks.ModBlocks;
import binary404.mystica.common.entity.ModEntities;
import binary404.mystica.common.tile.ModTiles;
import net.minecraft.client.Minecraft;
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

        RenderTypeLookup.setRenderLayer(ModBlocks.taint_fiber, RenderType.getCutout());

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.CULT_ZOMBIE, RenderRuneZombie::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.RANGED_CULT_ZOMBIE, RenderRuneZombie::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.CULT_ORB, RenderCultOrb::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BEHOLDER, RenderBeholder::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.THREAD, RenderWeaveThread::new);
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
