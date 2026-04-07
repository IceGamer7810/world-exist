package com.icegamer7810;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class WorldExistPlugin extends JavaPlugin {
    private WorldExistenceService worldExistenceService;

    @Override
    public void onEnable() {
        this.worldExistenceService = new WorldExistenceService();
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->
            event.registrar().register(
                DimensionCommand.create(this.worldExistenceService),
                "Checks whether a world exists",
                java.util.List.of("dm")
            )
        );
    }
}
