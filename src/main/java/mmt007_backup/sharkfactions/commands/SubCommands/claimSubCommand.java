package mmt007_backup.sharkfactions.commands.SubCommands;

import mmt007_backup.sharkfactions.SharkFMain;
import mmt007_backup.sharkfactions.commands.SubCommand;
import mmt007_backup.sharkfactions.lang.languageUtil;
import mmt007_backup.sharkfactions.models.FChunk;
import mmt007_backup.sharkfactions.models.Factions;
import mmt007_backup.sharkfactions.utils.JsonTableUtil;
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
    public String getSyntax() {
        return "Claim";
    }

    @Override
    public void perform(Player plr, String[] args) {
        Chunk chunk = Objects.requireNonNull(plr.getLocation().getChunk());

        Factions fac = JsonTableUtil.getFactionByPlayer(plr);
        if (!Objects.equals(plr.getUniqueId().toString(), fac.getOwner()) && !this.main.getConfig().getBoolean("faction-player-dominate-chunk")) {
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
        FChunk newChunk = new FChunk(chnkX, chnkZ, chunk.getWorld().getName());

        if (ch.size() > 0) {
            if(getSurroundingChunks(newChunk,ch) || !Objects.equals(newChunk.getWorld(), plr.getWorld().getName())){
                plr.sendMessage(languageUtil.getMessage("chunk-tooFar"));
                return;
            }
        }

        ch.add(newChunk);
        fac.setChunks(ch);
        JsonTableUtil.updateFaction(fac);
        plr.sendMessage(languageUtil.getMessage("chunk-claimed")
                .replaceAll("%coords%", "[" + chnkX + " " + chnkZ + "]"));
    }

    private boolean getSurroundingChunks(FChunk chunk, ArrayList<FChunk> factionChunks){
        for (int x = -1; x < 2; x++){
            for (int y = -1; y < 2; y++){
                FChunk neighbourChunk = new FChunk(
                        chunk.getX() + x,
                        chunk.getY() + y,
                        chunk.getWorld());

                if(factionChunks.contains(neighbourChunk)){return true;}
            }
        }

        return false;
    }
}
