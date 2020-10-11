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
                System.out.println(msg.id);
                switch (msg.id) {
                    case (1):
                        FXHelper.poof(msg.x, msg.y, msg.z, 0.8F, 0.2F, 0.8F, 0.2F);
                        FXHelper.flare(msg.x, msg.y, msg.z, 1.0F, 0.9F, 1.0F, 0.2F);
                        break;
                    case (2):
                        FXHelper.rune1(msg.x, msg.y, msg.z, 1.0F, 0.1F, 0.2F);
                        break;
                    case (3):
                        for (int i = 0; i < 2; i++) {
                            FXHelper.sparkle(rand.nextGaussian() * 0.4 + msg.x, rand.nextGaussian() * 0.4 + msg.y, rand.nextGaussian() * 0.4 + msg.z, rand.nextGaussian() * 0.02, rand.nextGaussian() * 0.02, rand.nextGaussian() * 0.02, 0.6F, 0.4F, 0.1F, 0.3F, 0.987F, 0.0F, 20);
                        }
                        break;
                    case (4):
                        FXHelper.wisp(msg.x, msg.y, msg.z, 0.0D, 0.1D, 0.0D, 0.1F, 0.6F, 0.1F, rand.nextFloat() * 0.8F, 50);
                        break;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
