package mcsrbot.benchmark.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mcsrbot.benchmark.BenchmarkScreen;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    // Must provide a constructor that calls the parent class constructor
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init()V", at = @At("TAIL"))
    private void addBenchmarkButton(CallbackInfo ci) {
        // Small square button: width 20, height 20
        // X coordinate: aligned to the left edge of the normal buttons (center - 100), then shifted left by 24 pixels
        int buttonWidth = 30;
        int buttonHeight = 20;
        int x = this.width / 2 - 100 - buttonWidth - 4;
        int y = this.height / 4 + 48; // Align with the top of the normal buttons (roughly the position of the singleplayer button)

        this.addButton(new ButtonWidget(
            x,
            y,
            buttonWidth,
            buttonHeight,
            new LiteralText("BM"), // Short text, or leave empty
            button -> MinecraftClient.getInstance().openScreen(
                new BenchmarkScreen(this)
            )
        ));
    }
}