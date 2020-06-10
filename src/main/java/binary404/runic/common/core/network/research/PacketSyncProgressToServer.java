package binary404.runic.common.core.network.research;

import binary404.runic.api.capability.CapabilityHelper;
import binary404.runic.api.research.ResearchCategories;
import binary404.runic.api.research.ResearchEntry;
import binary404.runic.api.research.ResearchStage;
import binary404.runic.common.config.ResearchManager;
import binary404.runic.common.core.util.InventoryUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncProgressToServer {

    private String key;
    private boolean first;
    private boolean checks;
    private boolean noFlags;

    public PacketSyncProgressToServer(String key, boolean first, boolean checks, boolean noFlags) {
        this.key = key;
        this.first = first;
        this.checks = checks;
        this.noFlags = noFlags;
    }

    public PacketSyncProgressToServer(String key, boolean first) {
        this(key, first, false, true);
    }

    public static void encode(PacketSyncProgressToServer msg, PacketBuffer buf) {
        buf.writeString(msg.key);
        buf.writeBoolean(msg.first);
        buf.writeBoolean(msg.checks);
        buf.writeBoolean(msg.noFlags);
    }

    public static PacketSyncProgressToServer decode(PacketBuffer buf) {
        return new PacketSyncProgressToServer(buf.readString(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(PacketSyncProgressToServer msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player != null) {
                System.out.println(msg.key);
                if (msg.first != CapabilityHelper.knowsResearch(player, msg.key)) {
                    if (msg.checks && !checkRequisites(player, msg.key)) {
                        return;
                    }
                    if (msg.noFlags) {
                        ResearchManager.noFlags = true;
                    }
                    ResearchManager.progressResearch(player, msg.key);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static boolean checkRequisites(PlayerEntity player, String key) {
        ResearchEntry research = ResearchCategories.getResearch(key);
        if (research.getStages() != null) {
            String[] r;
            Object[] c;
            int currentStage = CapabilityHelper.getKnowledge(player).getResearchStage(key) - 1;
            if (currentStage < 0) {
                return false;
            }
            if (currentStage >= research.getStages().length) {
                return true;
            }
            ResearchStage stage = research.getStages()[currentStage];
            Object[] o = stage.getObtain();
            if (o != null) {
                int a;
                NonNullList nnl;
                for (a = 0; a < o.length; ++a) {
                    ItemStack ts = ItemStack.EMPTY;
                    boolean ore = false;
                    if (o[a] instanceof ItemStack) {
                        ts = (ItemStack) o[a];
                    }
                    if (InventoryUtils.isPlayerCarryingAmount(player, ts, ore))
                        continue;
                    return false;
                }
                for (a = 0; a < o.length; ++a) {
                    boolean ore = false;
                    ItemStack ts = ItemStack.EMPTY;
                    if (o[a] instanceof ItemStack) {
                        ts = (ItemStack) o[a];
                    }
                    InventoryUtils.consumePlayerItem(player, ts);
                }
            }
            if ((c = stage.getCraft()) != null) {
                for (int a = 0; a < c.length; ++a) {
                    if (CapabilityHelper.getKnowledge(player).isResearchKnown("[#]" + stage.getCraftReference()[a]))
                        continue;
                    return false;
                }
            }
            if ((r = stage.getResearch()) != null) {
                for (int a = 0; a < r.length; ++a) {
                    if (CapabilityHelper.knowsResearchStrict(player, r[a])) continue;
                    return false;
                }
            }
        }
        return true;
    }

}
