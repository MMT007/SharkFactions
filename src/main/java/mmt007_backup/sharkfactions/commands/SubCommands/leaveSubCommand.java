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
    public String getSyntax() {
        return "Leave";
    }

    @Override
    public void perform(Player plr, String[] args) {

        if (Objects.equals(plr.getUniqueId().toString(), JsonTableUtil.getFactionByPlayer(plr).getOwner())) {
            new disbandSubCommand().perform(plr, args);
        } else {
            if (!Objects.equals(JsonTableUtil.getPlayer(plr.getUniqueId().toString()).getFuuid(), "")) {
                plr.sendMessage(languageUtil.getMessage("faction-hasNone"));
            }
            Players dplr = new Players(
                    plr.getUniqueId().toString(),
                    "",
                    new Invite("", InviteType.NONE));
            Factions fac = JsonTableUtil.getFactionByPlayer(plr);
            fac.setMembers(fac.getMembers() - 1);
            JsonTableUtil.updateFaction(fac);
            JsonTableUtil.updatePlayer(dplr);
            plr.sendMessage(languageUtil.getMessage("faction-left"));
        }
    }
}
