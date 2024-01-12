package mmt007_backup.sharkfactions.commands.subCommands;

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
        //--Initializes Player
        Players plr = JsonTableUtil.getPlayer(player.getUniqueId().toString());
        Player bukkitPlayer = Utilitis.getBukkitPlayer(plr);

        //--Initializes Factions
        Factions fac = JsonTableUtil.getFaction(plr.getFuuid());
        Factions otherFac;
        Invite invite;

        if(bukkitPlayer == null){return;}

        //--If Player Has No Faction, Means Invite Type Must Be {FACTIONINVITE} OR {NONE}
        if(fac.getUuid().equals("")){
            //-----------BRANCH: Player Invite {FACTIONINVITE : NONE} -----------

            //--Gets Invite, Check If There Are Any Invites, If Not, Send Message, Return.
            invite = plr.getInvite();
            if(invite.getType().equals(InviteType.NONE)){
                bukkitPlayer.sendMessage(languageUtil.getMessage("faction-notInvited"));
                return;
            }

            //--Gets Faction That Player Was Invited
            Factions invitedFac = JsonTableUtil.getFaction(invite.getUUID());

            //--Updates Player And Faction, Sends Message To Everyone.
            plr.setInvite(Invite.getEmpty());
            plr.setFuuid(invitedFac.getUuid());
            fac.setMembers(fac.getMembers() + 1);

            JsonTableUtil.updateFaction(fac);
            JsonTableUtil.updatePlayer(plr);

            sendNewPlayerMessage(fac.getUuid(), plr);
        }else{
            //-----------BRANCH: Faction Invite {ALLY : TRUCE : NONE} -----------

            //--Gets Invite And Checks If Player Has Owner Permission...
            //--If Not, Sends Message, Return.
            invite = fac.getInvite();

            if(!fac.getOwner().equals(plr.getUuid())){
                bukkitPlayer.sendMessage(languageUtil.getMessage("cant-perform-action"));
                return;
            }

            //--Gets Other Faction
            otherFac = JsonTableUtil.getFaction(invite.getUUID());

            //--Checks If Invite Is {ALLY : TRUCE : NONE}
            //--If None, Send Message, Return;
            if(invite.getType().equals(InviteType.ALLY)){
                //------ IF INVITE {ALLY}

                //--Set Them Both As Their Allies
                //--Sends Message To Everyone.
                ArrayList<String> otherAllies = otherFac.getAllys();
                ArrayList<String> allies = fac.getAllys();

                otherAllies.add(fac.getUuid());
                allies.add(invite.getUUID());

                fac.setAllys(allies);
                otherFac.setAllys(otherAllies);

                sendFactionMessage(fac,InviteType.ALLY,otherFac);
            } else if(invite.getType().equals(InviteType.TRUCE)) {
                //-------- IF INVITE {TRUCE}

                //--Removes Both From Their Enemies
                //--Sends Message To Everyone.
                ArrayList<String> otherEnemies = otherFac.getEnemies();
                ArrayList<String> enemies = fac.getEnemies();

                otherEnemies.remove(fac.getUuid());
                enemies.remove(invite.getUUID());

                fac.setEnemies(enemies);
                otherFac.setEnemies(otherEnemies);

                sendFactionMessage(fac,InviteType.TRUCE,otherFac);
            }else{
                bukkitPlayer.sendMessage(languageUtil.getMessage("faction-noFactionInvites"));
                return;
            }

            //--Updates Both Factions And Removes Invite From Faction
            fac.setInvite(Invite.getEmpty());
            JsonTableUtil.updateFaction(fac);
            JsonTableUtil.updateFaction(otherFac);
        }
    }

    private void sendNewPlayerMessage(String facUUID, Players newPlayer){
        //--Gets All Players From A Faction And New Player's Name
        ArrayList<Players> players = JsonTableUtil.getPlayersInFaction(facUUID);
        String newPlayerName =
                Utilitis.getBukkitPlayer(newPlayer) == null ?
                       Utilitis.getOfflineBukkitPlayer(newPlayer).getName() :
                        Utilitis.getBukkitPlayer(newPlayer).getName();

        //--Loops For Every Player And Checks If They are Online
        //--Sends The New Player Message.
        for(Players plr : players){
            Player bukkitPlr = Utilitis.getBukkitPlayer(plr);
            if (bukkitPlr == null){continue;}

            bukkitPlr.sendMessage(languageUtil.getMessage("faction-new-member")
                    .replaceAll("%plr%",newPlayerName));
        }
    }

    private static void sendFactionMessage(Factions fac, InviteType type, Factions otherFac){
        //--Sets Message Type
        String Message;

        if(type.equals(InviteType.ALLY)){Message = "faction-ally";}
        else{Message = "faction-truce";}

        //--Gets Players From Both Factions
        ArrayList<Players> players = JsonTableUtil.getPlayersInFaction(fac.getUuid());
        ArrayList<Players> otherPlayers = JsonTableUtil.getPlayersInFaction(otherFac.getUuid());

        //--Loops For Every Player In Both Faction And Sends Message Accordingly
        for(Players plr : players){
            Player bukkitPlr = Utilitis.getBukkitPlayer(plr);
            if (bukkitPlr == null){continue;}

            bukkitPlr.sendMessage(languageUtil.getMessage(Message)
                    .replaceAll("%fac%",otherFac.getName()));
        }

        for(Players plr : otherPlayers){
            Player bukkitPlr = Utilitis.getBukkitPlayer(plr);
            if (bukkitPlr == null){continue;}

            bukkitPlr.sendMessage(languageUtil.getMessage(Message)
                    .replaceAll("%fac%",fac.getName()));

        }
    }
}
