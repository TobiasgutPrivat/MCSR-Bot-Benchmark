package mcsrbot.benchmark.screen;

import mcsrbot.benchmark.WorldLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class BenchmarkScreen extends Screen {
    private final Screen parent;
    private TextFieldWidget seedField;
    private String status = "";

    public BenchmarkScreen(Screen parent) {
        super(new LiteralText("Speedrun Benchmark"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2;
        int y = this.height / 2;

        seedField = new TextFieldWidget(
                textRenderer,
                x - 100,
                y - 35,
                200,
                20,
                new LiteralText("Seed")
        );
        seedField.setMaxLength(128);
        seedField.setSuggestion("Enter a seed...");
        seedField.setVisible(true);
        addChild(seedField);

        addButton(new ButtonWidget(
                x - 100,
                y - 5,
                200,
                20,
                new LiteralText("Random World"),
                button -> {
                    WorldLoader.runRandom(this);
                }
        ));

        addButton(new ButtonWidget(
                x - 100,
                y + 20,
                200,
                20,
                new LiteralText("Run Seed"),
                button -> {
                    String seed = seedField.getText().trim();

                    if (seed.isEmpty()) {
                        status = "Enter a seed first.";
                        return;
                    }

                    WorldLoader.runSeed(this, seed);
                }
        ));

        addButton(new ButtonWidget(
                x - 100,
                y + 50,
                200,
                20,
                new LiteralText("Benchmark Sets"),
                button -> {
                    status = "Benchmark sets coming next.";
                }
        ));

        addButton(new ButtonWidget(
                x - 100,
                y + 80,
                200,
                20,
                new LiteralText("Back"),
                button -> client.openScreen(parent)
        ));
    }

    @Override
    public void render(
            MatrixStack matrices,
            int mouseX,
            int mouseY,
            float delta
    ) {
        renderBackground(matrices);

        drawCenteredText(
                matrices,
                textRenderer,
                title,
                width / 2,
                height / 2 - 75,
                0xFFFFFF
        );

        // Buttons first, then the text field on top so it can't be covered
        super.render(matrices, mouseX, mouseY, delta);
        seedField.render(matrices, mouseX, mouseY, delta);

        if (!status.isEmpty()) {
            drawCenteredText(
                    matrices,
                    textRenderer,
                    new LiteralText(status),
                    width / 2,
                    height - 25,
                    0xFFFFFF
            );
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Let the seed field grab focus on click before other widgets consume it
        if (seedField != null && seedField.mouseClicked(mouseX, mouseY, button)) {
            setFocused(seedField);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Ensure typing goes into the field when it's focused
        if (seedField != null && seedField.isFocused()
                && seedField.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}