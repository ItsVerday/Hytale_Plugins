package io.github.itsverday.carta.util.cache;

import com.hypixel.hytale.function.consumer.BooleanConsumer;

import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

public class SpatialBooleanCache2D {
    private final boolean[] values;
    private final double[] realPositions;

    private final int bitsXZ;
    private final int maskXZ;

    public SpatialBooleanCache2D(int bitsXZ) {
        this.bitsXZ = bitsXZ;
        int sizeXZ = 1 << bitsXZ;
        int size = sizeXZ * sizeXZ;

        maskXZ = ((1 << bitsXZ) - 1);

        values = new boolean[size];
        realPositions = new double[size * 2];

        // Make cell at (0, 0, 0) need recomputing since realX, realY, realZ for that cell are all initialized to 0.
        int badKey = computeKey(0, 0);
        realPositions[indexX(badKey)] = 1;
    }

    private int computeKey(double x, double z) {
        int xInt = (int) x;
        int zInt = (int) z;
        return (xInt & maskXZ) | ((zInt & maskXZ) << bitsXZ);
    }

    public void put(double x, double z, boolean value) {
        int key = computeKey(x, z);
        values[key] = value;
        realPositions[indexX(key)] = x;
        realPositions[indexZ(key)] = z;
    }

    public boolean getAndConsume(double x, double z, BooleanConsumer consumer) {
        int key = computeKey(x, z);
        if (realPositions[indexX(key)] != x) return false;
        if (realPositions[indexZ(key)] != z) return false;
        consumer.accept(values[key]);
        return true;
    }

    public boolean getOrCompute(double x, double z, BooleanSupplier supplier) {
        int key = computeKey(x, z);
        int keyX = indexX(key);
        int keyZ = indexZ(key);
        if (realPositions[keyX] == x && realPositions[keyZ] == z) return values[key];

        boolean value = supplier.getAsBoolean();
        values[key] = value;
        realPositions[keyX] = x;
        realPositions[keyZ] = z;
        return value;
    }

    private static int indexX(int i) {
        return i * 2;
    }

    private static int indexZ(int i) {
        return i * 2 + 1;
    }
}
