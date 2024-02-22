package mmt007_backup.sharkfactions.commands.subCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageMngr;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.models.Players;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.Utilitis;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class disbandSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "disband";
    }

    @Override
    public String getDescription() {
        return "Separa uma Facção";
    }

    @Override
    public String getPermission() {return "sharkfactions." + getName();}

    @Override
    public String[] getAutoComplete() {return new String[]{capitalize(getName())};}

    @Override
    public String getSyntax() {
        return "Disband";
    }

    @Override
    public void perform(Player plr, String[] args) {
        Factions fac = JsonTableUtil.getFaction(plr);

        if (fac.getUuid().equals("")) {
            plr.sendMessage(languageMngr.getMessage("faction-hasNone"));
            return;
        }

        if(!Utilitis.isFactionOwner(plr)){
            plr.sendMessage(languageMngr.getMessage("cant-perform-action"));
            return;
        }

            ArrayList<Players> fplrs = JsonTableUtil.getPlayersInFaction(fac.getUuid());
            for(Players p : fplrs) {
                p.setFuuid("");
                JsonTableUtil.updatePlayer(p);
            }

            JsonTableUtil.deleteFaction(fac.getUuid());
            plr.sendMessage(languageMngr.getMessage("faction-disbanded"));
    }
}
