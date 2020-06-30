package binary404.runic.common.core.network.fx;

import binary404.runic.client.FXHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketCultFX {

    double x, y, z, mx, my, mz;

    public PacketCultFX(double x, double y, double z, double mx, double my, double mz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.mx = mx;
        this.my = my;
        this.mz = mz;
    }

    public static void encode(PacketCultFX msg, PacketBuffer buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeDouble(msg.mx);
        buffer.writeDouble(msg.my);
        buffer.writeDouble(msg.mz);
    }

    public static PacketCultFX decode(PacketBuffer buffer) {
        return new PacketCultFX(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static void handle(PacketCultFX msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        ctx.get().enqueueWork(() -> {
            FXHelper.wisp(msg.x, msg.y, msg.z, msg.mx, msg.my, msg.mz, 0.4F, 0.4F, 0.4F, 0.08F, 200);
        });
        ctx.get().setPacketHandled(true);
    }

}
