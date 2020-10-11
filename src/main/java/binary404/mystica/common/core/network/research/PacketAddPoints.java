package binary404.mystica.common.core.network.research;

import binary404.mystica.api.capability.CapabilityHelper;
import binary404.mystica.api.capability.IPlayerKnowledge;
import binary404.mystica.api.research.ResearchCategories;
import binary404.mystica.api.research.ResearchCategory;
import binary404.mystica.api.research.ResearchEvent;
import binary404.mystica.common.config.ResearchManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAddPoints {

    String key;
    int amount;
    String research;
    boolean popup;

    public PacketAddPoints(ResearchCategory category, int points, String research, boolean popup) {
        this(category.key, points, research, popup);
    }

    public PacketAddPoints(String key, int amount, String research, boolean popup) {
        this.key = key;
        this.amount = amount;
        this.research = research;
        this.popup = popup;
    }

    public static void encode(PacketAddPoints msg, PacketBuffer buf) {
        buf.writeString(msg.key);
        buf.writeInt(msg.amount);
        buf.writeString(msg.research);
        buf.writeBoolean(msg.popup);
    }

    public static PacketAddPoints decode(PacketBuffer buf) {
        return new PacketAddPoints(buf.readString(), buf.readInt(), buf.readString(), buf.readBoolean());
    }

    public static void handle(PacketAddPoints msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player != null) {
                ResearchCategory cat = ResearchCategories.getResearchCategory(msg.key);
                int amount = msg.amount;
                IPlayerKnowledge knowledge = CapabilityHelper.getKnowledge(player);
                if (!knowledge.isResearchKnown(msg.research)) {
                    ResearchManager.addResearchPoints(player, cat, amount);
                    if (msg.popup) {
                        ResearchManager.startResearchWithPopup(player, msg.research);
                    } else {
                        knowledge.addResearch(msg.research);
                        knowledge.sync((ServerPlayerEntity) player);
                    }
                }
            }
        });
    }
}
