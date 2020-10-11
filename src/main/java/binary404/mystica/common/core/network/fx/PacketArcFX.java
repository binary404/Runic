package binary404.mystica.common.core.network.fx;

import binary404.fx_lib.fx.ParticleDispatcher;
import binary404.mystica.common.core.util.EntityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketArcFX {

    double x, y, z, tx, ty, tz;
    float r, g, b;
    boolean check;

    public PacketArcFX(double x, double y, double z, double tx, double ty, double tz, float r, float g, float b, boolean check) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
        this.r = r;
        this.g = g;
        this.b = b;
        this.check = check;
    }

    public static void encode(PacketArcFX msg, PacketBuffer buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeDouble(msg.tx);
        buffer.writeDouble(msg.ty);
        buffer.writeDouble(msg.tz);
        buffer.writeFloat(msg.r);
        buffer.writeFloat(msg.g);
        buffer.writeFloat(msg.b);
        buffer.writeBoolean(msg.check);
    }

    public static PacketArcFX decode(PacketBuffer buffer) {
        return new PacketArcFX(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readBoolean());
    }

    public static void handle(PacketArcFX msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        ctx.get().enqueueWork(() -> {
            if (msg.check) {
                if (!EntityUtils.hasRevealer(Minecraft.getInstance().getRenderViewEntity())) {
                    ctx.get().setPacketHandled(true);
                    return;
                }

            }
            ParticleDispatcher.arcFX(msg.x, msg.y, msg.z, msg.tx, msg.ty, msg.tz, msg.r, msg.g, msg.b);
        });
        ctx.get().setPacketHandled(true);
    }

}
