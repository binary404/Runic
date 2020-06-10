package binary404.runic.common.core.network.research;

import binary404.runic.api.capability.IPlayerKnowledge;
import binary404.runic.api.research.ResearchCategories;
import binary404.runic.api.research.ResearchCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketKnowledgeGain {

    private byte type;
    private String cat;

    public PacketKnowledgeGain(byte type, String value) {
        this.type = type;
        this.cat = value == null ? "" : value;
    }

    public static void encode(PacketKnowledgeGain msg, PacketBuffer buf) {
        buf.writeByte(msg.type);
        buf.writeString(msg.cat);
    }

    public static PacketKnowledgeGain decode(PacketBuffer buf) {
        return new PacketKnowledgeGain(buf.readByte(), buf.readString());
    }

    public static void handle(PacketKnowledgeGain message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPlayerEntity p = Minecraft.getInstance().player;
            ResearchCategory cat = message.cat.length() > 0 ? ResearchCategories.getResearchCategory(message.cat) : null;
        });

        ctx.get().setPacketHandled(true);
    }

}
