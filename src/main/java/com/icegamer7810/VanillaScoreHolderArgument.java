package com.icegamer7810;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class VanillaScoreHolderArgument implements CustomArgumentType<String, Object> {
    public static final VanillaScoreHolderArgument INSTANCE = new VanillaScoreHolderArgument();

    private final ArgumentType nativeType;

    private VanillaScoreHolderArgument() {
        try {
            final Class<?> scoreHolderArgumentClass = Class.forName("net.minecraft.commands.arguments.ScoreHolderArgument");
            final Method factoryMethod = findFactoryMethod(scoreHolderArgumentClass);
            this.nativeType = (ArgumentType) invokeFactory(factoryMethod);
        } catch (final ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to initialize vanilla ScoreHolderArgument bridge", exception);
        }
    }

    @Override
    public String parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        this.nativeType.parse(reader);
        return reader.getString().substring(start, reader.getCursor());
    }

    @Override
    public <S> String parse(final StringReader reader, final S source) throws CommandSyntaxException {
        return this.parse(reader);
    }

    @Override
    public ArgumentType<Object> getNativeType() {
        return this.nativeType;
    }

    private static Method findFactoryMethod(final Class<?> scoreHolderArgumentClass) throws NoSuchMethodException {
        for (final Method method : scoreHolderArgumentClass.getMethods()) {
            if (!method.getName().equals("scoreHolder")) {
                continue;
            }

            if (method.getParameterCount() == 0) {
                return method;
            }

            if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == boolean.class) {
                return method;
            }
        }

        throw new NoSuchMethodException("No compatible scoreHolder factory found");
    }

    private static Object invokeFactory(final Method factoryMethod)
        throws InvocationTargetException, IllegalAccessException {
        if (factoryMethod.getParameterCount() == 0) {
            return factoryMethod.invoke(null);
        }

        return factoryMethod.invoke(null, true);
    }
}
