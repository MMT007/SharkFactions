package mmt007_backup.sharkfactions.commands.SubCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.models.Invite;
import mmt007_backup.sharkfactions.models.InviteType;
import mmt007_backup.sharkfactions.models.Players;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class kickSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getDescription() {
        return "Tira o Player de Sua Facção";
    }

    @Override
    public String getSyntax() {
        return "Kick <Nome do Player>";
    }

    @Override
    public void perform(Player plr, String[] args) {
        if(args.length != 1) {
            plr.sendMessage("§7[Factions] §b/f "+ getSyntax());
            return;
        }
        Player plr2 = Bukkit.getServer().getPlayerExact(args[0]);
        if (plr2 == null) {
            plr.sendMessage(languageUtil.getMessage("player-nonExistent"));
        } else if (Objects.equals(JsonTableUtil.getPlayer(plr.getUniqueId().toString()).getFuuid(), "")) {
            plr.sendMessage(languageUtil.getMessage("faction-hasNone"));
        } else {
            if (!Objects.equals(JsonTableUtil.getPlayer(plr2.getUniqueId().toString()).getFuuid(), "")) {
                plr.sendMessage(languageUtil.getMessage("faction-notOnFaction"));
            }

            Factions fac = JsonTableUtil.getFactionByPlayer(plr);
            plr.sendMessage(languageUtil.getMessage("faction-playerKicked")
                    .replaceAll("%plr%", plr2.getName()));
            String facName = fac.getName();
            plr2.sendMessage(languageUtil.getMessage("faction-kicked")
                    .replaceAll("%fac%", facName));
            fac.setMembers(fac.getMembers() - 1);
            Players rplr = new Players(
                    plr2.getUniqueId().toString(),
                    "",
                    new Invite("", InviteType.NONE));
            JsonTableUtil.updateFaction(fac);
            JsonTableUtil.updatePlayer(rplr);
        }
    }
}
