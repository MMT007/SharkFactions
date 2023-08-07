package mmt007_backup.sharkfactions;

import mmt007_backup.sharkfactions.commands.SubCommands.*;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.menu.MenuMngr;
import mmt007_backup.sharkfactions.menu.models.InputType;
import mmt007_backup.sharkfactions.menu.models.playerOnInput;
import mmt007_backup.sharkfactions.models.Players;
import mmt007_backup.sharkfactions.utils.Utilitis;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;

import java.util.ArrayList;
import java.util.UUID;

public class ChatHandler implements Listener {
    Plugin main = SharkFMain.getPlugin();

    public ChatHandler() {
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        String onConfigJoin = "";
        Player plr = e.getPlayer();
        if (plr.hasPlayedBefore()) {
            onConfigJoin = languageUtil.getMessage("player-join");
        } else {
            onConfigJoin = languageUtil.getMessage("player-first-join");
        }

        assert onConfigJoin != null;

        onConfigJoin = onConfigJoin.replaceAll("%plr%", plr.getName());
        onConfigJoin = ChatColor.translateAlternateColorCodes('&', onConfigJoin);

        e.setJoinMessage(onConfigJoin);
    }

    @EventHandler
    public void onLeaveEvent(PlayerQuitEvent e) {
        Player plr = e.getPlayer();

        String onConfigJoin = languageUtil.getMessage("player-leave");

        assert onConfigJoin != null;

        onConfigJoin = onConfigJoin.replaceAll("%plr%", plr.getName());
        onConfigJoin = ChatColor.translateAlternateColorCodes('&', onConfigJoin);

        e.setQuitMessage(onConfigJoin);
    }

    @EventHandler
    public void sendMessageEvent(AsyncPlayerChatEvent e) {
        String msg = e.getMessage();
        Player plr = e.getPlayer();
        Factions fac = JsonTableUtil.getFaction(JsonTableUtil.getPlayer(plr.getUniqueId().toString()).getFuuid());
        playerOnInput poi = MenuMngr.getPlayerOnInput(plr);

        if(poi.getType() != InputType.NONE && poi.isOnInput()){
            String[] args = msg.split(" ");

            switch (poi.getType()){
                case CREATE -> new createSubCommand().perform(plr,args);
                case ALLY -> new allySubCommand().perform(plr,args);
                case ENEMY -> new enemySubCommand().perform(plr,args);
                case INVITE -> new inviteSubCommand().perform(plr,args);
                case KICK -> new kickSubCommand().perform(plr,args);
                case INFO -> new infoSubCommand().perform(plr,args);
                case TRUCE -> new truceSubCommand().perform(plr,args);
            }

            poi.setOnInput(false);
            poi.setType(InputType.NONE);
            MenuMngr.setPlayerOnInput(poi);
            e.setCancelled(true);
            return;
        }

        if(msg.charAt(0) == '.'){
            ArrayList<Players> plrs = JsonTableUtil.getPlayersInFaction(fac.getUuid());
            String isMember = fac.getOwner().equals(plr.getUniqueId().toString()) ?
                    languageUtil.getMessage("faction-owner-prefix"):
                    languageUtil.getMessage("faction-member-prefix");

            for(Players p : plrs){
                Player bplr = Utilitis.getBukkitPlayer(p);
                if(bplr != null){
                    bplr.sendMessage(isMember + "§b " + plr.getName() + " >> " + msg.replaceFirst(".", ""));
                }
            }
            e.setCancelled(true);
            return;
        }

        String onConfigFormat = languageUtil.getMessage("chat-format");
        String facTag = fac.getTag();

        assert onConfigFormat != null;

        onConfigFormat = onConfigFormat.replaceAll("%plr%", plr.getName());
        onConfigFormat = onConfigFormat.replaceAll("%msg%", msg);
        onConfigFormat = onConfigFormat.replaceAll("%fac%", facTag);
        onConfigFormat = ChatColor.translateAlternateColorCodes('&', onConfigFormat);
        e.setFormat(onConfigFormat);
    }
}
