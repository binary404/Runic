package binary404.runic.common.tile;

import binary404.runic.common.core.network.PacketHandler;
import binary404.runic.common.core.network.tile.PacketTileToClient;
import binary404.runic.common.core.network.tile.PacketTileToServer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileMod extends TileEntity {

    public TileMod(TileEntityType type) {
        super(type);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (getWorld() != null) {
            BlockState state = getWorld().getBlockState(pos);
            if (state != null && this.world instanceof ServerWorld) {
                state.getBlock().tick(state, (ServerWorld) this.world, getPos(), getWorld().rand);
                getWorld().notifyBlockUpdate(pos, state, state, 3);
            }
        }
    }

    public void syncTile(boolean rerender) {
        BlockState state = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 2 + (rerender ? 4 : 0));
    }

    public void messageFromServer(CompoundNBT nbt) {

    }

    public void messageFromClient(CompoundNBT nbt, ServerPlayerEntity player) {

    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT par1nbtTagCompound) {
        return writePacketNBT(super.write(par1nbtTagCompound));
    }

    @Nonnull
    @Override
    public final CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public void read(BlockState state, CompoundNBT par1nbtTagCompound) {
        super.read(state, par1nbtTagCompound);
        readPacketNBT(par1nbtTagCompound);
    }

    public CompoundNBT writePacketNBT(CompoundNBT cmp) {
        return cmp;
    }

    public void readPacketNBT(CompoundNBT cmp) {
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        readPacketNBT(pkt.getNbtCompound());
    }


    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), 255, getUpdateTag());
    }

}
