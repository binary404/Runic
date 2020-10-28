package binary404.mystica.client.gui;

import binary404.mystica.common.container.ModContainers;
import net.minecraft.client.gui.ScreenManager;

public class Screens {

    public static void register() {
        ScreenManager.registerFactory(ModContainers.arcane_workbench, GuiArcaneWorkbench::new);
    }

}
