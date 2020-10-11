package binary404.mystica.common.core.network;

import binary404.mystica.client.core.event.ClientEvents;
import binary404.mystica.client.core.handler.HudHandler;
import binary404.mystica.client.core.event.RenderEventHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateMystic {

    float mystic, flux;
    short base;

    public PacketUpdateMystic(float mystic, float flux, short base) {
        this.mystic = mystic;
        this.flux = flux;
        this.base = base;
    }

    public static void encode(PacketUpdateMystic msg, PacketBuffer buffer) {
        buffer.writeFloat(msg.mystic);
        buffer.writeFloat(msg.flux);
        buffer.writeShort(msg.base);
    }

    public static PacketUpdateMystic decode(PacketBuffer buffer) {
        return new PacketUpdateMystic(buffer.readFloat(), buffer.readFloat(), buffer.readShort());
    }

    public static void handle(PacketUpdateMystic msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        ctx.get().enqueueWork(() -> {
            ClientEvents.currentMystic = msg.mystic;
            ClientEvents.currentFlux = msg.flux;

            HudHandler hud = RenderEventHandler.hudHandler;
            hud.base = msg.base;
            hud.mystic = msg.mystic;
            hud.flux = msg.flux;
        });
        ctx.get().setPacketHandled(true);
    }

}
