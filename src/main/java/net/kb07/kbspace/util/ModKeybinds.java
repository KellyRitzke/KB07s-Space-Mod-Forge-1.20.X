package net.kb07.kbspace.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;


public class ModKeybinds {
    public static final String CATEGORY = "key.categories.kbspace";
    public static final KeyMapping LAUNCH_ROCKET = new KeyMapping(
            "key.kbspace.launch", // Translation key
            InputConstants.KEY_SPACE, // Default key (Spacebar)
            CATEGORY // Keybind category
    );

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(LAUNCH_ROCKET);
    }
}
