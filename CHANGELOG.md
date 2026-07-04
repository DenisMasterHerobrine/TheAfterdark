# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [1.1.0] - 2026-07-04

This is a large world generation and exploration update for The Afterdark. It adds new biomes, richer cave events, stronger enemies, better mod compatibility. Newly generated chunks will use the new terrain, biome placement, decorations, ores, and structures. Existing chunks stay unchanged.

### Added

- Added the Black Honey Grove biome, a dense organic cave biome with eerie atmosphere, resin falls, hive cysts, root arches, hanging hives and many more stuff around bees!
- Added the Silent Reservoirs biome, expanding The Afterdark with a colder, quieter water-focused cave region.
- Added Copper Necropolis gothic ruins, with different procedural variants inspired by copper gothic towers, chapels, arch halls, gates, and broken spires.
- Added new Copper Necropolis biome, a bit rare and quite small biome that includes gothic ruins, stone ribs, light veins, dark tar, water falls, and a more varied stone-and-copper surface palette.
- Added new cave ambience events for The Afterdark, including whispers, hunger, darkness, footsteps, heartbeat and many more creepy events. Never know what you will find out next in the dark!
- Now mobs in The Afterdark will have different buffs and equipment in different biomes based on difficulty, distance from spawn and other parameters.
- Added Harder Mobs armor blacklist configuration, allowing modpack authors to block specific armor items or armor tags from appearing on upgraded Afterdark mobs.
- Added compatibility biome tags for common mod loaders and datapacks, including `minecraft:is_overworld`, `c:is_overworld`, `c:is_cave`, `c:is_underground`, `forge:is_overworld`, `forge:is_cave`, and `forge:is_underground`.
- Added climate and theme biome tags for The Afterdark biomes, including hot, cold, wet, dry, lush, spooky, magical, mountain, aquatic, and snowy categories, with matching `c:` and `forge:` forwarding tags.
- Added vanilla structure biome hooks so mineshafts can generate across Afterdark biomes and ancient cities can generate in Deep Afterdark.
- Added richer extra loot for abandoned mineshaft chests when they generate inside The Afterdark.
- Added biome-specific decoration passes for Creepy Snowfields, Dripstone Deltas, Lush Gardens, Calcite Fields, Deep Afterdark, Copper Necropolis, Prismarine biomes, Dying Drylands, Silent Reservoirs, and Black Honey Grove.
- Added new lighting accents across the dimension such as ceiling lanterns, water glow nodes, crystals and so on.
- Added new terrain and decoration features such as dark tar, stone ribs, light veins, black honey vegetation, black honey structures, dripstone delta shelves, reservoir stone ribs, underwater packed mud, and prismarine peak ribs.
- Added English, Russian, and Korean localization entries for the new dimension content, biomes, cave events, and configuration-facing names.
- Added Gradle worldgen maintenance tasks, `fixWorldgenData` and `checkWorldgenData`, and wired validation into common, Fabric, and Forge resource processing.

### Changed

- Reworked The Afterdark biome source to use a broader and more balanced multi-noise climate layout.
- Rebalanced biome placement so Copper Necropolis, Deep Afterdark, warm biomes, cold biomes, wet biomes, and dry biomes appear more evenly instead of letting a few biomes dominate the dimension.
- Increased biome climate noise frequency for temperature and vegetation, making Afterdark biomes appear roughly three times more often and reducing long same-biome travel stretches.
- Rebalanced placed-feature counts and height ranges across The Afterdark so more features can use the full dimension height span.
- Raised dimension ambient light from `0.0` to `0.1` for slightly better baseline visibility.
- Expanded existing biome decoration with glow, vegetation, ponds, shelves, ice, calcite, basalt, moss, spores, water features, ribs, veins, and tar features.
- Reworked many placed features to use more consistent placement, rarity, count, height, and environment-scan rules.
- Reduced Calcite Fields calcite patch thickness and removed extra edge and bottom patch chance.
- Updated teleport altar structure placement and template pool data.
- Updated Fabric and Forge registration code for the new features, loot hooks, cave events, harder mobs, and worldgen registrations.
- Updated generated Fabric and Forge dimension type data for the current Afterdark dimension settings.

### Fixed

- Fixed waterloggable random patches so they can place in air or water, preserve waterlogged states, and report success only when a block is actually placed.
- Fixed `supported_disk` worldgen JSON to use the expected rule-provider shape with `fallback` and `rules`.
- Fixed the dirt dripleaf pool provider so it no longer uses grass blocks as one of its filler states.
- Fixed raw copper pillars placing the wrong raw block.
- Fixed lapis ore pillars placing the wrong ore block.
- Fixed coal ore pillars not being registered correctly for sparse placement in Deep Afterdark.
- Fixed several worldgen references, ordering issues, and placement definitions that could make datapack validation or feature generation fragile.
- Added build-time checks for missing feature references, invalid `environment_scan` ranges, unsupported `supported_disk` providers, and biome feature-order cycles.

### Compatibility

- Improved compatibility with ore, structure, and biome-aware mods by marking Afterdark biomes with broader common biome tags.
- Improved compatibility with mods that use cave, underground, overworld, climate, or theme tags to decide where ores, structures, mobs, or decorations can appear.
- Improved Lost Cities compatibility so its section generation is bounded more safely when structures intersect The Afterdark.
- Allowed vanilla mineshafts to target Afterdark biomes, making structure-based mod integrations more likely to work without manual configuration.
- Allowed ancient cities to target Deep Afterdark.
- Added extra mineshaft loot only when the chest is generated inside The Afterdark, avoiding unwanted loot changes in normal Overworld mineshafts.

### Developer Notes

- This update changes world generation heavily. Only newly generated chunks will receive the new biome distribution, decorations, structures, cave features, and Copper Necropolis ruins.
- Existing chunks from older versions will remain as they are.
- Datapack authors can override the new loot table, biome tags, placed features, configured features, and biome definitions as normal data files.

## [1.1.0.0.alpha.3] - 2026-05-18

This alpha has been rolled into the full `1.1.0` changelog above.

## [1.1.0.0.alpha.1] - 2026-03-28

This alpha has been rolled into the full `1.1.0` changelog above.
