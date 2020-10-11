package binary404.mystica.common.core.network;

import binary404.mystica.Mystica;
import binary404.mystica.common.core.network.fx.PacketArcFX;
import binary404.mystica.common.core.network.fx.PacketCultFX;
import binary404.mystica.common.core.network.fx.PacketFlame;
import binary404.mystica.common.core.network.fx.PacketMysticaFX;
import binary404.mystica.common.core.network.research.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL = "7";

    public static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Mystica.modid, "chan"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    public static void init() {
        int id = 0;

        //Research
        HANDLER.registerMessage(id++, PacketKnowledgeGain.class, PacketKnowledgeGain::encode, PacketKnowledgeGain::decode, PacketKnowledgeGain::handle);
        HANDLER.registerMessage(id++, PacketSyncProgressToServer.class, PacketSyncProgressToServer::encode, PacketSyncProgressToServer::decode, PacketSyncProgressToServer::handle);
        HANDLER.registerMessage(id++, PacketSyncResearchFlagsToServer.class, PacketSyncResearchFlagsToServer::endode, PacketSyncResearchFlagsToServer::decode, PacketSyncResearchFlagsToServer::handle);
        HANDLER.registerMessage(id++, PacketSyncKnowledge.class, PacketSyncKnowledge::encode, PacketSyncKnowledge::decode, PacketSyncKnowledge::handle);
        HANDLER.registerMessage(id++, PacketAddPoints.class, PacketAddPoints::encode, PacketAddPoints::decode, PacketAddPoints::handle);

        //FX
        HANDLER.registerMessage(id++, PacketMysticaFX.class, PacketMysticaFX::encode, PacketMysticaFX::decode, PacketMysticaFX::handle);
        HANDLER.registerMessage(id++, PacketCultFX.class, PacketCultFX::encode, PacketCultFX::decode, PacketCultFX::handle);
        HANDLER.registerMessage(id++, PacketFlame.class, PacketFlame::encode, PacketFlame::decode, PacketFlame::handle);
        HANDLER.registerMessage(id++, PacketArcFX.class, PacketArcFX::encode, PacketArcFX::decode, PacketArcFX::handle);

        HANDLER.registerMessage(id++, PacketUpdateMystic.class, PacketUpdateMystic::encode, PacketUpdateMystic::decode, PacketUpdateMystic::handle);
    }

    /**
     * Send message to all within 64 blocks that have this chunk loaded
     */
    public static void sendToNearby(World world, BlockPos pos, Object toSend) {
        if (world instanceof ServerWorld) {
            ServerWorld ws = (ServerWorld) world;
            ws.getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(pos), false)
                    .filter(p -> p.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
                    .forEach(p -> HANDLER.send(PacketDistributor.PLAYER.with(() -> p), toSend));
        }
    }

    public static void sendToNearby(World world, Entity e, Object toSend) {
        sendToNearby(world, e.getPosition(), toSend);
    }

    public static void sendTo(ServerPlayerEntity playerMP, Object toSend) {
        HANDLER.sendTo(toSend, playerMP.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendNonLocal(ServerPlayerEntity playerMP, Object toSend) {
        if (playerMP.server.isDedicatedServer() || !playerMP.getGameProfile().getName().equals(playerMP.server.getServerOwner())) {
            sendTo(playerMP, toSend);
        }
    }

    public static void sendToServer(Object msg) {
        HANDLER.sendToServer(msg);
    }
}
