package io.github.itsverday.carta.hytalegenerator.cartas;

import com.hypixel.hytale.builtin.hytalegenerator.workerindexer.WorkerIndexer;
import com.hypixel.hytale.builtin.hytalegenerator.worldstructure.BiCarta;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import java.util.List;

public class BatchComputeCarta extends BiCarta<Integer> {
    @Nonnull
    private final BiCarta<Integer> input;
    private final int batchSize;
    private final int cacheSize;

    private final int[] batches;
    private final int[] realBatchLocations;

    public BatchComputeCarta(@Nonnull BiCarta<Integer> input, int batchSize, int cacheSize) {
        this.input = input;
        this.batchSize = batchSize;
        this.cacheSize = cacheSize;

        batches = new int[cacheSize  * cacheSize * batchSize * batchSize];
        realBatchLocations = new int[cacheSize * cacheSize * 2];
        realBatchLocations[indexX(computeIndex(0, 0))] = 1;
    }

    @Override
    public Integer apply(int x, int z, @NonNullDecl WorkerIndexer.Id id) {
        int batchX = computeBatchLocation(x);
        int batchZ = computeBatchLocation(z);
        int batchIndex = computeIndex(batchX, batchZ);
        int batchOffset = batchIndex * batchSize * batchSize;
        int batchIndexX = indexX(batchIndex);
        int batchIndexZ = indexZ(batchIndex);
        int xm = batchX * batchSize;
        int zm = batchZ * batchSize;

        if (realBatchLocations[batchIndexX] != batchX || realBatchLocations[batchIndexZ] != batchZ) {
            realBatchLocations[batchIndexX] = batchX;
            realBatchLocations[batchIndexZ] = batchZ;

            for (int xi = 0; xi < batchSize; xi++) {
                for (int zi = 0; zi < batchSize; zi++) {
                    batches[batchOffset + xi + zi * batchSize] = input.apply(xm + xi, zm + zi, id);
                }
            }
        }

        return batches[batchOffset + (x - xm) + (z - zm) * batchSize];
    }

    private int computeBatchLocation(int x) {
        return Math.floorDiv(x, batchSize);
    }

    private int computeIndex(int batchX, int batchZ) {
        return Math.floorMod(batchX, cacheSize) + Math.floorMod(batchZ, cacheSize) * cacheSize;
    }

    private static int indexX(int x) {
        return x * 2;
    }

    private static int indexZ(int z) {
        return z * 2 + 1;
    }

    @Override
    public List<Integer> allPossibleValues() {
        return input.allPossibleValues();
    }
}
