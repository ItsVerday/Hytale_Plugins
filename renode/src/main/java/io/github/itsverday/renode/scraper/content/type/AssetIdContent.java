package io.github.itsverday.renode.scraper.content.type;

import io.github.itsverday.renode.scraper.codegen.CodeGeneratorUtils;
import io.github.itsverday.renode.scraper.content.Content;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AssetIdContent extends Content {
    @Nonnull
    private final String assetType;
    @Nullable
    private final String defaultValue;
    @Nullable
    private final Integer width;

    public AssetIdContent(String id, String label, @Nullable String description, @Nonnull String assetType, @Nullable String defaultValue, @Nullable Integer width) {
        super(id, label, description);
        this.assetType = assetType;
        this.defaultValue = defaultValue;
        this.width = width;
    }

    @Override
    public StringBuilder generateCode() {
        StringBuilder stringBuilder = new StringBuilder();
        CodeGeneratorUtils.writeFunctionCall(stringBuilder, "Renode.assetIdContent", getSchemaId(), getLabel(), assetType);
        CodeGeneratorUtils.writeOptionalFunctionCall(stringBuilder, ".withDefaultValue", defaultValue);
        CodeGeneratorUtils.writeOptionalFunctionCall(stringBuilder, ".withWidth", width);
        CodeGeneratorUtils.writeOptionalFunctionCall(stringBuilder, ".withDescription", getDescription());
        return stringBuilder;
    }
}
