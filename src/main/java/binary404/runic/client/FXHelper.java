package binary404.runic.client;

import binary404.fx_lib.fx.ParticleDispatcher;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class FXHelper {

    public static Random rand = new Random();

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
        part.alpha = new float[]{1.0F, 1.0F, 0.0F};
        part.rotstart = rand.nextFloat();
        part.rot = (float) rand.nextGaussian();
        part.location = new ResourceLocation("runic", "textures/misc/particles.png");
        ParticleDispatcher.genericFx(x, y, z, 0.0D, 0.0D, 0.0D, part);
    }

    public static void flare(double x, double y, double z, float r, float g, float b, float scale) {
        ParticleDispatcher.GenPart part = new ParticleDispatcher.GenPart();
        part.scale = new float[]{scale};
        part.r = r;
        part.g = g;
        part.b = b;
        part.grid = 16;
        part.age = 60;
        part.partStart = 77;
        part.partNum = 1;
        part.partInc = 1;
        part.loop = false;
        part.alpha = new float[]{1.0F, 0.0F};
        part.rotstart = rand.nextFloat();
        part.rot = (float) rand.nextGaussian();
        part.location = new ResourceLocation("runic", "textures/misc/particles.png");
        ParticleDispatcher.genericFx(x, y, z, 0.0D, 0.0D, 0.0D, part);
    }

    public static void rune1(double x, double y, double z, float r, float g, float b) {
        ParticleDispatcher.GenPart part = new ParticleDispatcher.GenPart();
        part.scale = new float[]{0.2F};
        part.r = r;
        part.g = g;
        part.b = b;
        part.age = 60;
        part.partStart = 384 + rand.nextInt(16);
        part.grid = 64;
        part.location = new ResourceLocation("runic", "textures/misc/particles.png");
        part.alpha = new float[]{1.0F};
        part.slowDown = 0.0D;
        part.grav = 0.9F;
        ParticleDispatcher.genericFx(x, y, z, 0.0D, 0.0D, 0.0D, part);
    }

    public static void sparkle(double x, double y, double z, double mx, double my, double mz, float r, float g, float b) {
        ParticleDispatcher.GenPart part = new ParticleDispatcher.GenPart();
        part.scale = new float[]{0.3F, 0.0F};
        part.r = r;
        part.g = g;
        part.b = b;
        part.grid = 64;
        part.age = 20;
        part.partStart = 320;
        part.partNum = 16;
        part.partInc = 1;
        part.loop = true;
        part.location = new ResourceLocation("runic", "textures/misc/particles.png");
        part.alpha = new float[]{0.8F};
        ParticleDispatcher.genericFx(x, y, z, mx, my, mz, part);
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
        part.location = new ResourceLocation("runic", "textures/misc/particles.png");
        part.alpha = new float[]{0.7F, 0.0F};
        ParticleDispatcher.genericFx(x, y, z, mx, my, mz, part);
    }

}
