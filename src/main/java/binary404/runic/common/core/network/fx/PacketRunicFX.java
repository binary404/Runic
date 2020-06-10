package binary404.runic.common.core.network.fx;

import binary404.runic.client.FXHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRunicFX {

    double x, y, z;
    int id;

    public PacketRunicFX(double x, double y, double z, int id) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
    }

    public static void encode(PacketRunicFX msg, PacketBuffer buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeInt(msg.id);
    }

    public static PacketRunicFX decode(PacketBuffer buffer) {
        return new PacketRunicFX(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readInt());
    }

    public static void handle(PacketRunicFX msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        ctx.get().enqueueWork(() -> {
            switch (msg.id) {
                case 1: {
                    FXHelper.poof(msg.x, msg.y, msg.z, 0.8F, 0.2F, 0.8F, 1.5F);
                    FXHelper.flare(msg.x, msg.y, msg.z, 1.0F, 0.9F, 1.0F, 1.5F);
                }
                case 2: {
                    FXHelper.rune1(msg.x, msg.y, msg.z, 1.0F, 0.1F, 0.2F);
                }
                default: {

                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
