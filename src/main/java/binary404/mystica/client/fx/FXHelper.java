package binary404.mystica.client.fx;

import binary404.fx_lib.fx.ParticleDispatcher;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class FXHelper {

    public static Random rand = new Random();

    public static World getWorld() {
        return Minecraft.getInstance().world;
    }

    public static ParticleDispatcher.GenPart defaultGen = new ParticleDispatcher.GenPart();
    public static ResourceLocation particleLocation = new ResourceLocation("mystica", "textures/misc/particles.png");

    static {
        defaultGen.location = particleLocation;
    }

    public static ParticleDispatcher.GenPart getGenPart(float r, float g, float b, float scale) {
        ParticleDispatcher.GenPart toReturn = defaultGen;
        toReturn.r = r;
        toReturn.g = g;
        toReturn.b = b;
        toReturn.scale = new float[]{scale};
        return toReturn;
    }

    public static void flame(double x, double y, double z, float scale) {
        ParticleDispatcher.GenPart part = getGenPart(1.0F, 1.0F, 1.0F, scale);
        part.grid = 64;
        part.age = 10;
        part.partStart = 640;
        part.partNum = 10;
        part.partInc = 1;
        part.loop = true;
        part.alpha = new float[]{1.0F};
        part.grav = -0.08F;
        ParticleDispatcher.genericFx(x, y, z, 0.0D, 0.0D, 0.0D, part);
    }

    public static void poof(double x, double y, double z, float r, float g, float b, float scale) {
        ParticleDispatcher.GenPart part = new ParticleDispatcher.GenPart();
        part.scale = new float[]{scale};
        part.r = r;
        part.g = g;
        part.b = b;
        part.grid = 16;
        part.age = 60;
        part.partStart = 123;
        part.partNum = 5;
        part.partInc = 1;
        part.loop = false;
        part.alpha = new float[]{1.0F, 0.0F};
        part.rotstart = rand.nextFloat();
        part.rot = (float) rand.nextGaussian();
        part.location = particleLocation;
        ParticleDispatcher.genericFx(x, y, z, 0.0D, 0.0D, 0.0D, part);
    }

    public static void flare(double x, double y, double z, float r, float g, float b, float scale) {
        ParticleDispatcher.GenPart part = new ParticleDispatcher.GenPart();
        part.scale = new float[]{scale, 0.0F};
        part.r = r;
        part.g = g;
        part.b = b;
        part.grid = 16;
        part.age = 60;
        part.partStart = 77;
        part.partNum = 1;
        part.partInc = 1;
        part.loop = false;
        part.alpha = new float[]{1.0F};
        part.rotstart = rand.nextFloat();
        part.rot = (float) rand.nextGaussian();
        part.location = particleLocation;
        ParticleDispatcher.genericFx(x, y, z, 0.0D, 0.0D, 0.0D, part);
    }

    public static void rune1(double x, double y, double z, float r, float g, float b) {
        rune1(x, y, z, 0, 0, 0, r, g, b, 0.4F, 60);
    }

    public static void rune1(double x, double y, double z, double mx, double my, double mz, float r, float g, float b, float gravity, int age) {
        ParticleDispatcher.GenPart part = new ParticleDispatcher.GenPart();
        part.scale = new float[]{0.2F};
        part.r = r;
        part.g = g;
        part.b = b;
        part.age = age;
        part.partStart = 384 + rand.nextInt(16);
        part.grid = 64;
        part.location = particleLocation;
        part.alpha = new float[]{1.0F};
        part.grav = gravity;
        ParticleDispatcher.genericFx(x, y, z, mx, my, mz, part);
    }

    public static void sparkle(double x, double y, double z, double mx, double my, double mz, float r, float g, float b, float scale, float decay, float grav, int baseAge) {
        ParticleDispatcher.GenPart part = new ParticleDispatcher.GenPart();
        int age = baseAge * 4 + rand.nextInt(baseAge);
        boolean sp = (rand.nextFloat() < 0.2D);
        part.scale = new float[]{scale, scale * 2.0F, scale / 2};
        part.r = r;
        part.g = g;
        part.b = b;
        part.grid = 64;
        part.age = age;
        part.partStart = sp ? 320 : 512;
        part.partNum = 16;
        part.partInc = 1;
        part.loop = true;
        part.location = particleLocation;
        part.alpha = new float[]{0.2F, 1.0F};
        part.slowDown = decay;
        part.grav = grav;
        ParticleDispatcher.genericFx(x, y, z, mx, my, mz, part);
    }

    public static void drawBlockSparkles(BlockPos p, Vector3d start) {
        VoxelShape vs = getWorld().getBlockState(p).getShape(getWorld(), p);
        List<AxisAlignedBB> bb = vs.toBoundingBoxList();
        for (AxisAlignedBB bs : bb) {
            int num = (int) (bs.getAverageEdgeLength() * 20.0D);

            for (Direction face : Direction.values()) {
                BlockState state = getWorld().getBlockState(p.offset(face));
                if (!state.isOpaqueCube(getWorld(), p.offset(face))) {
                    boolean rx = (face.getXOffset() == 0);
                    boolean ry = (face.getYOffset() == 0);
                    boolean rz = (face.getZOffset() == 0);
                    double mx = 0.5D + face.getXOffset() * 0.51D;
                    double my = 0.5D + face.getYOffset() * 0.51D;
                    double mz = 0.5D + face.getZOffset() * 0.51D;

                    for (int a = 0; a < num / 2; a++) {
                        double x = mx;
                        double y = my;
                        double z = mz;
                        if (rx) x += (getWorld()).rand.nextGaussian() * 0.6D;
                        if (ry) y += (getWorld()).rand.nextGaussian() * 0.6D;
                        if (rz) z += (getWorld()).rand.nextGaussian() * 0.6D;
                        x = MathHelper.clamp(x, bs.minX, bs.maxX);
                        y = MathHelper.clamp(y, bs.minY, bs.maxY);
                        z = MathHelper.clamp(z, bs.minZ, bs.maxZ);
                        float r = MathHelper.nextInt((getWorld()).rand, 255, 255) / 255.0F;
                        float g = MathHelper.nextInt((getWorld()).rand, 189, 255) / 255.0F;
                        float b = MathHelper.nextInt((getWorld()).rand, 64, 255) / 255.0F;
                        Vector3d v1 = new Vector3d(p.getX() + x, p.getY() + y, p.getZ() + z);
                        double delay = (getWorld()).rand.nextInt(5) + v1.distanceTo(start) * 16.0D;
                        sparkle(p.getX() + x, p.getY() + y, p.getZ() + z, 0.0D, 0.0025D, 0.0D, r, g, b, 0.04F + (float) (getWorld()).rand.nextGaussian() * 0.06F, 1.0F, 0.01F, 16);
                    }
                }
            }
        }
    }

    public static void wisp(double x, double y, double z, double mx, double my, double mz, float r, float g, float b, float scale, int age) {
        ParticleDispatcher.GenPart part = new ParticleDispatcher.GenPart();
        part.scale = new float[]{scale};
        part.r = r;
        part.g = g;
        part.b = b;
        part.grid = 32;
        part.age = age;
        part.partStart = 224;
        part.location = particleLocation;
        part.alpha = new float[]{0.7F, 0.0F};
        ParticleDispatcher.genericFx(x, y, z, mx, my, mz, part);
    }

}
