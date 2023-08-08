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
        //--Initializes Chunk and Faction Variables
        Chunk chunk = Objects.requireNonNull(plr.getLocation().getChunk());
        Factions fac = JsonTableUtil.getFactionByPlayer(plr);

        //--Checks If Player Has Owner Permission / Faction
        if (!Objects.equals(plr.getUniqueId().toString(), fac.getOwner())) {
            plr.sendMessage(languageUtil.getMessage("cant-perform-action"));
            return;
        }
        if (Objects.equals(JsonTableUtil.getPlayer(plr.getUniqueId().toString()).getFuuid(), "")) {
            plr.sendMessage(languageUtil.getMessage("faction-hasNone"));
            return;
        }

        //--Initializes Chunk Variables And Get Faction Chunks
        int chnkX = chunk.getX();
        int chnkZ = chunk.getZ();

        ArrayList<FChunk> ch = fac.getChunks();
        FChunk chunkToBeRemoved = new FChunk(chnkX, chnkZ, chunk.getWorld().getName());

        //--Checks If Faction Has Chunks, If None, Send Message And Return;
        if (ch.size() == 0) {
            plr.sendMessage(languageUtil.getMessage("chunk-none"));
            return;
        }

        //--Checks If Faction Has The Chunk Claimed, If Not, Send Message And Return;
        if(!ch.contains(chunkToBeRemoved)){
            plr.sendMessage(languageUtil.getMessage("chunk-notClaimed"));
            return;
        }

        //--Removes Chunk From Faction Claims, Updates Faction And Sends Message
        ch.remove(chunkToBeRemoved);
        fac.setChunks(ch);

        JsonTableUtil.updateFaction(fac);

        plr.sendMessage(languageUtil.getMessage("chunk-unClaimed")
                .replaceAll("%coords%", "[" + chnkX + " " + chnkZ + "]"));
    }
}
