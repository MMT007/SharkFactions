package mmt007_backup.sharkfactions.commands.SubCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.models.Invite;
import mmt007_backup.sharkfactions.models.InviteType;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.Utilitis;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class truceSubCommand extends SubCommand {

    @Override
    public String getName() {
        return "truce";
    }

    @Override
    public String getDescription() {
        return "Declara Um Tratado Paz A Um Inimigo";
    }

    @Override
    public String getSyntax() {
        return "Truce <Nome Da Fac>";
    }

    @Override
    public void perform(Player plr, String[] args) {
        //--Checks If Args Only Contain 1 String [returns syntax if not]
        if(args.length != 1){
            plr.sendMessage("§7[Factions] /f" + getSyntax());
            return;
        }

        //--Initializes Player's Faction And The Faction They Want The Truce With
        Factions fac = JsonTableUtil.getFactionByPlayer(plr);
        Factions trucedFac = JsonTableUtil.getFactionByName(args[0]);


        //--If The Faction To Have The Truce With Is Null, Send Message And Return.
        if(trucedFac.getUuid().equals("")){
            plr.sendMessage(languageUtil.getMessage("faction-nonExistent")
                    .replaceAll("%fac%",args[0]));
            return;
        }

        //--Checks If Player Has Owner Permission/ Faction.
        if(!plr.getUniqueId().toString().equals(fac.getOwner())){
            plr.sendMessage(languageUtil.getMessage("cant-perform-action"));
            return;
        }else if(fac.getUuid().equals("")){
            plr.sendMessage(languageUtil.getMessage("faction-hasNone"));
            return;
        }

        //--Sets Faction Invite And Checks For The Other Faction Owner
        trucedFac.setInvite(new Invite(fac.getUuid(), InviteType.TRUCE));
        Player trucedFacOwner =
                Utilitis.getBukkitPlayer(JsonTableUtil.getPlayer(trucedFac.getOwner()));

        //--Checks If Owner Is Online And If Yes, Send Truce Message
        if(trucedFacOwner != null){
            trucedFacOwner.sendMessage(languageUtil.getMessage("faction-truce-invite-received")
                    .replaceAll("%fac%",fac.getName()));
        }

        plr.sendMessage(languageUtil.getMessage("faction-truce-sent"));
    }
}
