package mmt007_backup.sharkfactions.commands.subCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageMngr;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.models.Invite;
import mmt007_backup.sharkfactions.models.Players;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import org.bukkit.entity.Player;

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
    public String getPermission() {return "sharkfactions." + getName();}

    @Override
    public String[] getAutoComplete() {return new String[]{capitalize(getName())};}

    @Override
    public String getSyntax() {
        return "Deny";
    }

    @Override
    public void perform(Player plr, String[] args) {
        if (JsonTableUtil.getPlayer(plr).getFuuid().equals("")) {
            plr.sendMessage(languageMngr.getMessage("faction-hasNone"));
        }

        Players p = JsonTableUtil.getPlayer(plr.getUniqueId().toString());
        Factions fac = JsonTableUtil.getFaction(plr);

        switch (p.getInvite().getType()){
            case ALLY -> {
                fac.setInvite(Invite.getEmpty());
                plr.sendMessage(languageMngr.getMessage("faction-ally-denied"));
                JsonTableUtil.updateFaction(fac);
            }
            case TRUCE -> {
                fac.setInvite(Invite.getEmpty());
                plr.sendMessage(languageMngr.getMessage("faction-truce-denied"));
                JsonTableUtil.updateFaction(fac);
            }
            case FACTIONINVITE -> {
                p.setInvite(Invite.getEmpty());
                plr.sendMessage(languageMngr.getMessage("faction-invite-denied"));
                JsonTableUtil.updatePlayer(p);
            }
        }
    }
}
