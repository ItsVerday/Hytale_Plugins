package io.github.itsverday.renode.builder;

import io.github.itsverday.renode.RenodePlugin;
import io.github.itsverday.renode.builder.content.type.*;
import io.github.itsverday.renode.builder.root.AbstractNodeRoot;
import io.github.itsverday.renode.builder.root.SingleNodeRoot;
import io.github.itsverday.renode.builder.root.VariantNodeRoot;
import io.github.itsverday.renode.export.WorkspaceExporter;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class Renode {
    public static NodeBuilder node(String id, String title) {
        return new NodeBuilder().withId(id).withTitle(title).addSelfInput();
    }

    public static NodeCategory category(String title, @Nullable String color) {
        return new NodeCategory(title, color);
    }

    public static NodeCategory category(String title) {
        return category(title, null);
    }

    public static NodeVariantClass variant(String name, @Nullable String color) {
        return new NodeVariantClass(name, color);
    }

    public static NodeVariantClass variant(String name) {
        return variant(name, null);
    }

    public static SingleNodeRoot root(NodeBuilder node, String name) {
        return new SingleNodeRoot(node, name);
    }

    public static VariantNodeRoot root(NodeVariantClass variant, String name) {
        return new VariantNodeRoot(variant, name);
    }

    public static NodeAssetIdContentBuilder assetIdContent(String id, String label, String assetType) {
        return new NodeAssetIdContentBuilder().withId(id).withLabel(label).withAssetType(assetType);
    }

    public static NodeCheckboxContentBuilder checkboxContent(String id, String label) {
        return new NodeCheckboxContentBuilder().withId(id).withLabel(label);
    }

    public static NodeCurveEditorContentBuilder curveEditorContent(String id, String label, String pointsPin, NodeBuilder pointNodeType, String xField, String yField) {
        return new NodeCurveEditorContentBuilder().withId(id).withLabel(label).withPointsPin(pointsPin).withPointNodeType(pointNodeType).withXField(xField).withYField(yField);
    }

    public static NodeCurveEditorContentBuilder curveEditorContent(String id, String label, String pointsPin, Supplier<NodeBuilder> pointNodeType, String xField, String yField) {
        return new NodeCurveEditorContentBuilder().withId(id).withLabel(label).withPointsPin(pointsPin).withPointNodeType(pointNodeType).withXField(xField).withYField(yField);
    }

    public static NodeEnumContentBuilder enumContent(String id, String label) {
        return new NodeEnumContentBuilder().withId(id).withLabel(label);
    }

    public static NodeFloatContentBuilder floatContent(String id, String label) {
        return new NodeFloatContentBuilder().withId(id).withLabel(label);
    }

    public static NodeIntegerContentBuilder integerContent(String id, String label) {
        return new NodeIntegerContentBuilder().withId(id).withLabel(label);
    }

    public static NodeIntegerSliderContentBuilder integerSliderContent(String id, String label, int min, int max, int tickFrequency) {
        return new NodeIntegerSliderContentBuilder().withId(id).withLabel(label).withMin(min).withMax(max).withTickFrequency(tickFrequency);
    }

    public static NodeListContentBuilder listContent(String id, String label, String listType) {
        return new NodeListContentBuilder().withId(id).withLabel(label).withListType(listType);
    }

    public static NodeObjectContentBuilder objectContent(String id, String label) {
        return new NodeObjectContentBuilder().withId(id).withLabel(label);
    }

    public static NodeSmallStringContentBuilder smallStringContent(String id, String label) {
        return new NodeSmallStringContentBuilder().withId(id).withLabel(label);
    }

    public static NodeStringContentBuilder stringContent(String id, String label) {
        return new NodeStringContentBuilder().withId(id).withLabel(label);
    }

    public static void registerNode(NodeBuilder node) {
        getRegistry().registerNode(node);
    }

    public static void registerVariant(NodeVariantClass variant) {
        getRegistry().registerVariant(variant);
    }

    public static void registerCategory(NodeCategory category) {
        getRegistry().registerCategory(category);
    }

    public static void registerRoot(AbstractNodeRoot root) {
        getRegistry().registerRoot(root);
    }

    public static void registerExporter(WorkspaceExporter exporter) {
        for (WorkspaceExporter other: RenodePlugin.getInstance().getExporters()) {
            if (exporter.equals(other)) {
                return;
            }
        }

        RenodePlugin.getInstance().getExporters().add(exporter);
    }

    private static NodeRegistry getRegistry() {
        return RenodePlugin.getInstance().getRegistry();
    }
}
