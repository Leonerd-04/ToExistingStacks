package Ieonerd04.ToExistingStacks.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

//Handles the screen that appears when using ModMenu
//I'd like to credit TerraformersMC again, as this code is from the Config in ModMenu
@Environment(EnvType.CLIENT)
public class ToExistingStacksConfigScreen extends GameOptionsScreen {
    private OptionListWidget buttonList;

    public ToExistingStacksConfigScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.of("SimpleHUD Options"));
    }

    protected void init(){
        buttonList = new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        for(SimpleOption<Boolean> option : ToExistingStacksConfig.OPTIONS) buttonList.addSingleOptionEntry(option);
        this.addDrawableChild(buttonList);

        this.addDrawableChild(
                ButtonWidget.builder(
                    Text.of("Done"),
                    (button) -> this.client.setScreen(this.parent)
                ).dimensions(this.width / 2 - 100, this.height - 27, 200, 20).build()
        );
    }

    public void removed(){
        ToExistingStacksConfig.save();
    }
}
