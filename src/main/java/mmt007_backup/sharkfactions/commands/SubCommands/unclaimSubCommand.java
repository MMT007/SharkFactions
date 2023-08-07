package mmt007_backup.sharkfactions.commands.SubCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.FChunk;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class unclaimSubCommand extends SubCommand {

    @Override
    public String getName() {
        return "unclaim";
    }

    @Override
    public String getDescription() {
        return "Abandona o Chunk que Você Está";
    }

    @Override
    public String getSyntax() {
        return "Unclaim";
    }

    @Override
    public void perform(Player plr, String[] args) {
        Chunk chunk = Objects.requireNonNull(plr.getLocation().getChunk());
        Factions fac = JsonTableUtil.getFactionByPlayer(plr);

        if (!Objects.equals(plr.getUniqueId().toString(), fac.getOwner())) {
            plr.sendMessage(languageUtil.getMessage("cant-perform-action"));
            return;
        }
        if (Objects.equals(JsonTableUtil.getPlayer(plr.getUniqueId().toString()).getFuuid(), "")) {
            plr.sendMessage(languageUtil.getMessage("faction-hasNone"));
            return;
        }

        int chnkX = chunk.getX();
        int chnkZ = chunk.getZ();

        ArrayList<FChunk> ch = fac.getChunks();
        FChunk chunkToBeRemoved = new FChunk(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());

        if (ch.size() == 0) {
            plr.sendMessage(languageUtil.getMessage("chunk-none"));
            return;
        }

        if(!ch.contains(chunkToBeRemoved)){
            plr.sendMessage(languageUtil.getMessage("chunk-notClaimed"));
            return;
        }


        ch.remove(chunkToBeRemoved);
        fac.setChunks(ch);
        JsonTableUtil.updateFaction(fac);
        plr.sendMessage(languageUtil.getMessage("chunk-unClaimed")
                .replaceAll("%coords%", "[" + chnkX + " " + chnkZ + "]"));
    }
}
