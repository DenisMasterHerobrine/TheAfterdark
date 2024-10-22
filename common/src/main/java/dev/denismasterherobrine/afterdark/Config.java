package dev.denismasterherobrine.afterdark;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, Config.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}