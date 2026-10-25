package io.github.itsverday.renode.definition.content.type;

import io.github.itsverday.renode.definition.content.NodeContentDefinition;
import org.bson.BsonDocument;
import org.bson.BsonString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NodeCurveEditorContentDefinition extends NodeContentDefinition {
    @Nonnull
    private final String pointsPin;
    @Nonnull
    private final String pointNodeType;
    @Nonnull
    private final String xField;
    @Nonnull
    private final String yField;

    public NodeCurveEditorContentDefinition(String id, String label, @Nullable String description, @Nonnull String pointsPin, @Nonnull String pointNodeType, @Nonnull String xField, @Nonnull String yField) {
        super(id, "CurveEditor", label, description);
        this.pointsPin = pointsPin;
        this.pointNodeType = pointNodeType;
        this.xField = xField;
        this.yField = yField;
    }

    @Nonnull
    public String getPointsPin() {
        return pointsPin;
    }

    @Nonnull
    public String getPointNodeType() {
        return pointNodeType;
    }

    @Nonnull
    public String getXField() {
        return xField;
    }

    @Nonnull
    public String getYField() {
        return yField;
    }

    @Override
    public BsonDocument encodeOptions() {
        BsonDocument document = new BsonDocument();
        document.put("PointsPin", new BsonString(getPointsPin()));
        document.put("PointNodeType", new BsonString(getPointNodeType()));
        document.put("XField", new BsonString(getXField()));
        document.put("YField", new BsonString(getYField()));
        return document;
    }
}
