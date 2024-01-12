package mmt007_backup.sharkfactions.commands.subCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.Utilitis;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

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

        Factions PFac = JsonTableUtil.getFaction(JsonTableUtil.getPlayer(plr.getUniqueId().toString()).getFuuid());
        Factions fac = JsonTableUtil.getFaction(args[0]);

        
        if (fac == null) {
            plr.sendMessage(languageUtil.getMessage("faction-nonExistent")
                    .replaceAll("%fac%",args[0]));
            return;
        }

        if(fac.getUuid().equals("")){
            plr.sendMessage(languageUtil.getMessage("faction-hasNone"));
            return;
        }

        if (!Utilitis.isFactionOwner(plr)) {
            plr.sendMessage(languageUtil.getMessage("cant-perform-action"));
            return;
        }
        
        
        
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
                .replaceAll("%fac%",args[0]));
        
    }
}
