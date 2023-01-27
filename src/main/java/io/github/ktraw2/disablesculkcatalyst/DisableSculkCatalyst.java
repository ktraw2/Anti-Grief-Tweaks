package io.github.ktraw2.disablesculkcatalyst;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.ktraw2.disablesculkcatalyst.ModConfig;

public class DisableSculkCatalyst implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("disable-sculk-catalyst");

	public static final ModConfig CONFIG = ModConfig.createAndLoad();
	public static final GameRules.Key<GameRules.BooleanRule> DISABLE_SCULK_CATALYST =
			(CONFIG.enableGamerule()) ?
					GameRuleRegistry.register("disableSculkCatalyst", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(CONFIG.defaultSetting()))
					: null;

	@Override
	public void onInitialize() {}
}
