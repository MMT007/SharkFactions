package mmt007_backup.sharkfactions;

import java.util.Objects;

import mmt007_backup.sharkfactions.commands.CommandManager;
import mmt007_backup.sharkfactions.lang.languageMngr;
import mmt007_backup.sharkfactions.menu.MenuMngr;
import mmt007_backup.sharkfactions.utils.helpSubCommandConsts;
import mmt007_backup.sharkfactions.utils.listSubCommandConsts;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.protectedChunkUtil;

public final class SharkFMain extends JavaPlugin implements Listener {
    private static SharkFMain plugin;
    private static Economy economy;
    private static Permission permission;

    public SharkFMain() {
    }

    public void onEnable() {
        plugin = this;
        setupOtherPlugins();

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
        languageMngr.loadMessages();
        protectedChunkUtil.load();
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        JsonTableUtil.createPlayer(e.getPlayer());
    }

    private static void setupPermission(){
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);

        if(rsp == null) return;
        permission = rsp.getProvider();

        Bukkit.getLogger().info("[[ SFactions ]] Permissões Abilitadas!");
        Bukkit.getLogger().info("[[ SFactions ]] Permissões: "+ permission.getName());
    }

    private static boolean setupEconomy(){
        if(!plugin.getConfig().getBoolean("use-economy")) return true;
        if(plugin.getServer().getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;

        economy = rsp.getProvider();

        Bukkit.getLogger().info("[[ SFactions ]] Economia Abilitada!");
        Bukkit.getLogger().info("[[ SFactions ]] Economia: "+ economy.getName());

        return economy != null;
    }

    private static void setupOtherPlugins(){
        if(!setupEconomy()){
            Bukkit.getLogger().severe("[[ SFactions ]] O Plugin Vault NÂO Está Instalado, Desabilitando Plugin");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

        setupPermission();
    }

    public static SharkFMain getPlugin() {
        return plugin;
    }
    public static Economy getEconomy() {
        return economy;
    }
    public static Permission getPermission() {return permission;}
}
