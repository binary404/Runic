package binary404.runic.common.world.dungeon;

import binary404.runic.Runic;
import binary404.runic.common.entity.ModEntities;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class DungeonStructure extends ScatteredStructure<NoFeatureConfig> {

    private static final List<Biome.SpawnListEntry> ENEMIES = Lists.newArrayList(
            new Biome.SpawnListEntry(EntityType.WITCH, 80, 1, 1),
            new Biome.SpawnListEntry(EntityType.ILLUSIONER, 140, 1, 1),
            new Biome.SpawnListEntry(ModEntities.BEHOLDER, 100, 1, 1)
    );

    private static final Set<String> STARTS = ImmutableSet.of(
            "start1"
    );

    private static final Set<String> HALLWAY = ImmutableSet.of(
            "hallway1", "hallway2", "hallway3", "hallway4"
    );

    private static final Set<String> STAIR = ImmutableSet.of(
            "stair1"
    );

    private static final Set<String> ROOMS = ImmutableSet.of(
            "room1_1", "room1_2", "room1_3"
    );

    static {
        DungeonBlockHandler handler = new DungeonBlockHandler();
        JigsawHelper.pool("dungeon", "starts").processor(handler).addMult("starts", STARTS, 1).register(JigsawPattern.PlacementBehaviour.RIGID);
        JigsawHelper.pool("dungeon", "hallway").processor(handler).addMult("hallway", HALLWAY, 1).addMult("stair", STAIR, 1).register(JigsawPattern.PlacementBehaviour.RIGID);
        JigsawHelper.pool("dungeon", "room").processor(handler).addMult("room", ROOMS, 1).register(JigsawPattern.PlacementBehaviour.RIGID);
        JigsawHelper.pool("dungeon", "exit").processor(handler).addMult("room", ROOMS, 100).addMult("hallway", HALLWAY, 110).register(JigsawPattern.PlacementBehaviour.RIGID);
    }

    public DungeonStructure() {
        super(fc -> NoFeatureConfig.NO_FEATURE_CONFIG);
        setRegistryName(Runic.modid, "dungeon");
    }

    @Override
    public List<Biome.SpawnListEntry> getSpawnList() {
        return ENEMIES;
    }

    @Override
    public int getSize() {
        return (int) Math.ceil(15 / 1.5);
    }

    @Override
    protected int getBiomeFeatureDistance(ChunkGenerator<?> chunkGenerator) {
        return 9;
    }

    @Override
    protected int getBiomeFeatureSeparation(ChunkGenerator<?> chunkGenerator) {
        return 3;
    }

    @Override
    protected int getSeedModifier() {
        return 17548392;
    }

    @Override
    public IStartFactory getStartFactory() {
        return Start::new;
    }

    @Override
    public String getStructureName() {
        return "dungeon";
    }

    public static class Start extends MarginedStructureStart {

        public Start(Structure<?> p_i225874_1_, int p_i225874_2_, int p_i225874_3_, MutableBoundingBox p_i225874_4_, int p_i225874_5_, long p_i225874_6_) {
            super(p_i225874_1_, p_i225874_2_, p_i225874_3_, p_i225874_4_, p_i225874_5_, p_i225874_6_);
        }

        @Override
        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
            BlockPos pos = new BlockPos(chunkX * 16, 40, chunkZ * 16);
            JigsawManager.addPieces(new ResourceLocation("runic", "dungeon/starts"), 25, Piece::new, generator, templateManagerIn, pos, components, this.rand);
            recalculateStructureSize();

            int maxTop = 60;
            if (bounds.maxY >= maxTop) {
                int shift = 5 + (bounds.maxY - maxTop);
                bounds.offset(0, -shift, 0);
                components.forEach(p -> p.offset(0, -shift, 0));
            }

            if (bounds.minY < 6) {
                int shift = 6 - bounds.minY;
                bounds.offset(0, shift, 0);
                components.forEach(p -> p.offset(0, shift, 0));
            }

            components.removeIf(c -> c.getBoundingBox().maxY >= maxTop);
        }
    }

    public static class Piece extends AbstractVillagePiece {
        public static IStructurePieceType PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "dungeon", DungeonStructure.Piece::new);

        public Piece(TemplateManager templateManagerIn, JigsawPiece jigsawPieceIn, BlockPos posIn, int p_i50560_4_, Rotation rotationIn, MutableBoundingBox boundsIn) {
            super(PIECE_TYPE, templateManagerIn, jigsawPieceIn, posIn, p_i50560_4_, rotationIn, boundsIn);
        }

        public Piece(TemplateManager templateManagerIn, CompoundNBT nbt) {
            super(templateManagerIn, nbt, PIECE_TYPE);
        }
    }

}
