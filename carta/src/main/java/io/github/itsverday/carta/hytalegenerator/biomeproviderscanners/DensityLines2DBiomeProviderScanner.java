package io.github.itsverday.carta.hytalegenerator.biomeproviderscanners;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.math.Normalizer;
import com.hypixel.hytale.builtin.hytalegenerator.pipe.Control;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector3d;

import javax.annotation.Nonnull;

public class DensityLines2DBiomeProviderScanner extends BiomeProviderScanner {
    @Nonnull
    private final Density radius;
    private final double radiusMin;
    private final double radiusMax;

    @Nonnull
    private final Density.Context rDensityContext = new Density.Context();
    @Nonnull
    private final Vector3d rOffset = new Vector3d();
    @Nonnull
    private final Control rControl = new Control();

    public DensityLines2DBiomeProviderScanner(@Nonnull Density radius, double radiusMin, double radiusMax) {
        this.radius = radius;
        this.radiusMin = radiusMin;
        this.radiusMax = radiusMax;
    }

    @Override
    public void generate(@NonNullDecl Context context) {
        rDensityContext.position = context.origin;
        double radiusDouble = Normalizer.normalize(-1, 1, radiusMin, radiusMax, radius.evaluate(rDensityContext));
        int radius = (int) Math.floor(radiusDouble);

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
