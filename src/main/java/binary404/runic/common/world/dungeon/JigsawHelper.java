package binary404.runic.common.world.dungeon;

import binary404.runic.Runic;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JigsawHelper {

    public static final FakeAirProcessor FAKE_AIR = new FakeAirProcessor();

    public static PoolBuilder pool(String namespace, String name) {
        return new PoolBuilder(namespace, name);
    }

    public static class PoolBuilder {

        private final String namespace, name;
        private final List<PiecePrototype> pieces = new LinkedList<>();
        private final List<StructureProcessor> globalProcessors = new LinkedList<>();

        private PoolBuilder(String namespace, String name) {
            this.namespace = namespace;
            this.name = name;

            globalProcessors.add(FAKE_AIR);
        }

        public PoolBuilder processor(StructureProcessor... processors) {
            for (StructureProcessor p : processors)
                globalProcessors.add(p);
            return this;
        }

        public PoolBuilder add(String name, int weight) {
            pieces.add(new PiecePrototype(name, weight));
            return this;
        }

        public PoolBuilder add(String name, int weight, StructureProcessor... processors) {
            pieces.add(new PiecePrototype(name, weight, processors));
            return this;
        }

        public PoolBuilder addMult(String dir, Iterable<String> names, int weight) {
            String pref = dir.isEmpty() ? "" : (dir + "/");
            for (String s : names)
                add(pref + s, weight);
            return this;
        }

        @SuppressWarnings("deprecation")
        public void register(JigsawPattern.PlacementBehaviour placementBehaviour) {
            ResourceLocation resource = new ResourceLocation(Runic.modid, namespace + "/" + name);

            List<Pair<JigsawPiece, Integer>> createdPieces =
                    pieces.stream()
                            .map(proto -> Pair.of((JigsawPiece) new SingleJigsawPiece((Runic.modid + ":" + namespace + "/" + proto.name), proto.processors), proto.weight))
                            .collect(ImmutableList.toImmutableList());

            JigsawManager.REGISTRY.register(new JigsawPattern(resource, new ResourceLocation("empty"), createdPieces, placementBehaviour));
        }

        private class PiecePrototype {
            final String name;
            final int weight;
            final List<StructureProcessor> processors;

            public PiecePrototype(String name, int weight) {
                this(name, weight, new StructureProcessor[0]);
            }

            public PiecePrototype(String name, int weight, StructureProcessor... processors) {
                this.name = name;
                this.weight = weight;
                this.processors = Streams.concat(Arrays.stream(processors), globalProcessors.stream()).collect(ImmutableList.toImmutableList());
            }
        }

    }

    private static class FakeAirProcessor extends StructureProcessor {

        private static final IStructureProcessorType TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Runic.modid + ":fake_air", FakeAirProcessor::new);

        public FakeAirProcessor() {
            // NO-OP
        }

        public FakeAirProcessor(Dynamic<?> dyn) {
            this();
        }

        @Override
        public Template.BlockInfo process(IWorldReader worldReaderIn, BlockPos pos, Template.BlockInfo p_215194_3_, Template.BlockInfo blockInfo, PlacementSettings placementSettingsIn) {
            if (blockInfo.state.getBlock() == Blocks.BARRIER)
                return new Template.BlockInfo(blockInfo.pos, Blocks.CAVE_AIR.getDefaultState(), new CompoundNBT());
            else if (blockInfo.state.getProperties().contains(BlockStateProperties.WATERLOGGED) && blockInfo.state.get(BlockStateProperties.WATERLOGGED))
                return new Template.BlockInfo(blockInfo.pos, blockInfo.state.with(BlockStateProperties.WATERLOGGED, false), blockInfo.nbt);

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

}
