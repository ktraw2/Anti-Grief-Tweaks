package io.github.ktraw2.antigrieftweaks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.ktraw2.antigrieftweaks.AntiGriefTweaks;
import io.github.ktraw2.antigrieftweaks.util.ConfigDimensionMatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.dimension.DimensionType;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AntiGriefTweaksCommand {
    public static void register(
            final CommandDispatcher<ServerCommandSource> dispatcher,
            final CommandRegistryAccess registryAccess,
            final CommandManager.RegistrationEnvironment ignoredEnvironment
    ){
        dispatcher.register(
                CommandManager.literal("antiGriefTweaks")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(
                                CommandManager.literal("disableTNTDimensions")
                                        .executes(context -> displayConfigValue(context, "disableTNTDimensions", Arrays.toString(AntiGriefTweaks.CONFIG.disableTNTDimensions())))
                                        .then(
                                                CommandManager.literal("add")
                                                        .then(dimensionArgument(registryAccess)
                                                                .executes(AntiGriefTweaksCommand::addDisabledTNTDimension)
                                                        )
                                        )
                                        .then(
                                                CommandManager.literal("remove")
                                                        .then(dimensionArgument(registryAccess)
                                                                .executes(AntiGriefTweaksCommand::removeDisabledTNTDimension)
                                                        )
                                        )
                        )
                        .then(
                                CommandManager.literal("disableSculkCatalyst")
                                        .then(
                                                disableSculkCatalystSubConfig("enableGamerule", AntiGriefTweaks.CONFIG.disableSculkCatalyst::enableGamerule, AntiGriefTweaks.CONFIG.disableSculkCatalyst::enableGamerule)
                                        )
                                        .then(
                                                disableSculkCatalystSubConfig("defaultSetting", AntiGriefTweaks.CONFIG.disableSculkCatalyst::defaultSetting, AntiGriefTweaks.CONFIG.disableSculkCatalyst::defaultSetting)
                                        )
                        )
        );
    }


    private static ArgumentBuilder<ServerCommandSource, ?> dimensionArgument(final CommandRegistryAccess registryAccess) {
        return CommandManager.argument("dimension", RegistryEntryArgumentType.registryEntry(registryAccess, RegistryKeys.DIMENSION_TYPE));
    }

    private static ArgumentBuilder<ServerCommandSource, ?> disableSculkCatalystSubConfig(
            String configName,
            Supplier<Boolean> getter,
            Consumer<Boolean> setter
    ) {
        final String qualifiedName = "disableSculkCatalyst." + configName;
        return CommandManager.literal(configName)
                .executes(context -> displayConfigValue(context, qualifiedName, String.valueOf(getter.get())))
                .then(
                        CommandManager.argument("value", BoolArgumentType.bool())
                                .executes(context -> {
                                    final boolean arg = BoolArgumentType.getBool(context, "value");
                                    setter.accept(arg);
                                    context.getSource().sendMessage(Text.translatable("command.anti-grief-tweaks.setConfigValue", qualifiedName, arg));
                                    return 1;
                                })
                );
    }

    private static int displayConfigValue(
            CommandContext<ServerCommandSource> context,
            String name,
            String value
    ) {
        context.getSource().sendMessage(Text.translatable("command.anti-grief-tweaks.displayConfigValue", name, value));
        return 1;
    }

    private static int addDisabledTNTDimension(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final RegistryEntry.Reference<DimensionType> arg = RegistryEntryArgumentType.getRegistryEntry(context, "dimension", RegistryKeys.DIMENSION_TYPE);
        final String dimensionKey = arg.registryKey().getValue().toString();
        if (ConfigDimensionMatcher.configContainsDimension(arg)) {
            context.getSource().sendMessage(Text.translatable("command.anti-grief-tweaks.disableTNTDimensions.add.configContainsDimension", dimensionKey));
            return 1;
        }

        final String[] tntDimensions = AntiGriefTweaks.CONFIG.disableTNTDimensions();

        final String[] newArray = Arrays.copyOf(tntDimensions, tntDimensions.length + 1);
        newArray[newArray.length - 1] = dimensionKey;

        AntiGriefTweaks.CONFIG.disableTNTDimensions(newArray);

        context.getSource().sendMessage(Text.translatable("command.anti-grief-tweaks.disableTNTDimensions.add.success", dimensionKey));

        return 1;
    }

    private static int removeDisabledTNTDimension(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final RegistryEntry.Reference<DimensionType> arg = RegistryEntryArgumentType.getRegistryEntry(context, "dimension", RegistryKeys.DIMENSION_TYPE);
        final String dimensionKey = arg.registryKey().getValue().toString();
        if (!ConfigDimensionMatcher.configContainsDimension(arg)) {
            context.getSource().sendMessage(Text.translatable("command.anti-grief-tweaks.disableTNTDimensions.remove.configNotContainsDimension", dimensionKey));
            return 1;
        }

        final String[] tntDimensions = AntiGriefTweaks.CONFIG.disableTNTDimensions();

        final String[] newArray = new String[tntDimensions.length - 1];
        int i = 0;
        for (String dimension : tntDimensions) {
            if (!Objects.equals(dimension, dimensionKey)) {
                newArray[i] = dimension;
                i++;
            }
        }

        AntiGriefTweaks.CONFIG.disableTNTDimensions(newArray);

        context.getSource().sendMessage(Text.translatable("command.anti-grief-tweaks.disableTNTDimensions.remove.success", dimensionKey));

        return 1;
    }
}
