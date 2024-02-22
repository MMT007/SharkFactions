package mmt007_backup.sharkfactions.commands.subCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageMngr;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.models.Players;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.Utilitis;
import org.bukkit.entity.Player;

public class leaveSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return "Sai de Sua Facção";
    }

    @Override
    public String getPermission() {return "sharkfactions." + getName();}

    @Override
    public String[] getAutoComplete() {return new String[]{capitalize(getName())};}

    @Override
    public String getSyntax() {
        return "Leave";
    }

    @Override
    public void perform(Player plr, String[] args) {
        Players dplr = JsonTableUtil.getPlayer(plr.getUniqueId().toString());
        Factions fac = JsonTableUtil.getFaction(plr);

        if (fac.getUuid().equals("")) {
            plr.sendMessage(languageMngr.getMessage("faction-hasNone"));
            return;
        }

        if (Utilitis.isFactionOwner(plr)) {
            new disbandSubCommand().perform(plr, args);
            return;
        }

        fac.setMembers(fac.getMembers() - 1);
        dplr.setFuuid("");

        JsonTableUtil.updateFaction(fac);
        JsonTableUtil.updatePlayer(dplr);

        plr.sendMessage(languageMngr.getMessage("faction-left"));

    }
}
