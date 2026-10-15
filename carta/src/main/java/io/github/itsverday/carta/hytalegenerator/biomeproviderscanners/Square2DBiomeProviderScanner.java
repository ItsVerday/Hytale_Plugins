package io.github.itsverday.carta.hytalegenerator.biomeproviderscanners;

import com.hypixel.hytale.builtin.hytalegenerator.pipe.Control;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector3d;

import javax.annotation.Nonnull;

public class Square2DBiomeProviderScanner extends BiomeProviderScanner {
    private final int radius;
    @Nonnull
    private final Vector3d rOffset = new Vector3d();
    @Nonnull
    private final Control rControl = new Control();

    public Square2DBiomeProviderScanner(double radius) {
        this.radius = (int) Math.floor(radius);
    }

    @Override
    public void generate(@NonNullDecl Context context) {
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
