package binary404.mystica.common.world.weave;

import binary404.mystica.common.core.util.PosXY;
import javafx.geometry.Pos;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.concurrent.ConcurrentHashMap;

public class WeaveWorld {

    RegistryKey<World> dim;
    ConcurrentHashMap<PosXY, Chunk> weaveChunks;
    ConcurrentHashMap<PosXY, Integer> rechargeTickets;

    public WeaveWorld(RegistryKey<World> dim) {
        this.weaveChunks = new ConcurrentHashMap<>();
        this.rechargeTickets = new ConcurrentHashMap<>();

        this.dim = dim;
    }

    public void addRechargeTicket(PosXY pos, int amount) {
        this.rechargeTickets.put(pos, amount);
    }

    public void removeRechargeTicket(PosXY pos) {
        this.rechargeTickets.remove(pos);
    }

    public int getRechargeTicket(int x, int z) {
        if (this.rechargeTickets.containsKey(new PosXY(x, z))) {
            return this.rechargeTickets.get(new PosXY(x, z));
        }
        return 0;
    }

    public ConcurrentHashMap<PosXY, Integer> getRechargeTickets() {
        return rechargeTickets;
    }

    public ConcurrentHashMap<PosXY, Chunk> getWeaveChunks() {
        return weaveChunks;
    }

    public void setWeaveChunks(ConcurrentHashMap<PosXY, Chunk> weaveChunks) {
        this.weaveChunks = weaveChunks;
    }

    public Chunk getWeaveChunkAt(int x, int y) {
        return getWeaveChunkAt(new PosXY(x, y));
    }

    public Chunk getWeaveChunkAt(PosXY loc) {
        Chunk chunk = this.weaveChunks.get(loc);

        return chunk;
    }
}
