package mmt007_backup.sharkfactions;

import java.util.Objects;

import mmt007_backup.sharkfactions.commands.CommandManager;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.menu.MenuMngr;
import mmt007_backup.sharkfactions.utils.helpSubCommandConsts;
import mmt007_backup.sharkfactions.utils.listSubCommandConsts;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;

public final class SharkFMain extends JavaPlugin implements Listener {
    private static SharkFMain plugin;

    public SharkFMain() {
    }

    public void onEnable() {
        plugin = this;

        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(new ChatHandler(), this);
        this.getServer().getPluginManager().registerEvents(new GeneralEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new MenuMngr(), this);

        (Objects.requireNonNull(this.getCommand("Factions"))).setExecutor(new CommandManager());

        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();

        JsonTableUtil.loadTables();
        helpSubCommandConsts.loadPages();
        listSubCommandConsts.loadPages();
        languageUtil.loadMessages();
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        JsonTableUtil.createPlayer(e.getPlayer());
    }

    public static SharkFMain getPlugin() {
        return plugin;
    }
}
