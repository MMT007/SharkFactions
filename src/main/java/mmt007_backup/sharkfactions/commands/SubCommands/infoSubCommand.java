package mmt007_backup.sharkfactions.commands.SubCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.models.Players;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.Utilitis;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class infoSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Mostra Informações Sobre A Facção";
    }

    @Override
    public String getSyntax() {
        return "Info";
    }

    @Override
    public void perform(Player plr, String[] args) {
        if(args.length != 1) {
            plr.sendMessage("§7[Factions] §b/f " + getSyntax());
            return;
        }

        String facn = args[0];
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
            return;
        }

        plr.sendMessage("""
                §7[Factions] §b--------------------------------------------------
                §7[Factions] §8%name - [ %tag ]
                §7[Factions]
                §7[Factions] §bMembros: %members
                §7[Factions] §3Aliados: %allys
                §7[Factions] §cInimigos: %enemies
                §7[Factions] §b---------------------------------------------------
                """.replace("%name", fac.getName())
                .replace("%tag", fac.getTag())
                .replace("%members", this.getMemberList(fac))
                .replace("%allys", this.getAllysList(fac))
                .replace("%enemies", this.getEnemiesList(fac)));
    }
    private String getMemberList(Factions fac){
        StringBuilder members = new StringBuilder();

        for(Players plr : JsonTableUtil.players){
            if(Objects.equals(plr.getFuuid(), fac.getUuid())){
                String plrn = "";
                Player p = Utilitis.getBukkitPlayer(plr);
                if(p == null){
                    OfflinePlayer oPlr = Utilitis.getOfflineBukkitPlayer(plr);
                    plrn = "§7" + oPlr.getName();
                }else{plrn = "§f" + p.getName();}

                members.append(plrn).append(" §b- ");
            }
        }
        if(members.length() > 5){members.delete(members.length() - 5, members.length());}

        return members.toString();
    }
    private String getAllysList(Factions fac){
        StringBuilder allys = new StringBuilder();

        for(String ally : fac.getAllys()){
            for(Factions f : JsonTableUtil.factions){
                if(Objects.equals(f.getUuid(), ally)){
                    allys.append("§3").append(f.getName()).append(" §b- ");
                }
            }
        }
        if(allys.length() > 5){allys.delete(allys.length() - 5, allys.length());}

        return allys.toString();
    }
    private String getEnemiesList(Factions fac){
        StringBuilder enemies = new StringBuilder();

        for(String enemy : fac.getEnemies()){
            for(Factions f : JsonTableUtil.factions){
                if(Objects.equals(f.getUuid(), enemy)){
                    enemies.append("§c").append(f.getName()).append(" §b- ");
                }
            }
        }
        if(enemies.length() > 5){enemies.delete(enemies.length() - 5, enemies.length());}

        return enemies.toString();
    }
}
