package binary404.runic.common.container;

import binary404.runic.api.item.IRuneItem;
import binary404.runic.common.container.slot.SlotInput;
import binary404.runic.common.container.slot.SlotOutput;
import binary404.runic.common.tile.TileRuneMolder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class RuneMolderContainer extends CommonContainer {

    public TileRuneMolder tileEntity;

    public RuneMolderContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
        this(windowId, playerInventory, new ItemStackHandler(2), (TileRuneMolder) Minecraft.getInstance().world.getTileEntity(extraData.readBlockPos()));
    }

    public RuneMolderContainer(int windowId, PlayerInventory inventory, IItemHandler handler, TileRuneMolder te) {
        super(ModContainers.RUNE_MOLDER, windowId, 2);

        int var6, var7;
        for (var6 = 0; var6 < 3; var6++) {
            for (var7 = 0; var7 < 9; var7++) {
                addSlot(new Slot(inventory, var7 + var6 * 9 + 9, 8 + var7 * 18, 110 + var6 * 18));
            }
        }
        for (var6 = 0; var6 < 9; var6++) {
            addSlot(new Slot(inventory, var6, 8 + var6 * 18, 168));
        }

        addSlot(new SlotMoldInput(handler, 0, 49, 26));
        addSlot(new SlotOutput(handler, 1, 120, 26));

        tileEntity = te;
    }

    public static class SlotMoldInput extends SlotInput {

        public SlotMoldInput(IItemHandler handler, int index, int xPosition, int yPosition) {
            super(handler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return stack.getItem() instanceof IRuneItem;
        }
    }

}
