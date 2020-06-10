package binary404.runic.common.core.network.research;

import binary404.runic.api.capability.CapabilityHelper;
import binary404.runic.api.capability.IPlayerKnowledge;
import binary404.runic.common.core.util.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncResearchFlagsToServer {

    String key;
    byte flags;

    public PacketSyncResearchFlagsToServer(PlayerEntity player, String key) {
        this.key = key;
        this.flags = Utils.pack(CapabilityHelper.getKnowledge(player).hasResearchFlag(key, IPlayerKnowledge.EnumResearchFlag.PAGE), CapabilityHelper.getKnowledge(player).hasResearchFlag(key, IPlayerKnowledge.EnumResearchFlag.RESEARCH));
    }

    public PacketSyncResearchFlagsToServer(String key, byte flags) {
        this.key = key;
        this.flags = flags;
    }

    public static void endode(PacketSyncResearchFlagsToServer msg, PacketBuffer buf) {
        buf.writeString(msg.key);
        buf.writeByte(msg.flags);
    }

    public static PacketSyncResearchFlagsToServer decode(PacketBuffer buf) {
        return new PacketSyncResearchFlagsToServer(buf.readString(), buf.readByte());
    }

    public static void handle(PacketSyncResearchFlagsToServer msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            boolean[] b = Utils.unpack(msg.flags);
            if (ctx.get().getSender() != null) {
                PlayerEntity player = ctx.get().getSender();
                if (b[0]) {
                    CapabilityHelper.getKnowledge(player).setResearchFlag(msg.key, IPlayerKnowledge.EnumResearchFlag.PAGE);
                } else {
                    CapabilityHelper.getKnowledge(player).clearResearchFlag(msg.key, IPlayerKnowledge.EnumResearchFlag.PAGE);
                }
                if (b[1]) {
                    CapabilityHelper.getKnowledge(player).setResearchFlag(msg.key, IPlayerKnowledge.EnumResearchFlag.RESEARCH);
                } else {
                    CapabilityHelper.getKnowledge(player).clearResearchFlag(msg.key, IPlayerKnowledge.EnumResearchFlag.RESEARCH);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
