package mmt007_backup.sharkfactions.commands.subCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageMngr;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class homeSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "home";
    }

    @Override
    public String getDescription() {
        return "Teleporta Para a Home da Facção";
    }

    @Override
    public String getPermission() {return "sharkfactions." + getName();}

    @Override
    public String[] getAutoComplete() {return new String[]{capitalize(getName())};}

    @Override
    public String getSyntax() {
        return "Home";
    }

    @Override
    public void perform(Player plr, String[] args) {
        Factions fac = JsonTableUtil.getFaction(plr);
        if (fac.getUuid().equals("")) {
            plr.sendMessage(languageMngr.getMessage("faction-hasNone"));
            return;
        }
        if (fac.getBaseLocation().getY() <= 0.0) {
            plr.sendMessage(languageMngr.getMessage("faction-noHome"));
            return;
        }

        World world = Bukkit.getWorld(fac.getBaseLocation().getWorld());
        Location home = new Location(world, fac.getBaseLocation().getX(), fac.getBaseLocation().getY(), fac.getBaseLocation().getZ());
        plr.sendMessage(languageMngr.getMessage("faction-teleporting"));
        plr.teleport(home);

    }
}
