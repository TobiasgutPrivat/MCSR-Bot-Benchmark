package mcsrbot.benchmark.mixin;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CreateWorldScreen.class)
public interface CreateWorldScreenAccessor {
    @Accessor("levelNameField")
    TextFieldWidget mcsrbot$getLevelNameField();

    @Accessor("moreOptionsDialog")
    net.minecraft.client.gui.screen.world.MoreOptionsDialog mcsrbot$getMoreOptionsDialog();

    @Accessor("saveDirectoryName")
    void mcsrbot$setSaveDirectoryName(String name);

    @Invoker("createLevel")
    void mcsrbot$invokeCreateLevel();
}
