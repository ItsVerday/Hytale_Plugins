package io.github.itsverday.carta;

import com.hypixel.hytale.assetstore.AssetMap;
import com.hypixel.hytale.assetstore.AssetPack;
import com.hypixel.hytale.assetstore.JsonAsset;
import com.hypixel.hytale.assetstore.codec.AssetCodec;
import com.hypixel.hytale.assetstore.event.LoadedAssetsEvent;
import com.hypixel.hytale.assetstore.event.RemovedAssetsEvent;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.builtin.hytalegenerator.assets.AssetManager;
import com.hypixel.hytale.builtin.hytalegenerator.assets.worldstructures.WorldStructureAsset;
import com.hypixel.hytale.builtin.hytalegenerator.plugin.HytaleGenerator;
import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.common.semver.SemverRange;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.packets.interface_.NotificationStyle;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.*;
import com.hypixel.hytale.server.core.asset.monitor.AssetMonitor;
import com.hypixel.hytale.server.core.asset.monitor.AssetMonitorHandler;
import com.hypixel.hytale.server.core.asset.monitor.EventKind;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import com.hypixel.hytale.server.core.util.NotificationUtil;
import io.github.itsverday.carta.hytalegenerator.assets.biomeproviderconditions.*;
import io.github.itsverday.carta.hytalegenerator.assets.biomeproviders.*;
import io.github.itsverday.carta.hytalegenerator.assets.biomeproviders.positioncellscelltypes.PositionCellsBiomeProviderCellTypeAsset;
import io.github.itsverday.carta.hytalegenerator.assets.biomeproviders.positioncellscelltypes.DistanceDelimitedPositionCellsBiomeProviderCellTypeAsset;
import io.github.itsverday.carta.hytalegenerator.assets.biomeproviders.positioncellscelltypes.FillPositionCellsBiomeProviderCellTypeAsset;
import io.github.itsverday.carta.hytalegenerator.assets.biomeproviders.positioncellscelltypes.OriginPositionCellsBiomeProviderCellTypeAsset;
import io.github.itsverday.carta.hytalegenerator.assets.biomeproviderscanners.*;
import io.github.itsverday.carta.hytalegenerator.assets.worldstructures.CartaWorldStructureAsset;
import io.github.itsverday.carta.hytalegenerator.biomeproviderconditions.BiomeProviderCondition;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;

public class CartaPlugin extends JavaPlugin {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private HytaleGenerator hytaleGeneratorPlugin;
    private AssetManager hytaleGeneratorAssetManager;
    private Method hytaleGeneratorAssetManagerTriggerReloadListeners;
    private DefaultAssetMap<String, WorldStructureAsset> worldStructureAssets = null;

    public CartaPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        hytaleGeneratorPlugin = (HytaleGenerator) PluginManager.get().getPlugin(PluginIdentifier.fromString("Hytale:HytaleGenerator"));
        try {
            Field hytaleGeneratorAssetManagerField = HytaleGenerator.class.getDeclaredField("assetManager");
            hytaleGeneratorAssetManagerField.setAccessible(true);
            hytaleGeneratorAssetManager = (AssetManager) hytaleGeneratorAssetManagerField.get(hytaleGeneratorPlugin);
            hytaleGeneratorAssetManagerTriggerReloadListeners = AssetManager.class.getDeclaredMethod("triggerReloadListeners");
            hytaleGeneratorAssetManagerTriggerReloadListeners.setAccessible(true);

            hytaleGeneratorAssetManager.registerReloadListener(() -> {
                if (worldStructureAssets != null) {
                    clearCachedCartaWorldStructures(worldStructureAssets);
                }
            });
        } catch (Exception e) {
            LOGGER.atWarning().withCause(e).log("Unable to setup hot reloading!");
        }

        getCodecRegistry(WorldStructureAsset.CODEC)
                .register("Carta", CartaWorldStructureAsset.class, CartaWorldStructureAsset.CODEC);

        getCodecRegistry(BiomeProviderAsset.CODEC)
                .register("Anchor", AnchorBiomeProviderAsset.class, AnchorBiomeProviderAsset.CODEC)
                .register("Cached", CachedBiomeProviderAsset.class, CachedBiomeProviderAsset.CODEC)
                .register("Conditional", ConditionalBiomeProviderAsset.class, ConditionalBiomeProviderAsset.CODEC)
                .register("Constant", ConstantBiomeProviderAsset.class, ConstantBiomeProviderAsset.CODEC)
                .register("FieldFunction", FieldFunctionBiomeProviderAsset.class, FieldFunctionBiomeProviderAsset.CODEC)
                .register("GradientWarp", GradientWarpBiomeProviderAsset.class, GradientWarpBiomeProviderAsset.CODEC)
                .register("ImageMap", ImageMapBiomeProviderAsset.class, ImageMapBiomeProviderAsset.CODEC)
                .register("Imported", ImportedBiomeProviderAsset.class, ImportedBiomeProviderAsset.CODEC)
                .register("Offset", OffsetBiomeProviderAsset.class, OffsetBiomeProviderAsset.CODEC)
                .register("PositionCells", PositionCellsBiomeProviderAsset.class, PositionCellsBiomeProviderAsset.CODEC)
                .register("PositionsPinch", PositionsPinchBiomeProviderAsset.class, PositionsPinchBiomeProviderAsset.CODEC)
                .register("PositionsTwist", PositionsTwistBiomeProviderAsset.class, PositionsTwistBiomeProviderAsset.CODEC)
                .register("Previous", PreviousBiomeProviderAsset.class, PreviousBiomeProviderAsset.CODEC)
                .register("Scaler", ScalerBiomeProviderAsset.class, ScalerBiomeProviderAsset.CODEC)
                .register("Smoothed", SmoothedBiomeProviderAsset.class, SmoothedBiomeProviderAsset.CODEC)
                .register("Staged", StagedBiomeProviderAsset.class, StagedBiomeProviderAsset.CODEC)
                .register("Switch", SwitchBiomeProviderAsset.class, SwitchBiomeProviderAsset.CODEC)
                .register("VectorOffset", VectorOffsetBiomeProviderAsset.class, VectorOffsetBiomeProviderAsset.CODEC)
                .register("VectorWarp", VectorWarpBiomeProviderAsset.class, VectorWarpBiomeProviderAsset.CODEC)
                .register("Weighted", WeightedBiomeProviderAsset.class, WeightedBiomeProviderAsset.CODEC);

        getCodecRegistry(BiomeProviderConditionAsset.CODEC)
                .register("And", AndBiomeProviderConditionAsset.class, AndBiomeProviderConditionAsset.CODEC)
                .register("FieldFunction", FieldFunctionBiomeProviderConditionAsset.class, FieldFunctionBiomeProviderConditionAsset.CODEC)
                .register("Imported", ImportedBiomeProviderConditionAsset.class, ImportedBiomeProviderConditionAsset.CODEC)
                .register("Not", NotBiomeProviderConditionAsset.class, NotBiomeProviderConditionAsset.CODEC)
                .register("Offset", OffsetBiomeProviderConditionAsset.class, OffsetBiomeProviderConditionAsset.CODEC)
                .register("Or", OrBiomeProviderConditionAsset.class, OrBiomeProviderConditionAsset.CODEC)
                .register("Scanner", ScannerBiomeProviderConditionAsset.class, ScannerBiomeProviderConditionAsset.CODEC)
                .register("SumRange", SumRangeBiomeProviderConditionAsset.class, SumRangeBiomeProviderConditionAsset.CODEC)
                .register("Value", ValueBiomeProviderConditionAsset.class, ValueBiomeProviderConditionAsset.CODEC)
                .register("Values", ValuesBiomeProviderConditionAsset.class, ValuesBiomeProviderConditionAsset.CODEC);

        getCodecRegistry(BiomeProviderScannerAsset.CODEC)
                .register("Adjacent2D", Adjacent2DBiomeProviderScannerAsset.class, Adjacent2DBiomeProviderScannerAsset.CODEC)
                .register("DensityLines2D", DensityLines2DBiomeProviderScannerAsset.class, DensityLines2DBiomeProviderScannerAsset.CODEC)
                .register("DensityRound2D", DensityRound2DBiomeProviderScannerAsset.class, DensityRound2DBiomeProviderScannerAsset.CODEC)
                .register("DensitySquare2D", DensitySquare2DBiomeProviderScannerAsset.class, DensitySquare2DBiomeProviderScannerAsset.CODEC)
                .register("Imported", ImportedBiomeProviderScannerAsset.class, ImportedBiomeProviderScannerAsset.CODEC)
                .register("Lines2D", Lines2DBiomeProviderScannerAsset.class, Lines2DBiomeProviderScannerAsset.CODEC)
                .register("Offset", OffsetBiomeProviderScannerAsset.class, OffsetBiomeProviderScannerAsset.CODEC)
                .register("Origin", OriginBiomeProviderScannerAsset.class, OriginBiomeProviderScannerAsset.CODEC)
                .register("Round2D", Round2DBiomeProviderScannerAsset.class, Round2DBiomeProviderScannerAsset.CODEC)
                .register("Square2D", Square2DBiomeProviderScannerAsset.class, Square2DBiomeProviderScannerAsset.CODEC)
                .register("Union", UnionBiomeProviderScannerAsset.class, UnionBiomeProviderScannerAsset.CODEC);

        getCodecRegistry(PositionCellsBiomeProviderCellTypeAsset.CODEC)
                .register("DistanceDelimited", DistanceDelimitedPositionCellsBiomeProviderCellTypeAsset.class, DistanceDelimitedPositionCellsBiomeProviderCellTypeAsset.CODEC)
                .register("Fill", FillPositionCellsBiomeProviderCellTypeAsset.class, FillPositionCellsBiomeProviderCellTypeAsset.CODEC)
                .register("Origin", OriginPositionCellsBiomeProviderCellTypeAsset.class, OriginPositionCellsBiomeProviderCellTypeAsset.CODEC);

        registerAssetStore(BiomeProviderAsset.class, "HytaleGenerator/BiomeProviders", BiomeProviderAsset::getId, BiomeProviderAsset.CODEC);
        registerAssetStore(BiomeProviderConditionAsset.class, "HytaleGenerator/BiomeProviderConditions", BiomeProviderConditionAsset::getId, BiomeProviderConditionAsset.CODEC);
        registerAssetStore(BiomeProviderScannerAsset.class, "HytaleGenerator/BiomeProviderScanners", BiomeProviderScannerAsset::getId, BiomeProviderScannerAsset.CODEC);

        getEventRegistry().register(LoadedAssetsEvent.class, WorldStructureAsset.class, this::worldStructuresReloaded);
        getEventRegistry().register(LoadAssetEvent.class, event -> {
            for (AssetPack pack: AssetModule.get().getAssetPacks()) {
                onAssetPackLoaded(pack);
            }
        });
        getEventRegistry().register(AssetPackRegisterEvent.class, event -> {
            onAssetPackLoaded(event.getAssetPack());
        });

        if (PluginManager.get().hasPlugin(PluginIdentifier.fromString("Verday:Renode"), SemverRange.fromString("${RENODE_CHECK_VERSION}"))) {
            try {
                RenodeIntegration.registerAllNodes();
            } catch (Exception e) {
                LOGGER.atWarning().withCause(e).log("Unable to setup Renode integration!");
            }
        }
    }

    private void clearCachedCartaWorldStructures(DefaultAssetMap<String, WorldStructureAsset> worldStructureAssets) {
        for (String assetKey: worldStructureAssets.getAssetMap().keySet()) {
            WorldStructureAsset asset = worldStructureAssets.getAsset(assetKey);
            if (asset instanceof CartaWorldStructureAsset cartaWorldStructureAsset) {
                cartaWorldStructureAsset.clearBuilt();
            }
        }
    }

    private void worldStructuresReloaded(@Nonnull LoadedAssetsEvent<String, WorldStructureAsset, DefaultAssetMap<String, WorldStructureAsset>> event) {
        worldStructureAssets = event.getAssetMap();
    }

    private <T extends JsonAssetWithMap<String, M>, M extends AssetMap<String, T>> void registerAssetStore(Class<T> assetClass, String path, Function<T, String> keyFunction, AssetCodec<String, T> codec) {
        getAssetRegistry().register(
                HytaleAssetStore.builder(assetClass, (M) new DefaultAssetMap<String, T>())
                .setPath(path)
                .setKeyFunction(keyFunction)
                .setCodec(codec)
                .build()
        );

        getEventRegistry().register(LoadedAssetsEvent.class, assetClass, this::loadedAssets);
        getEventRegistry().register(RemovedAssetsEvent.class, assetClass, this::removedAssets);
    }

    private <T extends JsonAsset<String>> void loadedAssets(@Nonnull LoadedAssetsEvent<String, T, DefaultAssetMap<String, T>> event) {
        triggerReload();
    }

    private <T extends JsonAsset<String>> void removedAssets(@Nonnull RemovedAssetsEvent<String, T, DefaultAssetMap<String, T>> event) {
        triggerReload();
    }

    private void onAssetPackLoaded(AssetPack pack) {
        Path base = pack.getRoot();
        watchPathForReload(base.resolve("Server").resolve("HytaleGenerator").resolve("Images"), path -> new HytaleGeneratorImagesAssetMonitorHandler());
    }

    private void watchPathForReload(Path path, Function<Path, AssetMonitorHandler> assetMonitorHandlerProducer) {
        if (!Files.isDirectory(path)) return;

        AssetMonitor assetMonitor = AssetModule.get().getAssetMonitor();
        if (assetMonitor == null) return;

        assetMonitor.monitorDirectoryFiles(path, assetMonitorHandlerProducer.apply(path));
    }

    private void triggerReload() {
        if (hytaleGeneratorPlugin == null) return;

        try {
            hytaleGeneratorAssetManagerTriggerReloadListeners.invoke(hytaleGeneratorAssetManager);
        } catch (Exception e) {
            LOGGER.atWarning().withCause(e).log("Unable to hot reload!");
        }
    }

    private class HytaleGeneratorImagesAssetMonitorHandler implements AssetMonitorHandler {
        @Override
        public Object getKey() {
            return CartaPlugin.this;
        }

        @Override
        public boolean test(Path path, EventKind eventKind) {
            return !Files.isDirectory(path);
        }

        @Override
        public void accept(Map<Path, EventKind> map) {
            CartaPlugin.this.triggerReload();
            NotificationUtil.sendNotificationToUniverse(Message.translation("server.general.assetstore.reloadAssets").param("class", "HytaleGenerator/Images").color("#A7AfA7"), "Icons/AssetNotifications/AssetReloaded.png", NotificationStyle.Default);
        }
    }
}