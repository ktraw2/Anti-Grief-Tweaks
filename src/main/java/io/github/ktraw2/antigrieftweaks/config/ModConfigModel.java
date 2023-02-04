package io.github.ktraw2.antigrieftweaks.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Nest;

@Modmenu(modId = "anti-grief-tweaks")
@Config(name = "anti-grief-tweaks", wrapperName = "ModConfig")
public class ModConfigModel {
    @Nest
    public DisableSculkCatalyst disableSculkCatalyst = new DisableSculkCatalyst();
    public String[] disableTNTDimensions = {};

    public static class DisableSculkCatalyst {
        public boolean enableGamerule = true;
        public boolean defaultSetting = false;
    }
}
