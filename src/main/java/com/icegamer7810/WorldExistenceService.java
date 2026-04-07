package com.icegamer7810;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public final class WorldExistenceService {
    private static final String MULTIVERSE_PLUGIN_NAME = "Multiverse-Core";

    public WorldExistenceService() {
    }

    public boolean exists(final String rawWorldName) {
        final String worldName = rawWorldName == null ? "" : rawWorldName.trim();
        if (worldName.isEmpty()) {
            return false;
        }

        return this.existsAsLoadedWorld(worldName)
            || this.existsAsWorldFolder(worldName)
            || this.existsInMultiverse(worldName);
    }

    private boolean existsAsLoadedWorld(final String worldName) {
        return Bukkit.getWorld(worldName) != null;
    }

    private boolean existsAsWorldFolder(final String worldName) {
        final File worldContainer = Bukkit.getWorldContainer();
        final Path worldPath = worldContainer.toPath().resolve(worldName);
        return Files.isRegularFile(worldPath.resolve("level.dat"));
    }

    private boolean existsInMultiverse(final String worldName) {
        final Plugin multiverse = Bukkit.getPluginManager().getPlugin(MULTIVERSE_PLUGIN_NAME);
        if (multiverse == null || !multiverse.isEnabled()) {
            return false;
        }

        final File worldsFile = multiverse.getDataFolder().toPath().resolve("worlds.yml").toFile();
        if (!worldsFile.isFile()) {
            return false;
        }

        final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(worldsFile);
        final ConfigurationSection worldsSection = configuration.getConfigurationSection("worlds");
        if (worldsSection == null) {
            return false;
        }

        if (worldsSection.contains(worldName)) {
            return true;
        }

        final String expected = worldName.toLowerCase(Locale.ROOT);
        for (final String key : worldsSection.getKeys(false)) {
            if (key.equalsIgnoreCase(worldName)) {
                return true;
            }

            final ConfigurationSection worldSection = worldsSection.getConfigurationSection(key);
            if (worldSection == null) {
                continue;
            }

            final String alias = worldSection.getString("alias");
            if (alias != null && alias.toLowerCase(Locale.ROOT).equals(expected)) {
                return true;
            }
        }

        return false;
    }
}
