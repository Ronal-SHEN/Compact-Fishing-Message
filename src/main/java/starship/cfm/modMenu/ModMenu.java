package starship.cfm.modMenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import starship.cfm.CompactFishingMessage;

public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ConfigScreen.buildScreen(CompactFishingMessage.getInstance(), parent);
    }
    // TODO: add open config file button
}