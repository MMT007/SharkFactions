package mmt007_backup.sharkfactions.commands.SubCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.FChunk;
import mmt007_backup.sharkfactions.models.FLocation;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Objects;

public class sethomeSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "sethome";
    }

    @Override
    public String getDescription() {
        return "Coloca a Home da Facção";
    }

    @Override
    public String getSyntax() {
        return "Sethome";
    }

    @Override
    public void perform(Player plr, String[] args) {
        if (Objects.equals(JsonTableUtil.getPlayer(plr.getUniqueId().toString()).getFuuid(), "")) {
            plr.sendMessage(languageUtil.getMessage("faction-hasNone"));
        } else if (!Objects.equals(plr.getUniqueId().toString(), JsonTableUtil.getFactionByPlayer(plr).getOwner())) {
            plr.sendMessage(languageUtil.getMessage("cant-perform-action"));
        } else if (plr.getLocation().getY() <= 0.0 && plr.getLocation().getY() % 1.0 == 0.0) {
            plr.sendMessage(languageUtil.getMessage("player-onNonFullBlock"));
        } else {
            double x = plr.getLocation().getX();
            double y = plr.getLocation().getY();
            double z = plr.getLocation().getZ();
            x = Math.floor(x) + 0.5;
            z = Math.floor(z) + 0.5;
            Chunk chk = plr.getLocation().getChunk();
            String world = chk.getWorld().getName().toLowerCase();
            Factions fac = JsonTableUtil.getFactionByPlayer(plr);
            FLocation location = new FLocation(x, y, z, world);
            FChunk chunk = new FChunk(chk.getX(), chk.getZ(), world);
            if (JsonTableUtil.isChunkFromPlayerFactions(plr, chunk) != 1) {
                plr.sendMessage(languageUtil.getMessage("home-canPlaceOnClaim"));
            } else {
                fac.setBaseLocation(location);
                JsonTableUtil.updateFaction(fac);
                plr.sendMessage(languageUtil.getMessage("home-placed"));
            }
        }

    }
}
