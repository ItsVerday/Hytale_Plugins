package io.github.itsverday.renode.scraper.content.type;

import io.github.itsverday.renode.scraper.codegen.CodeGeneratorUtils;
import io.github.itsverday.renode.scraper.content.Content;
import io.github.itsverday.renode.scraper.node.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CurveEditorContent extends Content {
    @Nonnull
    private final String pointsPin;
    @Nonnull
    private final String pointNodeType;
    @Nonnull
    private final String xField;
    @Nonnull
    private final String yField;

    private Node pointNodeTypeResolved;

    public CurveEditorContent(String id, String label, @Nullable String description, @Nonnull String pointsPin, @Nonnull String pointNodeType, @Nonnull String xField, @Nonnull String yField) {
        super(id, label, description);
        this.pointsPin = pointsPin;
        this.pointNodeType = pointNodeType;
        this.xField = xField;
        this.yField = yField;
    }

    @Nonnull
    public String getPointNodeType() {
        return pointNodeType;
    }

    public void setPointNodeTypeResolved(Node pointNodeTypeResolved) {
        this.pointNodeTypeResolved = pointNodeTypeResolved;
    }

    @Override
    public StringBuilder generateCode() {
        StringBuilder stringBuilder = new StringBuilder();
        CodeGeneratorUtils.writeFunctionCall(stringBuilder, "Renode.curveEditorContent", getSchemaId(), getLabel(), pointsPin, pointNodeTypeResolved, xField, yField);
        CodeGeneratorUtils.writeOptionalFunctionCall(stringBuilder, ".withDescription", getDescription());
        return stringBuilder;
    }
}
