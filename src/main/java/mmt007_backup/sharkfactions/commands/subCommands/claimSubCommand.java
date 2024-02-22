package mmt007_backup.sharkfactions.commands.subCommands;

import mmt007_backup.sharkfactions.SharkFMain;
import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageMngr;
import mmt007_backup.sharkfactions.models.FChunk;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.models.Tuple;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
import mmt007_backup.sharkfactions.utils.Utilitis;
import mmt007_backup.sharkfactions.utils.protectedChunkUtil;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Objects;

public class claimSubCommand extends SubCommand {
    Plugin main = SharkFMain.getPlugin();

    @Override
    public String getName() {
        return "claim";
    }

    @Override
    public String getDescription() {
        return "Domina o Chunk que Você Está";
    }

    @Override
    public String getPermission() {return "sharkfactions." + getName();}

    @Override
    public String[] getAutoComplete() {return new String[]{capitalize(getName())};}

    @Override
    public String getSyntax() {
        return "Claim";
    }

    @Override
    public void perform(Player plr, String[] args) {
        Chunk chunk = Objects.requireNonNull(plr.getLocation().getChunk());

        //-- checks If Player Is Faction Owner / Has A Faction
        Factions fac = JsonTableUtil.getFaction(plr);

        if (fac.getUuid().equals("")) {
            plr.sendMessage(languageMngr.getMessage("faction-hasNone"));
            return;
        }

        if (!Utilitis.isFactionOwner(plr) && !this.main.getConfig().getBoolean("faction-player-dominate-chunk")) {
            plr.sendMessage(languageMngr.getMessage("cant-perform-action"));
            return;
        }

        //--Sets Variable For The New Claimed Chunk
        int chnkX = chunk.getX();
        int chnkZ = chunk.getZ();


        ArrayList<FChunk> ch = fac.getChunks();
        FChunk newChunk = new FChunk(chnkX, chnkZ, chunk.getWorld().getName());

        //--Checks If Chunks Is In A Protected Area
        for(Tuple<FChunk,FChunk> area : protectedChunkUtil.getRegions().values()){
            if(newChunk.isInsideArea(area)){
                plr.sendMessage(languageMngr.getMessage("protectedArea-warn"));
                return;
            }
        }

        //--Checks If Faction Has Already Claimed Chunks
        //--Then Proceeds To Check If Chunk Is In The Same World As The First OR 1 Of The...
        //--Surrounding Chunks Is A Chunk From The Faction.
        if (ch.size() > 0) {
            if(!getSurroundingChunks(newChunk,ch) || !Objects.equals(newChunk.getWorld(),ch.get(0).getWorld())){
                plr.sendMessage(languageMngr.getMessage("chunk-tooFar"));
                return;
            }
        }

        //-- checks If Player Has Enough Money
        if(fac.getChunks().size() > 0) {
            if (!Utilitis.removeBalance(
                    plr, "claim-cost","claim-cost-per-chunk",fac.getChunks().size())
            )
                return;
        }

        //--Adds New Chunk To The Faction And Displays Message
        ch.add(newChunk);
        fac.setChunks(ch);
        JsonTableUtil.updateFaction(fac);
        plr.sendMessage(languageMngr.getMessage("chunk-claimed")
                .replaceAll("%coords%", "[" + chnkX + " " + chnkZ + "]"));
    }

    private boolean getSurroundingChunks(FChunk chunk, ArrayList<FChunk> factionChunks){
        //--Loops On A 3x3 Square With {chunk} Being The Center
        for (int x = -1; x < 2; x++){
            for (int y = -1; y < 2; y++){

                //--Truth Table For Checking If {X} And {Y} Are In The...
                //--Four Main Cardinal Directions.
                if((x == 0) ^ (y == 0)) {

                    //-- Creates Variable For The Surrounding Chunk
                    FChunk neighbourChunk = new FChunk(
                            chunk.getX() + x,
                            chunk.getY() + y,
                            chunk.getWorld());

                    //--Checks If The Surrounding Chunk Is Claimed By Player's Faction
                    if (factionChunks.contains(neighbourChunk)) return true;
                }
            }
        }

        return false;
    }
}
