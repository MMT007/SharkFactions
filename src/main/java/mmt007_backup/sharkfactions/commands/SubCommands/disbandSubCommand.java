package mmt007_backup.sharkfactions.commands.SubCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.models.Players;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

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
    public String getSyntax() {
        return "Disband";
    }

    @Override
    public void perform(Player plr, String[] args) {
        if (!Objects.equals(JsonTableUtil.getPlayer(plr.getUniqueId().toString()).getFuuid(), "")) {
            Factions fac = JsonTableUtil.getFactionByPlayer(plr);

            ArrayList<Players> fplrs = JsonTableUtil.getPlayersInFaction(fac.getUuid());
            for(Players p : fplrs) {
                p.setFuuid("");
                JsonTableUtil.updatePlayer(p);
            }

            JsonTableUtil.deleteFaction(fac.getUuid());
            plr.sendMessage(languageUtil.getMessage("faction-disbanded"));
        } else {
            plr.sendMessage(languageUtil.getMessage("faction-hasNone"));
        }
    }
}
