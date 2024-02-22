package mmt007_backup.sharkfactions.commands.subCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageMngr;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.models.Invite;
import mmt007_backup.sharkfactions.models.InviteType;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.Utilitis;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class allySubCommand extends SubCommand {
    @Override
    public String getName() {
        return "ally";
    }

    @Override
    public String getDescription() {
        return "Faz a Facção ser sua Aliada";
    }

    @Override
    public String getPermission() {return "sharkfactions." + getName();}

    @Override
    public String[] getAutoComplete() {return new String[]{capitalize(getName()),"add:remove","{name}"};}

    @Override
    public String getSyntax() {
        return "Ally <Add/Remove> <Nome da Facção>";
    }

    @Override
    public void perform(Player plr, String[] args) {
        //--Checks If There's Only One Command Argument
        if(args.length > 2) {
            plr.sendMessage("§7[Factions] §b/f " + getSyntax());
            return;
        }

        if (args[0].equalsIgnoreCase("add")){
            addAlly(plr, args);
        }else if (args[0].equalsIgnoreCase("remove")){
            removeAlly(plr,args);
        }else{
            plr.sendMessage("§7[Factions] §b/f " + getSyntax());
        }
    }

    private void addAlly(Player plr, String[] args){

        //--Initializes Factions Variables
        Factions fac = JsonTableUtil.getFaction(args[1]);
        Factions PFac = JsonTableUtil.getFaction(plr);

        //--Check If Player Has a Factions / Owner Permissions
        //--Also Checks If Faction Given Is Real
        if(PFac.getName().equals("")){
            plr.sendMessage(languageMngr.getMessage("faction-hasNone"));
            return;
        }

        if (!Utilitis.isFactionOwner(plr)) {
            plr.sendMessage(languageMngr.getMessage("cant-perform-action"));
            return;
        }

        if (fac.getName().equals("")) {
            plr.sendMessage(languageMngr.getMessage("faction-nonExistent")
                    .replaceAll("%fac%", args[1]));
            return;
        }

        //--Creates Invite And Updates Faction
        fac.setInvite(new Invite(PFac.getUuid(), InviteType.ALLY));
        JsonTableUtil.updateFaction(fac);

        //--Sends Ally Invite Sent Message
        plr.sendMessage(languageMngr.getMessage("faction-ally-sent")
                .replaceAll("%fac%", fac.getName()));

        //-- Gets Other Factions Owner And Sends The Invite Received Message
        Player facOwner = Utilitis.getBukkitPlayer(JsonTableUtil.getPlayer(fac.getOwner()));

        if (facOwner != null) {
            facOwner.sendMessage(languageMngr.getMessage("faction-ally-invite-received")
                    .replaceAll("%fac%",PFac.getName()));
        }

    }

    private void removeAlly(Player plr, String[] args){

        //--Initializes Factions Variables
        Factions fac = JsonTableUtil.getFaction(args[1]);
        Factions pFac = JsonTableUtil.getFaction(plr);

        //--Check If Player Has a Factions / Owner Permissions
        //--Also Checks If Faction Given Is Real
        if(pFac.getName().equals("")){
            plr.sendMessage(languageMngr.getMessage("faction-hasNone"));
            return;
        }

        if (!Utilitis.isFactionOwner(plr)) {
            plr.sendMessage(languageMngr.getMessage("cant-perform-action"));
            return;
        }

        if (fac.getName().equals("")) {
            plr.sendMessage(languageMngr.getMessage("faction-nonExistent")
                    .replaceAll("%fac%", args[1]));
            return;
        }


        //--Gets Allies And Remove Their UUID's
        ArrayList<String> F_allys = fac.getAllys();
        ArrayList<String> P_allys = pFac.getAllys();

        F_allys.remove(pFac.getUuid());
        P_allys.remove(fac.getUuid());

        fac.setAllys(F_allys);
        pFac.setAllys(P_allys);

        //--Updates Factions
        JsonTableUtil.updateFaction(fac);
        JsonTableUtil.updateFaction(pFac);

        //--Sends Ally Removed Message
        plr.sendMessage(languageMngr.getMessage("faction-ally-removed")
                .replaceAll("%fac%", fac.getName()));

        //-- Gets Other Factions Owner And Sends The Ally Removed Message
        Player facOwner = Utilitis.getBukkitPlayer(JsonTableUtil.getPlayer(fac.getOwner()));

        if (facOwner != null) {
            facOwner.sendMessage(languageMngr.getMessage("faction-ally-removed")
                    .replaceAll("%fac%",pFac.getName()));
        }

    }
}
