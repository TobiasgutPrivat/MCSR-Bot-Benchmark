package mcsrbot.benchmark;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BenchmarkManager {
    private static final Gson GSON =
        new GsonBuilder().setPrettyPrinting().create();
    
    private static final Path FILE =
        FabricLoader.getInstance().getConfigDir()
        .resolve("speedrun-benchmark-sets.json");

    private final List<BenchmarkSet> sets = new ArrayList<>();

    private BenchmarkSet activeSet;
    private List<String> activeSeeds = new ArrayList<>();
    private int currentIndex = 0;

    public BenchmarkManager() {
        loadSets();
    }

    public List<BenchmarkSet> getSets() {
        return sets;
    }

    public void addSet(BenchmarkSet set) {
        sets.add(set);
        saveSets();
    }

    public void removeSet(BenchmarkSet set) {
        sets.remove(set);
        saveSets();
    }

    public void saveSets() {
        try {
            Files.createDirectories(FILE.getParent());

            Files.write(
                    FILE,
                    GSON.toJson(sets).getBytes(StandardCharsets.UTF_8)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSets() {
        if (!Files.exists(FILE)) {
            return;
        }

        try {
            String json = new String(
                    Files.readAllBytes(FILE),
                    StandardCharsets.UTF_8
            );

            Type type = new TypeToken<List<BenchmarkSet>>() {}.getType();
            List<BenchmarkSet> loaded = GSON.fromJson(json, type);

            if (loaded != null) {
                sets.clear();
                sets.addAll(loaded);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String randomSeed() {
        return Long.toString(
                ThreadLocalRandom.current().nextLong()
        );
    }

    public void startSet(BenchmarkSet set) {
        activeSet = set;
        currentIndex = 0;

        activeSeeds = new ArrayList<>();

        if (set.randomSeeds) {
            for (int i = 0; i < set.randomSeedCount; i++) {
                activeSeeds.add(randomSeed());
            }
        } else {
            activeSeeds.addAll(set.seeds);
        }
    }

    public String getCurrentSeed() {
        if (currentIndex < 0 || currentIndex >= activeSeeds.size()) {
            return null;
        }

        return activeSeeds.get(currentIndex);
    }

    public boolean advanceSet() {
        if (activeSet == null) {
            return false;
        }

        currentIndex++;

        return currentIndex < activeSeeds.size();
    }

    public boolean hasActiveSet() {
        return activeSet != null;
    }

    public void finishSet() {
        activeSet = null;
        activeSeeds.clear();
        currentIndex = 0;
    }
}