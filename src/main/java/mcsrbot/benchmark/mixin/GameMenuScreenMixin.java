package mcsrbot.benchmark.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mcsrbot.benchmark.BenchmarkScreen;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init()V", at = @At("TAIL"))
    private void addBenchmarkButton(CallbackInfo ci) {
        int buttonWidth = 30;
        int buttonHeight = 20;

        // The GameMenuScreen layout differs from TitleScreen:
        // - Buttons are laid out in a grid starting at (width/2 - 102, height/4 + 8)
        // - "Back to Game" button is at top-left of the grid, aligned at x = width/2 - 102
        // Place the BM button to the left of that column, vertically at the same row.
        int x = this.width / 2 - 102 - buttonWidth - 4;
        int y = this.height / 4 + 8;

        this.addButton(new ButtonWidget(
            x,
            y,
            buttonWidth,
            buttonHeight,
            new LiteralText("BM"),
            button -> MinecraftClient.getInstance().openScreen(
                new BenchmarkScreen(this)
            )
        ));
    }
}