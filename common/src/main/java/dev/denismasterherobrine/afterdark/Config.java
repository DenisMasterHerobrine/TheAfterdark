package dev.denismasterherobrine.afterdark;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.List;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("./config/afterdark.json");

    public static Config INSTANCE = new Config();

    public float catalystSpawnChance = 0.15F;
    public boolean shouldSpawnCatalyst = true;
    public String[] lootTables = {
            "minecraft:chests/abandoned_mineshaft",
            "minecraft:chests/ancient_city",
            "minecraft:chests/buried_treasure",
            "minecraft:chests/end_city_treasure",
            "minecraft:chests/ruined_portal",
            "minecraft:chests/nether_bridge"
    };
    public boolean canReturnWithoutCatalyst = true;
    public boolean shouldGrassBurn = false;
    public List<String> GrassBlocks = List.of(
            "minecraft:grass",
            "minecraft:tall_grass"
    );
    public int SafeTeleportCheckRadius = 20;
    public int TeleportCatalystUses = 5;
    public boolean shouldTeleportReturnToSetWorld = false;
    public String returnSetWorld = "minecraft:overworld";
    public boolean afterdarkEventsEnabled = true;
    public float afterdarkEventChancePerCheck = 0.35F;
    public HarderMobsConfig harderMobs = new HarderMobsConfig();
    @Deprecated
    public HarderMobsConfig adaptiveMobDesign = null;

    public void repairMissingFields() {
        if (lootTables == null) {
            lootTables = new String[]{
                    "minecraft:chests/abandoned_mineshaft",
                    "minecraft:chests/ancient_city",
                    "minecraft:chests/buried_treasure",
                    "minecraft:chests/end_city_treasure",
                    "minecraft:chests/ruined_portal",
                    "minecraft:chests/nether_bridge"
            };
        }

        if (GrassBlocks == null) {
            GrassBlocks = List.of(
                    "minecraft:grass",
                    "minecraft:tall_grass"
            );
        }

        if (harderMobs == null) {
            harderMobs = adaptiveMobDesign != null ? adaptiveMobDesign : new HarderMobsConfig();
        }

        harderMobs.repairMissingFields();
        adaptiveMobDesign = null;
    }

    public static class HarderMobsConfig {
        public boolean enabled = true;
        public String concept = "Harder Mobs";
        public PowerBudgetConfig powerBudget = new PowerBudgetConfig();
        public VariantRarityConfig variantRarity = new VariantRarityConfig();
        public EquipmentConfig equipment = new EquipmentConfig();
        public EnchantmentConfig enchantments = new EnchantmentConfig();
        public AttributeConfig attributes = new AttributeConfig();
        public EffectConfig effects = new EffectConfig();
        public VisualConfig visuals = new VisualConfig();
        public ExplorationConfig exploration = new ExplorationConfig();

        public void repairMissingFields() {
            if (concept == null || concept.isBlank()) {
                concept = "Harder Mobs";
            }
            if (powerBudget == null) {
                powerBudget = new PowerBudgetConfig();
            }
            if (variantRarity == null) {
                variantRarity = new VariantRarityConfig();
            }
            if (equipment == null) {
                equipment = new EquipmentConfig();
            }
            if (enchantments == null) {
                enchantments = new EnchantmentConfig();
            }
            if (attributes == null) {
                attributes = new AttributeConfig();
            }
            if (effects == null) {
                effects = new EffectConfig();
            }
            if (visuals == null) {
                visuals = new VisualConfig();
            }
            if (exploration == null) {
                exploration = new ExplorationConfig();
            }

            powerBudget.repairMissingFields();
            variantRarity.repairMissingFields();
            equipment.repairMissingFields();
            enchantments.repairMissingFields();
            attributes.repairMissingFields();
            effects.repairMissingFields();
            visuals.repairMissingFields();
            exploration.repairMissingFields();
        }
    }

    public static class PowerBudgetConfig {
        public float vanillaPlusScale = 0.85F;
        public float moddedPackScale = 1.15F;
        public float localDifficultyWeight = 0.35F;
        public float distanceFromSpawnWeight = 0.20F;
        public float biomeThreatWeight = 0.25F;
        public float structureGuardianBonus = 0.30F;
        public int minBudget = 8;
        public int maxBudget = 80;

        public void repairMissingFields() {
            vanillaPlusScale = clamp(vanillaPlusScale, 0.0F, 10.0F);
            moddedPackScale = clamp(moddedPackScale, 0.0F, 10.0F);
            localDifficultyWeight = clamp(localDifficultyWeight, 0.0F, 10.0F);
            distanceFromSpawnWeight = clamp(distanceFromSpawnWeight, 0.0F, 10.0F);
            biomeThreatWeight = clamp(biomeThreatWeight, 0.0F, 10.0F);
            structureGuardianBonus = clamp(structureGuardianBonus, 0.0F, 10.0F);
            minBudget = Math.max(0, minBudget);
            maxBudget = Math.max(minBudget, maxBudget);
        }
    }

    public static class VariantRarityConfig {
        public float uncommonChance = 0.18F;
        public float rareChance = 0.07F;
        public float eliteChance = 0.018F;
        public float nemesisChance = 0.002F;

        public void repairMissingFields() {
            uncommonChance = clampChance(uncommonChance);
            rareChance = clampChance(rareChance);
            eliteChance = clampChance(eliteChance);
            nemesisChance = clampChance(nemesisChance);
        }
    }

    public static class EquipmentConfig {
        public boolean enabled = true;
        public boolean allowModdedArmor = true;
        public boolean allowModdedWeapons = true;
        public boolean preferTaggedEquipment = true;
        public boolean mixArmorPieces = true;
        public boolean useArmorTrims = true;
        public boolean dyeLeatherArmor = true;
        public int maxNetheriteArmorPieces = 1;
        public float armorTrimChance = 0.65F;
        public float leatherDyeChance = 0.85F;
        public int maxArmorScore = 34;
        public int maxWeaponScore = 22;
        public List<String> armorBlacklist = List.of();
        public List<String> armorBlacklistExamples = List.of(
                "item:examplemod:creative_chestplate",
                "tag:examplemod:overpowered_armor",
                "mod:example_unsafe_gear_mod"
        );

        public void repairMissingFields() {
            maxNetheriteArmorPieces = Math.max(0, Math.min(4, maxNetheriteArmorPieces));
            armorTrimChance = clampChance(armorTrimChance);
            leatherDyeChance = clampChance(leatherDyeChance);
            maxArmorScore = Math.max(0, maxArmorScore);
            maxWeaponScore = Math.max(0, maxWeaponScore);
            if (armorBlacklist == null) {
                armorBlacklist = List.of();
            }
            if (armorBlacklistExamples == null) {
                armorBlacklistExamples = List.of(
                        "item:examplemod:creative_chestplate",
                        "tag:examplemod:overpowered_armor",
                        "mod:example_unsafe_gear_mod"
                );
            }
        }

        public boolean isArmorBlacklisted(String itemId, String... tagIds) {
            if (itemId == null || armorBlacklist == null || armorBlacklist.isEmpty()) {
                return false;
            }

            String normalizedItemId = itemId.toLowerCase(Locale.ROOT);
            String itemRule = "item:" + normalizedItemId;
            String modRule = "mod:" + normalizedItemId.substring(0, Math.max(0, normalizedItemId.indexOf(':')));

            return armorBlacklist.stream()
                    .filter(rule -> rule != null && !rule.isBlank())
                    .map(rule -> rule.toLowerCase(Locale.ROOT).trim())
                    .anyMatch(rule -> rule.equals(normalizedItemId)
                            || rule.equals(itemRule)
                            || rule.equals(modRule)
                            || matchesAnyTagRule(rule, tagIds));
        }

        private static boolean matchesAnyTagRule(String rule, String... tagIds) {
            if (!rule.startsWith("tag:") || tagIds == null) {
                return false;
            }

            String expectedTag = rule.substring("tag:".length());
            return Arrays.stream(tagIds)
                    .filter(tagId -> tagId != null && !tagId.isBlank())
                    .map(tagId -> tagId.toLowerCase(Locale.ROOT).trim())
                    .anyMatch(tagId -> tagId.equals(expectedTag));
        }
    }

    public static class EnchantmentConfig {
        public boolean enabled = true;
        public int maxEnchantScore = 26;
        public int maxEnchantmentsPerItem = 3;
        public boolean roleBasedEnchantments = true;
        public List<String> enchantmentBlacklist = List.of(
                "minecraft:vanishing_curse"
        );

        public void repairMissingFields() {
            maxEnchantScore = Math.max(0, maxEnchantScore);
            maxEnchantmentsPerItem = Math.max(0, maxEnchantmentsPerItem);
            if (enchantmentBlacklist == null) {
                enchantmentBlacklist = List.of("minecraft:vanishing_curse");
            }
        }
    }

    public static class AttributeConfig {
        public boolean enabled = true;
        public float maxHealthMultiplier = 2.5F;
        public float maxDamageMultiplier = 1.75F;
        public float maxSpeedMultiplier = 1.35F;
        public float maxKnockbackResistance = 0.65F;
        public boolean requireTradeoffForMajorBuffs = true;

        public void repairMissingFields() {
            maxHealthMultiplier = Math.max(1.0F, maxHealthMultiplier);
            maxDamageMultiplier = Math.max(1.0F, maxDamageMultiplier);
            maxSpeedMultiplier = Math.max(1.0F, maxSpeedMultiplier);
            maxKnockbackResistance = clampChance(maxKnockbackResistance);
        }
    }

    public static class EffectConfig {
        public boolean enabled = true;
        public boolean preferConditionalEffects = true;
        public int maxPermanentEffectScore = 10;
        public int maxTriggeredEffectScore = 24;
        public List<String> effectBlacklist = List.of(
                "minecraft:regeneration"
        );

        public void repairMissingFields() {
            maxPermanentEffectScore = Math.max(0, maxPermanentEffectScore);
            maxTriggeredEffectScore = Math.max(0, maxTriggeredEffectScore);
            if (effectBlacklist == null) {
                effectBlacklist = List.of("minecraft:regeneration");
            }
        }
    }

    public static class VisualConfig {
        public boolean enabled = true;
        public boolean showEliteNames = true;
        public boolean useParticles = true;
        public boolean useGlowingForNemesis = true;

        public void repairMissingFields() {
        }
    }

    public static class ExplorationConfig {
        public boolean enabled = true;
        public boolean scaleWithBiomeThreat = true;
        public boolean scaleWithDistanceFromSpawn = true;
        public boolean boostStructureGuardians = true;
        public float rareVariantLootBonus = 0.20F;
        public float eliteVariantLootBonus = 0.45F;
        public float nemesisVariantLootBonus = 0.80F;

        public void repairMissingFields() {
            rareVariantLootBonus = Math.max(0.0F, rareVariantLootBonus);
            eliteVariantLootBonus = Math.max(0.0F, eliteVariantLootBonus);
            nemesisVariantLootBonus = Math.max(0.0F, nemesisVariantLootBonus);
        }
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, Config.class);
                if (INSTANCE == null) {
                    INSTANCE = new Config();
                }
                INSTANCE.repairMissingFields();
                save();
            } catch (IOException e) {
                e.printStackTrace();
                INSTANCE = new Config();
            }
        } else {
            INSTANCE.repairMissingFields();
            save();
        }
    }

    public static void save() {
        File parent = CONFIG_FILE.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            return;
        }

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static float clampChance(float value) {
        return clamp(value, 0.0F, 1.0F);
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
