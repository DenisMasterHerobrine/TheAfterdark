# Changelog

## 1.1.0.0.alpha.1
This update alters newly generated terrain can differ from older mod builds and existing chunks will stay as they are. Newer chunks may be altered a bit.

- Now dripstone-style pillars use one ConfigurableDripstoneStylePillarFeature and PillarPlacementRules instead of individual features for each pillar type and custom placement rules for each pillar type. This allows for more consistent pillar generation and easier addition of new pillar types in the future.
- Implemented optional datapack field `pillar_provider` as BlockStateProvider to override pillar column blocks for datapack developers.
- Lapis ore pillars now place lapis ore instead of redstone ore. Weird, right?
- Raw copper pillars now place raw copper blocks instead of raw gold blocks. Don't ask me why, I have no idea.
- Crystal spike, spiral, pond and anvil rocks now use worldgen random instead of Math.random for seed consistent new chunks.
- Coal ore pillar feature registered with sparse placement in deep_afterdark only. There were no coal pillars at all before lol.
- Added Lost Cities compatibility, so no more weird terrain generation inside LC features should spawn if placed in The Afterdark dimension, closes #4