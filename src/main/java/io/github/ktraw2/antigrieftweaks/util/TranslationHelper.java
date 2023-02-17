package io.github.ktraw2.antigrieftweaks.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;

import java.io.InputStreamReader;
import java.util.Objects;

public class TranslationHelper {
    private static final String LANG_PATH = "/assets/anti-grief-tweaks/lang/en_US.json";
    private static final JsonObject LANG_FILE = JsonParser.parseReader(new InputStreamReader(Objects.requireNonNull(TranslationHelper.class.getResourceAsStream(LANG_PATH)))).getAsJsonObject();
    private static final boolean IS_SERVER = FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER);

    public static Text getTranslatedText(final String key, final Object... args) {
        if (IS_SERVER) {
            return Text.of(LANG_FILE.get(key).getAsString().formatted(args));
        }

        return Text.translatable(key, args);
    }
}
