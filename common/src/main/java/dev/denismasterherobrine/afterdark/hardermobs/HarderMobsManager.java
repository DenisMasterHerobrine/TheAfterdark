package dev.denismasterherobrine.afterdark.hardermobs;

import dev.denismasterherobrine.afterdark.Config;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.item.trim.ArmorTrimPatterns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class HarderMobsManager {
    private static final UUID HEALTH_MODIFIER_ID = UUID.fromString("e57b38bb-664f-4d2a-9ca7-3da0cad9d8e0");
    private static final UUID DAMAGE_MODIFIER_ID = UUID.fromString("c7ead847-0f0e-4ce0-beb1-2f99f3dd9dc5");
    private static final UUID SPEED_MODIFIER_ID = UUID.fromString("cb6522de-121e-4c25-a99a-809313e38978");
    private static final UUID KNOCKBACK_MODIFIER_ID = UUID.fromString("66c944ef-bf0c-4647-a2a4-3cf090766a7c");
    private static final UUID ARMOR_MODIFIER_ID = UUID.fromString("621384d1-b1c3-4982-8488-8f4072927180");
    private static final UUID TOUGHNESS_MODIFIER_ID = UUID.fromString("680e770e-caa1-4411-b4e6-0a91ee4559fe");
    private static List<Item> cachedModdedArmor;
    private static List<Item> cachedModdedWeapons;

    private static final List<Item> VANILLA_ARMOR = List.of(
            Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS,
            Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS,
            Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
            Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS,
            Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS,
            Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS
    );

    private static final List<Item> VANILLA_WEAPONS = List.of(
            Items.WOODEN_SWORD, Items.STONE_SWORD, Items.GOLDEN_SWORD, Items.IRON_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD,
            Items.WOODEN_AXE, Items.STONE_AXE, Items.GOLDEN_AXE, Items.IRON_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE,
            Items.BOW, Items.CROSSBOW, Items.TRIDENT
    );

    private HarderMobsManager() {
    }

    public static EntityData applyAfterInitialize(MobEntity mob, ServerWorldAccess worldAccess, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData) {
        Config.HarderMobsConfig config = Config.INSTANCE.harderMobs;
        if (config == null || !config.enabled || mob == null || worldAccess == null) {
            return entityData;
        }

        ServerWorld world = worldAccess.toServerWorld();
        if (!world.getRegistryKey().equals(AfterdarkRegistry.AFTERDARK_LEVEL)) {
            return entityData;
        }

        if (mob.getType().getSpawnGroup() != SpawnGroup.MONSTER || isProcessed(mob)) {
            return entityData;
        }

        Random random = mob.getRandom();
        int budget = calculateBudget(config, mob, world, difficulty, spawnReason);
        Tier tier = pickTier(config, random, budget);
        if (tier == Tier.COMMON && random.nextFloat() > 0.55F) {
            markProcessed(mob, tier, Role.HARDENED);
            return entityData;
        }

        Role role = Role.pick(random);
        markProcessed(mob, tier, role);
        if (config.equipment.enabled) {
            equipMob(mob, world, config, random, budget, tier, role);
        }
        if (config.attributes.enabled) {
            applyAttributes(mob, config, tier, role);
        }
        if (config.effects.enabled) {
            applyEffects(mob, config, tier, role);
        }
        if (config.visuals.enabled) {
            applyVisuals(mob, world, config, tier, role);
        }

        return entityData;
    }

    public static void tick(MobEntity mob) {
        if (!(mob instanceof HarderMobEntityAccess access) || !access.the_afterdark$isHarderMob()) {
            return;
        }
        if (mob.getWorld().isClient() || !mob.getWorld().getRegistryKey().equals(AfterdarkRegistry.AFTERDARK_LEVEL)) {
            return;
        }

        String role = access.the_afterdark$getHarderMobRole();
        if (Role.BERSERKER.name().equals(role)) {
            tickBerserker(mob);
        } else if (Role.COMMANDER.name().equals(role) && mob.getWorld().getTime() % 60L == 0L) {
            tickCommander(mob);
        }
    }

    private static boolean isProcessed(MobEntity mob) {
        return mob instanceof HarderMobEntityAccess access && access.the_afterdark$isHarderMob();
    }

    private static void markProcessed(MobEntity mob, Tier tier, Role role) {
        if (mob instanceof HarderMobEntityAccess access) {
            access.the_afterdark$setHarderMob(true);
            access.the_afterdark$setHarderMobTier(tier.name());
            access.the_afterdark$setHarderMobRole(role.name());
        }
    }

    private static void tickBerserker(MobEntity mob) {
        if (mob.getHealth() <= mob.getMaxHealth() * 0.35F && !mob.hasStatusEffect(StatusEffects.STRENGTH)) {
            mob.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 20 * 12, 1, true, true));
            mob.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 8, 0, true, true));
        }
    }

    private static void tickCommander(MobEntity mob) {
        List<Entity> nearby = mob.getWorld().getOtherEntities(mob, mob.getBoundingBox().expand(8.0D), entity -> entity instanceof MobEntity other
                && other.isAlive()
                && other.getType().getSpawnGroup() == SpawnGroup.MONSTER
                && other instanceof HarderMobEntityAccess access
                && access.the_afterdark$isHarderMob());

        for (Entity entity : nearby) {
            if (entity instanceof MobEntity ally && !ally.hasStatusEffect(StatusEffects.RESISTANCE)) {
                ally.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 5, 0, true, true), mob);
            }
        }
    }

    private static int calculateBudget(Config.HarderMobsConfig config, MobEntity mob, ServerWorld world, LocalDifficulty difficulty, SpawnReason spawnReason) {
        Config.PowerBudgetConfig budgetConfig = config.powerBudget;
        BlockPos pos = mob.getBlockPos();
        double distance = Math.sqrt(pos.getSquaredDistance(world.getSpawnPos()));
        float distanceBoost = config.exploration.scaleWithDistanceFromSpawn ? MathHelper.clamp((float) (distance / 5000.0D), 0.0F, 1.0F) * budgetConfig.distanceFromSpawnWeight : 0.0F;
        float difficultyBoost = difficulty.getClampedLocalDifficulty() * budgetConfig.localDifficultyWeight;
        float spawnBoost = spawnReason == SpawnReason.STRUCTURE || spawnReason == SpawnReason.SPAWNER ? budgetConfig.structureGuardianBonus : 0.0F;
        float moddedScale = config.equipment.allowModdedArmor || config.equipment.allowModdedWeapons ? budgetConfig.moddedPackScale : budgetConfig.vanillaPlusScale;
        int budget = Math.round((budgetConfig.minBudget + 12.0F) * moddedScale * (1.0F + difficultyBoost + distanceBoost + spawnBoost));
        return MathHelper.clamp(budget, budgetConfig.minBudget, budgetConfig.maxBudget);
    }

    private static Tier pickTier(Config.HarderMobsConfig config, Random random, int budget) {
        Config.VariantRarityConfig rarity = config.variantRarity;
        float budgetBoost = MathHelper.clamp((budget - config.powerBudget.minBudget) / (float) Math.max(1, config.powerBudget.maxBudget - config.powerBudget.minBudget), 0.0F, 1.0F);
        float roll = random.nextFloat();
        if (roll < rarity.nemesisChance * (1.0F + budgetBoost)) return Tier.NEMESIS;
        if (roll < rarity.eliteChance * (1.0F + budgetBoost)) return Tier.ELITE;
        if (roll < rarity.rareChance * (1.0F + budgetBoost)) return Tier.RARE;
        if (roll < rarity.uncommonChance * (1.0F + budgetBoost)) return Tier.UNCOMMON;
        return Tier.COMMON;
    }

    private static void equipMob(MobEntity mob, ServerWorld world, Config.HarderMobsConfig config, Random random, int budget, Tier tier, Role role) {
        int armorBudget = Math.min(config.equipment.maxArmorScore, budget / 2 + tier.ordinal() * 5);
        ArmorTheme theme = ArmorTheme.pick(role, random);
        ItemStack[] armor = pickArmorPieces(config, random, armorBudget, tier, role, theme);
        for (ItemStack stack : armor) {
            if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem armorItem) {
                ItemStack prepared = prepareArmorPiece(world, stack, config, random, tier, theme);
                mob.equipStack(armorItem.getSlotType(), prepared);
                mob.setEquipmentDropChance(armorItem.getSlotType(), tier.dropChance);
            }
        }

        if (role != Role.HEXER || random.nextBoolean()) {
            ItemStack weapon = pickWeapon(config, random, Math.min(config.equipment.maxWeaponScore, budget / 3 + tier.ordinal() * 4));
            if (!weapon.isEmpty()) {
                mob.equipStack(EquipmentSlot.MAINHAND, prepareEquipment(weapon, config, random, tier, false));
                mob.setEquipmentDropChance(EquipmentSlot.MAINHAND, tier.dropChance);
            }
        }
    }

    private static ItemStack[] pickArmorPieces(Config.HarderMobsConfig config, Random random, int maxScore, Tier tier, Role role, ArmorTheme theme) {
        if (!config.equipment.mixArmorPieces) {
            return pickSingleMaterialArmorSet(config, random, maxScore, role);
        }

        ItemStack[] result = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
        List<Item> pool = armorPool(config.equipment);
        int remainingScore = Math.max(4, maxScore);
        int netheritePieces = 0;
        EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
        for (EquipmentSlot slot : slots) {
            if (random.nextFloat() > slotEquipChance(slot, tier, role)) {
                continue;
            }
            ItemStack piece = pickArmorPieceForSlot(config, random, pool, slot, remainingScore, tier, role, theme, netheritePieces);
            if (!piece.isEmpty() && piece.getItem() instanceof ArmorItem armorItem) {
                int score = armorScore(armorItem);
                remainingScore = Math.max(0, remainingScore - score);
                if (isNetheriteArmor(armorItem)) {
                    netheritePieces++;
                }
                result[indexForSlot(slot)] = piece;
            }
        }
        return result;
    }

    private static ItemStack pickArmorPieceForSlot(Config.HarderMobsConfig config, Random random, List<Item> pool, EquipmentSlot slot, int remainingScore, Tier tier, Role role, ArmorTheme theme, int netheritePieces) {
        List<Item> candidates = new ArrayList<>();
        int slotBudget = Math.max(3, remainingScore / Math.max(1, 4 - indexForSlot(slot))) + tier.ordinal();
        for (Item item : pool) {
            if (!(item instanceof ArmorItem armorItem) || armorItem.getSlotType() != slot) {
                continue;
            }
            ItemStack probe = new ItemStack(item);
            if (isBlacklistedArmor(config.equipment, probe)) {
                continue;
            }
            int score = armorScore(armorItem);
            if (score <= 0 || score > slotBudget + roleArmorAllowance(role, tier)) {
                continue;
            }
            if (isNetheriteArmor(armorItem) && (netheritePieces >= maxNetheritePieces(config, tier) || random.nextFloat() > netheriteChance(tier, role))) {
                continue;
            }
            int weight = armorWeight(score, slotBudget, armorItem, role, theme, tier);
            for (int i = 0; i < weight; i++) {
                candidates.add(item);
            }
        }
        if (candidates.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(candidates.get(random.nextInt(candidates.size())));
    }

    private static ItemStack[] pickSingleMaterialArmorSet(Config.HarderMobsConfig config, Random random, int maxScore, Role role) {
        ItemStack[] result = new ItemStack[0];
        int bestScore = -1;
        List<Item> pool = armorPool(config.equipment);
        for (Item item : pool) {
            if (!(item instanceof ArmorItem armorItem)) continue;
            ItemStack probe = new ItemStack(item);
            if (isBlacklistedArmor(config.equipment, probe)) continue;
            int baseScore = armorScore(armorItem);
            if (baseScore <= 0 || baseScore > maxScore + 4 || isNetheriteArmor(armorItem)) continue;
            if (result.length == 0 || random.nextInt(Math.max(1, maxScore + 6)) < baseScore || baseScore > bestScore && role == Role.BRUISER) {
                result = armorSetForMaterial(item, pool, config);
                bestScore = totalArmorScore(result);
            }
        }
        return result;
    }

    private static ItemStack[] armorSetForMaterial(Item sample, List<Item> pool, Config.HarderMobsConfig config) {
        ItemStack[] result = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
        if (!(sample instanceof ArmorItem sampleArmor)) {
            return result;
        }
        for (Item item : pool) {
            if (item instanceof ArmorItem armorItem && armorItem.getMaterial() == sampleArmor.getMaterial()) {
                ItemStack stack = new ItemStack(item);
                if (isBlacklistedArmor(config.equipment, stack)) {
                    continue;
                }
                switch (armorItem.getSlotType()) {
                    case HEAD -> result[0] = stack;
                    case CHEST -> result[1] = stack;
                    case LEGS -> result[2] = stack;
                    case FEET -> result[3] = stack;
                    default -> {
                    }
                }
            }
        }
        return result;
    }

    private static int totalArmorScore(ItemStack[] armor) {
        int score = 0;
        for (ItemStack stack : armor) {
            if (stack.getItem() instanceof ArmorItem armorItem) {
                score += armorScore(armorItem);
            }
        }
        return score;
    }

    private static boolean isBlacklistedArmor(Config.EquipmentConfig equipment, ItemStack stack) {
        Identifier id = Registries.ITEM.getId(stack.getItem());
        String[] tags = stack.streamTags().map(TagKey::id).map(Identifier::toString).toArray(String[]::new);
        return equipment.isArmorBlacklisted(id.toString(), tags);
    }

    private static ItemStack prepareArmorPiece(ServerWorld world, ItemStack stack, Config.HarderMobsConfig config, Random random, Tier tier, ArmorTheme theme) {
        if (config.equipment.dyeLeatherArmor && stack.getItem() instanceof DyeableItem dyeableItem && random.nextFloat() < config.equipment.leatherDyeChance) {
            dyeableItem.setColor(stack, theme.leatherColors[random.nextInt(theme.leatherColors.length)]);
        }
        if (config.equipment.useArmorTrims && tier.ordinal() >= Tier.UNCOMMON.ordinal() && random.nextFloat() < config.equipment.armorTrimChance) {
            applyTrim(world.getRegistryManager(), stack, theme, random);
        }
        return prepareEquipment(stack, config, random, tier, true);
    }

    private static void applyTrim(DynamicRegistryManager registryManager, ItemStack stack, ArmorTheme theme, Random random) {
        if (!(stack.getItem() instanceof ArmorItem) || stack.getItem() instanceof DyeableItem) {
            return;
        }
        Registry<ArmorTrimMaterial> materialRegistry = registryManager.get(RegistryKeys.TRIM_MATERIAL);
        Registry<ArmorTrimPattern> patternRegistry = registryManager.get(RegistryKeys.TRIM_PATTERN);
        Optional<RegistryEntry.Reference<ArmorTrimMaterial>> material = materialRegistry.getEntry(theme.trimMaterials[random.nextInt(theme.trimMaterials.length)]);
        Optional<RegistryEntry.Reference<ArmorTrimPattern>> pattern = patternRegistry.getEntry(theme.trimPatterns[random.nextInt(theme.trimPatterns.length)]);
        if (material.isPresent() && pattern.isPresent()) {
            ArmorTrim.apply(registryManager, stack, new ArmorTrim(material.get(), pattern.get()));
        }
    }

    private static int armorScore(ArmorItem armorItem) {
        return armorItem.getProtection() + Math.round(armorItem.getToughness() * 2.0F);
    }

    private static boolean isNetheriteArmor(ArmorItem armorItem) {
        return armorItem.getMaterial() == ArmorMaterials.NETHERITE;
    }

    private static int maxNetheritePieces(Config.HarderMobsConfig config, Tier tier) {
        if (tier.ordinal() < Tier.ELITE.ordinal()) {
            return 0;
        }
        return Math.min(config.equipment.maxNetheriteArmorPieces, tier == Tier.NEMESIS ? 2 : 1);
    }

    private static float netheriteChance(Tier tier, Role role) {
        float baseChance = tier == Tier.NEMESIS ? 0.32F : tier == Tier.ELITE ? 0.16F : 0.0F;
        return role == Role.BRUISER || role == Role.GUARDIAN ? baseChance + 0.10F : baseChance;
    }

    private static int armorWeight(int score, int slotBudget, ArmorItem armorItem, Role role, ArmorTheme theme, Tier tier) {
        int fit = Math.max(1, 8 - Math.abs(slotBudget - score));
        int materialBonus = theme.prefers(armorItem) ? 5 : 0;
        int roleBonus = role == Role.BRUISER || role == Role.GUARDIAN ? score / 2 : Math.max(1, 6 - score);
        int netheritePenalty = isNetheriteArmor(armorItem) && tier != Tier.NEMESIS ? 8 : 0;
        return Math.max(1, fit + materialBonus + roleBonus - netheritePenalty);
    }

    private static int roleArmorAllowance(Role role, Tier tier) {
        int base = tier.ordinal() + 2;
        return role == Role.BRUISER || role == Role.GUARDIAN ? base + 4 : base;
    }

    private static float slotEquipChance(EquipmentSlot slot, Tier tier, Role role) {
        float baseChance = switch (slot) {
            case CHEST -> 0.92F;
            case LEGS -> 0.76F;
            case HEAD -> 0.70F;
            case FEET -> 0.64F;
            default -> 0.0F;
        };
        if (role == Role.BRUISER || role == Role.GUARDIAN) {
            baseChance += 0.16F;
        } else if (role == Role.SKIRMISHER || role == Role.HEXER) {
            baseChance -= 0.14F;
        }
        return MathHelper.clamp(baseChance + tier.ordinal() * 0.04F, 0.15F, 1.0F);
    }

    private static int indexForSlot(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> 0;
            case CHEST -> 1;
            case LEGS -> 2;
            case FEET -> 3;
            default -> 0;
        };
    }

    private static ItemStack pickWeapon(Config.HarderMobsConfig config, Random random, int maxScore) {
        List<Item> pool = weaponPool(config.equipment);
        ItemStack result = ItemStack.EMPTY;
        int bestScore = -1;
        for (Item item : pool) {
            Identifier id = Registries.ITEM.getId(item);
            String path = id.getPath();
            if (!path.endsWith("_sword") && !path.endsWith("_axe") && item != Items.BOW && item != Items.CROSSBOW && item != Items.TRIDENT) {
                continue;
            }
            int score = weaponScore(item);
            if (score <= 0 || score > maxScore + 4) {
                continue;
            }
            if (result.isEmpty() || random.nextInt(Math.max(1, maxScore + 6)) < score || score > bestScore) {
                result = new ItemStack(item);
                bestScore = score;
            }
        }
        return result;
    }

    private static int weaponScore(Item item) {
        if (item == Items.NETHERITE_SWORD || item == Items.NETHERITE_AXE || item == Items.TRIDENT) return 18;
        if (item == Items.DIAMOND_SWORD || item == Items.DIAMOND_AXE || item == Items.CROSSBOW) return 14;
        if (item == Items.IRON_SWORD || item == Items.IRON_AXE || item == Items.BOW) return 10;
        if (item == Items.STONE_SWORD || item == Items.STONE_AXE) return 6;
        if (item == Items.WOODEN_SWORD || item == Items.WOODEN_AXE || item == Items.GOLDEN_SWORD || item == Items.GOLDEN_AXE) return 4;
        return 8;
    }

    private static ItemStack prepareEquipment(ItemStack stack, Config.HarderMobsConfig config, Random random, Tier tier, boolean armor) {
        if (!config.enchantments.enabled || tier == Tier.COMMON || random.nextFloat() > tier.enchantChance) {
            return stack;
        }
        Map<Enchantment, Integer> enchantments = new HashMap<>(EnchantmentHelper.get(stack));
        int maxCount = Math.max(0, config.enchantments.maxEnchantmentsPerItem);
        int count = Math.min(maxCount, 1 + tier.ordinal() / 2);
        List<Enchantment> candidates = new ArrayList<>(armor ? armorEnchantments(random) : weaponEnchantments(stack, random));
        Collections.shuffle(candidates, new java.util.Random(random.nextLong()));
        for (Enchantment enchantment : candidates) {
            if (count <= 0) break;
            Identifier id = Registries.ENCHANTMENT.getId(enchantment);
            if (!enchantment.isAcceptableItem(stack) || id != null && config.enchantments.enchantmentBlacklist.contains(id.toString())) {
                continue;
            }
            enchantments.put(enchantment, Math.min(enchantment.getMaxLevel(), 1 + random.nextInt(Math.max(1, tier.maxEnchantLevel))));
            count--;
        }
        EnchantmentHelper.set(enchantments, stack);
        return stack;
    }

    private static List<Enchantment> armorEnchantments(Random random) {
        return random.nextBoolean()
                ? List.of(Enchantments.PROTECTION, Enchantments.UNBREAKING, Enchantments.THORNS)
                : List.of(Enchantments.PROJECTILE_PROTECTION, Enchantments.FEATHER_FALLING, Enchantments.UNBREAKING);
    }

    private static List<Enchantment> weaponEnchantments(ItemStack stack, Random random) {
        Item item = stack.getItem();
        if (item == Items.BOW) {
            return List.of(Enchantments.POWER, Enchantments.PUNCH, Enchantments.UNBREAKING);
        }
        if (item == Items.CROSSBOW) {
            return List.of(Enchantments.QUICK_CHARGE, Enchantments.PIERCING, Enchantments.UNBREAKING);
        }
        if (item == Items.TRIDENT) {
            return List.of(Enchantments.IMPALING, Enchantments.LOYALTY, Enchantments.UNBREAKING);
        }
        return List.of(Enchantments.SHARPNESS, Enchantments.KNOCKBACK, Enchantments.UNBREAKING);
    }

    private static List<Item> armorPool(Config.EquipmentConfig equipment) {
        if (!equipment.allowModdedArmor) {
            return VANILLA_ARMOR;
        }
        if (cachedModdedArmor == null) {
            cachedModdedArmor = Registries.ITEM.stream()
                    .filter(item -> item instanceof ArmorItem)
                    .toList();
        }
        return cachedModdedArmor;
    }

    private static List<Item> weaponPool(Config.EquipmentConfig equipment) {
        if (!equipment.allowModdedWeapons) {
            return VANILLA_WEAPONS;
        }
        if (cachedModdedWeapons == null) {
            cachedModdedWeapons = Registries.ITEM.stream()
                    .filter(HarderMobsManager::isWeaponCandidate)
                    .toList();
        }
        return cachedModdedWeapons;
    }

    private static boolean isWeaponCandidate(Item item) {
        Identifier id = Registries.ITEM.getId(item);
        String path = id.getPath();
        return path.endsWith("_sword") || path.endsWith("_axe") || item == Items.BOW || item == Items.CROSSBOW || item == Items.TRIDENT;
    }

    private static void applyAttributes(MobEntity mob, Config.HarderMobsConfig config, Tier tier, Role role) {
        float tierScale = tier.attributeScale;
        switch (role) {
            case BRUISER -> {
                addMultiplier(mob, EntityAttributes.GENERIC_MAX_HEALTH, HEALTH_MODIFIER_ID, "Harder Mobs health", Math.min(config.attributes.maxHealthMultiplier - 1.0F, 0.35F + tierScale));
                addMultiplier(mob, EntityAttributes.GENERIC_MOVEMENT_SPEED, SPEED_MODIFIER_ID, "Harder Mobs speed tradeoff", -0.10F);
                addValue(mob, EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, KNOCKBACK_MODIFIER_ID, "Harder Mobs knockback", Math.min(config.attributes.maxKnockbackResistance, 0.15F + tierScale * 0.25F));
            }
            case SKIRMISHER -> {
                addMultiplier(mob, EntityAttributes.GENERIC_MOVEMENT_SPEED, SPEED_MODIFIER_ID, "Harder Mobs speed", Math.min(config.attributes.maxSpeedMultiplier - 1.0F, 0.08F + tierScale * 0.20F));
                addMultiplier(mob, EntityAttributes.GENERIC_MAX_HEALTH, HEALTH_MODIFIER_ID, "Harder Mobs light frame", -0.10F);
            }
            case HEXER -> addMultiplier(mob, EntityAttributes.GENERIC_FOLLOW_RANGE, KNOCKBACK_MODIFIER_ID, "Harder Mobs focus", 0.20F + tierScale * 0.25F);
            case BERSERKER -> addMultiplier(mob, EntityAttributes.GENERIC_ATTACK_DAMAGE, DAMAGE_MODIFIER_ID, "Harder Mobs damage", Math.min(config.attributes.maxDamageMultiplier - 1.0F, 0.15F + tierScale * 0.25F));
            case GUARDIAN -> {
                addValue(mob, EntityAttributes.GENERIC_ARMOR, ARMOR_MODIFIER_ID, "Harder Mobs armor", 2.0D + tier.ordinal() * 1.5D);
                addValue(mob, EntityAttributes.GENERIC_ARMOR_TOUGHNESS, TOUGHNESS_MODIFIER_ID, "Harder Mobs toughness", tier.ordinal());
            }
            case COMMANDER -> addMultiplier(mob, EntityAttributes.GENERIC_MAX_HEALTH, HEALTH_MODIFIER_ID, "Harder Mobs commander", Math.min(config.attributes.maxHealthMultiplier - 1.0F, 0.20F + tierScale * 0.35F));
        }
        mob.setHealth(mob.getMaxHealth());
    }

    private static void addMultiplier(MobEntity mob, EntityAttribute attribute, UUID id, String name, double value) {
        EntityAttributeInstance instance = mob.getAttributeInstance(attribute);
        if (instance != null && value != 0.0D) {
            instance.removeModifier(id);
            instance.addPersistentModifier(new EntityAttributeModifier(id, name, value, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    private static void addValue(MobEntity mob, EntityAttribute attribute, UUID id, String name, double value) {
        EntityAttributeInstance instance = mob.getAttributeInstance(attribute);
        if (instance != null && value != 0.0D) {
            instance.removeModifier(id);
            instance.addPersistentModifier(new EntityAttributeModifier(id, name, value, EntityAttributeModifier.Operation.ADDITION));
        }
    }

    private static void applyEffects(MobEntity mob, Config.HarderMobsConfig config, Tier tier, Role role) {
        int duration = tier == Tier.NEMESIS ? -1 : 20 * 60 * 8;
        StatusEffect effect = switch (role) {
            case HARDENED -> StatusEffects.GLOWING;
            case BRUISER, GUARDIAN -> StatusEffects.RESISTANCE;
            case SKIRMISHER -> StatusEffects.SPEED;
            case HEXER -> StatusEffects.INVISIBILITY;
            case BERSERKER -> StatusEffects.STRENGTH;
            case COMMANDER -> StatusEffects.GLOWING;
        };
        Identifier id = Registries.STATUS_EFFECT.getId(effect);
        if (id == null || config.effects.effectBlacklist.contains(id.toString())) {
            return;
        }
        int amplifier = tier == Tier.NEMESIS ? 1 : 0;
        mob.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier, true, config.visuals.useParticles));
    }

    private static void applyVisuals(MobEntity mob, ServerWorld world, Config.HarderMobsConfig config, Tier tier, Role role) {
        if (config.visuals.showEliteNames && tier.ordinal() >= Tier.RARE.ordinal()) {
            mob.setCustomName(Text.literal(tier.displayName + " " + role.displayName));
            mob.setCustomNameVisible(tier.ordinal() >= Tier.ELITE.ordinal());
        }
        if (config.visuals.useGlowingForNemesis && tier == Tier.NEMESIS) {
            mob.setGlowing(true);
        }
        if (config.visuals.useParticles) {
            DefaultParticleType particle = switch (role) {
                case HARDENED -> ParticleTypes.SOUL;
                case BRUISER, GUARDIAN -> ParticleTypes.SOUL;
                case SKIRMISHER -> ParticleTypes.CRIT;
                case HEXER -> ParticleTypes.WITCH;
                case BERSERKER -> ParticleTypes.SMOKE;
                case COMMANDER -> ParticleTypes.GLOW;
            };
            world.spawnParticles(particle, mob.getX(), mob.getY() + mob.getHeight() * 0.7D, mob.getZ(), 12 + tier.ordinal() * 6, 0.35D, 0.45D, 0.35D, 0.02D);
        }
    }

    private enum Tier {
        COMMON("Hardened", 0.0F, 0.02F, 1, 0.04F),
        UNCOMMON("Uncommon", 0.15F, 0.18F, 1, 0.04F),
        RARE("Rare", 0.30F, 0.35F, 2, 0.08F),
        ELITE("Elite", 0.50F, 0.60F, 3, 0.12F),
        NEMESIS("Nemesis", 0.80F, 0.90F, 4, 0.18F);

        private final String displayName;
        private final float attributeScale;
        private final float enchantChance;
        private final int maxEnchantLevel;
        private final float dropChance;

        Tier(String displayName, float attributeScale, float enchantChance, int maxEnchantLevel, float dropChance) {
            this.displayName = displayName;
            this.attributeScale = attributeScale;
            this.enchantChance = enchantChance;
            this.maxEnchantLevel = maxEnchantLevel;
            this.dropChance = dropChance;
        }
    }

    private enum Role {
        HARDENED("Hardened"),
        BRUISER("Bruiser"),
        SKIRMISHER("Skirmisher"),
        HEXER("Hexer"),
        BERSERKER("Berserker"),
        GUARDIAN("Guardian"),
        COMMANDER("Commander");

        private final String displayName;

        Role(String displayName) {
            this.displayName = displayName;
        }

        private static Role pick(Random random) {
            Role[] values = {BRUISER, SKIRMISHER, HEXER, BERSERKER, GUARDIAN, COMMANDER};
            return values[random.nextInt(values.length)];
        }
    }

    private enum ArmorTheme {
        ROOTED(
                new ArmorMaterials[]{ArmorMaterials.LEATHER, ArmorMaterials.CHAIN, ArmorMaterials.IRON},
                new int[]{0x2D2418, 0x3A4A2A, 0x5A3B24},
                new RegistryKey[]{ArmorTrimMaterials.COPPER, ArmorTrimMaterials.EMERALD, ArmorTrimMaterials.QUARTZ},
                new RegistryKey[]{ArmorTrimPatterns.WILD, ArmorTrimPatterns.WARD, ArmorTrimPatterns.HOST}
        ),
        FROSTED(
                new ArmorMaterials[]{ArmorMaterials.LEATHER, ArmorMaterials.CHAIN, ArmorMaterials.IRON, ArmorMaterials.DIAMOND},
                new int[]{0xD4E5F2, 0x8AAEC8, 0x5C6F86},
                new RegistryKey[]{ArmorTrimMaterials.IRON, ArmorTrimMaterials.DIAMOND, ArmorTrimMaterials.LAPIS},
                new RegistryKey[]{ArmorTrimPatterns.TIDE, ArmorTrimPatterns.SPIRE, ArmorTrimPatterns.WAYFINDER}
        ),
        CURSED(
                new ArmorMaterials[]{ArmorMaterials.CHAIN, ArmorMaterials.GOLD, ArmorMaterials.IRON},
                new int[]{0x1B1024, 0x34204A, 0x123629},
                new RegistryKey[]{ArmorTrimMaterials.AMETHYST, ArmorTrimMaterials.LAPIS, ArmorTrimMaterials.REDSTONE},
                new RegistryKey[]{ArmorTrimPatterns.EYE, ArmorTrimPatterns.SILENCE, ArmorTrimPatterns.VEX}
        ),
        EMBER(
                new ArmorMaterials[]{ArmorMaterials.LEATHER, ArmorMaterials.GOLD, ArmorMaterials.IRON},
                new int[]{0x3A1A0D, 0x713018, 0xA54A1F},
                new RegistryKey[]{ArmorTrimMaterials.REDSTONE, ArmorTrimMaterials.COPPER, ArmorTrimMaterials.GOLD},
                new RegistryKey[]{ArmorTrimPatterns.RIB, ArmorTrimPatterns.SNOUT, ArmorTrimPatterns.DUNE}
        ),
        DEEP(
                new ArmorMaterials[]{ArmorMaterials.CHAIN, ArmorMaterials.IRON, ArmorMaterials.DIAMOND},
                new int[]{0x15171F, 0x202C3A, 0x30404F},
                new RegistryKey[]{ArmorTrimMaterials.LAPIS, ArmorTrimMaterials.AMETHYST, ArmorTrimMaterials.NETHERITE},
                new RegistryKey[]{ArmorTrimPatterns.WARD, ArmorTrimPatterns.SILENCE, ArmorTrimPatterns.RIB}
        );

        private final ArmorMaterials[] preferredMaterials;
        private final int[] leatherColors;
        private final RegistryKey<ArmorTrimMaterial>[] trimMaterials;
        private final RegistryKey<ArmorTrimPattern>[] trimPatterns;

        ArmorTheme(ArmorMaterials[] preferredMaterials, int[] leatherColors, RegistryKey<ArmorTrimMaterial>[] trimMaterials, RegistryKey<ArmorTrimPattern>[] trimPatterns) {
            this.preferredMaterials = preferredMaterials;
            this.leatherColors = leatherColors;
            this.trimMaterials = trimMaterials;
            this.trimPatterns = trimPatterns;
        }

        private boolean prefers(ArmorItem armorItem) {
            if (!(armorItem.getMaterial() instanceof ArmorMaterials material)) {
                return false;
            }
            for (ArmorMaterials preferredMaterial : preferredMaterials) {
                if (material == preferredMaterial) {
                    return true;
                }
            }
            return false;
        }

        private static ArmorTheme pick(Role role, Random random) {
            return switch (role) {
                case SKIRMISHER -> random.nextBoolean() ? FROSTED : ROOTED;
                case HEXER -> CURSED;
                case BERSERKER -> EMBER;
                case GUARDIAN, BRUISER -> random.nextBoolean() ? DEEP : EMBER;
                case COMMANDER -> random.nextBoolean() ? CURSED : DEEP;
                case HARDENED -> values()[random.nextInt(values().length)];
            };
        }
    }
}
