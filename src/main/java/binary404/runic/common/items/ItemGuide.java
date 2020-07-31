package binary404.runic.common.items;

import binary404.runic.Runic;
import binary404.runic.api.multiblock.IMultiBlockTrigger;
import binary404.runic.api.multiblock.MultiBlockTrigger;
import binary404.runic.client.FXHelper;
import binary404.runic.client.core.handler.MultiBlockHandler;
import binary404.runic.common.core.util.EntityUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;

public class ItemGuide extends Item {

    public ItemGuide() {
        super(ModItems.defaultBuilder().maxStackSize(1));
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!playerIn.isSneaking())
            Runic.proxy.openGuide();
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        if (player.isSneaking() && MultiBlockHandler.multiblock != null) {
            if (context.getFace() == Direction.UP) {
                MultiBlockHandler.pos = context.getPos().add(0, 1, 0);
            } else {
                MultiBlockHandler.pos = context.getPos();
            }
            MultiBlockHandler.facing = MultiBlockHandler.getRotation(player);
            MultiBlockHandler.hasMultiBlock = true;
            return ActionResultType.SUCCESS;
        }
        player.swingArm(context.getHand());
        MultiBlockHandler.multiblock = null;
        MultiBlockHandler.hasMultiBlock = false;
        for (IMultiBlockTrigger trigger : IMultiBlockTrigger.triggers) {
            IMultiBlockTrigger.Placement place = trigger.getValidFace(context.getWorld(), player, context.getPos(), context.getFace());
            if (place != null) {
                trigger.execute(context.getWorld(), context.getPlayer(), context.getPos(), place, context.getFace());
                doSparkles(player, context.getWorld(), context.getPos(), (float) context.getHitVec().x, (float) context.getHitVec().y, (float) context.getHitVec().z, context.getHand(), trigger, place);
                return ActionResultType.SUCCESS;
            }
        }
        return super.onItemUse(context);
    }

    private void doSparkles(PlayerEntity player, World world, BlockPos pos, float hitX, float hitY, float hitZ, Hand
            hand, IMultiBlockTrigger trigger, IMultiBlockTrigger.Placement place) {
        Vector3d v1 = EntityUtils.posToHand(player, hand);
        Vector3d v2 = new Vector3d(pos.getX(), pos.getY(), pos.getZ());
        v2 = v2.add(0.5D, 0.5D, 0.5D);
        v2 = v2.subtract(v1);
        int cnt = 25;
        for (int a = 0; a < cnt; a++) {
            boolean floaty = (a < cnt / 3);
            float r = MathHelper.nextInt(world.rand, 255, 255) / 255.0F;
            float g = MathHelper.nextInt(world.rand, 189, 255) / 255.0F;
            float b = MathHelper.nextInt(world.rand, 64, 255) / 255.0F;
            FXHelper.sparkle(v1.x, v1.y, v1.z, v2.x / 6.0D + world.rand.nextGaussian() * 0.05D, v2.y / 6.0D + world.rand.nextGaussian() * 0.05D + (floaty ? 0.05D : 0.15D), v2.z / 6.0D + world.rand.nextGaussian() * 0.05D, r, g, b, 0.25F, floaty ? (float) (0.3F + world.rand.nextFloat() * 0.5D) : 0.85F, floaty ? 0.04F : 0.08F, 16);
        }

        List<BlockPos> sparkles = trigger.sparkle(world, player, pos, place);
        if (sparkles != null) {
            Vector3d v = (new Vector3d(pos.getX(), pos.getY(), pos.getZ())).add(hitX, hitY, hitZ);
            for (BlockPos p : sparkles) {
                FXHelper.drawBlockSparkles(p, v);
            }
        }
    }

}
