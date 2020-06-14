package binary404.runic.common.items;

import binary404.runic.Runic;
import binary404.runic.api.research.ResearchCategories;
import binary404.runic.api.research.ResearchEntry;
import binary404.runic.client.FXHelper;
import binary404.runic.client.gui.GuiResearchBrowser;
import binary404.runic.client.gui.GuiResearchPage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Collection;
import java.util.LinkedList;

public class ItemGuide extends Item {

    public ItemGuide() {
        super(ModItems.defaultBuilder().maxStackSize(1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        Runic.proxy.openGuide();
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
