package mcsrbot.benchmark;

import mcsrbot.benchmark.mixin.CreateWorldScreenAccessor;
import mcsrbot.benchmark.mixin.MoreOptionsDialogAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;

import java.util.OptionalLong;
import java.util.concurrent.ThreadLocalRandom;

public final class WorldLoader {
    private WorldLoader() {
    }

    public static void runRandom(Screen parent) {
        String seed = Long.toString(
                ThreadLocalRandom.current().nextLong()
        );
        runSeed(parent, seed);
    }

    public static void runSeed(Screen parent, String seed) {
        if (seed == null || seed.trim().isEmpty()) {
            return;
        }
        createWorld(parent, seed.trim());
    }

    private static void createWorld(Screen parent, String seed) {
        MinecraftClient client = MinecraftClient.getInstance();

        // 1. Build the screen. init() runs when openScreen is called.
        CreateWorldScreen screen = new CreateWorldScreen(parent);
        client.openScreen(screen);

        // 2. Now the screen is initialised; safe to reach into it.
        CreateWorldScreenAccessor accessor = (CreateWorldScreenAccessor) screen;

        // 3. Give the world a unique name so saveDirectoryName doesn't collide.
        String worldName = "benchmark_" + seed + "_" + System.currentTimeMillis();
        accessor.mcsrbot$getLevelNameField().setText(worldName);

        // 4. Push the seed into MoreOptionsDialog.
        MoreOptionsDialog dialog = accessor.mcsrbot$getMoreOptionsDialog();
        MoreOptionsDialogAccessor dialogAccessor = (MoreOptionsDialogAccessor) dialog;
        dialogAccessor.mcsrbot$setSeed(OptionalLong.of(seedToLong(seed)));

        // 5. Kick off world creation.
        accessor.mcsrbot$invokeCreateLevel();
    }

    /**
     * Vanilla converts string seeds to a long the same way a
     * Java string hash would: via String.hashCode(). If the seed
     * is already numeric, parse it directly.
     */
    private static long seedToLong(String seed) {
        try {
            return Long.parseLong(seed);
        } catch (NumberFormatException ignored) {
            // Mirrors the behaviour of the seed field in vanilla:
            // non-numeric seeds are hashed.
            return seed.hashCode();
        }
    }
}