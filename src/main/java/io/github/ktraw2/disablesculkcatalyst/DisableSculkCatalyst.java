package io.github.ktraw2.disablesculkcatalyst;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisableSculkCatalyst implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("disable-sculk-catalyst");
	public static final GameRules.Key<GameRules.BooleanRule> DISABLE_SCULK_CATALYST =
			GameRuleRegistry.register("disableSculkCatalyst", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(false));

	@Override
	public void onInitialize() {}
}
