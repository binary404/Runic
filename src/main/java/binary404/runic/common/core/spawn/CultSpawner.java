package binary404.runic.common.core.spawn;

import binary404.runic.common.entity.EntityCultZombie;
import binary404.runic.common.entity.EntityRangedCultZombie;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;

import java.util.Random;

public class CultSpawner {

    private static int timer;

    public static void spawnCults(ServerWorld world) {
        Random random = world.rand;
        if (world.getDayTime() >= 17000 && world.getDayTime() <= 19000) {
            --timer;
            if (timer > 0) {
                return;
            }
            if (random.nextInt(10) != 0) {
                return;
            } else {
                int playerCount = world.getPlayers().size();
                if (playerCount < 1) {
                    return;
                }
                PlayerEntity player = world.getPlayers().get(random.nextInt(playerCount));
                if (player.isSpectator()) {
                    return;
                } else {
                    timer += 200 + random.nextInt(200);
                    int startX = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    int startY = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    BlockPos.Mutable pos = new BlockPos.Mutable(player).move(startX, 0, startY);

                    int iterations = random.nextInt(4) + 2;
                    for (int i = 0; i < iterations; ++i) {
                        spawnCultist(world, pos, random);
                        pos.setY(world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).getY());
                        pos.setX(pos.getX() + random.nextInt(5) - random.nextInt(5));
                        pos.setZ(pos.getZ() + random.nextInt(5) - random.nextInt(5));
                    }
                }
            }
        }
    }

    public static void spawnCultist(World world, BlockPos pos, Random random) {
        BlockState state = world.getBlockState(pos);
        if (!WorldEntitySpawner.isSpawnableSpace(world, pos, state, state.getFluidState())) {
            return;
        }
        EntityCultZombie cultist;
        switch (random.nextInt(2)) {
            case 1: {
                cultist = new EntityRangedCultZombie(world);
                break;
            }
            default: {
                cultist = new EntityCultZombie(world);
                break;
            }
        }
        cultist.setPosition(pos.getX(), pos.getY(), pos.getZ());
        cultist.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.MOB_SUMMONED, null, null);
        world.addEntity(cultist);
    }

}
