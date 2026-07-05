package dev.denismasterherobrine.afterdark.hardermobs;

import dev.denismasterherobrine.afterdark.Config;
import dev.denismasterherobrine.afterdark.TheAfterdark;
import dev.denismasterherobrine.afterdark.registry.AfterdarkRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.armortrim.TrimPatterns;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ServerLevelAccessor;

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

    public static SpawnGroupData applyAfterInitialize(Mob mob, ServerLevelAccessor worldAccess, DifficultyInstance difficulty, MobSpawnType spawnReason, SpawnGroupData entityData) {
        Config.HarderMobsConfig config = Config.INSTANCE.harderMobs;
        if (config == null || !config.enabled || mob == null || worldAccess == null) {
            return entityData;
        }

        ServerLevel world = worldAccess.getLevel();
        if (!world.dimension().equals(AfterdarkRegistry.AFTERDARK_LEVEL)) {
            return entityData;
        }

        if (mob.getType().getCategory() != MobCategory.MONSTER || isProcessed(mob)) {
            return entityData;
        }

        RandomSource random = mob.getRandom();
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

    public static void tick(Mob mob) {
        if (!(mob instanceof HarderMobEntityAccess access) || !access.the_afterdark$isHarderMob()) {
            return;
        }
        if (mob.level().isClientSide() || !mob.level().dimension().equals(AfterdarkRegistry.AFTERDARK_LEVEL)) {
            return;
        }

        String role = access.the_afterdark$getHarderMobRole();
        if (Role.BERSERKER.name().equals(role)) {
            tickBerserker(mob);
        } else if (Role.COMMANDER.name().equals(role) && mob.level().getGameTime() % 60L == 0L) {
            tickCommander(mob);
        }
    }

    private static boolean isProcessed(Mob mob) {
        return mob instanceof HarderMobEntityAccess access && access.the_afterdark$isHarderMob();
    }

    private static void markProcessed(Mob mob, Tier tier, Role role) {
        if (mob instanceof HarderMobEntityAccess access) {
            access.the_afterdark$setHarderMob(true);
            access.the_afterdark$setHarderMobTier(tier.name());
            access.the_afterdark$setHarderMobRole(role.name());
        }
    }

    private static void tickBerserker(Mob mob) {
        if (mob.getHealth() <= mob.getMaxHealth() * 0.35F && !mob.hasEffect(MobEffects.DAMAGE_BOOST)) {
            mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 12, 1, true, true));
            mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 8, 0, true, true));
        }
    }

    private static void tickCommander(Mob mob) {
        List<Entity> nearby = mob.level().getEntities(mob, mob.getBoundingBox().inflate(8.0D), entity -> entity instanceof Mob other
                && other.isAlive()
                && other.getType().getCategory() == MobCategory.MONSTER
                && other instanceof HarderMobEntityAccess access
                && access.the_afterdark$isHarderMob());

        for (Entity entity : nearby) {
            if (entity instanceof Mob ally && !ally.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
                ally.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 5, 0, true, true), mob);
            }
        }
    }

    private static int calculateBudget(Config.HarderMobsConfig config, Mob mob, ServerLevel world, DifficultyInstance difficulty, MobSpawnType spawnReason) {
        Config.PowerBudgetConfig budgetConfig = config.powerBudget;
        BlockPos pos = mob.blockPosition();
        double distance = Math.sqrt(pos.distSqr(world.getSharedSpawnPos()));
        float distanceBoost = config.exploration.scaleWithDistanceFromSpawn ? Mth.clamp((float) (distance / 5000.0D), 0.0F, 1.0F) * budgetConfig.distanceFromSpawnWeight : 0.0F;
        float difficultyBoost = difficulty.getSpecialMultiplier() * budgetConfig.localDifficultyWeight;
        float spawnBoost = spawnReason == MobSpawnType.STRUCTURE || spawnReason == MobSpawnType.SPAWNER ? budgetConfig.structureGuardianBonus : 0.0F;
        float moddedScale = config.equipment.allowModdedArmor || config.equipment.allowModdedWeapons ? budgetConfig.moddedPackScale : budgetConfig.vanillaPlusScale;
        int budget = Math.round((budgetConfig.minBudget + 12.0F) * moddedScale * (1.0F + difficultyBoost + distanceBoost + spawnBoost));
        return Mth.clamp(budget, budgetConfig.minBudget, budgetConfig.maxBudget);
    }

    private static Tier pickTier(Config.HarderMobsConfig config, RandomSource random, int budget) {
        Config.VariantRarityConfig rarity = config.variantRarity;
        float budgetBoost = Mth.clamp((budget - config.powerBudget.minBudget) / (float) Math.max(1, config.powerBudget.maxBudget - config.powerBudget.minBudget), 0.0F, 1.0F);
        float roll = random.nextFloat();
        if (roll < rarity.nemesisChance * (1.0F + budgetBoost)) return Tier.NEMESIS;
        if (roll < rarity.eliteChance * (1.0F + budgetBoost)) return Tier.ELITE;
        if (roll < rarity.rareChance * (1.0F + budgetBoost)) return Tier.RARE;
        if (roll < rarity.uncommonChance * (1.0F + budgetBoost)) return Tier.UNCOMMON;
        return Tier.COMMON;
    }

    private static void equipMob(Mob mob, ServerLevel world, Config.HarderMobsConfig config, RandomSource random, int budget, Tier tier, Role role) {
        int armorBudget = Math.min(config.equipment.maxArmorScore, budget / 2 + tier.ordinal() * 5);
        ArmorTheme theme = ArmorTheme.pick(role, random);
        ItemStack[] armor = pickArmorPieces(config, random, armorBudget, tier, role, theme);
        for (ItemStack stack : armor) {
            if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem armorItem) {
                ItemStack prepared = prepareArmorPiece(world, stack, config, random, tier, theme);
                mob.setItemSlot(armorItem.getEquipmentSlot(), prepared);
                mob.setDropChance(armorItem.getEquipmentSlot(), tier.dropChance);
            }
        }

        if (role != Role.HEXER || random.nextBoolean()) {
            ItemStack weapon = pickWeapon(config, random, Math.min(config.equipment.maxWeaponScore, budget / 3 + tier.ordinal() * 4));
            if (!weapon.isEmpty()) {
                mob.setItemSlot(EquipmentSlot.MAINHAND, prepareEquipment(world, weapon, config, random, tier, false));
                mob.setDropChance(EquipmentSlot.MAINHAND, tier.dropChance);
            }
        }
    }

    private static ItemStack[] pickArmorPieces(Config.HarderMobsConfig config, RandomSource random, int maxScore, Tier tier, Role role, ArmorTheme theme) {
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

    private static ItemStack pickArmorPieceForSlot(Config.HarderMobsConfig config, RandomSource random, List<Item> pool, EquipmentSlot slot, int remainingScore, Tier tier, Role role, ArmorTheme theme, int netheritePieces) {
        List<Item> candidates = new ArrayList<>();
        int slotBudget = Math.max(3, remainingScore / Math.max(1, 4 - indexForSlot(slot))) + tier.ordinal();
        for (Item item : pool) {
            if (!(item instanceof ArmorItem armorItem) || armorItem.getEquipmentSlot() != slot) {
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

    private static ItemStack[] pickSingleMaterialArmorSet(Config.HarderMobsConfig config, RandomSource random, int maxScore, Role role) {
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
                switch (armorItem.getEquipmentSlot()) {
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
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String[] tags = stack.getTags().map(TagKey::location).map(ResourceLocation::toString).toArray(String[]::new);
        return equipment.isArmorBlacklisted(id.toString(), tags);
    }

    private static ItemStack prepareArmorPiece(ServerLevel world, ItemStack stack, Config.HarderMobsConfig config, RandomSource random, Tier tier, ArmorTheme theme) {
        if (config.equipment.dyeLeatherArmor && isLeatherArmor(stack) && random.nextFloat() < config.equipment.leatherDyeChance) {
            stack.set(net.minecraft.core.component.DataComponents.DYED_COLOR, new DyedItemColor(theme.leatherColors[random.nextInt(theme.leatherColors.length)], true));
        }
        if (config.equipment.useArmorTrims && tier.ordinal() >= Tier.UNCOMMON.ordinal() && random.nextFloat() < config.equipment.armorTrimChance) {
            applyTrim(world.registryAccess(), stack, theme, random);
        }
        return prepareEquipment(world, stack, config, random, tier, true);
    }

    private static void applyTrim(RegistryAccess registryManager, ItemStack stack, ArmorTheme theme, RandomSource random) {
        if (!(stack.getItem() instanceof ArmorItem) || isLeatherArmor(stack)) {
            return;
        }
        Registry<TrimMaterial> materialRegistry = registryManager.registryOrThrow(Registries.TRIM_MATERIAL);
        Registry<TrimPattern> patternRegistry = registryManager.registryOrThrow(Registries.TRIM_PATTERN);
        Optional<Holder.Reference<TrimMaterial>> material = materialRegistry.getHolder(theme.trimMaterials[random.nextInt(theme.trimMaterials.length)]);
        Optional<Holder.Reference<TrimPattern>> pattern = patternRegistry.getHolder(theme.trimPatterns[random.nextInt(theme.trimPatterns.length)]);
        if (material.isPresent() && pattern.isPresent()) {
            stack.set(DataComponents.TRIM, new ArmorTrim(material.get(), pattern.get()));
        }
    }

    private static int armorScore(ArmorItem armorItem) {
        return armorItem.getDefense() + Math.round(armorItem.getToughness() * 2.0F);
    }

    private static boolean isNetheriteArmor(ArmorItem armorItem) {
        return armorItem.getMaterial() == ArmorMaterials.NETHERITE;
    }

    private static boolean isLeatherArmor(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armorItem && armorItem.getMaterial() == ArmorMaterials.LEATHER;
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
        return Mth.clamp(baseChance + tier.ordinal() * 0.04F, 0.15F, 1.0F);
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

    private static ItemStack pickWeapon(Config.HarderMobsConfig config, RandomSource random, int maxScore) {
        List<Item> pool = weaponPool(config.equipment);
        ItemStack result = ItemStack.EMPTY;
        int bestScore = -1;
        for (Item item : pool) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
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

    private static ItemStack prepareEquipment(ServerLevel world, ItemStack stack, Config.HarderMobsConfig config, RandomSource random, Tier tier, boolean armor) {
        if (!config.enchantments.enabled || tier == Tier.COMMON || random.nextFloat() > tier.enchantChance) {
            return stack;
        }
        int maxCount = Math.max(0, config.enchantments.maxEnchantmentsPerItem);
        int count = Math.min(maxCount, 1 + tier.ordinal() / 2);
        List<ResourceKey<Enchantment>> candidates = new ArrayList<>(armor ? armorEnchantments(random) : weaponEnchantments(stack, random));
        Collections.shuffle(candidates, new java.util.Random(random.nextLong()));
        Registry<Enchantment> enchantmentRegistry = world.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        for (ResourceKey<Enchantment> enchantmentKey : candidates) {
            if (count <= 0) break;
            ResourceLocation id = enchantmentKey.location();
            Optional<Holder.Reference<Enchantment>> enchantment = enchantmentRegistry.getHolder(enchantmentKey);
            if (enchantment.isEmpty() || config.enchantments.enchantmentBlacklist.contains(id.toString()) || !enchantment.get().value().canEnchant(stack)) {
                continue;
            }
            stack.enchant(enchantment.get(), Math.min(enchantment.get().value().getMaxLevel(), 1 + random.nextInt(Math.max(1, tier.maxEnchantLevel))));
            count--;
        }
        return stack;
    }

    private static List<ResourceKey<Enchantment>> armorEnchantments(RandomSource random) {
        return random.nextBoolean()
                ? List.of(Enchantments.PROTECTION, Enchantments.UNBREAKING, Enchantments.THORNS)
                : List.of(Enchantments.PROJECTILE_PROTECTION, Enchantments.FEATHER_FALLING, Enchantments.UNBREAKING);
    }

    private static List<ResourceKey<Enchantment>> weaponEnchantments(ItemStack stack, RandomSource random) {
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
            cachedModdedArmor = BuiltInRegistries.ITEM.stream()
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
            cachedModdedWeapons = BuiltInRegistries.ITEM.stream()
                    .filter(HarderMobsManager::isWeaponCandidate)
                    .toList();
        }
        return cachedModdedWeapons;
    }

    private static boolean isWeaponCandidate(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        String path = id.getPath();
        return path.endsWith("_sword") || path.endsWith("_axe") || item == Items.BOW || item == Items.CROSSBOW || item == Items.TRIDENT;
    }

    private static void applyAttributes(Mob mob, Config.HarderMobsConfig config, Tier tier, Role role) {
        float tierScale = tier.attributeScale;
        switch (role) {
            case BRUISER -> {
                addMultiplier(mob, Attributes.MAX_HEALTH, HEALTH_MODIFIER_ID, "Harder Mobs health", Math.min(config.attributes.maxHealthMultiplier - 1.0F, 0.35F + tierScale));
                addMultiplier(mob, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER_ID, "Harder Mobs speed tradeoff", -0.10F);
                addValue(mob, Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_MODIFIER_ID, "Harder Mobs knockback", Math.min(config.attributes.maxKnockbackResistance, 0.15F + tierScale * 0.25F));
            }
            case SKIRMISHER -> {
                addMultiplier(mob, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER_ID, "Harder Mobs speed", Math.min(config.attributes.maxSpeedMultiplier - 1.0F, 0.08F + tierScale * 0.20F));
                addMultiplier(mob, Attributes.MAX_HEALTH, HEALTH_MODIFIER_ID, "Harder Mobs light frame", -0.10F);
            }
            case HEXER -> addMultiplier(mob, Attributes.FOLLOW_RANGE, KNOCKBACK_MODIFIER_ID, "Harder Mobs focus", 0.20F + tierScale * 0.25F);
            case BERSERKER -> addMultiplier(mob, Attributes.ATTACK_DAMAGE, DAMAGE_MODIFIER_ID, "Harder Mobs damage", Math.min(config.attributes.maxDamageMultiplier - 1.0F, 0.15F + tierScale * 0.25F));
            case GUARDIAN -> {
                addValue(mob, Attributes.ARMOR, ARMOR_MODIFIER_ID, "Harder Mobs armor", 2.0D + tier.ordinal() * 1.5D);
                addValue(mob, Attributes.ARMOR_TOUGHNESS, TOUGHNESS_MODIFIER_ID, "Harder Mobs toughness", tier.ordinal());
            }
            case COMMANDER -> addMultiplier(mob, Attributes.MAX_HEALTH, HEALTH_MODIFIER_ID, "Harder Mobs commander", Math.min(config.attributes.maxHealthMultiplier - 1.0F, 0.20F + tierScale * 0.35F));
        }
        mob.setHealth(mob.getMaxHealth());
    }

    private static void addMultiplier(Mob mob, Holder<Attribute> attribute, UUID id, String name, double value) {
        AttributeInstance instance = mob.getAttribute(attribute);
        if (instance != null && value != 0.0D) {
            ResourceLocation modifierId = modifierId(id);
            instance.removeModifier(modifierId);
            instance.addPermanentModifier(new AttributeModifier(modifierId, value, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    private static void addValue(Mob mob, Holder<Attribute> attribute, UUID id, String name, double value) {
        AttributeInstance instance = mob.getAttribute(attribute);
        if (instance != null && value != 0.0D) {
            ResourceLocation modifierId = modifierId(id);
            instance.removeModifier(modifierId);
            instance.addPermanentModifier(new AttributeModifier(modifierId, value, AttributeModifier.Operation.ADD_VALUE));
        }
    }

    private static ResourceLocation modifierId(UUID id) {
        return ResourceLocation.fromNamespaceAndPath(TheAfterdark.MOD_ID, id.toString());
    }

    private static void applyEffects(Mob mob, Config.HarderMobsConfig config, Tier tier, Role role) {
        int duration = tier == Tier.NEMESIS ? -1 : 20 * 60 * 8;
        Holder<MobEffect> effect = switch (role) {
            case HARDENED -> MobEffects.GLOWING;
            case BRUISER, GUARDIAN -> MobEffects.DAMAGE_RESISTANCE;
            case SKIRMISHER -> MobEffects.MOVEMENT_SPEED;
            case HEXER -> MobEffects.INVISIBILITY;
            case BERSERKER -> MobEffects.DAMAGE_BOOST;
            case COMMANDER -> MobEffects.GLOWING;
        };
        ResourceLocation id = effect.unwrapKey().map(ResourceKey::location).orElse(null);
        if (id == null || config.effects.effectBlacklist.contains(id.toString())) {
            return;
        }
        int amplifier = tier == Tier.NEMESIS ? 1 : 0;
        mob.addEffect(new MobEffectInstance(effect, duration, amplifier, true, config.visuals.useParticles));
    }

    private static void applyVisuals(Mob mob, ServerLevel world, Config.HarderMobsConfig config, Tier tier, Role role) {
        if (config.visuals.showEliteNames && tier.ordinal() >= Tier.RARE.ordinal()) {
            mob.setCustomName(Component.literal(tier.displayName + " " + role.displayName));
            mob.setCustomNameVisible(tier.ordinal() >= Tier.ELITE.ordinal());
        }
        if (config.visuals.useGlowingForNemesis && tier == Tier.NEMESIS) {
            mob.setGlowingTag(true);
        }
        if (config.visuals.useParticles) {
            SimpleParticleType particle = switch (role) {
                case HARDENED -> ParticleTypes.SOUL;
                case BRUISER, GUARDIAN -> ParticleTypes.SOUL;
                case SKIRMISHER -> ParticleTypes.CRIT;
                case HEXER -> ParticleTypes.WITCH;
                case BERSERKER -> ParticleTypes.SMOKE;
                case COMMANDER -> ParticleTypes.GLOW;
            };
            world.sendParticles(particle, mob.getX(), mob.getY() + mob.getBbHeight() * 0.7D, mob.getZ(), 12 + tier.ordinal() * 6, 0.35D, 0.45D, 0.35D, 0.02D);
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

        private static Role pick(RandomSource random) {
            Role[] values = {BRUISER, SKIRMISHER, HEXER, BERSERKER, GUARDIAN, COMMANDER};
            return values[random.nextInt(values.length)];
        }
    }

    private enum ArmorTheme {
        ROOTED(
                new Holder[]{ArmorMaterials.LEATHER, ArmorMaterials.CHAIN, ArmorMaterials.IRON},
                new int[]{0x2D2418, 0x3A4A2A, 0x5A3B24},
                new ResourceKey[]{TrimMaterials.COPPER, TrimMaterials.EMERALD, TrimMaterials.QUARTZ},
                new ResourceKey[]{TrimPatterns.WILD, TrimPatterns.WARD, TrimPatterns.HOST}
        ),
        FROSTED(
                new Holder[]{ArmorMaterials.LEATHER, ArmorMaterials.CHAIN, ArmorMaterials.IRON, ArmorMaterials.DIAMOND},
                new int[]{0xD4E5F2, 0x8AAEC8, 0x5C6F86},
                new ResourceKey[]{TrimMaterials.IRON, TrimMaterials.DIAMOND, TrimMaterials.LAPIS},
                new ResourceKey[]{TrimPatterns.TIDE, TrimPatterns.SPIRE, TrimPatterns.WAYFINDER}
        ),
        CURSED(
                new Holder[]{ArmorMaterials.CHAIN, ArmorMaterials.GOLD, ArmorMaterials.IRON},
                new int[]{0x1B1024, 0x34204A, 0x123629},
                new ResourceKey[]{TrimMaterials.AMETHYST, TrimMaterials.LAPIS, TrimMaterials.REDSTONE},
                new ResourceKey[]{TrimPatterns.EYE, TrimPatterns.SILENCE, TrimPatterns.VEX}
        ),
        EMBER(
                new Holder[]{ArmorMaterials.LEATHER, ArmorMaterials.GOLD, ArmorMaterials.IRON},
                new int[]{0x3A1A0D, 0x713018, 0xA54A1F},
                new ResourceKey[]{TrimMaterials.REDSTONE, TrimMaterials.COPPER, TrimMaterials.GOLD},
                new ResourceKey[]{TrimPatterns.RIB, TrimPatterns.SNOUT, TrimPatterns.DUNE}
        ),
        DEEP(
                new Holder[]{ArmorMaterials.CHAIN, ArmorMaterials.IRON, ArmorMaterials.DIAMOND},
                new int[]{0x15171F, 0x202C3A, 0x30404F},
                new ResourceKey[]{TrimMaterials.LAPIS, TrimMaterials.AMETHYST, TrimMaterials.NETHERITE},
                new ResourceKey[]{TrimPatterns.WARD, TrimPatterns.SILENCE, TrimPatterns.RIB}
        );

        private final Holder<ArmorMaterial>[] preferredMaterials;
        private final int[] leatherColors;
        private final ResourceKey<TrimMaterial>[] trimMaterials;
        private final ResourceKey<TrimPattern>[] trimPatterns;

        ArmorTheme(Holder<ArmorMaterial>[] preferredMaterials, int[] leatherColors, ResourceKey<TrimMaterial>[] trimMaterials, ResourceKey<TrimPattern>[] trimPatterns) {
            this.preferredMaterials = preferredMaterials;
            this.leatherColors = leatherColors;
            this.trimMaterials = trimMaterials;
            this.trimPatterns = trimPatterns;
        }

        private boolean prefers(ArmorItem armorItem) {
            Holder<ArmorMaterial> material = armorItem.getMaterial();
            for (Holder<ArmorMaterial> preferredMaterial : preferredMaterials) {
                if (material == preferredMaterial || material.equals(preferredMaterial)) {
                    return true;
                }
            }
            return false;
        }

        private static ArmorTheme pick(Role role, RandomSource random) {
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
