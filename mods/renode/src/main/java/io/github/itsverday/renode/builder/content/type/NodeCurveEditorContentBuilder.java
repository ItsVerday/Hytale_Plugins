package io.github.itsverday.renode.builder.content.type;

import io.github.itsverday.renode.builder.NodeBuilder;
import io.github.itsverday.renode.builder.content.NodeContentBuilder;
import io.github.itsverday.renode.definition.content.NodeContentDefinition;
import io.github.itsverday.renode.definition.content.type.NodeCurveEditorContentDefinition;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class NodeCurveEditorContentBuilder extends NodeContentBuilder<NodeCurveEditorContentBuilder> {
    private String pointsPin;
    private Supplier<NodeBuilder> pointNodeType;
    private String xField;
    private String yField;

    public String getPointsPin() {
        return pointsPin;
    }

    public NodeCurveEditorContentBuilder withPointsPin(@Nonnull String pointsPin) {
        this.pointsPin = pointsPin;
        return this;
    }

    public Supplier<NodeBuilder> getPointNodeType() {
        return pointNodeType;
    }

    public NodeCurveEditorContentBuilder withPointNodeType(@Nonnull NodeBuilder pointNodeType) {
        return withPointNodeType(() -> pointNodeType);
    }

    public NodeCurveEditorContentBuilder withPointNodeType(@Nonnull Supplier<NodeBuilder> pointNodeType) {
        this.pointNodeType = pointNodeType;
        return this;
    }

    public String getXField() {
        return xField;
    }

    public NodeCurveEditorContentBuilder withXField(@Nonnull String xField) {
        this.xField = xField;
        return this;
    }

    public String getYField() {
        return yField;
    }

    public NodeCurveEditorContentBuilder withYField(@Nonnull String yField) {
        this.yField = yField;
        return this;
    }

    @NullableDecl
    @Override
    public NodeContentDefinition buildContent(String contentId) {
        return new NodeCurveEditorContentDefinition(contentId, getLabel(), getDescription(), getPointsPin(), getPointNodeType().get().getId(), getXField(), getYField());
    }
}
