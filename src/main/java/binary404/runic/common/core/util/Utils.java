/*
 *
 * this class was created by <CSchlaud04> it is distributed as part of the demonic mod.
 *
 * demonic is closed source and is distributed under the demonic licence.
 *
 * all of the content of this mod(including code, models, assets and any other data distributed with the mod) is made exclusively for the demonic mod.
 *
 * you may read this code for educational purposes but any other use is forbidden
 *
 * CSchlaud04 or any other developer of the demonic mod may use this code as they please.
 *
 * class made @ [Sep 24, 2019, 2:19]
 *
 */

package binary404.runic.common.core.util;

import binary404.runic.Runic;
import com.google.common.base.CaseFormat;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static CompoundNBT writeBlockPosToNBT(BlockPos pos, CompoundNBT compound) {
        compound.putInt("bposX", pos.getX());
        compound.putInt("bposY", pos.getY());
        compound.putInt("bposZ", pos.getZ());
        return compound;
    }

    public static BlockPos readBlockPosFromNBT(CompoundNBT compound) {
        int x = compound.getInt("bposX");
        int y = compound.getInt("bposY");
        int z = compound.getInt("bposZ");
        return new BlockPos(x, y, z);
    }

    @Nonnull
    public static <T> T getEnumEntry(Class<T> enumClazz, int index) {
        if (!enumClazz.isEnum()) {
            throw new IllegalArgumentException("Called getEnumEntry on class " + enumClazz.getName() + " which isn't an enum.");
        }
        T[] values = enumClazz.getEnumConstants();
        if (values.length == 0) {
            throw new IllegalArgumentException(enumClazz.getName() + " has no enum constants.");
        }
        return values[MathHelper.clamp(index, 0, values.length - 1)];
    }

    public static Vector3d rotateAroundY(Vector3d vec, float angle) {
        float var2 = MathHelper.cos(angle);
        float var3 = MathHelper.sin(angle);
        double var4 = vec.x * var2 + vec.z * var3;
        double var6 = vec.y;
        double var8 = vec.z * var2 - vec.x * var3;
        return new Vector3d(var4, var6, var8);
    }

    public static float pointDistanceSpace(double x1, double y1, double z1, double x2, double y2, double z2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
    }

    public static void writeCompoundNBTToBuffer(PacketBuffer bb, CompoundNBT nbt) {
        if (nbt == null) {

            bb.writeByte(0);
        } else {

            try {

                CompressedStreamTools.write(nbt, (DataOutput) new ByteBufOutputStream(bb));
            } catch (IOException ioexception) {

                throw new EncoderException(ioexception);
            }
        }
    }

    public static CompoundNBT readCompoundNBTFromBuffer(PacketBuffer bb) {
        int i = bb.readerIndex();
        byte b0 = bb.readByte();

        if (b0 == 0) {
            return null;
        }

        bb.readerIndex(i);
        try {
            return CompressedStreamTools.read((DataInput) new ByteBufInputStream(bb), new NBTSizeTracker(2097152L));
        } catch (IOException iOException) {

            return null;
        }
    }

    public static byte pack(final boolean... vals) {
        byte result = 0;
        for (final boolean bit : vals) {
            result = (byte) (result << 1 | ((bit & true) ? 1 : 0));
        }
        return result;
    }

    public static boolean[] unpack(final byte val) {
        final boolean[] result = new boolean[8];
        for (int i = 0; i < 8; ++i) {
            result[i] = ((byte) (val >> 7 - i & 0x1) == 1);
        }
        return result;
    }

    public static boolean getBit(int value, int bit) {
        return ((value & 1 << bit) != 0);
    }

    public static int setBit(int value, int bit) {
        return value | 1 << bit;
    }


    public static boolean isWoodLog(World world, BlockPos pos) {
        BlockState bs = world.getBlockState(pos);
        Block bi = bs.getBlock();
        if (bs.getMaterial() == Material.WOOD)
            return true;
        //Tags todo
        return false;
    }

    public static byte[] getSha(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    public static boolean matchesHash(String str, String hash) {
        try {
            return toHexString(getSha(str)).equals(hash);
        } catch (NoSuchAlgorithmException e) {

        }
        return false;
    }

    public static boolean isLyingInCone(final double[] x, final double[] t, final double[] b, final float aperture) {
        final double halfAperture = aperture / 2.0f;
        final double[] apexToXVect = dif(t, x);
        final double[] axisVect = dif(t, b);
        final boolean isInInfiniteCone = dotProd(apexToXVect, axisVect) / magn(apexToXVect) / magn(axisVect) > Math.cos(halfAperture);
        if (!isInInfiniteCone) {
            return false;
        }
        final boolean isUnderRoundCap = dotProd(apexToXVect, axisVect) / magn(axisVect) < magn(axisVect);
        return isUnderRoundCap;
    }

    public static double dotProd(final double[] a, final double[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

    public static double[] dif(final double[] a, final double[] b) {
        return new double[]{a[0] - b[0], a[1] - b[1], a[2] - b[2]};
    }

    public static double magn(final double[] a) {
        return Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]);
    }


    public static Vector3d calculateVelocity(final Vector3d from, final Vector3d to, final double heightGain, final double gravity) {
        final double endGain = to.y - from.y;
        final double horizDist = Math.sqrt(distanceSquared2d(from, to));
        final double gain = heightGain;
        final double maxGain = (gain > endGain + gain) ? gain : (endGain + gain);
        final double a = -horizDist * horizDist / (4.0 * maxGain);
        final double b = horizDist;
        final double c = -endGain;
        final double slope = -b / (2.0 * a) - Math.sqrt(b * b - 4.0 * a * c) / (2.0 * a);
        final double vy = Math.sqrt(maxGain * gravity);
        final double vh = vy / slope;
        final double dx = to.x - from.x;
        final double dz = to.z - from.z;
        final double mag = Math.sqrt(dx * dx + dz * dz);
        final double dirx = dx / mag;
        final double dirz = dz / mag;
        final double vx = vh * dirx;
        final double vz = vh * dirz;
        return new Vector3d(vx, vy, vz);
    }

    public static double distanceSquared2d(final Vector3d from, final Vector3d to) {
        final double dx = to.x - from.x;
        final double dz = to.z - from.z;
        return dx * dx + dz * dz;
    }

    public static double distanceSquared3d(final Vector3d from, final Vector3d to) {
        final double dx = to.x - from.x;
        final double dy = to.y - from.y;
        final double dz = to.z - from.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public static void writeString(ByteBuf buf, String toWrite) {
        byte[] str = toWrite.getBytes(Charset.forName("UTF-8"));
        buf.writeInt(str.length);
        buf.writeBytes(str);
    }

    public static String readString(PacketBuffer buf) {
        int length = buf.readInt();
        byte[] strBytes = new byte[length];
        buf.readBytes(strBytes, 0, length);
        return new String(strBytes, Charset.forName("UTF-8"));
    }

    public static String readString(ByteBuf buf) {
        int length = buf.readInt();
        byte[] strBytes = new byte[length];
        buf.readBytes(strBytes, 0, length);
        return new String(strBytes, Charset.forName("UTF-8"));
    }

    public static int x(Rotation rot, int x, int z) {
        switch (rot) {
            case NONE:
                return x;
            case CLOCKWISE_180:
                return -x;
            case CLOCKWISE_90:
                return z;
            default:
                return -z;
        }
    }

    public static int z(Rotation rot, int x, int z) {
        switch (rot) {
            case NONE:
                return z;
            case CLOCKWISE_180:
                return -z;
            case CLOCKWISE_90:
                return x;
            default:
                return -x;
        }
    }


    public static Rotation fixHorizontal(Rotation rot) {
        switch (rot) {
            case CLOCKWISE_90:
                return Rotation.COUNTERCLOCKWISE_90;
            case COUNTERCLOCKWISE_90:
                return Rotation.CLOCKWISE_90;
            default:
                return rot;
        }
    }

    public static Ingredient asIngredient(Object object) {
        if (object instanceof Ingredient)
            return (Ingredient) object;

        else if (object instanceof Item)
            return Ingredient.fromItems((Item) object);

        else if (object instanceof Block)
            return Ingredient.fromStacks(newStack((Block) object));

        else if (object instanceof ItemStack)
            return Ingredient.fromStacks((ItemStack) object);

        throw new IllegalArgumentException("Cannot convert object of type " + object.getClass().toString() + " to an Ingredient!");
    }

    public static ItemStack newStack(Block block) {
        return newStack(block, 1);
    }

    public static ItemStack newStack(Block block, int size) {
        return newStack(Item.getItemFromBlock(block), size);
    }

    public static ItemStack newStack(Item item) {
        return newStack(item, 1);
    }

    public static ItemStack newStack(Item item, int size) {
        return newStack(item, size);
    }

    public static <R> R cast(Object instance) {
        return (R) instance;
    }

    public static ResourceLocation fromClass(Object object, @Nullable String cutPrefix) {
        return fromClass(object, cutPrefix, null);
    }

    public static ResourceLocation fromClass(Class<?> clazz, @Nullable String cutPrefix) {
        return fromClass(clazz, cutPrefix, null);
    }

    public static ResourceLocation fromClass(Object object, @Nullable String cutPrefix, @Nullable String cutSuffix) {
        return fromClass(object.getClass(), cutPrefix, cutSuffix);
    }

    public static ResourceLocation fromClass(Class<?> clazz, @Nullable String cutPrefix, @Nullable String cutSuffix) {
        String name = clazz.getSimpleName();
        if (clazz.getEnclosingClass() != null) {
            name = clazz.getEnclosingClass().getSimpleName() + name;
        }
        if (cutPrefix != null && name.startsWith(cutPrefix)) {
            name = name.substring(cutPrefix.length());
        }
        if (cutSuffix != null && name.endsWith(cutSuffix)) {
            name = name.substring(0, name.length() - cutSuffix.length());
        }
        name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        return Runic.key(name);
    }

}
