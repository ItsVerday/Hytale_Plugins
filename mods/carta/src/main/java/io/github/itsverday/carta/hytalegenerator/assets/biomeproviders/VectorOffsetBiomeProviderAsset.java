package io.github.itsverday.carta.hytalegenerator.assets.biomeproviders;

import com.hypixel.hytale.builtin.hytalegenerator.assets.vectorproviders.VectorProviderAsset;
import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.VectorProvider;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.github.itsverday.carta.hytalegenerator.biomeproviders.BiomeProvider;
import io.github.itsverday.carta.hytalegenerator.biomeproviders.VectorOffsetBiomeProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class VectorOffsetBiomeProviderAsset extends BiomeProviderAsset {
    public static final BuilderCodec<VectorOffsetBiomeProviderAsset> CODEC = BuilderCodec.builder(
            VectorOffsetBiomeProviderAsset.class,
            VectorOffsetBiomeProviderAsset::new,
            BiomeProviderAsset.ABSTRACT_CODEC
    )
            .append(
                    new KeyedCodec<>("Input", BiomeProviderAsset.CODEC, false),
                    (asset, field) -> asset.inputAsset = field,
                    asset -> asset.inputAsset
            )
            .add()
            .append(
                    new KeyedCodec<>("Offset", VectorProviderAsset.CODEC, false),
                    (asset, field) -> asset.offsetAsset = field,
                    asset -> asset.offsetAsset
            )
            .add()
            .append(
                    new KeyedCodec<>("Rounding", Codec.BOOLEAN, true),
                    (asset, field) -> asset.rounding = field,
                    asset -> asset.rounding
            )
            .add()
            .build();

    private BiomeProviderAsset inputAsset = null;
    private VectorProviderAsset offsetAsset = null;
    private boolean rounding = false;

    @NullableDecl
    @Override
    public BiomeProvider build(@NonNullDecl Argument argument) {
        if (isSkipped()) return null;
        if (offsetAsset == null) return null;

        VectorProvider offset = offsetAsset.build(new VectorProviderAsset.Argument(argument.parentSeed, argument.referenceBundle, argument.workerId, argument.threadBridge));
        BiomeProvider input = BiomeProviderAsset.buildStatic(inputAsset, argument, rounding);

        return new VectorOffsetBiomeProvider(input, offset, rounding);
    }

    @Override
    public void cleanUp() {
        if (inputAsset != null) inputAsset.cleanUp();
        if (offsetAsset != null) offsetAsset.cleanUp();
    }
}
