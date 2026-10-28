# Infinistack
Are you tired of running out of inventory space during adventures, or of your chest storage overflowing with the same common items?
Infinistack is a Hytale Plugin to easily override or remove the stack size limits on items.
By default, it makes all items stackable up to 1 billion, excluding any unstackable items (such as Tools/Weapons/Armor).
This behavior can be customized, including changing the default stack size and including typically unstackable items.
Additionally, individual items can have their stack sizes overridden, including with regex-based matching.

## Configuration
If you are looking to just increase item stack sizes in a sensible way, you do not need to touch the configuration file.
For more fine-grained control, you can edit the configuration in your world's mods folder, under `Verday_Infinistack`.
The configuration options are as follows:
- `Enabled` (boolean): Whether to enable Infinistack, `true` or `false`.
- `ExcludeUnstackable` (boolean): Whether to ignore items that are typically unstackable. If `true`, these items will *not* have their maximum stack sizes changed. If *false*, these items will be treated like all other items.
- `MaxStack` (integer): The default maximum stack size to use. Defaults to `999999999` (1 less than 1 billion).
- `ItemIdOverrides` (Map<String, Integer>): Specific item maximum stack size overrides to apply. You must use the exact asset ID for the item, case-sensitive.
- `RegexOverrides` (Map<String, Integer>): Regex-based item maximum stack size overrides to apply. The regex is matched against the item's asset ID, and if they match, the override is applied.