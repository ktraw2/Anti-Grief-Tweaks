package io.github.ktraw2.disablesculkcatalyst;

import io.wispforest.owo.config.annotation.Config;

@Config(name = "disable-sculk-catalyst", wrapperName = "ModConfig")
public class ModConfigModel {
    public boolean enableGamerule = true;
    public boolean defaultSetting = false;
}
