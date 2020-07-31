package binary404.runic.common.core.network;

import binary404.runic.Runic;
import binary404.runic.common.core.network.fx.PacketCultFX;
import binary404.runic.common.core.network.fx.PacketFlame;
import binary404.runic.common.core.network.fx.PacketRunicFX;
import binary404.runic.common.core.network.research.PacketKnowledgeGain;
import binary404.runic.common.core.network.research.PacketSyncKnowledge;
import binary404.runic.common.core.network.research.PacketSyncProgressToServer;
import binary404.runic.common.core.network.research.PacketSyncResearchFlagsToServer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL = "6";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Runic.modid, "channel"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    public static void init() {
        int id = 0;

        //Research
        INSTANCE.registerMessage(id++, PacketKnowledgeGain.class, PacketKnowledgeGain::encode, PacketKnowledgeGain::decode, PacketKnowledgeGain::handle);
        INSTANCE.registerMessage(id++, PacketSyncProgressToServer.class, PacketSyncProgressToServer::encode, PacketSyncProgressToServer::decode, PacketSyncProgressToServer::handle);
        INSTANCE.registerMessage(id++, PacketSyncResearchFlagsToServer.class, PacketSyncResearchFlagsToServer::endode, PacketSyncResearchFlagsToServer::decode, PacketSyncResearchFlagsToServer::handle);
        INSTANCE.registerMessage(id++, PacketSyncKnowledge.class, PacketSyncKnowledge::encode, PacketSyncKnowledge::decode, PacketSyncKnowledge::handle);

        //FX
        INSTANCE.registerMessage(id++, PacketRunicFX.class, PacketRunicFX::encode, PacketRunicFX::decode, PacketRunicFX::handle);
        INSTANCE.registerMessage(id++, PacketCultFX.class, PacketCultFX::encode, PacketCultFX::decode, PacketCultFX::handle);
        INSTANCE.registerMessage(id++, PacketFlame.class, PacketFlame::encode, PacketFlame::decode, PacketFlame::handle);
    }

    public static void sendToNearby(World world, Entity e, Object toSend) {
        sendToNearby(world, new BlockPos(e.getPosX(), e.getPosY(), e.getPosZ()), toSend);
    }

    public static void sendToNearby(World world, BlockPos pos, Object toSend) {
        if (world instanceof ServerWorld) {
            ServerWorld ws = (ServerWorld) world;

            ws.getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(pos), false).filter(p -> p.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64).forEach(p -> INSTANCE.send(PacketDistributor.PLAYER.with(() -> p), toSend));
        }
    }


}
