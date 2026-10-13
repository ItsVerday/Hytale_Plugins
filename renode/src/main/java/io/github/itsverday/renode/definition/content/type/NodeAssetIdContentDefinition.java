package io.github.itsverday.renode.definition.content.type;

import io.github.itsverday.renode.definition.content.NodeContentDefinition;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NodeAssetIdContentDefinition extends NodeContentDefinition {
    @Nonnull
    private final String assetType;
    @Nullable
    private final String defaultValue;
    @Nullable
    private final Integer width;

    public NodeAssetIdContentDefinition(String id, String label, @Nullable String description, @Nonnull String assetType, @Nullable String defaultValue, @Nullable Integer width) {
        super(id, "AssetId", label, description);
        this.assetType = assetType;
        this.defaultValue = defaultValue;
        this.width = width;
    }

    @Nonnull
    public String getAssetType() {
        return assetType;
    }

    @Nullable
    public String getDefaultValue() {
        return defaultValue;
    }

    @Nullable
    public Integer getWidth() {
        return width;
    }

    @Override
    public BsonDocument encodeOptions() {
        BsonDocument document = new BsonDocument();
        document.put("AssetType", new BsonString(getAssetType()));
        if (getDefaultValue() != null) document.put("Default", new BsonString(getDefaultValue()));
        if (getWidth() != null) document.put("Width", new BsonInt32(getWidth()));
        return document;
    }
}
