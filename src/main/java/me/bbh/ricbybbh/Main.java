package me.bbh.ricbybbh;

import me.bbh.ricbybbh.commands.Toggle;
import me.bbh.ricbybbh.events.Event;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private static Main instance;

    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.register();
    }

    private void register() {
        new Event(this);
        new Toggle(this);
    }
}
