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
        if(args.length != 1){
            plr.sendMessage("§7[Factions] /f" + getSyntax());
            return;
        }

        Factions fac = JsonTableUtil.getFactionByPlayer(plr);
        Factions trucedFac = null;

        for(Factions f : JsonTableUtil.factions){
            if (f.getName().equalsIgnoreCase(args[0])){
                trucedFac = f;
                break;
            }
        }

        if(trucedFac == null){
            plr.sendMessage(languageUtil.getMessage("faction-nonExistent")
                    .replaceAll("%fac%",args[0]));
            return;
        }if(!plr.getUniqueId().toString().equals(fac.getOwner())){
            plr.sendMessage(languageUtil.getMessage("cant-perform-action"));
        }

        trucedFac.setInvite(new Invite(fac.getUuid(), InviteType.TRUCE));
        Player trucedFacOwner =
                Utilitis.getBukkitPlayer(JsonTableUtil.getPlayer(trucedFac.getOwner()));

        if(trucedFacOwner != null){
            trucedFacOwner.sendMessage(languageUtil.getMessage("faction-truce-invite-received")
                    .replaceAll("%fac%",fac.getName()));
        }

        plr.sendMessage(languageUtil.getMessage("faction-truce-sent"));
    }
}
