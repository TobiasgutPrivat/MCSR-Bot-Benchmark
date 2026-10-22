package mcsrbot.benchmark;

import mcsrbot.benchmark.mixin.CreateWorldScreenAccessor;
import mcsrbot.benchmark.mixin.MoreOptionsDialogAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;

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

        if (client.world != null) {
            client.world.disconnect();
            client.disconnect(
                    new net.minecraft.client.gui.screen.SaveLevelScreen(
                            new net.minecraft.text.TranslatableText(
                                    "menu.savingLevel"
                            )
                    )
            );

            client.openScreen(new net.minecraft.client.gui.screen.TitleScreen());

            // Start creation on a later client task.
            client.execute(() -> openCreateWorldScreen(parent, seed));
            return;
        }

        openCreateWorldScreen(parent, seed);
    }

    private static void openCreateWorldScreen(Screen parent, String seed) {
        MinecraftClient client = MinecraftClient.getInstance();

        CreateWorldScreen screen = new CreateWorldScreen(parent);
        client.openScreen(screen);

        CreateWorldScreenAccessor accessor =
                (CreateWorldScreenAccessor) screen;

        String worldName =
                "benchmark_" + seed + "_" + System.currentTimeMillis();

        accessor.mcsrbot$getLevelNameField().setText(worldName);

        MoreOptionsDialogAccessor dialogAccessor =
                (MoreOptionsDialogAccessor)
                        accessor.mcsrbot$getMoreOptionsDialog();

        dialogAccessor.mcsrbot$getSeedTextField()
                .setText(String.valueOf(seedToLong(seed)));

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