package io.github.itsverday.carta.hytalegenerator.cartas;

import com.hypixel.hytale.builtin.hytalegenerator.workerindexer.WorkerIndexer;
import com.hypixel.hytale.builtin.hytalegenerator.worldstructure.BiCarta;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SingleThreadedCarta extends BiCarta<Integer> {
    @Nonnull
    private final BiCarta<Integer> input;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "HytaleGenerator-Carta-Worker");
        t.setPriority(Thread.MIN_PRIORITY);
        t.setDaemon(true);
        return t;
    });

    public SingleThreadedCarta(@Nonnull BiCarta<Integer> input) {
        this.input = input;
    }

    @Override
    public Integer apply(int x, int z, @NonNullDecl WorkerIndexer.Id id) {
        Future<Integer> future = executor.submit(() -> input.apply(x, z, id));

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Integer> allPossibleValues() {
        return input.allPossibleValues();
    }
}
