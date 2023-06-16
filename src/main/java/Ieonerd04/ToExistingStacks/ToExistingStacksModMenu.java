package Ieonerd04.ToExistingStacks;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import Ieonerd04.ToExistingStacks.config.ToExistingStacksConfigScreen;

public class ToExistingStacksModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory(){
        return ToExistingStacksConfigScreen::new;
    }
}
