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

        if(args.length != 1) {
            plr.sendMessage("§7[Factions] §b/f " + getSyntax());
            return;
        }
        String facn = args[0];
        Factions PFac = JsonTableUtil.getFaction(JsonTableUtil.getPlayer(plr.getUniqueId().toString()).getFuuid());
        if(Objects.equals(PFac.getUuid(), "")){
            plr.sendMessage(languageUtil.getMessage("faction-hasNone"));
            return;
        }
        if (!Objects.equals(PFac.getOwner(), plr.getUniqueId().toString())) {
            plr.sendMessage(languageUtil.getMessage("cant-perform-action"));
        } else {
            Factions fac = null;

            for (Factions f : JsonTableUtil.factions) {
                if (Objects.equals(f.getName().toLowerCase(), facn.toLowerCase())) {
                    fac = f;
                    break;
                }
            }

            if (fac == null) {
                plr.sendMessage(languageUtil.getMessage("faction-nonExistent").replaceAll("%fac%", facn));
            } else {
                fac.setInvite(new Invite(PFac.getUuid(), InviteType.ALLY));
                JsonTableUtil.updateFaction(fac);
                plr.sendMessage(languageUtil.getMessage("faction-ally-sent").replaceAll("%fac%",facn));

                Player faconwer = Utilitis.getBukkitPlayer(JsonTableUtil.getPlayer(fac.getOwner()));

                if (faconwer != null) {
                    faconwer.sendMessage(languageUtil.getMessage("faction-ally-invite-received")
                            .replaceAll("%fac%",PFac.getName()));
                }
            }
        }
    }
}
