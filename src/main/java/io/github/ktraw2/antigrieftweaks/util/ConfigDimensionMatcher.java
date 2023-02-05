package io.github.ktraw2.antigrieftweaks.util;

import io.github.ktraw2.antigrieftweaks.AntiGriefTweaks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;

import java.util.Arrays;

public class ConfigDimensionMatcher {
    public static boolean configContainsDimension(RegistryEntry<DimensionType> dimension) {
        return Arrays.stream(AntiGriefTweaks.CONFIG.disableTNTDimensions())
                .map(Identifier::new)
                .anyMatch(dimension::matchesId);
    }
}
