package io.github.itsverday.carta.hytalegenerator.biomeproviderscanners;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.math.Normalizer;
import com.hypixel.hytale.builtin.hytalegenerator.pipe.Control;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector3d;

import javax.annotation.Nonnull;

public class DensitySquare2DBiomeProviderScanner extends BiomeProviderScanner {
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

    public DensitySquare2DBiomeProviderScanner(@Nonnull Density radius, double radiusMin, double radiusMax) {
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

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (rControl.stop) return;

                rOffset.set(x, 0, z);
                context.pipe.accept(rOffset, rControl);
            }
        }
    }
}
