package io.github.itsverday.infinistack;

import com.hypixel.hytale.assetstore.event.LoadedAssetsEvent;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class InfinistackPlugin extends JavaPlugin {
    public static HytaleLogger LOGGER;

    private final Config<InfinistackConfiguration> config;
    private Field maxStackField;

    public InfinistackPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER = getLogger();
        config = withConfig(InfinistackConfiguration.CODEC);
    }

    @Override
    protected void setup() {
        config.save().thenRun(() -> LOGGER.atInfo().log("Config file saved."));

        try {
            maxStackField = Item.class.getDeclaredField("maxStack");
            maxStackField.setAccessible(true);
        } catch (Exception e) {
            LOGGER.at(Level.SEVERE).withCause(e).log("Unable to setup maxStack overriding! Item stack sizes will not change!");
        }

        getEventRegistry().register(LoadedAssetsEvent.class, Item.class, this::loadedItems);
    }

    private void loadedItems(@Nonnull LoadedAssetsEvent<String, Item, DefaultAssetMap<String, Item>> event) {
        if (!config.get().isEnabled()) return;
        if (maxStackField == null) return;

        try {
            int modifiedCount = 0;
            for (String assetId : event.getLoadedAssets().keySet()) {
                Item asset = event.getLoadedAssets().get(assetId);
                int currentMaxStack = (int) maxStackField.get(asset);
                Integer newMaxStack = computeMaxStack(assetId, asset, currentMaxStack);
                if (newMaxStack != null && currentMaxStack != newMaxStack) {
                    maxStackField.set(asset, newMaxStack);
                    modifiedCount++;
                }
            }

            LOGGER.at(Level.INFO).log("Overrode Item stack size for %s Items!", modifiedCount);
        } catch (Exception e) {
            LOGGER.at(Level.WARNING).withCause(e).log("Exception occurred when overriding Item stack sizes!");
        }
    }

    private Integer computeMaxStack(String assetId, Item asset, int currentMaxStack) {
        if (config.get().isExcludeUnstackable()) {
            if (currentMaxStack == 1) return null;
            if (asset.getTool() != null) return null;
            if (asset.getWeapon() != null) return null;
            if (asset.getArmor() != null) return null;
            if (asset.getBuilderTool() != null) return null;
            if (asset.getBlockSelectorToolData() != null) return null;
        }

        for (Map.Entry<String, Integer> entry: config.get().getItemIdOverrides().entrySet()) {
            if (entry.getKey().equals(assetId)) return entry.getValue();
        }

        for (Map.Entry<String, Integer> entry: config.get().getRegexOverrides().entrySet()) {
            Pattern pattern = Pattern.compile(entry.getKey());
            if (pattern.matcher(assetId).matches()) return entry.getValue();
        }

        return config.get().getMaxStack();
    }
}
