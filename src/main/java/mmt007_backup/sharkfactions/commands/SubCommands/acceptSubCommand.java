package mmt007_backup.sharkfactions.commands.SubCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.models.Invite;
import mmt007_backup.sharkfactions.models.InviteType;
import mmt007_backup.sharkfactions.models.Players;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.Utilitis;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class acceptSubCommand extends SubCommand {

    @Override
    public String getName() {
        return "accept";
    }

    @Override
    public String getDescription() {
        return "Aceitar Pedidos de Facção";
    }

    @Override
    public String getSyntax() {
        return "Accept";
    }

    @Override
    public void perform(Player player, String[] args) {
        Players plr = JsonTableUtil.getPlayer(player.getUniqueId().toString());
        Player bukkitPlayer = Utilitis.getBukkitPlayer(plr);

        Factions fac = JsonTableUtil.getFaction(plr.getFuuid());
        Factions otherFac = null;
        Invite invite;

        if(bukkitPlayer == null){
            return;
        }

        if(fac.getUuid().equals("")){
            invite = plr.getInvite();
            if(invite.getType().equals(InviteType.NONE)){
                bukkitPlayer.sendMessage(languageUtil.getMessage("faction-notInvited"));
                return;
            }

            Factions invitedFac = JsonTableUtil.getFaction(invite.getUUID());

            plr.setInvite(Invite.getEmpty());
            plr.setFuuid(invitedFac.getUuid());
            fac.setMembers(fac.getMembers() + 1);

            JsonTableUtil.updateFaction(fac);
            JsonTableUtil.updatePlayer(plr);

            sendNewPlayerMessage(fac.getUuid(), plr);
        }else{
            invite = fac.getInvite();

            if(!fac.getOwner().equals(plr.getUuid())){
                bukkitPlayer.sendMessage(languageUtil.getMessage("cant-perform-action"));
            }

            if(invite.getType().equals(InviteType.ALLY)){
                otherFac = JsonTableUtil.getFaction(invite.getUUID());

                ArrayList<String> otherAllies = otherFac.getAllys();
                ArrayList<String> allies = fac.getAllys();

                otherAllies.add(fac.getUuid());
                allies.add(invite.getUUID());

                fac.setAllys(allies);
                otherFac.setAllys(otherAllies);

                sendFactionMessage(fac,InviteType.ALLY,otherFac);
            } else if(invite.getType().equals(InviteType.TRUCE)) {
                otherFac = JsonTableUtil.getFaction(invite.getUUID());

                ArrayList<String> otherEnemies = otherFac.getEnemies();
                ArrayList<String> enemies = fac.getEnemies();

                otherEnemies.remove(fac.getUuid());
                enemies.remove(invite.getUUID());

                fac.setEnemies(enemies);
                otherFac.setEnemies(otherEnemies);

                sendFactionMessage(fac,InviteType.TRUCE,otherFac);
            }

            fac.setInvite(Invite.getEmpty());
            JsonTableUtil.updateFaction(fac);
            JsonTableUtil.updateFaction(otherFac);
        }
    }

    private void sendNewPlayerMessage(String facUUID, Players newPlayer){
        ArrayList<Players> players = JsonTableUtil.getPlayersInFaction(facUUID);
        String newPlayerName =
                Utilitis.getBukkitPlayer(newPlayer) == null ?
                       Utilitis.getOfflineBukkitPlayer(newPlayer).getName() :
                        Utilitis.getBukkitPlayer(newPlayer).getName();

        for(Players plr : players){
            Player bukkitPlr = Utilitis.getBukkitPlayer(plr);

            if (bukkitPlr == null){
                continue;
            }

            bukkitPlr.sendMessage(languageUtil.getMessage("faction-new-member")
                    .replaceAll("%plr%",newPlayerName));
        }
    }

    private static void sendFactionMessage(Factions fac, InviteType type, Factions otherFac){
        String Message;

        if(type.equals(InviteType.ALLY)){
            Message = "faction-ally";
        }else{
            Message = "faction-truce";
        }

        ArrayList<Players> players = JsonTableUtil.getPlayersInFaction(fac.getUuid());
        ArrayList<Players> otherPlayers = JsonTableUtil.getPlayersInFaction(otherFac.getUuid());

        for(Players plr : players){
            Player bukkitPlr = Utilitis.getBukkitPlayer(plr);

            if (bukkitPlr == null){
                continue;
            }

            bukkitPlr.sendMessage(languageUtil.getMessage(Message)
                    .replaceAll("%fac%",otherFac.getName()));
        }

        for(Players plr : otherPlayers){
            Player bukkitPlr = Utilitis.getBukkitPlayer(plr);

            if (bukkitPlr == null){
                continue;
            }

            bukkitPlr.sendMessage(languageUtil.getMessage(Message)
                    .replaceAll("%fac%",fac.getName()));

        }
    }
}
