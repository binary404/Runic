package binary404.runic.common.core.network;

import binary404.runic.Runic;
import binary404.runic.common.core.network.research.PacketKnowledgeGain;
import binary404.runic.common.core.network.research.PacketSyncKnowledge;
import binary404.runic.common.core.network.research.PacketSyncProgressToServer;
import binary404.runic.common.core.network.research.PacketSyncResearchFlagsToServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL = "3";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Runic.modid, "chan"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    public static void init() {
        int id = 0;

        INSTANCE.registerMessage(id++, PacketKnowledgeGain.class, PacketKnowledgeGain::encode, PacketKnowledgeGain::decode, PacketKnowledgeGain::handle);
        INSTANCE.registerMessage(id++, PacketSyncProgressToServer.class, PacketSyncProgressToServer::encode, PacketSyncProgressToServer::decode, PacketSyncProgressToServer::handle);
        INSTANCE.registerMessage(id++, PacketSyncResearchFlagsToServer.class, PacketSyncResearchFlagsToServer::endode, PacketSyncResearchFlagsToServer::decode, PacketSyncResearchFlagsToServer::handle);
        INSTANCE.registerMessage(id++, PacketSyncKnowledge.class, PacketSyncKnowledge::encode, PacketSyncKnowledge::decode, PacketSyncKnowledge::handle);
    }

}
