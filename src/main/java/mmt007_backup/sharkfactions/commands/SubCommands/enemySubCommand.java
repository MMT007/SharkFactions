package mmt007_backup.sharkfactions.commands.SubCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.Utilitis;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class enemySubCommand extends SubCommand {
    @Override
    public String getName() {
        return "enemy";
    }

    @Override
    public String getDescription() {
        return "Faz a Facção ser sua Inimiga";
    }

    @Override
    public String getSyntax() {
        return "Enemy <Nome da Facção>";
    }

    @Override
    public void perform(Player plr, String[] args) {
        if(args.length != 1) {
            plr.sendMessage("§7[Factions] §b/f " + getSyntax());
        }

        String facn = args[0];
        Factions PFac = JsonTableUtil.getFaction(JsonTableUtil.getPlayer(plr.getUniqueId().toString()).getFuuid());
        Factions fac = null;

        for (Factions f : JsonTableUtil.factions) {
            if (Objects.equals(f.getName().toLowerCase(), facn.toLowerCase())) {
                fac = f;
                break;
            }
        }

        if (fac == null) {
            plr.sendMessage(languageUtil.getMessage("faction-nonExistent")
                    .replaceAll("%fac%",facn));
        } else if (!Objects.equals(plr.getUniqueId().toString(), PFac.getOwner())) {
            plr.sendMessage(languageUtil.getMessage("cant-perform-action"));
        } else {
            for(String f : PFac.getAllys()){
                if(Objects.equals(f, fac.getUuid())){
                    plr.sendMessage(languageUtil.getMessage("faction-allied"));
                    return;
                }
            }
            for(String f : PFac.getEnemies()){
                if(Objects.equals(f, fac.getUuid())){
                    plr.sendMessage(languageUtil.getMessage("faction-alreadyEnemy"));
                    return;
                }
            }

            ArrayList<String> list = PFac.getEnemies();
            ArrayList<String> listo = fac.getEnemies();

            list.add(fac.getUuid());
            listo.add(PFac.getUuid());

            PFac.setEnemies(list);
            fac.setEnemies(listo);

            JsonTableUtil.updateFaction(fac);
            JsonTableUtil.updateFaction(PFac);

            Player EFOwner = Utilitis.getBukkitPlayer(JsonTableUtil.getPlayer(fac.getOwner()));

            if (EFOwner != null){
                EFOwner.sendMessage(languageUtil.getMessage("faction-enemy")
                        .replaceAll("%fac%",PFac.getName()));
            }
            plr.sendMessage(languageUtil.getMessage("faction-enemy")
                    .replaceAll("%fac%",facn));
        }
    }
}
