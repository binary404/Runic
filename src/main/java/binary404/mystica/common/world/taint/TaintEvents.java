package binary404.mystica.common.world.taint;

import binary404.mystica.common.core.util.RandomItemChooser;
import binary404.mystica.common.entity.ModEntities;
import binary404.mystica.common.entity.taint.EntityTaintCloud;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import java.util.ArrayList;

public class TaintEvents {

    static ArrayList<RandomItemChooser.Item> trigger_events = new ArrayList<>();

    static {
        trigger_events.add(new TaintTriggerItem(0, 5));
    }

    public static boolean taintTriggerEvent(World world, BlockPos pos) {
        RandomItemChooser ric = new RandomItemChooser();
        TaintTriggerItem triggerItem = (TaintTriggerItem) ric.chooseOnWeight(trigger_events);
        pos = pos.add(world.rand.nextInt(16), 0, world.rand.nextInt(16));
        BlockPos pos2 = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos);
        switch (triggerItem.event) {
            case 0:
                EntityTaintCloud cloud = new EntityTaintCloud(ModEntities.TAINT_CLOUD, world, pos2.add(0, 10, 0), 2000);
                world.addEntity(cloud);
        }
        return true;
    }

    static class TaintTriggerItem implements RandomItemChooser.Item {
        int weight;
        int event;

        protected TaintTriggerItem(int event, int weight) {
            this.weight = weight;
            this.event = event;
        }

        @Override
        public double getWeight() {
            return this.weight;
        }
    }

}
