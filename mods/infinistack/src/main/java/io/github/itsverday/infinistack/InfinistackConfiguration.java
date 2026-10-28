package io.github.itsverday.infinistack;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;

import java.util.HashMap;
import java.util.Map;

public class InfinistackConfiguration {
    public static final BuilderCodec<InfinistackConfiguration> CODEC = BuilderCodec.builder(
            InfinistackConfiguration.class, InfinistackConfiguration::new
    )
            .append(
                    new KeyedCodec<>("Enabled", Codec.BOOLEAN, false),
                    (config, field) -> config.enabled = field,
                    config -> config.enabled
            )
            .add()
            .append(
                    new KeyedCodec<>("ExcludeUnstackable", Codec.BOOLEAN, false),
                    (config, field) -> config.excludeUnstackable = field,
                    config -> config.excludeUnstackable
            )
            .add()
            .append(
                    new KeyedCodec<>("MaxStack", Codec.INTEGER, false),
                    (config, field) -> config.maxStack = field,
                    config -> config.maxStack
            )
            .add()
            .append(
                    new KeyedCodec<>("ItemIdOverrides", new MapCodec<>(Codec.INTEGER, HashMap::new), false),
                    (config, field) -> config.itemIdOverrides = field,
                    config -> config.itemIdOverrides
            )
            .add()
            .append(
                    new KeyedCodec<>("RegexOverrides", new MapCodec<>(Codec.INTEGER, HashMap::new), false),
                    (config, field) -> config.regexOverrides = field,
                    config -> config.regexOverrides
            )
            .add()
            .build();

    private boolean enabled = true;
    private boolean excludeUnstackable = true;
    private int maxStack = 999_999_999;
    private Map<String, Integer> itemIdOverrides = new HashMap<>();
    private Map<String, Integer> regexOverrides = new HashMap<>();

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isExcludeUnstackable() {
        return excludeUnstackable;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public Map<String, Integer> getItemIdOverrides() {
        return itemIdOverrides;
    }

    public Map<String, Integer> getRegexOverrides() {
        return regexOverrides;
    }
}
