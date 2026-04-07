package com.icegamer7810;

import com.mojang.brigadier.arguments.ArgumentType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class VanillaScoreHolderArgument {
    private static final ArgumentType NATIVE_TYPE = createNativeType();

    private VanillaScoreHolderArgument() {
    }

    public static ArgumentType<Object> nativeType() {
        return NATIVE_TYPE;
    }

    private static ArgumentType createNativeType() {
        try {
            final Class<?> scoreHolderArgumentClass = Class.forName("net.minecraft.commands.arguments.ScoreHolderArgument");
            final Method factoryMethod = findFactoryMethod(scoreHolderArgumentClass);
            return (ArgumentType) invokeFactory(factoryMethod);
        } catch (final ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to initialize vanilla ScoreHolderArgument bridge", exception);
        }
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

        return factoryMethod.invoke(null, false);
    }
}
