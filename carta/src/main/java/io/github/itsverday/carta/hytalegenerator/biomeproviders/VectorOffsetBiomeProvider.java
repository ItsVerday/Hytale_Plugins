package io.github.itsverday.carta.hytalegenerator.biomeproviders;

import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.VectorProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector3d;

import javax.annotation.Nonnull;
import java.util.List;

public class VectorOffsetBiomeProvider extends BiomeProvider {
    @Nonnull
    private final BiomeProvider input;
    @Nonnull
    private final VectorProvider offset;
    private final boolean rounding;

    private final Vector3d rPosition = new Vector3d();
    private final Vector3d rOffset = new Vector3d();
    private final VectorProvider.Context rVectorContext = new VectorProvider.Context();
    private final BiomeProvider.Context rContext = new Context();

    public VectorOffsetBiomeProvider(@Nonnull BiomeProvider input, @Nonnull VectorProvider offset, boolean rounding) {
        this.input = input;
        this.offset = offset;
        this.rounding = rounding;

        rVectorContext.anchor = new Vector3d();
    }

    @Override
    public int process(@NonNullDecl Context context) {
        rPosition.set(context.position);

        rVectorContext.anchor.set(context.anchor);
        rVectorContext.position.set(rPosition);
        offset.evaluate(rVectorContext, rOffset);

        rPosition.add(rOffset);
        if (rounding) rPosition.floor();

        rContext.assign(context);
        rContext.position = rPosition;
        return input.process(rContext);
    }

    @Override
    public List<Integer> allPossibleValues() {
        return input.allPossibleValues();
    }
}
