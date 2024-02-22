package mmt007_backup.sharkfactions.commands.subCommands;

import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageMngr;
import mmt007_backup.sharkfactions.models.FChunk;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.Utilitis;
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
    public String getPermission() {return "sharkfactions." + getName();}

    @Override
    public String[] getAutoComplete() {return new String[]{capitalize(getName())};}

    @Override
    public String getSyntax() {
        return "Unclaim";
    }

    @Override
    public void perform(Player plr, String[] args) {
        //--Initializes Chunk and Faction Variables
        Chunk chunk = Objects.requireNonNull(plr.getLocation().getChunk());
        Factions fac = JsonTableUtil.getFaction(plr);

        //--Checks If Player Has Owner Permission / Faction
        if (!Utilitis.isFactionOwner(plr)) {
            plr.sendMessage(languageMngr.getMessage("cant-perform-action"));
            return;
        }

        if (fac.getUuid().equals("")) {
            plr.sendMessage(languageMngr.getMessage("faction-hasNone"));
            return;
        }

        //--Initializes Chunk Variables And Get Faction Chunks
        int chnkX = chunk.getX();
        int chnkZ = chunk.getZ();

        ArrayList<FChunk> ch = fac.getChunks();
        FChunk chunkToBeRemoved = new FChunk(chnkX, chnkZ, chunk.getWorld().getName());

        //--Checks If Faction Has Chunks, If None, Send Message And Return;
        if (ch.size() == 0) {
            plr.sendMessage(languageMngr.getMessage("chunk-none"));
            return;
        }

        //--Checks If Faction Has The Chunk Claimed, If Not, Send Message And Return;
        if(!ch.contains(chunkToBeRemoved)){
            plr.sendMessage(languageMngr.getMessage("chunk-notClaimed"));
            return;
        }


        //--Removes Chunk From Faction Claims, Updates Faction And Sends Message
        ch.remove(chunkToBeRemoved);
        fac.setChunks(ch);

        JsonTableUtil.updateFaction(fac);

        plr.sendMessage(languageMngr.getMessage("chunk-unClaimed")
                .replaceAll("%coords%", "[" + chnkX + " " + chnkZ + "]"));
    }
}
