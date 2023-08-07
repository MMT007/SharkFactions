package mmt007_backup.sharkfactions.commands.SubCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.models.Invite;
import mmt007_backup.sharkfactions.models.InviteType;
import mmt007_backup.sharkfactions.models.Players;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import org.bukkit.entity.Player;

import java.util.Objects;

public class denySubCommand extends SubCommand {
    @Override
    public String getName() {
        return "deny";
    }

    @Override
    public String getDescription() {
        return "Negar Pedidos de Facção";
    }

    @Override
    public String getSyntax() {
        return "Deny";
    }

    @Override
    public void perform(Player plr, String[] args) {
        if (Objects.equals(JsonTableUtil.getPlayer(plr.getUniqueId().toString()).getFuuid(), "")) {
            plr.sendMessage(languageUtil.getMessage("faction-hasNone"));
        }

        Players p = JsonTableUtil.getPlayer(plr.getUniqueId().toString());
        Factions fac = JsonTableUtil.getFactionByPlayer(plr);
        if(p.getInvite().getType().equals(InviteType.FACTIONINVITE)){
            p.setInvite(Invite.getEmpty());
            plr.sendMessage(languageUtil.getMessage("faction-invite-denied"));
            JsonTableUtil.updatePlayer(p);
        }else
        if(fac.getInvite().getType().equals(InviteType.ALLY)){
            fac.setInvite(Invite.getEmpty());
            plr.sendMessage(languageUtil.getMessage("faction-ally-denied"));
            JsonTableUtil.updateFaction(fac);
        }else
        if(fac.getInvite().getType().equals(InviteType.TRUCE)){
            fac.setInvite(Invite.getEmpty());
            plr.sendMessage(languageUtil.getMessage("faction-truce-denied"));
            JsonTableUtil.updateFaction(fac);
        }
    }
}
