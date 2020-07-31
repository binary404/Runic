package binary404.runic.common.tile;

import binary404.runic.api.RunicApiCraftingHelper;
import binary404.runic.api.capability.CapabilityHelper;
import binary404.runic.api.recipe.ForgeRecipe;
import binary404.runic.client.FXHelper;
import binary404.runic.common.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class TileArcaneForge extends TileMod implements ITickableTileEntity {

    int ticks = 0;
    int stageTicks = 0;

    public Stage stage = Stage.IDLE;

    ItemStack stack = ItemStack.EMPTY;

    ArrayList<ItemEntity> itemEntities = new ArrayList<>();
    ArrayList<ItemStack> itemStacks = new ArrayList<>();
    ForgeRecipe recipe = null;
    public int heat = 0;

    public TileArcaneForge() {
        super(ModTiles.ARCANE_FORGE);
    }

    @Override
    public void tick() {
        ticks++;

        if (this.stack == ItemStack.EMPTY) {
            this.stage = Stage.IDLE;
        }

        if (stage != Stage.IDLE) {
            stageTicks++;
        }

        if (stage == Stage.GATHER_ITEMS) {
            heat++;
            if (!itemEntities.isEmpty() && this.ticks % 40 == 0 && !world.isRemote) {
                for (ItemEntity entity : itemEntities) {
                    for (ItemStack stack : itemStacks) {
                        if (entity.getItem().getItem() == stack.getItem()) {
                            entity.getItem().shrink(1);
                            if (entity.getItem().getCount() <= 1) {
                                entity.remove();
                            }
                        }
                    }
                }
                itemEntities.clear();
                this.markDirty();
                advanceStage();
            }
        }

        if (stage == Stage.START_CRAFT) {
            heat += world.rand.nextInt(3);
            world.addBlockEvent(getPos(), ModBlocks.arcane_anvil, 1, 1);
            if (recipe != null) {
                if (stageTicks >= recipe.time) {
                    stageTicks = 0;
                    advanceStage();
                }
            } else {
                stage = Stage.IDLE;
            }
        }

        if (stage == Stage.CRAFT) {
            if (this.ticks % 40 == 0) {
                List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(this.getPos()).grow(2.0D));
                for (PlayerEntity player : players) {
                    player.attackEntityFrom(DamageSource.IN_FIRE, this.heat / 8);
                }
            }
            world.addBlockEvent(getPos(), ModBlocks.arcane_anvil, 2, 0);
            if (this.ticks % 20 == 0) {
                world.addBlockEvent(getPos(), ModBlocks.arcane_anvil, 3, 0);
                world.addBlockEvent(getPos(), ModBlocks.arcane_anvil, 1, 2);
            }
            if (recipe != null) {
                if (stageTicks >= recipe.time * 2) {
                    advanceStage();
                }
            } else {
                stage = Stage.IDLE;
            }
        }
        if (stage == Stage.RESET) {
            if (recipe != null) {
                this.stack = recipe.output.copy();
                this.recipe = null;
                this.itemStacks.clear();
                this.stageTicks = 0;
                this.heat = 0;
                this.advanceStage();
                this.markDirty();
            } else {
                this.advanceStage();
            }
        }

    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        switch (id) {
            case 1: {
                if (world.isRemote) {
                    FXHelper.flame(this.pos.getX() + 0.5 + world.rand.nextGaussian() * 0.2, this.pos.getY() + type + 0.2 + world.rand.nextGaussian() * 0.2, this.pos.getZ() + 0.5 + world.rand.nextGaussian() * 0.2, 0.2F);
                }
                return true;
            }
            case 2: {
                if (world.isRemote) {
                    for (int i = -2; i <= 2; i++) {
                        for (int j = -2; j <= 2; j++) {
                            BlockPos posTest = this.pos.add(i, 0, j);
                            BlockState state = world.getBlockState(posTest);
                            if (state.getBlock() == ModBlocks.storage || state.getBlock() == ModBlocks.furnace) {
                                Vector3d otherVec = new Vector3d(posTest.up(2).getX(), posTest.up(2).getY(), posTest.up(2).getZ());
                                Vector3d target = new Vector3d(this.pos.getX(), this.pos.getY(), this.pos.getZ());
                                Vector3d motion = target.subtract(otherVec).mul(0.03, 0.03, 0.03);
                                float r = 0.7F + 0.3F * (float) Math.random();
                                float g = 0.2F;
                                float b = 0.2F;
                                FXHelper.wisp(otherVec.x + 0.5, otherVec.y + 0.3, otherVec.z + 0.5, motion.x, motion.y, motion.z, r, g, b, 0.1F, 45);
                                FXHelper.wisp(otherVec.x + 0.5, otherVec.y + 0.3, otherVec.z + 0.5, 0.0D, 0.0D, 0.0D, r, g, b, 0.25F, 10);
                            }
                        }
                    }
                }
                return true;
            }
            case 3: {
                if (world.isRemote) {
                    for (int i = 0; i < 50; i++) {
                        float r = 0.6F + 0.3F * (float) Math.random();
                        float g = 0.3F + 0.3F * (float) Math.random();
                        float b = 0.1F;
                        FXHelper.sparkle(this.pos.getX() + 0.5 + world.rand.nextGaussian() * 0.4, this.pos.getY() + 0.5 + world.rand.nextGaussian() * 0.4, this.pos.getZ() + 0.5 + world.rand.nextGaussian() * 0.4, world.rand.nextGaussian() * 0.08, world.rand.nextGaussian() * 0.08, world.rand.nextGaussian() * 0.08, r, g, b, 0.1F, 0.987F, 0.04F, 8);
                    }
                }
                return true;
            }
            default:
                return super.receiveClientEvent(id, type);
        }
    }

    public void tryCraft(PlayerEntity player) {
        if (player.world.isRemote)
            return;
        if (!this.stack.isEmpty() && this.stage == Stage.IDLE) {
            itemEntities.clear();
            List<ItemEntity> entities = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(this.getPos()).grow(2.0D));

            for (ItemEntity entity : entities) {
                itemEntities.add(entity);
                for (int i = 0; i < entity.getItem().getCount(); i++) {
                    ItemStack toAdd = new ItemStack(entity.getItem().getItem(), 1);
                    itemStacks.add(toAdd);
                }
            }

            recipe = RunicApiCraftingHelper.findForgeRecipe(itemStacks, this.stack);
            if (recipe != null)
                if (CapabilityHelper.knowsResearch(player, recipe.research)) {
                    this.advanceStage();
                } else {
                    recipe = null;
                }
        }
    }

    @Override
    public CompoundNBT writePacketNBT(CompoundNBT cmp) {
        CompoundNBT itemCmp = new CompoundNBT();
        if (!stack.isEmpty()) {
            cmp.put("item", stack.write(itemCmp));
        }
        cmp.putInt("stage", this.stage.ordinal());
        return super.writePacketNBT(cmp);
    }

    @Override
    public void readPacketNBT(CompoundNBT cmp) {
        CompoundNBT itemCmp = cmp.getCompound("item");
        stack = ItemStack.read(itemCmp);
        this.stage = Stage.values()[cmp.getInt("stage")];
        super.readPacketNBT(cmp);
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    private void advanceStage() {
        switch (stage) {
            case IDLE:
                stage = Stage.GATHER_ITEMS;
                break;
            case GATHER_ITEMS:
                stage = Stage.START_CRAFT;
                break;
            case START_CRAFT:
                stage = Stage.CRAFT;
                break;
            case CRAFT:
                stage = Stage.RESET;
                break;
            case RESET:
                stage = Stage.IDLE;
                break;
        }
        stageTicks = 0;
    }

    public enum Stage {
        IDLE,
        GATHER_ITEMS,
        START_CRAFT,
        CRAFT,
        RESET
    }
}
