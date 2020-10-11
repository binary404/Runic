package binary404.mystica.common.core.network.fx;

import binary404.mystica.client.fx.FXHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketFlame {

    double x, y, z;
    float scale;

    public PacketFlame(double x, double y, double z, float scale) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.scale = scale;
    }

    public static void encode(PacketFlame msg, PacketBuffer buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeFloat(msg.scale);
    }

    public static PacketFlame decode(PacketBuffer buffer) {
        return new PacketFlame(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readFloat());
    }

    public static void handle(PacketFlame msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        ctx.get().enqueueWork(() -> {
            FXHelper.flame(msg.x, msg.y, msg.z, msg.scale);
        });
        ctx.get().setPacketHandled(true);
    }

}
