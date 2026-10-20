package mcsrbot.benchmark.mixin;

import mcsrbot.benchmark.screen.BenchmarkScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {

    @Shadow
    protected abstract <T extends AbstractButtonWidget> T addButton(T button);

    @Inject(
        method = "init()V",
        at = @At("TAIL")
    )
    private void addBenchmarkButton(CallbackInfo ci) {
        TitleScreen screen = (TitleScreen) (Object) this;

        int x = screen.width / 2 - 100;
        int y = screen.height / 4 + 96;

        addButton(new ButtonWidget(
            x,
            y,
            200,
            20,
            new LiteralText("Speedrun Benchmark"),
            button -> MinecraftClient.getInstance().openScreen(
                new BenchmarkScreen(screen)
            )
        ));
    }
}