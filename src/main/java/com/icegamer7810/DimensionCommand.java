package com.icegamer7810;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class DimensionCommand {
    private static final String PASS_KEY = "commands.execute.conditional.pass_count";
    private static final String FAIL_KEY = "commands.execute.conditional.fail_count";
    private static final SimpleCommandExceptionType NO_ENTITY_FOUND = new SimpleCommandExceptionType(new LiteralMessage("No entity was found"));

    private DimensionCommand() {
    }

    public static LiteralCommandNode<CommandSourceStack> create(final WorldExistenceService worldExistenceService) {
        return Commands.literal("dimension")
            .then(Commands.literal("check")
                .then(Commands.argument("input", VanillaScoreHolderArgument.INSTANCE)
                    .executes(context -> {
                        final String input = context.getArgument("input", String.class);
                        final String worldName = resolveWorldName(context.getSource().getSender(), input);
                        return reply(context.getSource().getSender(), worldExistenceService.exists(worldName));
                    })))
            .build();
    }

    private static String resolveWorldName(final CommandSender sender, final String input) throws CommandSyntaxException {
        if (!input.startsWith("@")) {
            return StringArgumentType.string().parse(new com.mojang.brigadier.StringReader(input));
        }

        final List<Entity> entities = sender.getServer().selectEntities(sender, input);
        if (entities.isEmpty()) {
            throw NO_ENTITY_FOUND.create();
        }

        final Entity entity = entities.getFirst();
        return entity instanceof Player player ? player.getName() : entity.getUniqueId().toString();
    }

    private static int reply(final CommandSender sender, final boolean exists) {
        final int result = exists ? Command.SINGLE_SUCCESS : 0;
        sender.sendMessage(Component.translatable(exists ? PASS_KEY : FAIL_KEY, Component.text(result)));
        return result;
    }
}
