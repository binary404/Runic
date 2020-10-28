package binary404.mystica.common.core.network.fx;

import binary404.mystica.client.fx.FXHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;

public class PacketMysticaFX {

    double x, y, z;
    int id;

    public PacketMysticaFX(double x, double y, double z, int id) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
    }

    public static void encode(PacketMysticaFX msg, PacketBuffer buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeInt(msg.id);
    }

    public static PacketMysticaFX decode(PacketBuffer buffer) {
        return new PacketMysticaFX(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readInt());
    }

    public static void handle(PacketMysticaFX msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            @Override
            public void run() {
                Random rand = new Random();
                switch (msg.id) {
                    case (1):
                        FXHelper.poof(msg.x, msg.y, msg.z, 0.8F, 0.2F, 0.8F, 0.2F);
                        FXHelper.flare(msg.x, msg.y, msg.z, 1.0F, 0.9F, 1.0F, 0.2F);
                        break;
                    case (2):
                        FXHelper.drawTaintCloudParticles(msg.x, msg.y, msg.z);
                        break;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
