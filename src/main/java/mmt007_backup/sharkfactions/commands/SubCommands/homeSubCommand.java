package mmt007_backup.sharkfactions.commands.SubCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Objects;

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
    public String getSyntax() {
        return "Home";
    }

    @Override
    public void perform(Player plr, String[] args) {
        Factions fac = JsonTableUtil.getFactionByPlayer(plr);
        if (Objects.equals(JsonTableUtil.getPlayer(plr.getUniqueId().toString()).getFuuid(), "")) {
            plr.sendMessage(languageUtil.getMessage("faction-hasNone"));
        } else if (fac.getBaseLocation().getY() <= 0.0) {
            plr.sendMessage(languageUtil.getMessage("faction-noHome"));
        } else {
            World world = Bukkit.getWorld(fac.getBaseLocation().getWorld());
            Location home = new Location(world, fac.getBaseLocation().getX(), fac.getBaseLocation().getY(), fac.getBaseLocation().getZ());
            plr.sendMessage(languageUtil.getMessage("faction-teleporting"));
            plr.teleport(home);
        }
    }
}
