package binary404.runic.common.tile;

import binary404.runic.api.recipe.ChiselRecipes;
import binary404.runic.client.FXHelper;
import binary404.runic.common.blocks.ModBlocks;
import binary404.runic.common.core.feature.power.IPowerReciever;
import binary404.runic.common.core.feature.power.IPowerTransfer;
import binary404.runic.common.core.network.PacketHandler;
import binary404.runic.common.core.network.fx.PacketRunicFX;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;

public class TileChisel extends TileMod implements ITickableTileEntity {

    int ticks = 0;
    int processTicks = 0;
    int power = 0;

    public TileChisel() {
        super(ModTiles.CHISEL);
    }

    @Override
    public void tick() {
        if (!this.world.isRemote) {
            ticks++;
            if(ticks % 20 == 0) {
                if(this.canRecievePower())
                    this.recievePower(5);
            }
            if (!this.world.isBlockPowered(this.pos) && canProcess()) {
                PacketHandler.sendToNearby(world, this.pos, new PacketRunicFX(this.pos.getX() + 0.5, this.pos.up().getY() + 0.5, this.pos.getZ() + 0.5, 3));
                for (ChiselRecipes.ChiselRecipe recipe : ChiselRecipes.registry) {
                    Block block = this.world.getBlockState(this.pos.up()).getBlock();
                    Block test = recipe.input;
                    if (block == test) {
                        processTicks++;
                        if (processTicks >= 200) {
                            processTicks = 0;
                            this.power = 0;
                            world.destroyBlock(this.pos.up(), false);
                            ItemEntity entity = new ItemEntity(this.world, this.pos.down().getX(), this.pos.down().getY(), this.pos.down().getZ(), new ItemStack(recipe.output));
                            entity.setPosition(this.pos.getX() + 0.5, this.pos.down().getY(), this.pos.getZ() + 0.5);
                            this.world.addEntity(entity);
                        }
                    }
                }
            }
        }
    }

    public void recievePower(int power) {
        this.power += power;
    }

    public boolean canProcess() {
        return this.power >= 50;
    }

    public boolean canRecievePower() {
        return this.power <= 50;
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT par1nbtTagCompound) {
        par1nbtTagCompound.putInt("power", this.power);
        return super.write(par1nbtTagCompound);
    }

    @Override
    public void read(CompoundNBT par1nbtTagCompound) {
        super.read(par1nbtTagCompound);
        this.power = par1nbtTagCompound.getInt("power");
    }
}
