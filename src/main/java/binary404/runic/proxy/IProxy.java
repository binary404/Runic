package binary404.runic.proxy;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IProxy {

    default void registerEventHandlers() {
    }

    default void openGui(Screen screen) {
    }

    default void openGuide() {
    }

    default PlayerEntity getClientPlayer() {
        return null;
    }

    default World getClientWorld() {
        return null;
    }
}
