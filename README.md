# The Afterdark Project

A dimension Minecraft mod related to the afterlife, the dark, and the unknown.

[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1110531?style=for-the-badge&logo=curseforge&logoColor=%230d0d0d&labelColor=%23f16436&color=%230d0d0d)](https://www.curseforge.com/minecraft/mc-mods/the-afterdark)

## Configuration

Configuration values can be changed using the `config/afterdark.json` file:

- `catalystSpawnChance`: The chance (as a float) for a catalyst to spawn.
- `shouldSpawnCatalyst`: Whether catalysts should spawn in lootTables.
- `lootTables`: An array of loot table paths where catalysts can be found.
- `canReturnWithoutCatalyst`: Whether players can return without a catalyst.
- `shouldGrassBurn`: Whether grass blocks should burn.
- `GrassBlocks`: A list of grass block types that can burn.
- `SafeTeleportCheckRadius`: The radius (in blocks) to check for safe teleportation.
- `TeleportCatalystUses`: The number of uses a teleport catalyst has.
- `shouldTeleportReturnToSetWorld`: Whether back teleportation should return to a set world.
- `returnSetWorld`: The world to return to when teleporting back.

***Note:*** Most of these values (actually all), are require to ***full restart*** of the game/server to take effect.