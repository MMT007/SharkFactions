package mmt007_backup.sharkfactions.commands.subCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageMngr;
import mmt007_backup.sharkfactions.models.FChunk;
import mmt007_backup.sharkfactions.models.FLocation;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.Utilitis;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

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
    public String getPermission() {return "sharkfactions." + getName();}

    @Override
    public String[] getAutoComplete() {return new String[]{capitalize(getName())};}

    @Override
    public String getSyntax() {
        return "Sethome";
    }

    @Override
    public void perform(Player plr, String[] args) {
        Factions fac = JsonTableUtil.getFaction(plr);

        if (fac.getUuid().equals("")) {
            plr.sendMessage(languageMngr.getMessage("faction-hasNone"));
            return;
        }

        if (!Utilitis.isFactionOwner(plr)) {
            plr.sendMessage(languageMngr.getMessage("cant-perform-action"));
            return;
        }
        if (plr.getLocation().getY() % 1.0 == 0.0) {
            plr.sendMessage(languageMngr.getMessage("player-onNonFullBlock"));
            return;
        }

            double x = plr.getLocation().getX();
            double y = plr.getLocation().getY();
            double z = plr.getLocation().getZ();

            x = Math.floor(x) + 0.5;
            z = Math.floor(z) + 0.5;

            Chunk chk = plr.getLocation().getChunk();
            String world = chk.getWorld().getName().toLowerCase();
            FLocation location = new FLocation(x, y, z, world);
            FChunk chunk = new FChunk(chk.getX(), chk.getZ(), world);

            if (JsonTableUtil.isChunkFromPlayerFactions(plr, chunk) != 1) {
                plr.sendMessage(languageMngr.getMessage("home-canPlaceOnClaim"));
                return;
            }


            fac.setBaseLocation(location);JsonTableUtil.updateFaction(fac);
            plr.sendMessage(languageMngr.getMessage("home-placed"));
        }
}
