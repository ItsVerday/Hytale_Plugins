package io.github.itsverday.carta.hytalegenerator.biomeproviderscanners;

import com.hypixel.hytale.builtin.hytalegenerator.pipe.Control;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector3d;

import javax.annotation.Nonnull;

public class Lines2DBiomeProviderScanner extends BiomeProviderScanner {
    private final int radius;
    @Nonnull
    private final Vector3d rOffset = new Vector3d();
    @Nonnull
    private final Control rControl = new Control();

    public Lines2DBiomeProviderScanner(double radius) {
        this.radius = (int) Math.floor(radius);
    }

    @Override
    public void generate(@NonNullDecl Context context) {
        rControl.reset();

        rOffset.set(0, 0, 0);
        context.pipe.accept(rOffset, rControl);

        for (int distance = 1; distance <= radius; distance++) {
            if (rControl.stop) return;
            rOffset.set(distance, 0, 0);
            context.pipe.accept(rOffset, rControl);

            if (rControl.stop) return;
            rOffset.set(-distance, 0, 0);
            context.pipe.accept(rOffset, rControl);

            if (rControl.stop) return;
            rOffset.set(0, 0, distance);
            context.pipe.accept(rOffset, rControl);

            if (rControl.stop) return;
            rOffset.set(0, 0, -distance);
            context.pipe.accept(rOffset, rControl);
        }
    }
}
