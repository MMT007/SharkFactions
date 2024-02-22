package mmt007_backup.sharkfactions.commands.subCommands;

import mmt007_backup.sharkfactions.SharkFMain;
import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageMngr;
import mmt007_backup.sharkfactions.models.FChunk;
import mmt007_backup.sharkfactions.models.Tuple;
import mmt007_backup.sharkfactions.utils.protectedChunkUtil;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class protectRegionSubCommand extends SubCommand {
    private static final Permission permission = SharkFMain.getPermission();
    @Override
    public String getName() {
        return "protectregion";
    }

    @Override
    public String getDescription() {
        return "Proteje Uma Região";
    }

    @Override
    public String getPermission() {return "sharkfactions.admin." + getName();}

    @Override
    public String getSyntax() {
        return "ProtectRegion <remove> <name> | <set> <name> <X1> <Z1> <X2> <Z2>";
    }

    @Override
    public String[] getAutoComplete() {return new String[]{capitalize(getName()),"remove:set","{region}"};}

    @Override
    public void perform(Player plr, String[] args) {
        if(!(plr.isOp() || permission.has(plr,getPermission()))){
           plr.sendMessage(languageMngr.getMessage("cant-perform-action"));
           return;
        }

        if(args.length < 2){
            plr.sendMessage("§7[Factions] §b/f " + getSyntax());
            return;
        }

        switch (args[0].toLowerCase()){
            case "remove" -> {
                protectedChunkUtil.removeRegion(args[1]);
                plr.sendMessage(languageMngr.getMessage("protectedArea-removed"));
            }
            case "set" -> {
                if(args.length < 6){
                    plr.sendMessage("§7[Factions] §b/f " + getSyntax());
                    return;
                }

                setRegion(plr, args);
                plr.sendMessage(languageMngr.getMessage("protectedArea-set"));
            }

            default -> plr.sendMessage("§7[Factions] §b/f " + getSyntax());
        }
    }

    private static void setRegion(Player plr,String[] args){
        String region = args[1];
        String world = plr.getWorld().toString();


        int x1 = Integer.parseInt(args[2]);
        int z1 = Integer.parseInt(args[3]);
        int x2 = Integer.parseInt(args[4]);
        int z2 = Integer.parseInt(args[5]);

        FChunk upper = new FChunk(Math.max(x1, x2),Math.max(z1, z2),world);
        FChunk lower = new FChunk(Math.min(x1, x2),Math.min(z1, z2),world);

        protectedChunkUtil.setChunk(region, new Tuple<>(upper,lower));
    }
}
