package binary404.mystica.common.core.network.research;

import binary404.mystica.Mystica;
import binary404.mystica.api.capability.CapabilityHelper;
import binary404.mystica.api.capability.IPlayerKnowledge;
import binary404.mystica.api.research.ResearchCategories;
import binary404.mystica.api.research.ResearchEntry;
import binary404.mystica.client.gui.ResearchToast;
import binary404.mystica.common.core.util.Utils;
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
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = Minecraft.getInstance().player;
            IPlayerKnowledge pk = CapabilityHelper.getKnowledge(player);
            pk.deserializeNBT(msg.data);
            for (String key : pk.getResearchList()) {
                if (pk.hasResearchFlag(key, IPlayerKnowledge.EnumResearchFlag.POPUP)) {
                    ResearchEntry ri = ResearchCategories.getResearch(key);
                    if (ri != null) {
                        Minecraft.getInstance().getToastGui().add(new ResearchToast(ri));
                    }
                }
                pk.clearResearchFlag(key, IPlayerKnowledge.EnumResearchFlag.POPUP);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
