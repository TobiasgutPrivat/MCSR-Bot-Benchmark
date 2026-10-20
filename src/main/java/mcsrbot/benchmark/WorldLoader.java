package mcsrbot.benchmark;

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

        openWorldCreation(parent, seed);
    }

    public static void runSeed(Screen parent, String seed) {
        if (seed == null || seed.trim().isEmpty()) {
            return;
        }

        openWorldCreation(parent, seed.trim());
    }

    private static void openWorldCreation(
            Screen parent,
            String seed
    ) {
        MinecraftClient client = MinecraftClient.getInstance();

        CreateWorldScreen screen = new CreateWorldScreen(parent);

        // TODO: Inject the seed into the vanilla creation settings.
        // TODO: Trigger createLevel() automatically after settings
        //       are initialized, once the 1.16.1 Mixin is added.

        client.openScreen(screen);
    }
}