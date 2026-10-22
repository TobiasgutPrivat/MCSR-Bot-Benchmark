package mcsrbot.benchmark.mixin;

import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.OptionalLong;

@Mixin(MoreOptionsDialog.class)
public interface MoreOptionsDialogAccessor {
    @Accessor("seed")
    void mcsrbot$setSeed(OptionalLong seed);

    @Accessor("seed")
    OptionalLong mcsrbot$getSeed();
}