package com.icegamer7810;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public final class DimensionCommand {
    private DimensionCommand() {
    }

    public static LiteralCommandNode<CommandSourceStack> create(final WorldExistenceService worldExistenceService) {
        return Commands.literal("dimension")
            .then(Commands.literal("check")
                .then(Commands.argument("target", ArgumentTypes.entity())
                    .executes(context -> {
                        final EntitySelectorArgumentResolver resolver = context.getArgument("target", EntitySelectorArgumentResolver.class);
                        final Entity entity = resolver.resolve(context.getSource()).getFirst();
                        final String worldName = entity.getWorld().getName();
                        return reply(context.getSource().getSender(), worldExistenceService.exists(worldName));
                    }))
                .then(Commands.argument("name", StringArgumentType.string())
                    .executes(context -> {
                        final String worldName = StringArgumentType.getString(context, "name");
                        return reply(context.getSource().getSender(), worldExistenceService.exists(worldName));
                    })))
            .build();
    }

    private static int reply(final CommandSender sender, final boolean exists) {
        final int result = exists ? Command.SINGLE_SUCCESS : 0;
        sender.sendPlainMessage(Integer.toString(result));
        return result;
    }
}
