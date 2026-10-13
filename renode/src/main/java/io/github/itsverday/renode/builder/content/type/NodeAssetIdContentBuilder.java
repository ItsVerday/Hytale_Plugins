package io.github.itsverday.renode.builder.content.type;

import io.github.itsverday.renode.builder.content.NodeContentBuilder;
import io.github.itsverday.renode.definition.content.NodeContentDefinition;
import io.github.itsverday.renode.definition.content.type.NodeAssetIdContentDefinition;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NodeAssetIdContentBuilder extends NodeContentBuilder<NodeAssetIdContentBuilder> {
    private String assetType;
    @Nullable
    private String defaultValue = null;
    @Nullable
    private Integer width = null;

    public String getAssetType() {
        return assetType;
    }

    public NodeAssetIdContentBuilder withAssetType(@Nonnull String assetType) {
        this.assetType = assetType;
        return this;
    }

    @Nullable
    public String getDefaultValue() {
        return defaultValue;
    }

    public NodeAssetIdContentBuilder withDefaultValue(@Nullable String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Nullable
    public Integer getWidth() {
        return width;
    }

    public NodeAssetIdContentBuilder withWidth(@Nullable Integer width) {
        this.width = width;
        return this;
    }

    @NullableDecl
    @Override
    public NodeContentDefinition buildContent(String contentId) {
        return new NodeAssetIdContentDefinition(contentId, getLabel(), getDescription(), getAssetType(), getDefaultValue(), getWidth());
    }
}
