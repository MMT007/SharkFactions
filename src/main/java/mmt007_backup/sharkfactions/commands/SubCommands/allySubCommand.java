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

import java.util.Objects;
import java.util.UUID;

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
    public String getSyntax() {
        return "Ally <Nome da Facção>";
    }

    @Override
    public void perform(Player plr, String[] args) {
        //--Checks If There's Only One Command Argument
        if(args.length != 1) {
            plr.sendMessage("§7[Factions] §b/f " + getSyntax());
            return;
        }

        //--Initializes Factions Variables
        Factions fac = JsonTableUtil.getFactionByName(args[0]);
        Factions PFac = JsonTableUtil.getFactionByPlayer(plr);

        //--Check If Player Has a Factions / Owner Permissions
        //--Also Checks If Faction Given Is Real
        if(PFac.getName().equals("")){
            plr.sendMessage(languageUtil.getMessage("faction-hasNone"));
            return;
        }

        if (!PFac.getOwner().equalsIgnoreCase(plr.getUniqueId().toString())) {
            plr.sendMessage(languageUtil.getMessage("cant-perform-action"));
            return;
        }

        if (fac.getName().equals("")) {
            plr.sendMessage(languageUtil.getMessage("faction-nonExistent")
                    .replaceAll("%fac%", args[0]));
            return;
        }

        //--Creates Invite And Updates Faction
        fac.setInvite(new Invite(PFac.getUuid(), InviteType.ALLY));
        JsonTableUtil.updateFaction(fac);

        //--Sends Ally Invite Sent Message
        plr.sendMessage(languageUtil.getMessage("faction-ally-sent")
                .replaceAll("%fac%", fac.getName()));

        //-- Gets Other Factions Owner And Sends The Invite Received Message
        Player facOwner = Utilitis.getBukkitPlayer(JsonTableUtil.getPlayer(fac.getOwner()));

        if (facOwner != null) {
            facOwner.sendMessage(languageUtil.getMessage("faction-ally-invite-received")
                    .replaceAll("%fac%",PFac.getName()));
        }
    }
}
