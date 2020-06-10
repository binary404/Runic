package binary404.runic.common.core.network.research;

import binary404.runic.api.capability.CapabilityHelper;
import binary404.runic.api.capability.IPlayerKnowledge;
import binary404.runic.api.research.ResearchCategories;
import binary404.runic.api.research.ResearchEntry;
import binary404.runic.common.core.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncKnowledge {
    protected CompoundNBT data;

    public PacketSyncKnowledge(PlayerEntity entity) {
        IPlayerKnowledge pk = CapabilityHelper.getKnowledge(entity);
        this.data = pk.serializeNBT();
        for (String key : pk.getResearchList()) {
            pk.clearResearchFlag(key, IPlayerKnowledge.EnumResearchFlag.POPUP);
        }
    }

    public PacketSyncKnowledge(CompoundNBT nbt) {
        this.data = nbt;
    }

    public static void encode(PacketSyncKnowledge msg, PacketBuffer buf) {
        Utils.writeCompoundNBTToBuffer(buf, msg.data);
    }

    public static PacketSyncKnowledge decode(PacketBuffer buf) {
        return new PacketSyncKnowledge(Utils.readCompoundNBTFromBuffer(buf));
    }

    public static void handle(PacketSyncKnowledge msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = Minecraft.getInstance().player;
            IPlayerKnowledge pk = CapabilityHelper.getKnowledge(player);
            pk.deserializeNBT(msg.data);
            for (String key : pk.getResearchList()) {
                ResearchEntry ri;
                if (pk.hasResearchFlag(key, IPlayerKnowledge.EnumResearchFlag.POPUP) && (ri = ResearchCategories.getResearch(key)) != null) {
                    //Stuff
                }
                pk.clearResearchFlag(key, IPlayerKnowledge.EnumResearchFlag.POPUP);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
