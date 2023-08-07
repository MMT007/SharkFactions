package mmt007_backup.sharkfactions.commands.SubCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.Invite;
import mmt007_backup.sharkfactions.models.InviteType;
import mmt007_backup.sharkfactions.models.Players;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class inviteSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getDescription() {
        return "Convida O Player Para Sua Facção";
    }

    @Override
    public String getSyntax() {
        return "Invite <Nome do Player>";
    }

    @Override
    public void perform(Player plr, String[] args) {
        if(args.length != 1) {
            plr.sendMessage("§7[Factions] §b/f " + getSyntax());
            return;
        }
        Player plr2 = Bukkit.getServer().getPlayerExact(args[0]);
        if (plr2 == null) {
            plr.sendMessage(languageUtil.getMessage("player-nonExistent"));
        } else if (Objects.equals(JsonTableUtil.getPlayer(plr.getUniqueId().toString()).getFuuid(), "")) {
            plr.sendMessage(languageUtil.getMessage("facntion-hasNone"));
        } else if (!Objects.equals(JsonTableUtil.getPlayer(plr2.getUniqueId().toString()).getFuuid(), "")) {
            plr.sendMessage(languageUtil.getMessage("faction-otherAlreadyOn")
                    .replaceAll("%plr%",plr.getName()));
        } else {
            Players splr = new Players(
                    plr2.getUniqueId().toString(),
                    "",
                    new Invite(JsonTableUtil.getFactionByPlayer(plr).getUuid(), InviteType.FACTIONINVITE));
            JsonTableUtil.updatePlayer(splr);
            plr.sendMessage(languageUtil.getMessage("faction-otherInvited")
                    .replaceAll("%plr%",plr2.getName()));
            plr2.sendMessage(languageUtil.getMessage("faction-invited")
                    .replaceAll("%fac%",JsonTableUtil.getFactionByPlayer(plr).getName()));
        }
    }
}
