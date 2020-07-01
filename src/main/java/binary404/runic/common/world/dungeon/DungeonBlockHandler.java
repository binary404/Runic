package binary404.runic.common.world.dungeon;

import binary404.runic.Runic;
import binary404.runic.common.blocks.BlockMobCrystal;
import binary404.runic.common.container.CommonContainer;
import binary404.runic.common.entity.ModEntities;
import binary404.runic.common.tile.world.TileMobCrystal;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraft.world.storage.loot.LootTables;

import java.util.Random;

public class DungeonBlockHandler extends StructureProcessor {

    private static final IStructureProcessorType TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Runic.modid + "block_handler", DungeonBlockHandler::new);

    public DungeonBlockHandler() {
    }

    public DungeonBlockHandler(Dynamic<?> dyn) {
        this();
    }

    @Override
    public Template.BlockInfo process(IWorldReader worldReaderIn, BlockPos pos, Template.BlockInfo p_215194_3_, Template.BlockInfo blockInfo, PlacementSettings placementSettingsIn, Template template) {
        if (blockInfo.state.getBlock() instanceof ChestBlock) {
            Random rand = placementSettingsIn.getRandom(blockInfo.pos);
            TileEntity tile = TileEntity.create(blockInfo.nbt);
            if (tile instanceof ChestTileEntity) {
                ChestTileEntity chest = (ChestTileEntity) tile;
                chest.setLootTable(null, 0);
                for (int i = 0; i < chest.getSizeInventory(); i++)
                    chest.setInventorySlotContents(i, ItemStack.EMPTY);

                chest.setLootTable(LootTables.CHESTS_END_CITY_TREASURE, rand.nextLong());
                CompoundNBT nbt = new CompoundNBT();
                chest.write(nbt);
                return new Template.BlockInfo(blockInfo.pos, blockInfo.state, nbt);
            }
        }

        if (blockInfo.state.getBlock() instanceof SpawnerBlock) {
            Random rand = placementSettingsIn.getRandom(blockInfo.pos);
            TileEntity tile = TileEntity.create(blockInfo.nbt);

            if (tile instanceof MobSpawnerTileEntity) {
                MobSpawnerTileEntity spawner = (MobSpawnerTileEntity) tile;
                AbstractSpawner logic = spawner.getSpawnerBaseLogic();

                double val = rand.nextDouble();
                if (val > 0.95) {
                    logic.setEntityType(EntityType.VINDICATOR);
                } else if (val > 0.5) {
                    logic.setEntityType(ModEntities.BEHOLDER);
                } else {
                    logic.setEntityType(EntityType.CAVE_SPIDER);
                }

                tile.markDirty();
                CompoundNBT nbt = new CompoundNBT();
                tile.write(nbt);
                return new Template.BlockInfo(blockInfo.pos, blockInfo.state, nbt);
            }
        }

        if (blockInfo.state.getBlock() instanceof BlockMobCrystal) {
            Random rand = placementSettingsIn.getRandom(blockInfo.pos);
            TileEntity tile = TileEntity.create(blockInfo.nbt);

            if (tile instanceof TileMobCrystal) {
                double val = rand.nextDouble();
                if (val > 0.95) {
                    ((TileMobCrystal) tile).setMobType(EntityType.VINDICATOR);
                } else if (val > 0.5) {
                    ((TileMobCrystal) tile).setMobType(ModEntities.BEHOLDER);
                } else {
                    ((TileMobCrystal) tile).setMobType(EntityType.CAVE_SPIDER);
                }
            }
            tile.markDirty();
            CompoundNBT nbt = new CompoundNBT();
            tile.write(nbt);
            return new Template.BlockInfo(blockInfo.pos, blockInfo.state, nbt);
        }

        if (blockInfo.state.getBlock() == Blocks.STONE || blockInfo.state.getBlock() == Blocks.COBBLESTONE || blockInfo.state.getBlock() == Blocks.STONE_BRICKS) {
            Random rand = placementSettingsIn.getRandom(blockInfo.pos);
            int randomNumber = rand.nextInt(10);
            switch (randomNumber) {
                case 0: {
                    return new Template.BlockInfo(blockInfo.pos, Blocks.COBBLESTONE.getDefaultState(), blockInfo.nbt);
                }
                case 1: {
                    return new Template.BlockInfo(blockInfo.pos, Blocks.GRAVEL.getDefaultState(), blockInfo.nbt);
                }
                case 2: {
                    return new Template.BlockInfo(blockInfo.pos, Blocks.POLISHED_ANDESITE.getDefaultState(), blockInfo.nbt);
                }
                case 4: {
                    return new Template.BlockInfo(blockInfo.pos, Blocks.MOSSY_COBBLESTONE.getDefaultState(), blockInfo.nbt);
                }
                case 6: {
                    return new Template.BlockInfo(blockInfo.pos, Blocks.STONE.getDefaultState(), blockInfo.nbt);
                }
                case 8: {
                    return new Template.BlockInfo(blockInfo.pos, Blocks.STONE_BRICKS.getDefaultState(), blockInfo.nbt);
                }
                case 9: {
                    return new Template.BlockInfo(blockInfo.pos, Blocks.ANDESITE.getDefaultState(), blockInfo.nbt);
                }
            }
        }

        return blockInfo;
    }

    @Override
    protected IStructureProcessorType getType() {
        return TYPE;
    }

    @Override
    protected <T> Dynamic<T> serialize0(DynamicOps<T> ops) {
        return new Dynamic<>(ops);
    }

}
